package com.hnv99.web.controller.system;

import com.hnv99.common.response.ResponseResult;
import com.hnv99.system.domain.SysHall;
import com.hnv99.system.service.impl.SysHallServiceImpl;
import com.hnv99.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class SysHallController extends BaseController {

    @Autowired
    private SysHallServiceImpl sysHallService;

    @GetMapping("/sysHall")
    public ResponseResult findAllHalls(SysHall sysHall) {
        startPage();
        return getResult(sysHallService.findAllHalls(sysHall));
    }

    @GetMapping("/sysHall/{hallId}")
    public ResponseResult findHallById(@PathVariable Long hallId) {
        SysHall sysHall = new SysHall();
        sysHall.setHallId(hallId);
        return getResult(sysHallService.findHallById(sysHall));
    }

    @PostMapping("/sysHall")
    public ResponseResult addHall(@Validated @RequestBody SysHall sysHall) {
        return getResult(sysHallService.addHall(sysHall));
    }

    @PutMapping("/sysHall")
    public ResponseResult updateHall(@Validated @RequestBody SysHall sysHall) {
        int rows = sysHallService.updateHall(sysHall);
        return getResult(rows);
    }

    @PostMapping("/sysHall/delete")
    public ResponseResult deleteHall(@RequestBody SysHall[] sysHalls) {
        return getResult(sysHallService.deleteHall(sysHalls));
    }

}
