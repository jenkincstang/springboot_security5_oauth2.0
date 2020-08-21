package cn.hinson.service.imp;

import cn.hinson.dao.SysRoleDao;
import cn.hinson.dao.SysUserDao;
import cn.hinson.domain.SysRole;
import cn.hinson.domain.SysUser;
import cn.hinson.dto.UserDto;
import cn.hinson.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserServiceImp implements SysUserService {

    @Autowired
    SysUserDao sysUserDao;
    @Autowired
    SysRoleDao sysRoleDao;

    @Override
    public SysUser saveSysUser(UserDto userDto) {
        SysUser sysUser = new SysUser();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        sysUser.setUsername(userDto.getUsername());
        sysUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        SysRole sysRole = sysRoleDao.findByName("ROLE_USER");
        List<SysRole> roles = new ArrayList<>();
        roles.add(sysRole);
        sysUser.setSysRoles(roles);
        return sysUserDao.save(sysUser);
    }

    @Override
    public SysUser saveSysUser(SysUser sysUser) {
        return sysUserDao.save(sysUser);
    }

    @Override
    public SysUser getUserByName(String username) {
        return sysUserDao.findByName(username);
    }

    @Override
    public SysUser getUserByTwitterId(String twitterId) {
        return sysUserDao.findByTwitterId(twitterId);
    }

    @Override
    public SysUser getUserByFacebookId(String facebookId) {
        return sysUserDao.findByFacebookId(facebookId);
    }

    @Override
    public SysUser getUserByGithubId(String githubId) {
        return sysUserDao.findByGithubId(githubId);
    }
}
