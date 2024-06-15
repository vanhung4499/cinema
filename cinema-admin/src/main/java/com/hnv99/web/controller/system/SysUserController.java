package com.hnv99.web.controller.system;

import com.hnv99.common.response.ResponseResult;
import com.hnv99.system.domain.SysUser;
import com.hnv99.system.domain.vo.SysUserVo;
import com.hnv99.system.service.impl.SysUserServiceImpl;
import com.hnv99.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SysUserController extends BaseController {
    @Autowired
    private SysUserServiceImpl sysUserService;

    @GetMapping("/sysUser")
    public ResponseResult findAllUsers(SysUser sysUser) {
        startPage();
        List<SysUser> data = sysUserService.findAllUsers(sysUser);
        return getResult(data);
    }

    @GetMapping("/sysUser/{id}")
    public ResponseResult findUserById(@PathVariable Long id) {
        return getResult(sysUserService.findUserById(id));
    }

    /**
     * Add user request, registration is also here
     * @param sysUser User information
     * @return Response message
     */
    @PostMapping("/sysUser")
    public ResponseResult addUser(@Validated @RequestBody SysUser sysUser) {
        return getResult(sysUserService.addUser(sysUser));
    }


    @PutMapping("/sysUser")
    public ResponseResult updateUser(@Validated @RequestBody SysUser sysUser) {
        return getResult(sysUserService.updateUser(sysUser));
    }

    @DeleteMapping("/sysUser/{ids}")
    public ResponseResult deleteUser(@PathVariable Long[] ids) {
        return getResult(sysUserService.deleteUser(ids));
    }

    /**
     * User login request
     *
     * @param sysUserVo User login information
     * @return Response message
     */
    @RequestMapping("/sysUser/login")
    public ResponseResult login(@RequestBody SysUserVo sysUserVo) {
        return getResult(sysUserService.login(sysUserVo));
    }


    /**
     * User registration request
     *
     * @param req User registration information
     * @return Response message
     */
    @PostMapping("/sysUser/register")
    public ResponseResult register(@Validated @RequestBody SysUser sysUser) {
        // Registration only accepts certain parameter values, create a new instance to receive only the parameters accepted for registration
        SysUser registerUserInfo = new SysUser();
        registerUserInfo.setUserName(sysUser.getUserName());
        registerUserInfo.setPassword(sysUser.getPassword());
        registerUserInfo.setSex(sysUser.getSex());
        registerUserInfo.setPhoneNumber(sysUser.getPhoneNumber());
        return getResult(sysUserService.addUser(registerUserInfo));
    }
}
