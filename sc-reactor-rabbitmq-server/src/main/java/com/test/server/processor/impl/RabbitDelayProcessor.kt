package com.test.server.processor.impl

import com.test.api.loadbalancer.LoadBalancer
import com.test.api.utils.JsonUtils
import com.test.server.pojo.PublishDetails
import com.test.server.processor.DelayProcessor
import com.test.server.properties.PushProperties
import com.test.server.properties.RabbitProperties
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.rabbitmq.Sender

/**
 * @author 费世程
 * @date 2021/3/18 14:57
 */
class RabbitDelayProcessor(private val rabbitLoadBalancer: LoadBalancer<Sender>,
                           private val pushProperties: PushProperties,
                           private val rabbitProperties: RabbitProperties) : DelayProcessor {

  private val log = LoggerFactory.getLogger(RabbitDelayProcessor::class.java)!!

  override fun delay(publishDetails: PublishDetails): Mono<Unit> {
    log.debug("delay message -> {}", JsonUtils.toJsonString(publishDetails))
    TODO()
  }

  override fun init() {
    TODO()
  }

  override fun destroy() {
    TODO()
  }
}
