package com.test.server.pojo

import java.util.*

/**
 * 推送事件信息
 *
 * @author 费世程
 * @date 2021/3/16 16:07
 */
class EventMessage<T> {

  var eventCode: String = ""
  var broadcast: Boolean = false
  var conditions: Set<String> = Collections.emptySet()
  var payload: T? = null
  var delayMillis: Long = -1L
  val createTimestamp = System.currentTimeMillis()

}
