package com.test.server.pojo.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.function.Function;

/**
 * @author 费世程
 * @date 2020/10/13 0:27
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Res<T> {

  /**
   * 是否成功
   */
  private boolean success;
  /**
   * 响应状态码
   */
  private int code;
  /**
   * 响应说明
   */
  private String message;
  /**
   * 响应数据
   * jackson 实体转json 为NULL的字段不参加序列化（即不显示）
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;
  /**
   * 分页 页码
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer pageNum;
  /**
   * 分页 页大小
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer pageSize;
  /**
   * 分页 数据总数
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long total;

  private Res() {

  }

  public boolean isSuccess() {
    return success;
  }

  public boolean isFailure() {
    return !success;
  }

  //---------------------------------------- 静态方法 -------------------------------------------//

  @NotNull
  public static <T> Res<T> success() {
    return success(CommonResMsg.SUCCESS);
  }

  @NotNull
  public static <T> Res<T> success(@NotNull CommonResMsg resMsg) {
    Res<T> res = new Res<>();
    res.success = true;
    res.code = resMsg.code();
    res.message = resMsg.desc();
    return res;
  }

  @NotNull
  public static <T> Res<T> success(@NotNull String message) {
    Res<T> res = new Res<>();
    res.success = true;
    res.code = CommonResMsg.SUCCESS.code();
    res.message = message;
    return res;
  }

  @NotNull
  public static <T> Res<T> data(@NotNull T data) {
    Res<T> res = success(CommonResMsg.SUCCESS);
    res.data = data;
    return res;
  }

  @NotNull
  public static <T> Res<T> data(@NotNull T data, @NotNull String message) {
    Res<T> res = new Res<>();
    res.success = true;
    res.code = CommonResMsg.SUCCESS.code();
    res.message = message;
    res.data = data;
    return res;
  }

  @NotNull
  public static <T> Res<T> error() {
    return error(CommonResMsg.BAD_REQUEST);
  }

  @NotNull
  public static <T> Res<T> error(CommonResMsg resMsg) {
    Res<T> res = new Res<>();
    res.success = false;
    res.code = resMsg.code();
    res.message = resMsg.desc();
    return res;
  }

  @NotNull
  public static <T> Res<T> error(String message) {
    Res<T> res = new Res<>();
    res.success = false;
    res.code = CommonResMsg.BAD_REQUEST.code();
    res.message = message;
    return res;
  }

  @NotNull
  public static <T> Res<T> exception() {
    Res<T> res = new Res<>();
    res.success = false;
    res.code = CommonResMsg.INTERNAL_SERVER_ERROR.code();
    res.message = CommonResMsg.INTERNAL_SERVER_ERROR.desc();
    return res;
  }

  @NotNull
  public static <T> Res<T> exception(Throwable t) {
    return exception(t.getMessage());
  }

  @NotNull
  public static <T> Res<T> exception(String message) {
    Res<T> res = new Res<>();
    res.success = false;
    res.code = CommonResMsg.INTERNAL_SERVER_ERROR.code();
    res.message = message;
    return res;
  }

  @NotNull
  public <R> Res<R> convertNewRes(Function<T, R> function) {
    Res<R> res = new Res<>();
    res.success = this.success;
    res.code = this.code;
    res.message = this.message;
    if (this.data != null) {
      res.data = function.apply(this.getData());
    }
    res.pageNum = this.pageNum;
    res.pageSize = this.pageSize;
    res.total = this.total;
    return res;
  }

  //------------------------------------------- getter~setter~ -----------------------------------

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public int getCode() {
    return code;
  }

  public Res<T> setCode(int code) {
    this.code = code;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total = total;
  }

}
