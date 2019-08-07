package com.by.blcu.core.constant;

public class ProjectConstant {

   // 项目基础包名称
   public static final String BASE_PACKAGE = "com.by.blcu.mall";

   // Model所在包
   public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";

   // Mapper所在包
   public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";

   // Service所在包
   public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";

   // ServiceImpl所在包
   public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";

   // Controller所在包
   public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";

   // Mapper插件基础接口的完全限定名
   public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.universal.Dao";
   
 //文件上传储存的地址
   public static final String SAVEFILEPATH = "D://file";

   //token 二次加密
   public static final String TOKEN_CACHE_PREFIX = "blcu.cache.token.";

}