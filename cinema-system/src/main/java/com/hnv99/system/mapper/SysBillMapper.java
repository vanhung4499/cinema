package com.hnv99.system.mapper;

import com.hnv99.system.domain.SysBill;

import java.util.List;


public interface SysBillMapper {

    List<SysBill> findAllBills(SysBill sysBill);

    SysBill findBillById(Long id);

    int addBill(SysBill sysBill);

    int updateBill(SysBill sysBill);

    int deleteBill(Long id);

    List<SysBill> findTimeoutBill();

}
