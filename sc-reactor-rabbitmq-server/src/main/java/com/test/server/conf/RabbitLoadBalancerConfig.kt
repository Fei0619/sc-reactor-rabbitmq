package com.test.server.conf

import com.test.api.loadbalancer.LoadBalancer
import com.test.api.loadbalancer.WeightedPollingLoadBalancer
import com.test.server.properties.RabbitProperties
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.rabbitmq.Sender

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

      }

    }
    TODO()
  }

}
