package com.hnv99.system.mapper;

import com.hnv99.system.domain.SysSession;
import com.hnv99.system.domain.vo.SysSessionVo;

import java.util.List;

public interface SysSessionMapper {

    /**
     * Query based on conditions
     *
     * @param sysSessionVo Query conditions
     * @return List of sessions
     */
    List<SysSession> findByVo(SysSessionVo sysSessionVo);

    /**
     * Query one
     *
     * @param sysSession Query conditions
     * @return List of sessions
     */
    List<SysSession> findSessionByMovieIdOrHallId(SysSession sysSession);

    /**
     * Query one
     *
     * @param id Session ID
     * @return Session information
     */
    SysSession findSessionById(Long id);

    /**
     * Query one, without querying related information
     *
     * @param id Session ID
     * @return Session information
     */
    SysSession findOneSession(Long id);

    /**
     * Add
     *
     * @param sysSession Session information
     * @return Number of rows affected
     */
    int addSession(SysSession sysSession);

    /**
     * Modify
     *
     * @param sysSession Session information
     * @return Number of rows affected
     */
    int updateSession(SysSession sysSession);

    /**
     * Delete
     *
     * @param id Session ID
     * @return Number of rows affected
     */
    int deleteSession(Long id);

    /**
     * Query session information for the past 5 days based on cinema ID and movie ID
     *
     * @param movieId Movie ID
     * @return List of sessions
     */
    List<SysSession> findSessionByMovieId(Long movieId);

}
