package com.test.server.processor

import com.test.server.pojo.EventMessage
import com.test.server.pojo.common.Res
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2021/3/16 16:06
 */
interface EventPublisher<T> {

  fun publishOne(): Mono<Res<T>>

}
