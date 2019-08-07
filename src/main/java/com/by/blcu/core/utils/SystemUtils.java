package com.by.blcu.core.utils;

import com.by.blcu.core.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 系统工具类
 *
 * @author MrBird
 */
public class SystemUtils {

    private static Logger log = LoggerFactory.getLogger(SystemUtils.class);

    /**
     * 模拟两个用户
     *
     * @return List<User>
     */
    private static List<User> users() {
        List<User> users = new ArrayList<>();
        // 模拟两个用户：
        // 1. 用户名 admin，密码 123456，角色 admin（管理员），权限 "user:add"，"user:view"
        // 1. 用户名 scott，密码 123456，角色 regist（注册用户），权限 "user:view"
        users.add(new User(
                "admin",
                "bfc62b3f67a4c3e57df84dad8cc48a3b",
                new HashSet<>(Collections.singletonList("admin")),
                new HashSet<>(Arrays.asList("user:add", "user:view","user:view1","user:view2","user:view3","user:view4","user:view5","user:view6","user:v1iew","user:2view","user:3view","user:v2iew","use2r:view","user:view","user:view"
                ,"user:vie32fdw","user:12view","user:kklview","user:vaiew","user:vixew","user:hyview","usesdfr:view","user:gfdview","user:sfdview","user:vsiew","userdf:view","us0er:view","user789:view","us8er:vggiew","user:456view","use43r:view","user:view","user:view",
                        "user:vdiew","useioir:view","user:view","user:view","user:view","user:viedfw","user:vsfiew","user:vi2ew","user:vdfiew","user:vidfgew","user:wqview","user:vniew","user:vivew","user:vbview"
                ,"usehr:view","user:viasdew","user:12view","user:vi2ew","user:vsiew","user:vixew","user:bnview","usebnr:view","user:vbnview"))));
        users.add(new User(
                "scott",
                "11bd73355c7bbbac151e4e4f943e59be",
                new HashSet<>(Collections.singletonList("regist")),
                new HashSet<>(Collections.singletonList("user:view"))));
        return users;
    }

    /**
     * 获取用户
     *
     * @param username 用户名
     * @return 用户
     */
    public static User getUser(String username) {
        List<User> users = SystemUtils.users();
        return users.stream().filter(user -> StringUtils.equalsIgnoreCase(username, user.getUsername())).findFirst().orElse(null);
    }

}
