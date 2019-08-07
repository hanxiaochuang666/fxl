package com.by.blcu.core.ret;

import java.io.Serializable;

/**
 * @Description: 返回对象实体
 */
public class RetResult<T> implements Serializable {

   private static final long serialVersionUID = -157218739114393541L;
   public int code;

   private String msg;

   private T data;

   private long total;

   public RetResult<T> setCode(RetCode retCode) {
      this.code = retCode.code;
      return this;
   }

   public int getCode() {
      return code;
   }

   public RetResult<T> setCode(int code) {
      this.code = code;
      return this;
   }

   public String getMsg() {
      return msg;
   }

   public RetResult<T> setMsg(String msg) {
      this.msg = msg;
      return this;
   }

   public T getData() {
      return data;
   }

   public RetResult<T> setData(T data) {
      this.data = data;
      return this;
   }

   public long getTotal() {
      return total;
   }

   public RetResult<T> setTotal(long total) {
      this.total = total;
      return this;
   }

}