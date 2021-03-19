package com.test.server.push

import com.test.api.pojo.common.PushType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

/**
 * @author 费世程
 * @date 2021/3/19 15:52
 */
@Component("LoadBalancerWebClientPusher")
class LoadBalancerWebClientPusher(private val loadBalancerWebClient: WebClient.Builder)
  : WebClientPusher(PushType.LoadBalance) {
  override fun getWebClient(): WebClient {
    return loadBalancerWebClient.build()
  }

}
