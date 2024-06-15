package com.hnv99.system.service;

import com.hnv99.system.domain.SysHall;

import java.util.List;


public interface SysHallService {
    List<SysHall> findAllHalls(SysHall sysHall);

    SysHall findHallById(SysHall sysHall);

    int addHall(SysHall sysHall);

    int updateHall(SysHall sysHall);

    int deleteHall(SysHall[] sysHall);
}
