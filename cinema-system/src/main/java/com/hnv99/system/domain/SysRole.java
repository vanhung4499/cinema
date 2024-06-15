package com.hnv99.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    // Role name
    @NotBlank(message = "Role name cannot be blank")
    private String roleName;

    // Role description
    @NotBlank(message = "Role description cannot be blank")
    private String roleDesc;

    // Permissions owned by the role, stored in multiple levels for convenience, named "children" for easy access to all permissions
    private List<SysResource> children;
}
