package cn.hinson.service;

import cn.hinson.domain.SysRole;

public interface SysRoleService {

  SysRole saveSysRole(SysRole sysRole);

  SysRole getSysRoleByName(String username);
}
