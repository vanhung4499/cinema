package com.hnv99.system.mapper;

import com.hnv99.system.domain.LoginUser;
import com.hnv99.system.domain.SysUser;
import com.hnv99.system.domain.vo.SysUserVo;

import java.util.List;

public interface SysUserMapper {
    List<SysUser> findAllUsers(SysUser sysUser);

    SysUser findUserById(Long id);

    /**
     * JWT authentication
     * @param userName username
     * @return user information
     */
    SysUser findUserByName(String userName);

    int addUser(SysUser sysUser);

    int updateUser(SysUser sysUser);

    int deleteUser(Long id);

    LoginUser findLoginUser(SysUserVo sysUserVo);

    /**
     * Query all IDs of users with the specified name
     *
     * @param userName username
     * @return List of user IDs
     */
    List<Long> checkUserNameUnique(String userName);
}
