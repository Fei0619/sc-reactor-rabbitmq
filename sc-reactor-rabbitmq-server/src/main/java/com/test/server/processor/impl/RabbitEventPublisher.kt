package com.test.server.processor.impl

import com.test.server.processor.EventPublisher
import com.test.server.pojo.common.Res
import com.test.server.properties.RabbitProperties
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2021/3/17 9:11
 */
class RabbitEventPublisher(private val rabbitProperties: RabbitProperties) : EventPublisher<Unit> {

  override fun publishOne(): Mono<Res<Unit>> {

    TODO()
  }

}
