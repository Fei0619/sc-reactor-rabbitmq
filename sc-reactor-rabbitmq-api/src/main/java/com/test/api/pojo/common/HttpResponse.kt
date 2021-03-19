package com.test.api.pojo.common

/**
 * @author 费世程
 * @date 2021/3/19 14:48
 */
data class HttpResponse(
    val body: String,
    val statusCode: Int,
    val headers: Map<String, List<String>>) {
  companion object {
    val const = HttpResponse("", 0, emptyMap())
  }
}
