package com.hnv99.system.service;

import com.hnv99.system.domain.SysMovie;
import com.hnv99.system.domain.vo.SysMovieVo;

import java.util.List;

public interface SysMovieService {

    List<SysMovie> findAllMovies(SysMovieVo sysMovieVo);

    SysMovie findMovieById(Long id);

    SysMovie findOneMovie(Long id);

    int addMovie(SysMovie sysMovie);

    int updateMovie(SysMovie sysMovie);

    int deleteMovie(Long[] ids);

    // Get all movies shown in a single cinema
//    List<SysMovie> findByCinemaId(Long id);


    // Get various types of lists
    List<SysMovie> totalBoxOfficeList();

    List<SysMovie> domesticBoxOfficeList();

    List<SysMovie> foreignBoxOfficeList();

}
