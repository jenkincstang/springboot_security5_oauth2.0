package cn.hinson.security.service;

import cn.hinson.dao.SysUserDao;
import cn.hinson.domain.SysRole;
import cn.hinson.domain.SysUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService { //自定义UserDetailsService 接口

  @Autowired
  SysUserDao sysUserDao;

  protected final Log logger = LogFactory.getLog(this.getClass());

  @Override
  public UserDetails loadUserByUsername(String username) { //自定义认证
    System.out.println("loadUserByUserName: " + username);
    SysUser user = sysUserDao.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("用户名不存在");
    }
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    //用于添加用户的权限。只要把用户权限添加到authorities。
    for (SysRole role : user.getSysRoles()) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
      System.out.println("RoleName:>>"+role.getName());
    }
    UserDetails userDetails = new User(user.getUsername(), user.getPassword(), authorities);
    logger.info("userDetails" + userDetails);
    return userDetails;
  }
}