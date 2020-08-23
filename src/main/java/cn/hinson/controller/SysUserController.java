package cn.hinson.controller;

import cn.hinson.domain.SysRole;
import cn.hinson.domain.SysUser;
import cn.hinson.dto.UserDto;
import cn.hinson.service.SysRoleService;
import cn.hinson.service.SysUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SysUserController {

    protected final Log logger = LogFactory.getLog(this.getClass());
    @Autowired
    private final SysUserService sysUserService;
    @Autowired
    private final SysRoleService sysRoleService;

    public SysUserController(SysUserService sysUserService, SysRoleService sysRoleService) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
    }

    @GetMapping("/test")
    public String test() {
        //访问这个url注册一个username：hinson1 password：password的用户
        SysUser sysUser = sysUserService.getUserByName("admin");
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setUsername("admin");
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String tmp = passwordEncoder.encode("password");
            sysUser.setPassword(tmp);
            SysRole adminRole = sysRoleService.getSysRoleByName("ROLE_ADMIN");
            SysRole userRole = sysRoleService.getSysRoleByName("ROLE_USER");
            List<SysRole> roles = new ArrayList<>();
            roles.add(adminRole);
            roles.add(userRole);
            sysUser.setSysRoles(roles);
            sysUserService.saveSysUser(sysUser);
        }
        return "success";
    }

//    @GetMapping("/login")
//    public String login(){
//        return ": login";
//    }

    @GetMapping("/user")
    public Principal getUsers(@AuthenticationPrincipal Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AuthenticationPrincipal Name:>>" + authentication.getName());
        return principal;
    }

    @GetMapping("/userManager")
    public String getUserManagerInfo() {
        return "User Manager Page";
    }

    @GetMapping("/sysManager")
    public String getSystemManagerInfo() {
        return "System Manager Page";
    }

    @GetMapping("/userInfo")
    public String getUserInfo() {
        return "User Information Page";
    }

    @PostMapping("/register")
    public String register(@RequestBody UserDto userDto) {
        System.out.println("Register User");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //访问这个url注册一个用户
        SysUser existUser = sysUserService.getUserByName(userDto.getUsername());
        if (existUser == null) {
            SysUser sysUser = new SysUser();
            sysUser.setUsername(userDto.getUsername());
            sysUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            sysUserService.saveSysUser(sysUser);
        } else {
            if (existUser.getPassword() == null) {
                existUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
                sysUserService.saveSysUser(existUser);
            } else {
                return "User already Exists!";
            }
        }
        return "Register User Success!";
    }
}
