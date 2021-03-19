package com.test.server.push

import com.test.api.pojo.common.PushType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

/**
 * @author 费世程
 * @date 2021/3/19 15:50
 */
@Component("DefaultWebClientPusher")
class DefaultWebClientPusher(private val wenClient: WebClient)
  : WebClientPusher(PushType.Host) {

  override fun getWebClient(): WebClient {
    return wenClient
  }

}
