package com.test.api.loadbalancer

/**
 * @author 费世程
 * @date 2021/3/18 16:07
 */
class Server<S>(
    /**
     * 服务唯一id
     */
    val id: String,
    /**
     * 服务实体
     */
    val server: S) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Server<*>
    if (this.id != other.id) return false
    return true
  }

  override fun hashCode(): Int {
    return this.id.hashCode()
  }

}
