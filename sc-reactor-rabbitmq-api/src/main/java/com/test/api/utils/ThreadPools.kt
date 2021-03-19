package com.test.api.utils

import com.google.common.util.concurrent.ThreadFactoryBuilder
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import java.lang.Exception
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author 费世程
 * @date 2021/3/19 9:44
 */
@Suppress("unused")
object ThreadPools {

  var commonPool: ThreadPoolExecutor
  var commonSchedule: Scheduler

  /** 服务器核心线程数 */
  private val availableProcessorsCount = Runtime.getRuntime().availableProcessors()
  private const val commonPoolNamePrefix = "common-pool-"
  /** 核心线程数 */
  private var commonPoolCoreSize = 0
  /**
   * 最大线程数
   */
  private var commonPoolMaxSize = availableProcessorsCount.shl(4)

  init {
    val commonPoolCoreSizeStr = System.getProperty("commonPoolCoreSize")
    val commonPoolCoreSizeVal = try {
      commonPoolCoreSizeStr?.toInt() ?: commonPoolCoreSize
    } catch (e: Exception) {
      commonPoolCoreSize
    }
    val commonPoolMaxSizeStr = System.getProperty("commonPoolMaxSize")
    val commonPoolMaxSizeVal = try {
      commonPoolMaxSizeStr?.toInt() ?: commonPoolMaxSize
    } catch (e: Exception) {
      commonPoolMaxSize
    }
    commonPoolCoreSize = commonPoolCoreSizeVal
    commonPoolMaxSize = commonPoolMaxSizeVal

    commonPool = ThreadPoolExecutor(
        commonPoolCoreSize,
        commonPoolMaxSize,
        60L, TimeUnit.SECONDS,
        SynchronousQueue(),
        ThreadFactoryBuilder().setNameFormat("$commonPoolNamePrefix%d").build(),
        ThreadPoolExecutor.CallerRunsPolicy()
    )
    commonSchedule = Schedulers.fromExecutor(commonPool)
    Runtime.getRuntime().addShutdownHook(Thread { commonPool.shutdown() })

  }

}
