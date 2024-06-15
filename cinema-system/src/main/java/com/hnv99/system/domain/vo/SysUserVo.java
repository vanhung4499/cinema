package com.hnv99.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Encapsulates user login input information
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysUserVo implements Serializable {

    private String userName;  // User's username

    private String password;  // User's password

}
