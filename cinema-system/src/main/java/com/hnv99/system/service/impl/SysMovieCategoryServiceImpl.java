package com.hnv99.system.service.impl;

import com.hnv99.system.domain.SysMovieCategory;
import com.hnv99.system.domain.SysMovieToCategory;
import com.hnv99.system.mapper.SysMovieCategoryMapper;
import com.hnv99.system.service.SysMovieCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysMovieCategoryServiceImpl implements SysMovieCategoryService {

    @Autowired
    private SysMovieCategoryMapper sysMovieCategoryMapper;

    @Override
    public List<SysMovieCategory> findAllCategories() {
        return sysMovieCategoryMapper.findAllCategorys();
    }

    @Override
    public SysMovieCategory findCategoryById(Long id) {
        return sysMovieCategoryMapper.findCategoryById(id);
    }

    @Override
    public int addCategory(SysMovieCategory sysMovieCategory) {
        return sysMovieCategoryMapper.addCategory(sysMovieCategory);
    }

    @Override
    public int updateCategory(SysMovieCategory sysMovieCategory) {
        return sysMovieCategoryMapper.updateCategory(sysMovieCategory);
    }

    @Override
    public int deleteCategory(Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += sysMovieCategoryMapper.deleteCategory(id);
        }
        return rows;
    }


    @Override
    public int addMovieToCategory(SysMovieToCategory sysMovieToCategory) {
        return sysMovieCategoryMapper.addMovieToCategory(sysMovieToCategory);
    }

    @Override
    public int deleteMovieToCategory(SysMovieToCategory sysMovieToCategory) {
        return sysMovieCategoryMapper.deleteMovieToCategory(sysMovieToCategory);
    }

}
