package com.test.api.loadbalancer

/**
 * @author 费世程
 * @date 2021/3/18 16:47
 */
class WeightedPollingLoadBalancer<S>() : LoadBalancer<S> {

  @Suppress("unused")
  constructor(nodeUnavailableCallback: (Node<S>) -> Unit,
              serverUnavailableCallback: () -> Unit) : this() {
    this.nodeUnavailableCallback = nodeUnavailableCallback
    this.serverUnavailableCallback = serverUnavailableCallback
  }

  val nodes = ArrayList<Node<S>>()
  var nodeUnavailableCallback: (Node<S>) -> Unit = {}
  var serverUnavailableCallback: () -> Unit = {}

  override fun register(node: Node<S>) {
    synchronized(this) {
      this.nodes.add(node)
    }
  }

  override fun select(): Server<S>? {
    if (nodes.isEmpty()) {
      return null
    } else if (nodes.size == 1) {
      return nodes[0].server
    }
    val availableNodes = nodes.filter { it.available.get() }
    if (availableNodes.isEmpty()) {
      // 没有可用节点
      synchronized(this) {
        serverUnavailableCallback.invoke()
        nodes.forEach { node -> node.reset() }
      }
      return null
    }
    var total = 0
    var nodeOfMaxWeight: Node<S>? = null
    for (node in availableNodes) {
      val effectiveWeight = node.effectiveWeight.get()
      total += effectiveWeight
      node.currentWeight.addAndGet(effectiveWeight)
      nodeOfMaxWeight = if (nodeOfMaxWeight == null) {
        node
      } else {
        if (nodeOfMaxWeight < node) node else nodeOfMaxWeight
      }
    }
    nodeOfMaxWeight!!.currentWeight.addAndGet(-total)
    return nodeOfMaxWeight.server
  }

  override fun onInvokeSuccess(server: Server<S>) {
    val id = server.id
    synchronized(id) {
      nodes.filter { it.server.id === id }
          .forEach { node ->
            node.effectiveWeight.updateAndGet { effectiveWeight ->
              if (effectiveWeight < node.weight) effectiveWeight + 1 else effectiveWeight
            }
          }
    }
  }

  override fun onInvokeFail(server: Server<S>) {
    val id = server.id
    synchronized(id) {
      nodes.filter { it.server.id === id }.forEach { node ->
        val effectiveWeight = node.effectiveWeight.decrementAndGet()
        if (effectiveWeight == 0) {
          nodeUnavailableCallback.invoke(node)
        }
      }
    }
  }

  override fun unavailable(server: Server<S>) {
    val id = server.id
    synchronized(id) {
      nodes.filter { it.server.id == id }.forEach { node ->
        if (node.available.getAndSet(false)) {
          nodeUnavailableCallback.invoke(node)
        }
      }
    }
  }

  override fun resetServer(server: Server<S>) {
    val id = server.id
    synchronized(id) {
      nodes.filter { it.server.id == id }.forEach { node ->
        node.reset()
      }
    }
  }

}
