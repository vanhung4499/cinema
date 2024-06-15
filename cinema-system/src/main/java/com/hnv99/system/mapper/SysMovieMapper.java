package com.hnv99.system.mapper;

import com.hnv99.system.domain.SysMovie;
import com.hnv99.system.domain.vo.SysMovieVo;

import java.util.List;


public interface SysMovieMapper {

    /**
     * Perform a conditional pagination query based on the parameters of the passed-in object
     * @param sysMovieVo The object containing the query parameters
     * @return The list of movies that meet the query conditions
     */
    List<SysMovie> findAllMovies(SysMovieVo sysMovieVo);

    /**
     * Query by ID
     * @param id The ID of the movie to be queried
     * @return The movie information that meets the query conditions
     */
    SysMovie findMovieById(Long id);

    /**
     * Query information of a movie without querying related information
     * @param id The ID of the movie to be queried
     * @return The movie information that meets the query conditions
     */
    SysMovie findOneMovie(Long id);

    int addMovie(SysMovie sysMovie);

    int updateMovie(SysMovie sysMovie);

    int deleteMovie(Long id);

    /**
     * Query all movies showing in a cinema
     *
     * @param id The ID of the cinema to be queried
     * @return The list of movies that meet the query conditions
     */
    List<SysMovie> findMovieByCinemaId(Long id);

    /**
     * Total box office list
     * @return total box office list
     */
    List<SysMovie> totalBoxOfficeList();

    /**
     * Domestic box office list. Among the domestic movies that have been released, take the top 10 by box office. Domestic movies with movieArea in (Hong Kong, Taiwan, Mainland China).
     * @return domestic box office list
     */
    List<SysMovie> domesticBoxOfficeList();

    /**
     * Foreign box office list. Among the foreign movies that have been released, take the top 10 by box office. Foreign movies with movieArea not in (Hong Kong, Taiwan, Mainland China).
     * @return foreign box office list
     */
    List<SysMovie> foreignBoxOfficeList();

}
