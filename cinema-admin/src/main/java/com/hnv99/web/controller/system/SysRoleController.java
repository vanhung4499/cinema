package com.hnv99.web.controller.system;

import com.hnv99.common.response.ResponseResult;
import com.hnv99.system.domain.SysRole;
import com.hnv99.system.service.impl.SysRoleServiceImpl;
import com.hnv99.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SysRoleController extends BaseController {

    @Autowired
    SysRoleServiceImpl sysRoleService;

    @GetMapping("/sysRole")
    public ResponseResult findAllRoles() {
        startPage();
        List<SysRole> data = sysRoleService.findAllRoles();
        return getResult(data);
    }

    @GetMapping("/sysRole/{id}")
    public ResponseResult findRoleById(@PathVariable Long id) {
        return getResult(sysRoleService.findRoleById(id));
    }

    @PostMapping("/sysRole")
    public ResponseResult addRole(@Validated @RequestBody SysRole sysRole) {
        return getResult(sysRoleService.addRole(sysRole));
    }

    @PutMapping("/sysRole")
    public ResponseResult updateRole(@Validated @RequestBody SysRole sysRole) {
        return getResult(sysRoleService.updateRole(sysRole));
    }

    @DeleteMapping("/sysRole/{ids}")
    public ResponseResult deleteRole(@PathVariable Long[] ids) {
        return getResult(sysRoleService.deleteRole(ids));
    }

    /**
     * Assign permissions to the role with the specified ID, including adding or deleting permissions.
     * @param roleId Role ID
     * @param keys Permission IDs
     * @return Response message
     */
    @PostMapping("/sysRole/{roleId}")
    public ResponseResult allotRight(@PathVariable Long roleId, @RequestBody Long[] keys) {
        return getResult(sysRoleService.allotRight(roleId, keys));
    }

}
