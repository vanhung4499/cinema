package com.hnv99.system.domain.vo;

import com.hnv99.system.domain.SysBill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Receive data for adding orders from the front end
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysBillVo implements Serializable {

    // Order information
    private SysBill sysBill;

    // Updated seat information for sessions if successful
    private String sessionSeats;

}
