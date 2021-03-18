package com.test.server.processor.impl

import com.rabbitmq.client.AMQP
import com.test.server.processor.EventPublisher
import com.test.api.pojo.common.Res
import com.test.api.Destroyable
import com.test.api.Initable
import com.test.server.pojo.PublishDetails
import com.test.server.processor.DelayProcessor
import com.test.server.properties.RabbitProperties
import org.slf4j.LoggerFactory
import reactor.core.Disposable
import reactor.core.publisher.Mono
import reactor.rabbitmq.Receiver

/**
 * @author 费世程
 * @date 2021/3/17 9:11
 */
class RabbitEventPublisher(private val rabbitProperties: RabbitProperties,
                           private val delayProcessor: DelayProcessor)
  : EventPublisher<Unit>, Initable, Destroyable {

  private val log = LoggerFactory.getLogger(RabbitEventPublisher::class.java)
  private val disposes = ArrayList<Disposable>()
  private val receivers = ArrayList<Receiver>()
  private val basicProperties = AMQP.BasicProperties().builder()
      // 设置消息是否持久化：1-非持久化，2-持久化
      .deliveryMode(2).build()

  override fun publishOne(publishDetails: PublishDetails): Mono<Res<Unit>> {

    TODO()
  }

  override fun init() {
    rabbitProperties.servers.forEach { server ->
      disposes.add(startOne(server.host, server.port))
    }
  }

  private fun startOne(host: String, port: Int): Disposable {
    TODO()
  }

  override fun destroy() {
    TODO()
  }
}
