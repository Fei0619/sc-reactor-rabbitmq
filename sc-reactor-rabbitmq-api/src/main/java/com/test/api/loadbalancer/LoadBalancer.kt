package com.test.api.loadbalancer

/**
 * @author 费世程
 * @date 2021/3/18 16:06
 */
interface LoadBalancer<S> {

  /**
   * 注册节点
   */
  fun register(node: Node<S>)

  /**
   * 选取节点
   */
  fun select(): Server<S>?

  fun select(retry: Int): Server<S>? {
    var server: Server<S>? = null
    var tempNum = 0
    while (server == null && tempNum++ < retry) {
      server = select()
    }
    return server
  }

  /**
   * 服务调用成功回执
   */
  fun onInvokeSuccess(server: Server<S>)

  /**
   * 服务调用失败回执，对服务进行降级处理
   */
  fun onInvokeFail(server: Server<S>)

  /**
   * 将服务标记为不可用
   */
  fun unavailable(server: Server<S>)

  /**
   * 重置服务节点信息
   */
  fun resetServer(server: Server<S>)

}
