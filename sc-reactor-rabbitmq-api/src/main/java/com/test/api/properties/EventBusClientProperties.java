package com.test.api.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author 费世程
 * @date 2021/3/17 10:49
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "sc.bus.subscribe")
public class EventBusClientProperties {

  private String serviceName = "";
  private List<SubscriberProperties> subscribers = Collections.emptyList();

  @Data
  public static class SubscriberProperties {
    private String topic;
    private String condition;
    private Integer pushType;
    private String receiveUrl;
  }

}
