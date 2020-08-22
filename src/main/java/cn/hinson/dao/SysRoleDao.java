package cn.hinson.dao;

import cn.hinson.domain.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleDao extends JpaRepository<SysRole, Integer> {

  SysRole findByName(String roleName);
}