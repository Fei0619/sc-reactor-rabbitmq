package com.test.server.push

import com.test.api.pojo.common.HttpResponse
import com.test.api.pojo.common.Res
import com.test.api.utils.JsonUtils
import com.test.server.pojo.SubscribeSnapshot
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import java.lang.Exception

/**
 * @author 费世程
 * @date 2021/3/19 14:34
 */
data class OncePushContext(
    /** 推送响应结果 */
    val pushResponse: HttpResponse,
    /**  本次推送对应的订阅者信息 */
    val pushTargetInfo: SubscribeSnapshot) {

  companion object {
    private val log = LoggerFactory.getLogger(OncePushContext::class.java)!!
  }

  /** 执行推送时间戳 */
  var pushTime = 0L
  /** 推送响应时间戳 */
  var responseTime = 0L
  @Transient
  var result: Res<*>? = null

  @java.beans.Transient
  fun isSuccess(): Boolean {
    if (pushResponse.statusCode != HttpStatus.OK.value()) {
      return false
    } else {
      val body = pushResponse.body
      try {
        if (result == null) {
          synchronized(this) {
            if (result == null) {
              result = JsonUtils.parseJson(body, Res::class.java)
            }
          }
        }
        if (!result!!.isSuccess) {
          return false
        }
      } catch (e: Exception) {
        log.warn("解析响应异常：response={} , e={}", body, e)
        return false
      }
    }
    return true
  }

}
