package com.by.blcu.core.ret;

/**
 * @Description: 响应码枚举，参考HTTP状态码的语义
 * @author 
 */
public enum RetCode {

   // 成功
   SUCCESS(200),

   // 业务异常
   BUSINESS_ERROR(300),

   // 失败
   FAIL(400),

   // 未认证（签名错误）
   UNAUTHORIZED(401),

   // 接口不存在
   NOT_FOUND(404),

   // 服务器内部错误
   INTERNAL_SERVER_ERROR(500);

   public int code;

   RetCode(int code) {
      this.code = code;
   }
}