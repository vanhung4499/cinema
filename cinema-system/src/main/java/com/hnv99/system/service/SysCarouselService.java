package com.hnv99.system.service;

import com.hnv99.system.domain.SysCarousel;

import java.util.List;

public interface SysCarouselService {
    List<SysCarousel> findAllCarousels();

    SysCarousel findCarouselById(Long id);

    int addCarousel(SysCarousel sysCarousel);

    int updateCarousel(SysCarousel sysCarousel);

    int deleteCarousel(Long[] ids);
}
