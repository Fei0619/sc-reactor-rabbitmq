package com.test.api.loadbalancer

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author 费世程
 * @date 2021/3/18 16:07
 */
class Node<S>(
    /**
     * 服务实体
     */
    val server: Server<S>,
    /**
     * 配置文件中指定的该服务的权重
     */
    val weight: Int) : Comparable<Node<S>> {

  /**
   * 服务节点有效权重，初始值为weight
   * 后续当有请求过来选取节点的时会逐步增加，最后再恢复到weight
   * 增加此字段是为了当服务调用失败时降低其权重
   */
  var effectiveWeight = AtomicInteger(weight)
  /**
   * 服务节点当前实际权重，初始为0
   * 每次选取后端时，会遍历所有的节点，对于每个节点，将节点的currentWeight加上effectiveWeight
   * 并累加所有节点的effectiveWeight保存为total
   * 选取currentWeight最大的节点，并将它的currentWeight减去total,其他没选中的节点currentWeight不变
   */
  var currentWeight = AtomicInteger(0)
  /**
   * 服务是否可用
   */
  var available = AtomicBoolean(true)

  override fun compareTo(other: Node<S>): Int {
    return currentWeight.get() - other.currentWeight.get()
  }

  fun reset() {
    this.effectiveWeight = AtomicInteger(weight)
    this.currentWeight = AtomicInteger(0)
    this.available = AtomicBoolean(true)
  }

}
