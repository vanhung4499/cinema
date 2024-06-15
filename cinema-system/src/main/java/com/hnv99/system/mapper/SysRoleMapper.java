package com.hnv99.system.mapper;

import com.hnv99.system.domain.SysRole;

import java.util.List;

public interface SysRoleMapper {

    List<SysRole> findAllRoles();

    SysRole findRoleById(Long id);

    int addRole(SysRole sysRole);

    int updateRole(SysRole sysRole);

    int deleteRole(Long id);

    // Assign permissions to the current role
    int addRight(Long roleId, Long resourceId);

    int deleteRight(Long roleId, Long resourceId);

    // Query all third-level permission IDs for the specified role
    List<Long> findAllRights(Long roleId);

}
