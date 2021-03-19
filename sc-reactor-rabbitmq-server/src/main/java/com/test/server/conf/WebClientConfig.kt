package com.test.server.conf

import com.test.server.properties.PushProperties
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

/**
 * @author 费世程
 * @date 2021/3/19 16:10
 */
@Configuration
open class WebClientConfig {

  @Bean
  open fun httpClient(pushProperties: PushProperties): HttpClient {
    val connectionTimeout = pushProperties.connectionTimeout.toMillis()
    val writeTimeout = pushProperties.writeTimeout.toMillis()
    val readTimeout = pushProperties.readTimeout.toMillis()

    return HttpClient.create().tcpConfiguration { tcpClient ->
      if (connectionTimeout > 0) {
        tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout.toInt())
      }
      tcpClient.doOnConnected { connection ->
        if (writeTimeout > 0) {
          connection.addHandler(WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS))
        }
        if (readTimeout > 0) {
          connection.addHandler(ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
        }
      }
    }
  }

}
