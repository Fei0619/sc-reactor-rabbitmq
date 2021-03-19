package com.test.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author 费世程
 * @date 2021/3/18 15:02
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "sc.bus.push")
public class PushProperties {

  private Duration connectionTimeout = Duration.ZERO;
  private Duration readTimeout = Duration.ZERO;
  private Duration writeTimeout = Duration.ZERO;
  /**
   * 同时进行的推送并行数限制
   */
  private int concurrency = 1000;

  public void setConcurrency(int concurrency) {
    if (concurrency < 256) {
      concurrency = 256;
    }
    this.concurrency = concurrency;
  }

  public Duration getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(Duration connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public Duration getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(Duration readTimeout) {
    this.readTimeout = readTimeout;
  }

  public Duration getWriteTimeout() {
    return writeTimeout;
  }

  public void setWriteTimeout(Duration writeTimeout) {
    this.writeTimeout = writeTimeout;
  }

  public int getConcurrency() {
    return concurrency;
  }
}
