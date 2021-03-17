package com.test.server.pojo

import java.util.*

/**
 * 消息推送详情
 *
 * @author 费世程
 * @date 2021/3/17 9:30
 */
class PublishDetails {

  val eventId = UUID.randomUUID().toString().replace("-", "")
  val eventMessage: EventMessage<Any> = EventMessage()
  val subscribers: List<SubscribeSnapshot> = emptyList()
  val needDelay: Boolean = eventMessage.delayMillis > 0

}
