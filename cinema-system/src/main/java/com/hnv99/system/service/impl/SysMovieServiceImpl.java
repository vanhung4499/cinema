package com.hnv99.system.service.impl;

import com.hnv99.system.domain.SysMovie;
import com.hnv99.system.domain.vo.SysMovieVo;
import com.hnv99.system.mapper.SysMovieMapper;
import com.hnv99.system.service.SysMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMovieServiceImpl implements SysMovieService {

    @Autowired
    private SysMovieMapper sysMovieMapper;

    @Override
    public List<SysMovie> findAllMovies(SysMovieVo sysMovieVo) {
        return sysMovieMapper.findAllMovies(sysMovieVo);
    }

    @Override
    public SysMovie findMovieById(Long id) {
        return sysMovieMapper.findMovieById(id);
    }

    @Override
    public SysMovie findOneMovie(Long id) {
        return sysMovieMapper.findOneMovie(id);
    }

    @Override
    public int addMovie(SysMovie sysMovie) {
        return sysMovieMapper.addMovie(sysMovie);
    }

    @Override
    public int updateMovie(SysMovie sysMovie) {
        return sysMovieMapper.updateMovie(sysMovie);
    }

    @Override
    public int deleteMovie(Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += sysMovieMapper.deleteMovie(id);
        }
        return rows;
    }

    /**
     * Total box office list
     *
     * @return
     */
    @Override
    public List<SysMovie> totalBoxOfficeList() {
        return sysMovieMapper.totalBoxOfficeList();
    }

    /**
     * Domestic box office list. Among the domestic movies that have been released, take the top 10 by box office. Domestic movies with movieArea in (Hong Kong, Taiwan, Mainland China).
     *
     * @return
     */
    @Override
    public List<SysMovie> domesticBoxOfficeList() {
        return sysMovieMapper.domesticBoxOfficeList();
    }

    /**
     * Foreign box office list. Among the foreign movies that have been released, take the top 10 by box office. Foreign movies with movieArea not in (Hong Kong, Taiwan, Mainland China).
     *
     * @return
     */
    @Override
    public List<SysMovie> foreignBoxOfficeList() {
        return sysMovieMapper.foreignBoxOfficeList();
    }

}
