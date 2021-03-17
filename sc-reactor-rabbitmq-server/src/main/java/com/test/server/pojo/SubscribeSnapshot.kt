package com.test.server.pojo

import com.test.api.pojo.common.PushType

/**
 * @author 费世程
 * @date 2021/3/17 10:01
 */
class SubscribeSnapshot {

  var serviceName: String = ""
  var pushUri: String = ""
  var uriType: Int = PushType.LoadBalance.code()
  var retryNum: Int = 0

}
