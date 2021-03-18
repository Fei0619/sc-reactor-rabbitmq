package com.test.api.utils

import org.apache.commons.lang.StringUtils
import org.slf4j.LoggerFactory
import java.lang.NumberFormatException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * @author 费世程
 * @date 2021/3/17 15:13
 */
class ConditionMatcher {

  companion object {
    private val log = LoggerFactory.getLogger(ConditionMatcher::class.java)
    /**
     * 解析事件推送条件表达式
     */
    fun parseConditionString(conditionExpression: String): List<Set<String>> {
      if (StringUtils.isBlank(conditionExpression)) return emptyList()
      val conditionGroups = StringUtils.split(conditionExpression, "|")
      val conditions = ArrayList<Set<String>>()
      for (condition in conditionGroups) {
        val conditionSet = StringUtils.split(condition, "&").toSet()
        conditions.add(conditionSet)
      }
      return conditions
    }

    /**
     * 判断发送方和订阅方条件是否匹配
     */
    fun match(senderConditions: Map<String, Set<String>>, subscriberConditions: List<Set<String>>): Boolean {
      if (senderConditions.isEmpty()) return false
      if (subscriberConditions.isEmpty()) return true
      for (subCondition in subscriberConditions) {
        if (matchConditions(senderConditions, subCondition)) {
          return true
        }
      }
      return false
    }

    private fun matchConditions(senderConditions: Map<String, Set<String>>, subscriberConditionGroup: Set<String>): Boolean {
      if (subscriberConditionGroup.isEmpty()) return true
      for (subCondition in subscriberConditionGroup) {
        var operate = '0'
        var index = -1
        val charArray = subCondition.toCharArray()
        for (i in 0..charArray.size) {
          val c = charArray[i]
          if (c == '=' || c == '>' || c == '<' || c == '^') {
            operate = c
            index = i
            break
          }
        }
        if (index < 0) {
          log.error("条件验证不通过，缺少运算符 -> subscriberConditionGroup={}", subscriberConditionGroup)
          return false
        }
        val conditionKey = StringUtils.substring(subCondition, 0, index)
        val conditionValue = StringUtils.substring(subCondition, index + 1, subCondition.length)
        val senderConditionValue = senderConditions[conditionKey]
        if (senderConditionValue == null || senderConditionValue.isEmpty()) {
          return false
        }
        when (operate) {
          '=' -> {
            if (!senderConditionValue.contains(conditionValue)) {
              return false
            }
          }
          '>' -> {
            for (value in senderConditionValue) {
              var sub: Int?
              var send: Int?
              try {
                sub = conditionValue.toInt()
                send = value.toInt()
              } catch (e: NumberFormatException) {
                log.error("条件验证不通过 -> parseLong occur NumberFormatException")
                return false
              }
              if (sub > send) return false
            }
          }
          '<' -> {
            for (value in senderConditionValue) {
              var sub: Int?
              var send: Int?
              try {
                sub = conditionValue.toInt()
                send = value.toInt()
              } catch (e: NumberFormatException) {
                log.error("条件验证不通过 -> parseLong occur NumberFormatException")
                return false
              }
              if (sub < send) return false
            }
          }
          '^' -> {
            var flag = false
            for (value in StringUtils.split(conditionValue, ",")) {
              if (senderConditionValue.contains(value)) {
                flag = true
                break
              }
            }
            if (!flag) {
              return false
            }
          }
          else -> {
            log.error("条件验证不通过 -> 不合法的操作符:{}", subCondition)
          }
        }
      }
      return true
    }

  }
}

fun main() {
  val conditionExpression = "userId^1001,1002,1003&tenantId=1|tenantId>10|roleId=admin"
  val conditionGroup = ConditionMatcher.parseConditionString(conditionExpression)
  System.err.println(JsonUtils.toJsonString(conditionGroup))
  System.err.println("----------")
  val senderConditions = HashMap<String, Set<String>>()
  senderConditions.putIfAbsent("userId", listOf("1001").toSet())
  senderConditions.putIfAbsent("tenantId", listOf("1").toSet())
  System.err.println(ConditionMatcher.match(senderConditions, conditionGroup))
}
