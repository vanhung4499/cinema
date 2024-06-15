package com.hnv99.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Represents the currently logged-in user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginUser implements Serializable {
    // Information of the logged-in user
    private SysUser sysUser;
    // Id of the cinema managed by the user
    private Long cinemaId;
    // Name of the cinema managed by the user
    private String cinemaName;

    // Token issued by the system
    private String token;

}
