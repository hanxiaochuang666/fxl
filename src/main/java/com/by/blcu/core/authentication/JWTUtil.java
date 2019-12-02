package com.by.blcu.core.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.by.blcu.core.configurer.ShiroConfigurer;
import com.by.blcu.core.utils.SpringContextUtil;
import com.by.blcu.manager.model.ManagerOrgType;
import com.by.blcu.manager.model.SsoUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class JWTUtil {
//    public static Logger log = LoggerFactory.getLogger(JWTUtil.class);
      private static final long EXPIRE_TIME = SpringContextUtil.getBean(ShiroConfigurer.class).getJwtTimeOut() * 1000;

    /**
     * 校验 token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            log.info("token is valid");
            return true;
        } catch (Exception e) {
            log.info("token is invalid{}", e.getMessage());
            return false;
        }
    }

    /**
     * 从 token中获取用户名
     *
     * @return token中包含的用户名
     */
    public static Map<String, Claim> getUserInfo(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaims();
        } catch (JWTDecodeException e) {
            log.error("error：{}", e.getMessage());
            return null;
        }
    }


    /**
     * 生成 token
     *
     * @param ssoUser  用户信息
     * @param secret   用户的密码
     * @return token
     */
    public static String sign(SsoUser ssoUser, String secret, List<ManagerOrgType> orgCodes) {
        try {
            //多机构支持
            ManagerOrgType orgType = orgCodes.get(0);
            String username = StringUtils.lowerCase(ssoUser.getUserName());
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim("username", username)
                    .withClaim("userId", ssoUser.getId())
                    .withClaim("orgCode", orgType.getOrgCode())
                    .withClaim("type", orgType.getType())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("error：{}", e);
            return null;
        }
    }

}
