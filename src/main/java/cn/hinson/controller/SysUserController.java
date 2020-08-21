package cn.hinson.controller;

import cn.hinson.domain.SysUser;
import cn.hinson.dto.UserDto;
import cn.hinson.service.SysUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SysUserController {

  protected final Log logger = LogFactory.getLog(this.getClass());
  @Autowired
  private final SysUserService sysUserService;

  public SysUserController(SysUserService sysUserService) {
    this.sysUserService = sysUserService;
  }

  @GetMapping("/user")
  public Principal getUsers(Principal principal) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    logger.info(authentication.getName());
    return principal;
  }

  @PostMapping("/register")
  public String register(@RequestBody UserDto userDto) {
    //访问这个url注册一个用户
    SysUser sysUser = new SysUser();
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    sysUser.setUsername(userDto.getUsername());
    sysUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
    sysUserService.saveSysUser(sysUser);
    return "Register User Success!";
  }
}
