package com.test.api.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * @author 费世程
 * @date 2021/3/18 10:04
 */
@Suppress("unused")
object JsonUtils {
  private val mapper = ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .findAndRegisterModules()

  private val ignoreNullMapper = ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .findAndRegisterModules()

  @JvmOverloads
  fun <T> toJsonString(t: T, ignoreNull: Boolean = true): String {
    val writerMapper = if (ignoreNull) ignoreNullMapper else mapper
    return writerMapper.writeValueAsString(t)
  }

  fun <T> parseJson(jsonString: String, clazz: Class<T>): T {
    return ignoreNullMapper.readValue(jsonString, clazz)
  }

}
