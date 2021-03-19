package com.test.server.push

import com.test.api.pojo.common.HttpResponse
import com.test.api.pojo.common.PushType
import com.test.server.pojo.EventMessage
import com.test.server.pojo.SubscribeSnapshot
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/**
 * @author 费世程
 * @date 2021/3/19 15:36
 */
abstract class WebClientPusher(pushType: PushType) : Pusher(pushType) {

  override fun executePush(eventMessage: EventMessage<*>, subscribe: SubscribeSnapshot)
      : Mono<OncePushContext> {
    return getWebClient().post()
        .uri(subscribe.pushUri)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(BodyInserters.fromValue(eventMessage))
        .exchange()
        .flatMap { clientResponse ->
          val statusCode = clientResponse.statusCode()
          val headers = LinkedHashMap<String, List<String>>()
          clientResponse.headers().asHttpHeaders().forEach { t, u ->
            headers[t] = u
          }
          clientResponse.bodyToMono(String::class.java).map { body ->
            val pushResponse = HttpResponse(body, statusCode.value(), headers.toMap())
            OncePushContext(pushResponse, subscribe)
          }
        }
  }

  abstract fun getWebClient(): WebClient

}
