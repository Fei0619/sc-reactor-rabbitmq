package com.test.server.push

import reactor.core.publisher.Mono
import javax.annotation.PostConstruct

/**
 * @author 费世程
 * @date 2021/3/17 9:22
 */
abstract class Pusher {

  /*
   * @PostConstruct注释用于在依赖关系注入完成之后需要执行的方法上
   */
  @PostConstruct
  fun register() {

  }

  companion object {

    fun doPush(): Mono<Unit> {
      TODO()
    }

  }

}