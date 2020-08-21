package cn.hinson.service;

import cn.hinson.domain.SysUser;
import cn.hinson.dto.UserDto;

public interface SysUserService {
    SysUser saveSysUser(UserDto userDto);
    SysUser saveSysUser(SysUser sysUser);
    SysUser getUserByName(String username);
    SysUser getUserByTwitterId(String twitterId);
    SysUser getUserByFacebookId(String facebookId);
    SysUser getUserByGithubId(String githubId);
}
