package cn.hinson.dao;

import cn.hinson.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserDao extends JpaRepository<SysUser, Integer> {

  SysUser findByUsername(String username);

  SysUser findByFacebookId(String facebookId);

  SysUser findByTwitterId(String twitterId);

  SysUser findByGithubId(String githubId);
}