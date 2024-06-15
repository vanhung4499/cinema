package com.hnv99.web.controller.system;

import com.hnv99.common.exception.DataNotFoundException;
import com.hnv99.common.response.ResponseResult;
import com.hnv99.common.utils.ApplicationContextUtils;
import com.hnv99.system.domain.SysBill;
import com.hnv99.system.domain.SysMovie;
import com.hnv99.system.domain.SysSession;
import com.hnv99.system.domain.vo.SysBillVo;
import com.hnv99.system.service.impl.SysBillServiceImpl;
import com.hnv99.system.service.impl.SysMovieServiceImpl;
import com.hnv99.system.service.impl.SysSessionServiceImpl;
import com.hnv99.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SysBillController extends BaseController {

    @Autowired
    private SysBillServiceImpl sysBillService;

    @Autowired
    private SysSessionServiceImpl sysSessionService;

    @Autowired
    private SysMovieServiceImpl sysMovieService;

    @GetMapping("/sysBill")
    public ResponseResult findAllBills(SysBill sysBill) {
        startPage();
        // Cancel all timed out orders and release seat resources
        ApplicationContextUtils.getBean("cancelTimeoutBill");
        List<SysBill> data = sysBillService.findAllBills(sysBill);
        return getResult(data);
    }

    @GetMapping("/sysBill/{id}")
    public ResponseResult findBillById(@PathVariable Long id) {
        return getResult(sysBillService.findBillById(id));
    }

    @PostMapping("/sysBill")
    public ResponseResult addBill(@Validated @RequestBody SysBillVo sysBillVo) {
        // Get current session information
        SysSession curSession = sysSessionService.findOneSession(sysBillVo.getSysBill().getSessionId());
        if (curSession == null) {
            throw new DataNotFoundException("The session for adding orders was not found");
        }
        System.out.println(curSession.getSessionSeats());
        curSession.setSessionSeats(sysBillVo.getSessionSeats());

        int addSallNums = sysBillVo.getSysBill().getSeats().split(",").length;
        curSession.setSallNums(curSession.getSallNums() + addSallNums);
        // Update session seat information
        sysSessionService.updateSession(curSession);

        Object obj = sysBillService.addBill(sysBillVo.getSysBill());
        if (obj instanceof Integer) {
            return getResult((Integer) obj);
        }
        return getResult(obj);
    }

    @PutMapping("/sysBill")
    public ResponseResult pay(@RequestBody SysBill sysBill) {
        int rows = sysBillService.updateBill(sysBill);
        if (rows > 0 && sysBill.getPayState()) {
            // Update seat status for the session
            SysSession curSession = sysSessionService.findOneSession(sysBill.getSessionId());
            if (curSession == null) {
                throw new DataNotFoundException("Session for paying orders not found");
            }
            // Update movie box office
            SysMovie curMovie = sysMovieService.findOneMovie(curSession.getMovieId());
            if (curMovie == null) {
                throw new DataNotFoundException("Movie for paying orders not found");
            }
            // Number of seats for the order
            int seatNum = sysBill.getSeats().split(",").length;
            double price = curSession.getSessionPrice();
            curMovie.setMovieBoxOffice(curMovie.getMovieBoxOffice() + seatNum * price);
            sysMovieService.updateMovie(curMovie);
        }
        return getResult(rows);
    }

    @PutMapping("/sysBill/cancel")
    public ResponseResult cancel(@RequestBody SysBillVo sysBillVo) {
        // Order cancellation, update order status
        int rows = sysBillService.updateBill(sysBillVo.getSysBill());
        if (rows > 0 && sysBillVo.getSysBill().getCancelState()) {
            // The canceled order seats are no longer occupied, update the seat status of the session
            SysSession curSession = sysSessionService.findOneSession(sysBillVo.getSysBill().getSessionId());
            // Number of canceled order seats
            int cancelSallNums = sysBillVo.getSysBill().getSeats().split(",").length;
            curSession.setSallNums(curSession.getSallNums() - cancelSallNums);
            if (curSession == null) {
                throw new DataNotFoundException("The session for adding orders was not found");
            }
            curSession.setSessionSeats(sysBillVo.getSessionSeats());
            sysSessionService.updateSession(curSession);
        }
        return getResult(rows);
    }

    @DeleteMapping("/sysBill/{ids}")
    public ResponseResult deleteBill(@PathVariable Long[] ids) {
        return getResult(sysBillService.deleteBill(ids));
    }

}
