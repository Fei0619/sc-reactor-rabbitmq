package com.test.api.pojo

/**
 * 消息订阅者详情 - 从数据库或配置文件中加载 </br>
 * topic - List<SubscribeDetails>
 *
 * @author 费世程
 * @date 2021/3/17 9:44
 */
class SubscribeDetails {

  var pushType: Int = 0
  var serviceName: String = ""
  var url: String = ""
  var conditions: Set<String> = emptySet()

}
