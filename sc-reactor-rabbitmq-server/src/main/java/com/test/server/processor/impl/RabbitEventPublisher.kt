package com.test.server.processor.impl

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.AlreadyClosedException
import com.rabbitmq.client.Delivery
import com.test.api.loadbalancer.LoadBalancer
import com.test.api.loadbalancer.Server
import com.test.server.processor.EventPublisher
import com.test.api.pojo.common.Res
import com.test.api.utils.JsonUtils
import com.test.server.pojo.PublishDetails
import com.test.server.processor.DelayProcessor
import com.test.server.properties.PushProperties
import com.test.server.properties.RabbitProperties
import com.test.server.push.Pusher
import com.test.server.share.RabbitUtils
import org.slf4j.LoggerFactory
import reactor.core.Disposable
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import reactor.rabbitmq.ConsumeOptions
import reactor.rabbitmq.OutboundMessage
import reactor.rabbitmq.Receiver
import reactor.rabbitmq.Sender
import java.lang.Exception
import java.nio.charset.StandardCharsets

/**
 * @author 费世程
 * @date 2021/3/17 9:11
 */
class RabbitEventPublisher(private val rabbitLoadBalancer: LoadBalancer<Sender>,
                           private val rabbitProperties: RabbitProperties,
                           private val pushProperties: PushProperties,
                           private val delayProcessor: DelayProcessor)
  : EventPublisher<Unit> {

  private val log = LoggerFactory.getLogger(RabbitEventPublisher::class.java)
  private val disposes = ArrayList<Disposable>()
  private val receivers = ArrayList<Receiver>()
  private val retry = 3

  private val basicProperties = AMQP.BasicProperties().builder()
      // 设置消息是否持久化：1-非持久化，2-持久化
      .deliveryMode(2).build()

  override fun publishOne(publishDetails: PublishDetails): Mono<Res<Unit>> {
    val message = JsonUtils.toJsonString(publishDetails)
    val outboundMessage = if (rabbitProperties.isEnableMessagePersistence) {
      OutboundMessage(rabbitProperties.exchangeName, rabbitProperties.eventMessageQueue, basicProperties, message.toByteArray(StandardCharsets.UTF_8))
    } else {
      OutboundMessage(rabbitProperties.exchangeName, rabbitProperties.eventMessageQueue, message.toByteArray(StandardCharsets.UTF_8))
    }
    val selectedServer: Server<Sender> = rabbitLoadBalancer.select(retry)
        ?: return Res.error<Unit>("服务暂不可用!").toMono()
    log.debug("rabbitmqLoadbalancer select -> {} ", selectedServer.id)
    return selectedServer.server.sendWithPublishConfirms(outboundMessage.toMono())
        .collectList().map { Res.success<Unit>() }
        .doOnError { ex ->
          if (ex is AlreadyClosedException) {
            log.error("rabbitmq AlreadyClosed -> {}", ex.message)
            rabbitLoadBalancer.unavailable(selectedServer)
          } else {
            log.error("rabbitmq 服务调用异常 -> {}", ex.message)
            rabbitLoadBalancer.onInvokeFail(selectedServer)
          }
        }
  }

  override fun init() {
    rabbitProperties.servers.forEach { server ->
      disposes.add(startOne(server.host, server.port))
    }
  }

  private fun startOne(host: String, port: Int): Disposable {
    val receiverOptions = RabbitUtils.createReceiverOptions(rabbitProperties, host, port)
    val receiver = Receiver(receiverOptions)
    receivers.add(receiver)
    val consumeOptions = ConsumeOptions().qos(pushProperties.concurrency)
    return receiver.consumeAutoAck(rabbitProperties.eventMessageQueue, consumeOptions)
        .flatMap({ delivery: Delivery ->
          val messageString = delivery.body.toString(StandardCharsets.UTF_8)
          val publishDetails = try {
            JsonUtils.parseJson(messageString, PublishDetails::class.java)
          } catch (e: Exception) {
            log.error("parseJson PublishDetails occur Exception -> {}", e.message)
            PublishDetails()
          }
          log.debug("rabbitEventConsumer 接收到一个事件：{}", messageString)
          val subscribers = publishDetails.subscribers
          if (subscribers.isEmpty()) {
            Unit.toMono()
          } else {
            val eventMessage = publishDetails.eventMessage
            if (publishDetails.needDelay) {
              log.debug("延时消息，{}ms后推送...", eventMessage.delayMillis)
              delayProcessor.delay(publishDetails)
            } else {
              log.debug("实时消息，即将推送...")
              subscribers.toFlux().flatMap { subscribe ->
                Pusher.doPush(eventMessage, subscribe)
              }.collectList().map { }
            }
          }.onErrorResume {
            log.error("EventConsume error -> {}", it.message)
            Unit.toMono()
          }
        }, pushProperties.concurrency).subscribe()
  }

  override fun destroy() {
    disposes.forEach { dispose -> dispose.dispose() }
    receivers.forEach { receiver -> receiver.close() }
  }
}
