package com.test.api.pojo.common;

/**
 * 消息推送类型
 *
 * @author 费世程
 * @date 2021/3/17 10:02
 */
public enum PushType {
  /**
   * 负载均衡
   */
  LoadBalance(0),
  /**
   * 实际url
   */
  Host(1),
  ;
  private Integer code;

  public Integer code() {
    return this.code;
  }

  PushType(Integer code) {
    this.code = code;
  }

}
