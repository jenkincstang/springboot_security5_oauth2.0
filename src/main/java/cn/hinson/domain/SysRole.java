package cn.hinson.domain;

import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "sys_role")
public class SysRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  @ManyToMany(mappedBy = "sysRoles",fetch= FetchType.EAGER)
  private List<SysUser> sysUsers;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<SysUser> getSysUsers() {
    return sysUsers;
  }

  public void setSysUsers(List<SysUser> sysUsers) {
    this.sysUsers = sysUsers;
  }
}
