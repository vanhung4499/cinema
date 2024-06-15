package com.hnv99.system.mapper;

import com.hnv99.system.domain.SysCinema;

public interface SysCinemaMapper {

    SysCinema findCinema();

    int updateCinema(SysCinema sysCinema);

    // Display information of a single cinema on the front end, returning all movies currently showing in the cinema
    SysCinema findCinemaById(Long id);

}
