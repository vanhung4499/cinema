package com.hnv99.system.service;

import com.hnv99.system.domain.SysMovieCategory;
import com.hnv99.system.domain.SysMovieToCategory;

import java.util.List;


public interface SysMovieCategoryService {
    List<SysMovieCategory> findAllCategories();

    SysMovieCategory findCategoryById(Long id);

    int addCategory(SysMovieCategory sysMovieCategory);

    int updateCategory(SysMovieCategory sysMovieCategory);

    int deleteCategory(Long[] ids);

    int addMovieToCategory(SysMovieToCategory sysMovieToCategory);

    int deleteMovieToCategory(SysMovieToCategory sysMovieToCategory);


}
