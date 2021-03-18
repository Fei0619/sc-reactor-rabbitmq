package com.test.server.share

/**
 * @author 费世程
 * @date 2021/3/18 14:39
 */
object CommonUtils {

  fun onSuccess(condition: () -> Boolean, execute: () -> Unit) {
    if (condition.invoke()) {
      execute.invoke()
    }
  }

}
