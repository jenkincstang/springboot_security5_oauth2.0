package cn.hinson.security;

import cn.hinson.domain.SysRole;
import cn.hinson.domain.SysUser;
import cn.hinson.service.SysRoleService;
import cn.hinson.service.SysUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GithubPrincipalExtractor extends AbstractPrincipalExtractor {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysRoleService sysRoleService;
    protected final Log logger = LogFactory.getLog(this.getClass());
    @Override
    public SysUser getUserByOpenId(String id) {
        System.out.println("GithubPrincipalExtractor");
        return sysUserService.getUserByGithubId(id);
    }

    @Override
    public SysRole getUserRoleByOauth2ClientName() {
//        SysRole role = new SysRole();
//        role.setName("GITHUB");
        System.out.println("ROLE_USER::>>"+sysRoleService.getSysRoleByName("ROLE_USER"));
        return sysRoleService.getSysRoleByName("ROLE_USER");
    }
}
