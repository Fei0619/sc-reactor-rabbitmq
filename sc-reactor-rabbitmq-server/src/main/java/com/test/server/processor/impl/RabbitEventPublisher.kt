package com.test.server.processor.impl

import com.test.server.processor.EventPublisher
import com.test.api.pojo.common.Res
import com.test.api.Destroyable
import com.test.api.Initable
import com.test.server.properties.RabbitProperties
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2021/3/17 9:11
 */
class RabbitEventPublisher(private val rabbitProperties: RabbitProperties)
  : EventPublisher<Unit>, Initable, Destroyable {

  override fun publishOne(): Mono<Res<Unit>> {

    TODO()
  }

  override fun init() {
    TODO()
  }

  override fun destroy() {
    TODO()
  }
}
