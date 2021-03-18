package com.test.api.service

import com.test.api.pojo.SubscribeDetails
import com.test.api.properties.EventBusClientProperties
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.InitializingBean
import java.util.stream.Collectors

/**
 * @author 费世程
 * @date 2021/3/17 13:55
 */
class SubscribeFactory(private val eventBusClientProperties: EventBusClientProperties) : InitializingBean {

  override fun afterPropertiesSet() {
    init()
  }

  companion object {
    private val Topic_Subscriber: Map<String, List<SubscribeDetails>> = HashMap()

    fun getTopicSubscribers(topic: String): List<SubscribeDetails> {
      return Topic_Subscriber[topic] ?: emptyList()
    }
  }

  private fun init() {
    val serviceName = eventBusClientProperties.serviceName
    eventBusClientProperties.subscribers!!.stream().forEach { subscriber ->
      val topic = subscriber.topic
      val condition = subscriber.condition
      val pushType = subscriber.pushType
      val receiveUrl = subscriber.receiveUrl
      val subItem = SubscribeDetails().apply {
        this.serviceName = serviceName
        this.pushType = pushType
        this.url = receiveUrl
        this.conditions = StringUtils.split(condition, '|').toList().stream().map { con ->
          StringUtils.split(con, "&").toSet()
        }.collect(Collectors.toList())
      }
      Topic_Subscriber.getOrElse(topic, { listOf(subItem) })
    }
  }

}
