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
public class SysResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Resource name cannot be blank")
    private String name;

    private String path;

    // Menu permission level
    private Integer level;

    private Long parentId;

    // Parent menu
    private SysResource parent;

    // Sub-menus
    private List<SysResource> children;

}
