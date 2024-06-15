package com.hnv99.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    // User ID
    private Long userId;

    // Username
    @NotBlank(message = "Username cannot be blank")
    private String userName;

    // Password
    @NotBlank(message = "Password cannot be blank")
    private String password;

    // Salt
    private String salt;

    // Email
    @Email(message = "Invalid email format")
    private String email;

    // Phone number
    private String phoneNumber;

    // Gender
    private Boolean sex;

    // User avatar
    private String userPicture;

    // Role ID corresponding to the user, simplified to one-to-one relationship
    private Long roleId;

    private String birthday;

    private String autograph;

    // User's role
    private SysRole sysRole;
}
