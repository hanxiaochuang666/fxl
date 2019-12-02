package com.by.blcu.core.constant;

public class RedisBusinessKeyConst {

    //for user Authentication 用户认证
	public static class Authentication{
		public static final String AuthenticationPrefixKey 	= "ATH.";
		//token 二次加密
		public static final String TOKEN_CACHE_PREFIX = "ATH.user.token.";
		//在线用户记录
		public static final String TOKEN_ACTIVE_USER = "ATH.active.user";
	}

    //for mall management 系统管理
    public static class SystemManager{
        public static final String MALL_PREFIX = "SM.";

    }

	//for mall management 商城管理
	public static class Mall{
		public static final String MALL_PREFIX = "ML.";

	}

	//for course management 课程管理
	public static class Course{
        public static final String COURSE_PREFIX = "CS.";

	}

    //for ShortMessage 短消息业务.
	public static class ShortMessage{
        public static final String SHORT_MESSAGE_PREFIX = "SMS.";

	}
	

}
