package com.hnv99.system.service;

import com.hnv99.system.domain.SysCinema;


public interface SysCinemaService {

    SysCinema findCinema();

    int updateCinema(SysCinema sysCinema);

    SysCinema findCinemaById(Long id);

}
