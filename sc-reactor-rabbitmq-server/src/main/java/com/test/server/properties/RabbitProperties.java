package com.test.server.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 费世程
 * @date 2021/3/16 23:37
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "sc.bus.rabbit")
public class RabbitProperties {

  @NonNull
  private String virtualHost = "/";
  @NonNull
  private String username = "";
  @NonNull
  private String password = "";
  private List<RabbitServer> servers = new ArrayList<>();
  /**
   * 当某一节点被标记为不可用时，等待一段时间后尝试将其重新加入负载均衡服务列表
   * 如果该值小于0，则该节点永远不会重新加入负载均衡
   */
  private long reloadUnavailableNodeDelaySeconds = 600L;
  /**
   * 是否开启消息持久化
   */
  private boolean enableMessagePersistence = false;
  @NonNull
  private String exchangeName = "sc.messageExchange";
  @NonNull
  private String delayExchangeName = "sc.delay.messageExchange";
  @NonNull
  private String eventMessageQueue = "sc.eventMessageQueue";
  @NonNull
  private String delayMessageQueue = "sc.delay.messageQueue";
  @NonNull
  private String eventPushLogQueue = "sc.eventPushQueue";

  @Data
  public static class RabbitServer {
    private String host;
    private Integer port;
    private Integer weight = 10;

    public String getHost() {
      return host;
    }

    public void setHost(String host) {
      this.host = host;
    }

    public Integer getPort() {
      return port;
    }

    public void setPort(Integer port) {
      this.port = port;
    }

    public Integer getWeight() {
      return weight;
    }

    public void setWeight(Integer weight) {
      this.weight = weight;
    }
  }

  public String getVirtualHost() {
    return virtualHost;
  }

  public void setVirtualHost(String virtualHost) {
    this.virtualHost = virtualHost;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<RabbitServer> getServers() {
    return servers;
  }

  public void setServers(List<RabbitServer> servers) {
    this.servers = servers;
  }

  public long getReloadUnavailableNodeDelaySeconds() {
    return reloadUnavailableNodeDelaySeconds;
  }

  public void setReloadUnavailableNodeDelaySeconds(long reloadUnavailableNodeDelaySeconds) {
    this.reloadUnavailableNodeDelaySeconds = reloadUnavailableNodeDelaySeconds;
  }

  public boolean isEnableMessagePersistence() {
    return enableMessagePersistence;
  }

  public void setEnableMessagePersistence(boolean enableMessagePersistence) {
    this.enableMessagePersistence = enableMessagePersistence;
  }

  public String getExchangeName() {
    return exchangeName;
  }

  public void setExchangeName(String exchangeName) {
    this.exchangeName = exchangeName;
  }

  public String getDelayExchangeName() {
    return delayExchangeName;
  }

  public void setDelayExchangeName(String delayExchangeName) {
    this.delayExchangeName = delayExchangeName;
  }

  public String getEventMessageQueue() {
    return eventMessageQueue;
  }

  public void setEventMessageQueue(String eventMessageQueue) {
    this.eventMessageQueue = eventMessageQueue;
  }

  public String getDelayMessageQueue() {
    return delayMessageQueue;
  }

  public void setDelayMessageQueue(String delayMessageQueue) {
    this.delayMessageQueue = delayMessageQueue;
  }

  public String getEventPushLogQueue() {
    return eventPushLogQueue;
  }

  public void setEventPushLogQueue(String eventPushLogQueue) {
    this.eventPushLogQueue = eventPushLogQueue;
  }
}
