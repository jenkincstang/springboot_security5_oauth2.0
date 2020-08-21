package cn.hinson.service.imp;

import cn.hinson.dao.SysRoleDao;
import cn.hinson.domain.SysRole;
import cn.hinson.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImp implements SysRoleService {

  @Autowired
  SysRoleDao sysRoleDao;

  @Override
  public SysRole saveSysRole(SysRole sysRole) {
    return sysRoleDao.save(sysRole);
  }

  @Override
  public SysRole getSysRoleByName(String roleName) {
    return sysRoleDao.findByName(roleName);
  }
}
