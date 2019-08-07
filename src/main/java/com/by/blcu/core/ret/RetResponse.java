package com.by.blcu.core.ret;

/**
 * @Description: 将结果转换为封装后的对象
 * @author 李程
 * @date 2019
 */
public class RetResponse {

   private final static String SUCCESS = "success";

   public static <T> RetResult<T> makeOKRsp() {
      return new RetResult<T>().setCode(RetCode.SUCCESS).setMsg(SUCCESS);
   }

   public static <T> RetResult<T> makeOKRsp(T data) {
      return new RetResult<T>().setCode(RetCode.SUCCESS).setMsg(SUCCESS).setData(data);
   }

   public static <T> RetResult<T> makeErrRsp(String message) {
      return new RetResult<T>().setCode(RetCode.FAIL).setMsg(message);
   }

   public static <T> RetResult<T> makeRsp(int code, String msg) {
      return new RetResult<T>().setCode(code).setMsg(msg);
   }
   
   public static <T> RetResult<T> makeRsp(int code, String msg, T data) {
      return new RetResult<T>().setCode(code).setMsg(msg).setData(data);
   }

   public static <T> RetResult<T> makeRsp(T data,long total){
      return new RetResult<T>().setCode(RetCode.SUCCESS).setData(data).setTotal(total);
   }
}