package cn.hinson.dao;

import cn.hinson.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserDao extends JpaRepository<SysUser, Integer> {

  SysUser findByName(String username);

  SysUser findByFacebookId(String facebookId);

  SysUser findByTwitterId(String twitterId);

  SysUser findByGithubId(String githubId);
}