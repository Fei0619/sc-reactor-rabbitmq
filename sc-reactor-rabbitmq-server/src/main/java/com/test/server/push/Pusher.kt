package com.test.server.push

import com.test.api.pojo.common.CommonResMsg
import com.test.api.pojo.common.HttpResponse
import com.test.api.pojo.common.PushType
import com.test.api.utils.JsonUtils
import com.test.server.pojo.EventMessage
import com.test.server.pojo.SubscribeSnapshot
import io.netty.channel.ConnectTimeoutException
import io.netty.handler.timeout.ReadTimeoutException
import io.netty.handler.timeout.WriteTimeoutException
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.SocketException
import javax.annotation.PostConstruct

/**
 * @author 费世程
 * @date 2021/3/17 9:22
 */
abstract class Pusher(private val pushType: PushType) {

  /*
   * @PostConstruct注释用于在依赖关系注入完成之后需要执行的方法上
   */
  @PostConstruct
  fun register() {
    registerPusher(pushType, this)
  }

  companion object {
    private val log = LoggerFactory.getLogger(Pusher::class.java)!!
    private val pusherMap = HashMap<Int, Pusher>()

    private fun registerPusher(pushType: PushType, pusher: Pusher) {
      val code = pushType.code()
      if (pusherMap[code] != null) {
        log.error("pushType冲突 -> {}", code)
      }
      pusherMap[code] = pusher
    }

    fun doPush(eventMessage: EventMessage<*>, subscribe: SubscribeSnapshot): Mono<Unit> {
      val uriType = subscribe.uriType
      val pusher = pusherMap[uriType]
      if (pusher == null) {
        log.error("uriType={} , get pusher return null...", uriType)
        throw RuntimeException("uriType=$uriType , get pusher return null...")
      }
      return pusher.push(eventMessage, subscribe)
    }

  }

  abstract fun executePush(eventMessage: EventMessage<*>, subscribe: SubscribeSnapshot): Mono<OncePushContext>

  fun push(eventMessage: EventMessage<*>, subscribe: SubscribeSnapshot): Mono<Unit> {
    val pushTime = System.currentTimeMillis()
    return executePush(eventMessage, subscribe).map {
      it.pushTime = pushTime
      it.responseTime = System.currentTimeMillis()
      it
    }.onErrorResume { throwable ->
      val message = when (throwable) {
        is SocketException -> {
          val message = "push SocketException -> ${throwable.message}"
          log.debug(message)
          message
        }
        is ConnectTimeoutException -> {
          val message = "push exception -> connect timeout"
          log.debug(message)
          message
        }
        is ReadTimeoutException -> {
          val message = "push exception -> read timeout"
          log.debug(message)
          message
        }
        is WriteTimeoutException -> {
          val message = "push exception -> write timeout"
          log.debug(message)
          message
        }
        is ConnectException -> {
          val message = "push exception -> ${throwable.message}"
          log.debug(message)
          message
        }
        else -> {
          val message = "push exception -> ${throwable.message}"
          log.error(message)
          message
        }
      }
      val pushResponse = HttpResponse(message, CommonResMsg.INTERNAL_SERVER_ERROR.code(), emptyMap())
      OncePushContext(pushResponse, subscribe).apply {
        this.pushTime = pushTime
        responseTime = System.currentTimeMillis()
      }.toMono()
    }.flatMap { oncePushContext ->
      if (oncePushContext.isSuccess()) {
        log.debug("推送成功，response -> {}", JsonUtils.toJsonString(oncePushContext.pushResponse))
        Unit.toMono()
      } else {
        // todo 推送失败处理
        val message = "推送失败，response -> ${JsonUtils.toJsonString(oncePushContext.pushResponse)}"
        log.error(message)
        Unit.toMono()
      }
    }
  }

}
