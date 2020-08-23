package cn.hinson.security.service;

import cn.hinson.dao.SysUserDao;
import cn.hinson.security.AbstractPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;

public class MyUserInfoTokenServices extends UserInfoTokenServices {

    public MyUserInfoTokenServices(String userInfoEndPointUrl, String clientId, AbstractPrincipalExtractor principalExtractor) {
        super(userInfoEndPointUrl, clientId);
        super.setPrincipalExtractor(principalExtractor);
    }
}
