package com.test.server.conf

import com.test.api.loadbalancer.LoadBalancer
import com.test.server.processor.DelayProcessor
import com.test.server.processor.EventPublisher
import com.test.server.processor.impl.RabbitDelayProcessor
import com.test.server.processor.impl.RabbitEventPublisher
import com.test.server.properties.PushProperties
import com.test.server.properties.RabbitProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.rabbitmq.Sender

/**
 * @author 费世程
 * @date 2021/3/19 11:16
 */
@Configuration
open class DefaultBeanConfig(private val rabbitProperties: RabbitProperties,
                             private val pushProperties: PushProperties) {

  @Bean
  @ConditionalOnMissingBean
  open fun eventPublisher(rabbitLoadBalancer: LoadBalancer<Sender>,
                          delayProcessor: DelayProcessor): EventPublisher<*> {
    return RabbitEventPublisher(rabbitLoadBalancer, rabbitProperties, pushProperties, delayProcessor)
  }

  @Bean
  @ConditionalOnMissingBean
  open fun delayProcessor(rabbitLoadBalancer: LoadBalancer<Sender>): DelayProcessor {
    return RabbitDelayProcessor(rabbitLoadBalancer, pushProperties, rabbitProperties)
  }

  @Bean
  open fun reactorClientHttpConnector(httpClient: HttpClient): ReactorClientHttpConnector {
    return ReactorClientHttpConnector(httpClient)
  }

  @Bean
  @LoadBalanced
  open fun loadBalancedWebClientBuilder(reactorClientHttpConnector: ReactorClientHttpConnector)
      : WebClient.Builder {
    return WebClient.builder().clientConnector(reactorClientHttpConnector)
  }

  @Bean
  open fun webClient(reactorClientHttpConnector: ReactorClientHttpConnector): WebClient {
    return WebClient.builder().clientConnector(reactorClientHttpConnector).build()
  }

}
