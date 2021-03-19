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

    public String getTopic() {
      return topic;
    }

    public void setTopic(String topic) {
      this.topic = topic;
    }

    public String getCondition() {
      return condition;
    }

    public void setCondition(String condition) {
      this.condition = condition;
    }

    public Integer getPushType() {
      return pushType;
    }

    public void setPushType(Integer pushType) {
      this.pushType = pushType;
    }

    public String getReceiveUrl() {
      return receiveUrl;
    }

    public void setReceiveUrl(String receiveUrl) {
      this.receiveUrl = receiveUrl;
    }
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public List<SubscriberProperties> getSubscribers() {
    return subscribers;
  }

  public void setSubscribers(List<SubscriberProperties> subscribers) {
    this.subscribers = subscribers;
  }
}
