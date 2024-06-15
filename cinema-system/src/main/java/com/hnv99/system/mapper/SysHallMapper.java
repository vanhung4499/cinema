package com.hnv99.system.mapper;

import com.hnv99.system.domain.SysHall;

import java.util.List;


public interface SysHallMapper {
    List<SysHall> findAllHalls(SysHall sysHall);

    SysHall findHallById(SysHall sysHall);

    int addHall(SysHall sysHall);

    int updateHall(SysHall sysHall);

    int deleteHall(SysHall sysHall);
}
