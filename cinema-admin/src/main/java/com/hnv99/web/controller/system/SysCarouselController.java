package com.hnv99.web.controller.system;

import com.hnv99.common.response.ResponseResult;
import com.hnv99.system.domain.SysCarousel;
import com.hnv99.system.service.SysCarouselService;
import com.hnv99.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class SysCarouselController extends BaseController {
    @Autowired
    private SysCarouselService sysCarouselService;

    @GetMapping("/sysCarousel")
    public ResponseResult findCarousel() {
        return getResult(sysCarouselService.findAllCarousels());
    }

    @PostMapping("/sysCarousel")
    public ResponseResult addCarousel(@Validated @RequestBody SysCarousel sysCarousel) {
        return getResult(sysCarouselService.addCarousel(sysCarousel));
    }

    @PutMapping("/sysCarousel")
    public ResponseResult updateCarousel(@Validated @RequestBody SysCarousel sysCarousel) {
        return getResult(sysCarouselService.updateCarousel(sysCarousel));
    }

    @DeleteMapping("/sysCarousel/{ids}")
    public ResponseResult deleteCarousel(@PathVariable Long[] ids) {
        return getResult(sysCarouselService.deleteCarousel(ids));
    }
}
