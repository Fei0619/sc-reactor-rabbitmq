package com.test.server.share

import com.rabbitmq.client.ConnectionFactory
import com.test.server.properties.RabbitProperties
import org.apache.commons.lang.StringUtils
import reactor.core.scheduler.Schedulers
import reactor.rabbitmq.ReceiverOptions

/**
 * @author 费世程
 * @date 2021/3/18 14:27
 */
object RabbitUtils {

  fun createReceiverOptions(rabbitProperties: RabbitProperties, host: String, port: Int): ReceiverOptions {
    val connectionFactory = ConnectionFactory()
    connectionFactory.useNio()
    connectionFactory.host = host
    connectionFactory.port = port
    CommonUtils.onSuccess({ StringUtils.isNotBlank(rabbitProperties.username) },
        { connectionFactory.username = rabbitProperties.username })
    CommonUtils.onSuccess({ StringUtils.isNotBlank(rabbitProperties.password) },
        { connectionFactory.password = rabbitProperties.password })
    connectionFactory.virtualHost = rabbitProperties.virtualHost
    return ReceiverOptions()
        .connectionFactory(connectionFactory)
        .connectionSubscriptionScheduler(Schedulers.elastic())
  }

}
