package com.hnv99.web.controller.system;

import com.hnv99.common.response.ResponseResult;
import com.hnv99.system.domain.SysSession;
import com.hnv99.system.domain.vo.SysSessionVo;
import com.hnv99.system.service.impl.SysSessionServiceImpl;
import com.hnv99.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SysSessionController extends BaseController {

    @Autowired
    private SysSessionServiceImpl sysSessionService;

    /**
     * Find all sessions based on the criteria in the vo. Set pageSize=100 or another large number in the frontend ticket purchasing part.
     *
     * @param sysSessionVo Session query criteria
     * @return Response message
     */
    @GetMapping("/sysSession")
    public ResponseResult findByVo(SysSessionVo sysSessionVo) {
        startPage();
        List<SysSession> list = sysSessionService.findByVo(sysSessionVo);
        return getResult(list);
    }

    @GetMapping("/sysSession/find/{id}")
    public ResponseResult findSessionById(@PathVariable Long id) {
        return getResult(sysSessionService.findSessionById(id));
    }

    @GetMapping("/sysSession/isAbleEdit")
    public ResponseResult findSessionByMovieIdOrHallId(SysSession sysSession) {
        return getResult(sysSessionService.findSessionByMovieIdOrHallId(sysSession));
    }

    @PostMapping("/sysSession")
    public ResponseResult addSession(@RequestBody SysSession sysSession) {
        return getResult(sysSessionService.addSession(sysSession));
    }

    @PutMapping("/sysSession")
    public ResponseResult updateSession(@RequestBody SysSession sysSession) {
        return getResult(sysSessionService.updateSession(sysSession));
    }

    @DeleteMapping("/sysSession/{ids}")
    public ResponseResult deleteSession(@PathVariable Long[] ids) {
        return getResult(sysSessionService.deleteSession(ids));
    }
}
