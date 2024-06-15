package com.hnv99.system.service.impl;

import com.hnv99.common.utils.JwtUtil;
import com.hnv99.common.utils.SaltUtils;
import com.hnv99.system.domain.LoginUser;
import com.hnv99.system.domain.SysUser;
import com.hnv99.system.domain.vo.SysUserVo;
import com.hnv99.system.mapper.SysUserMapper;
import com.hnv99.system.service.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> findAllUsers(SysUser sysUser) {
        return sysUserMapper.findAllUsers(sysUser);
    }

    @Override
    public SysUser findUserById(Long id) {
        return sysUserMapper.findUserById(id);
    }

    @Override
    public SysUser findUserByName(String userName) {
        return sysUserMapper.findUserByName(userName);
    }

    /**
     * Handles the logic for user registration.
     *
     * @param sysUser The system user object containing user details.
     * @return The result of the user addition operation.
     */
    @Override
    public int addUser(SysUser sysUser) {
        // Check if the username is unique. -1L indicates a new user (not updating an existing user).
        if (!isUserNameUnique(sysUser.getUserName(), -1L)) {
            throw new AuthenticationException("Username already exists.");
        }

        // Generate a salt and hash the password using MD5 with the salt and hash iterations.
        String salt = SaltUtils.getSalt(8);
        Md5Hash md5Hash = new Md5Hash(sysUser.getPassword(), salt, 1024);

        // Set the hashed password and salt in the SysUser object.
        sysUser.setPassword(md5Hash.toHex());
        sysUser.setSalt(salt);

        // Call the mapper to add the user to the database.
        return sysUserMapper.addUser(sysUser);
    }

    /**
     * Handles the logic for updating a user.
     *
     * @param sysUser The system user object containing updated user details.
     * @return The result of the user update operation.
     */
    @Override
    public int updateUser(SysUser sysUser) {
        // Check if the username is unique for the user being updated.
        if (!isUserNameUnique(sysUser.getUserName(), sysUser.getUserId())) {
            throw new AuthenticationException("Username already exists.");
        }

        // Retrieve the original user details from the database.
        SysUser originUser = sysUserMapper.findUserById(sysUser.getUserId());
        if (originUser == null) {
            throw new AuthenticationException("User does not exist.");
        }

        // Check if the password has been changed.
        if (!originUser.getPassword().equals(sysUser.getPassword())) {
            // If the password is changed, rehash the new password with a new salt.
            String salt = SaltUtils.getSalt(8);
            Md5Hash md5Hash = new Md5Hash(sysUser.getPassword(), salt, 1024);

            // Set the new hashed password and salt in the SysUser object.
            sysUser.setPassword(md5Hash.toHex());
            sysUser.setSalt(salt);
        }

        // Call the mapper to update the user details in the database.
        return sysUserMapper.updateUser(sysUser);
    }

    @Override
    public int deleteUser(Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += sysUserMapper.deleteUser(id);
        }
        return rows;
    }

    @Override
    public LoginUser login(SysUserVo sysUserVo) {
        // Retrieve user information based on the provided username.
        SysUser user = sysUserMapper.findUserByName(sysUserVo.getUserName());
        if (user == null) {
            throw new AuthenticationException("Username does not exist.");
        }

        // Validate the password by hashing the provided password with the stored salt and comparing it.
        Md5Hash md5Hash = new Md5Hash(sysUserVo.getPassword(), user.getSalt(), 1024);
        if (!user.getPassword().equals(md5Hash.toHex())) {
            throw new AuthenticationException("Invalid username or password.");
        }

        // Set up the login user object.
        LoginUser loginUser = findLoginUser(sysUserVo);

        // Issue a token for the authenticated user.
        String token = JwtUtil.sign(user.getUserName(), user.getPassword());
        loginUser.setToken(token);

        return loginUser;
    }



    @Override
    public LoginUser findLoginUser(SysUserVo sysUserVo) {
        return sysUserMapper.findLoginUser(sysUserVo);
    }

    @Override
    public boolean isUserNameUnique(String userName, Long userId) {
        List<Long> userIds = sysUserMapper.checkUserNameUnique(userName);
        for (Long id : userIds) {
            if (id.equals(userId)) {
                return true;
            }
        }
        return userIds.isEmpty();
    }
}
