package com.test.server.processor.impl

import com.test.server.pojo.PublishDetails
import com.test.server.processor.DelayProcessor
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2021/3/18 14:57
 */
class RabbitDelayProcessor : DelayProcessor {

  override fun delay(publishDetails: PublishDetails): Mono<Unit> {
    TODO()
  }

  override fun init() {
    TODO()
  }

  override fun destroy() {
    TODO()
  }
}
