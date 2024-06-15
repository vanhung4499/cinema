package com.hnv99.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents an order entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysBill implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Long billId;

    // Order status: 0 for unpaid, 1 for completed
    private Boolean payState;

    // Id of the user who placed the order
    @NotNull(message = "User ID for placing the order cannot be null")
    private Long userId;

    // Id of the session to which the order belongs
    @NotNull(message = "Session ID for the order cannot be null")
    private Long sessionId;

    // Seats selected for the order, e.g., "Row 1 Seat 10", "Row A Seat 5"
    @NotBlank(message = "Selected seats for the order cannot be blank")
    private String seats;

    private Boolean cancelState;

    private Boolean cancelRole;

    private Date createTime;

    private Date deadline;

    private Date cancelTime;

    // Username used as a fuzzy query condition
    private String queryByUserName;

    // Administrator operation identification and remarks. When an administrator places an order or adds or modifies it, the remarks must be filled in. This is not required for the ticketing subsystem and should be cleared.
    private String remark;

    // Deletion status: 1 for deleted, 0 for not deleted
    private Boolean delState;

    // Foreign key relationships
    private SysSession sysSession;

    private SysUser sysUser;

}
