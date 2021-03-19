package com.test.server.processor

import com.test.api.Destroyable
import com.test.api.Initable
import com.test.server.pojo.PublishDetails
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2021/3/18 14:05
 */
interface DelayProcessor : Initable, Destroyable {

  fun delay(publishDetails: PublishDetails): Mono<Unit>

}
