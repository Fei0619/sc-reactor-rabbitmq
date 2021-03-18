package com.test.server.processor

import com.test.api.Destroyable
import com.test.api.Initable
import com.test.api.pojo.common.Res
import com.test.server.pojo.PublishDetails
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2021/3/16 16:06
 */
interface EventPublisher<T> : Initable, Destroyable {

  fun publishOne(publishDetails: PublishDetails): Mono<Res<T>>

}
