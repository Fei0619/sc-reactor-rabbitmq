package com.test.api.service

import com.test.api.pojo.SubscribeDetails
import com.test.api.properties.EventBusClientProperties
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * @author 费世程
 * @date 2021/3/17 13:55
 */
@Component
class SubscribeService(private val eventBusClientProperties: EventBusClientProperties) {

  @PostConstruct
  fun subscribe() {

  }

  companion object {
    private val Topic_Subscriber: Map<String, List<SubscribeDetails>> = emptyMap()

    fun getTopicSubscribers(topic: String): List<SubscribeDetails> {
      return Topic_Subscriber[topic] ?: emptyList()
    }

  }

}
