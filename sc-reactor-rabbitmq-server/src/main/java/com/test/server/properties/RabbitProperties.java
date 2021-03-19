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
@Data
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
  }

}
