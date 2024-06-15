package com.hnv99.system.mapper;

import com.hnv99.system.domain.SysMovieCategory;
import com.hnv99.system.domain.SysMovieToCategory;

import java.util.List;


public interface SysMovieCategoryMapper {

    List<SysMovieCategory> findAllCategorys();

    SysMovieCategory findCategoryById(Long id);

    List<SysMovieCategory> findOneMovieCategorys(Long id);

    int addCategory(SysMovieCategory sysMovieCategory);

    int updateCategory(SysMovieCategory sysMovieCategory);

    int deleteCategory(Long id);

    int addMovieToCategory(SysMovieToCategory sysMovieToCategory);

    int deleteMovieToCategory(SysMovieToCategory sysMovieToCategory);

}
