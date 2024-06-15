package com.hnv99.system.service.impl;

import com.hnv99.system.domain.SysCarousel;
import com.hnv99.system.mapper.SysCarouselMapper;
import com.hnv99.system.service.SysCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysCarouselServiceImpl implements SysCarouselService {
    @Autowired
    private SysCarouselMapper sysCarouselMapper;

    @Override
    public List<SysCarousel> findAllCarousels() {
        return sysCarouselMapper.findAllCarousels();
    }

    @Override
    public SysCarousel findCarouselById(Long id) {
        return sysCarouselMapper.findCarouselById(id);
    }

    @Override
    public int addCarousel(SysCarousel sysCarousel) {
        return sysCarouselMapper.addCarousel(sysCarousel);
    }

    @Override
    public int updateCarousel(SysCarousel sysCarousel) {
        return 0;
    }

    @Override
    public int deleteCarousel(Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += sysCarouselMapper.deleteCarousel(id);
        }
        return rows;
    }
}
