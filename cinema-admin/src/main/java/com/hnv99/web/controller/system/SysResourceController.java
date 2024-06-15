package com.hnv99.web.controller.system;

import com.hnv99.common.response.ResponseResult;
import com.hnv99.system.domain.SysResource;
import com.hnv99.system.service.impl.SysResourceServiceImpl;
import com.hnv99.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SysResourceController extends BaseController {

    @Autowired
    SysResourceServiceImpl sysResourceService;

    @GetMapping("/sysResource")
    public ResponseResult findAllResources() {
        startPage();
        List<SysResource> data = sysResourceService.findAllResources();
        return getResult(data);
    }

    @GetMapping("/sysResource/children")
    public ResponseResult findWithChildren() {
        return getResult((Object) sysResourceService.findWithChildren());
    }

    @GetMapping("/sysResource/{id}")
    public ResponseResult findResourceById(@PathVariable Long id) {
        return getResult(sysResourceService.findResourceById(id));
    }

    @GetMapping("/sysResource/tree")
    public ResponseResult findAllWithAllChildren() {
        return getResult(sysResourceService.findAllWithAllChildren());
    }

    @PostMapping("/sysResource")
    public ResponseResult addResource(@Validated @RequestBody SysResource sysResource) {
        return getResult(sysResourceService.addResource(sysResource));
    }

    @PutMapping("/sysResource")
    public ResponseResult updateResource(@Validated @RequestBody SysResource sysResource) {
        return getResult(sysResourceService.updateResource(sysResource));
    }

    @DeleteMapping("/sysResource/{ids}")
    public ResponseResult deleteResource(@PathVariable Long[] ids) {
        return getResult(sysResourceService.deleteResource(ids));
    }

}
