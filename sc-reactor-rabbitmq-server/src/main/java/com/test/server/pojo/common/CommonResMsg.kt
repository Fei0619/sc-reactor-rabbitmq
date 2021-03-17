package com.test.server.pojo.common

/**
 * @author 费世程
 * @date 2020/10/13 8:42
 */
enum class CommonResMsg(private val code: Int, private val message: String)
  : EnumMsg {
  SUCCESS(200, "Success!"),
  BAD_REQUEST(400, "Bad Request!"),
  NOT_FOUND(404, "Not Found!"),
  INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
  ;

  override fun code(): Int {
    return code
  }

  override fun desc(): String {
    return message
  }
}
