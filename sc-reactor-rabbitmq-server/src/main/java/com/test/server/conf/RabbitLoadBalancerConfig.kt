package com.test.server.conf

import com.rabbitmq.client.ConnectionFactory
import com.test.api.loadbalancer.LoadBalancer
import com.test.api.loadbalancer.Node
import com.test.api.loadbalancer.Server
import com.test.api.loadbalancer.WeightedPollingLoadBalancer
import com.test.api.utils.ThreadPools
import com.test.server.properties.RabbitProperties
import com.test.server.share.CommonUtils
import org.apache.commons.lang.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.scheduler.Schedulers
import reactor.rabbitmq.*
import java.util.concurrent.TimeUnit

/**
 * @author 费世程
 * @date 2021/3/19 9:20
 */
@Configuration
open class RabbitLoadBalancerConfig(private val rabbitProperties: RabbitProperties) {

  private val log = LoggerFactory.getLogger(RabbitLoadBalancerConfig::class.java)

  @Bean
  open fun rabbitLoadBalancer(): LoadBalancer<Sender> {
    val loadbalancer = WeightedPollingLoadBalancer<Sender>()
    loadbalancer.nodeUnavailableCallback = { node ->
      val id = node.server.id
      log.debug("Rabbitmq sender {}节点异常，已被移除负载均衡服务列表...", id)
      if (rabbitProperties.reloadUnavailableNodeDelaySeconds > 0) {
        ThreadPools.commonSchedule.schedule({
          log.debug("尝试将异常节点重新加入负载均衡服务列表，serviceId={}", node.server.id)
          loadbalancer.resetServer(node.server)
        }, rabbitProperties.reloadUnavailableNodeDelaySeconds, TimeUnit.SECONDS)
      }
      //todo 发送告警短信或邮件
    }
    loadbalancer.serverUnavailableCallback = {
      log.error("警告：所有节点都不可用！！！")
      // todo 发送告警短信或邮件
    }
    rabbitProperties.servers.forEach { rabbitServer ->
      val host = rabbitServer.host
      val port = rabbitServer.port
      val weight = rabbitServer.weight
      val sender = createRabbitSender(host, port)
      val server = Server("$host:$port", sender)
      val node = Node(server, weight)
      loadbalancer.register(node)
    }
    return loadbalancer
  }

  private fun createRabbitSender(host: String, port: Int): Sender {
    val connectionFactory = ConnectionFactory()
    connectionFactory.useNio()
    connectionFactory.host = host
    connectionFactory.port = port
    CommonUtils.onSuccess({ StringUtils.isNotBlank(rabbitProperties.username) }, { connectionFactory.username = rabbitProperties.username })
    CommonUtils.onSuccess({ StringUtils.isNotBlank(rabbitProperties.password) }, { connectionFactory.password = rabbitProperties.password })
    connectionFactory.virtualHost = rabbitProperties.virtualHost
    val sendOptions = SenderOptions()
        .connectionFactory(connectionFactory)
        .resourceManagementScheduler(Schedulers.elastic())
    val exchangeName = rabbitProperties.exchangeName
    val delayExchangeName = rabbitProperties.delayExchangeName
    val eventMessageQueue = rabbitProperties.eventMessageQueue
    val delayMessageQueue = rabbitProperties.delayMessageQueue
    val eventPushLogQueue = rabbitProperties.eventPushLogQueue
    val sender = RabbitFlux.createSender(sendOptions)
    sender.declare(ExchangeSpecification().apply {
      this.name(exchangeName)
      this.type("direct")
      this.durable(true)
      this.autoDelete(false)
    }).then(sender.declare(ExchangeSpecification().apply {
      this.name(delayExchangeName)
      this.type("x-delayed-message")
      this.durable(true)
      this.autoDelete(false)
      this.arguments(mapOf("x-delayed-type" to "direct"))
    })).then(sender.declare(QueueSpecification().apply {
      this.name(eventMessageQueue)
      this.durable(true)
      this.autoDelete(false)
    })).then(sender.declare(QueueSpecification().apply {
      this.name(delayMessageQueue)
      this.durable(true)
      this.autoDelete(false)
    })).then(sender.declare(QueueSpecification().apply {
      this.name(eventPushLogQueue)
      this.durable(true)
      this.autoDelete(false)
    }))
        .then(sender.bind(ResourcesSpecification.binding(exchangeName, exchangeName, eventMessageQueue)))
        .then(sender.bind(ResourcesSpecification.binding(delayExchangeName, delayExchangeName, delayMessageQueue)))
        .then(sender.bind(ResourcesSpecification.binding(exchangeName, exchangeName, eventPushLogQueue)))
        .subscribe { log.info("rabbitmq exchange and queue declared and bound...") }
    return sender
  }

}
