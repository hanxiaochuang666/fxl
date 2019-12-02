package com.by.blcu.core.ret;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Description: 业务类异常
 * 
 */
@Slf4j
public class ServiceException extends RuntimeException implements Serializable{

   private static final long serialVersionUID = 1213855733833039552L;

   public ServiceException() {
   }

   public ServiceException(String message) {
      super(message);
      log.error("mesg is "+message);
   }


   public ServiceException(String message, Throwable cause) {
      super(message, cause);
   }

   public ServiceException(Throwable cause) {
      super(cause);
   }

}