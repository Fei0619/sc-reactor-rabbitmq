package com.test.server.conf

import com.test.server.processor.DelayProcessor
import com.test.server.processor.EventPublisher
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * @author 费世程
 * @date 2021/3/18 15:49
 */
@Component
class Runner(private val eventPublisher: EventPublisher<*>,
             private val delayProcessor: DelayProcessor) : ApplicationRunner {

  override fun run(args: ApplicationArguments?) {
    eventPublisher.init()
    delayProcessor.init()
    Runtime.getRuntime().addShutdownHook(Thread {
      eventPublisher.destroy()
      delayProcessor.destroy()
    })
  }
}
