package com.hnv99.system.service;

import com.hnv99.system.domain.SysRole;

import java.util.List;


public interface SysRoleService {
    List<SysRole> findAllRoles();

    SysRole findRoleById(Long id);

    int addRole(SysRole sysRole);

    int updateRole(SysRole sysRole);

    int deleteRole(Long[] ids);

    int allotRight(Long roleId, Long[] resourceIds);
}
