package com.hnv99.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Represents a hall entity.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SysHall implements Serializable {
    private final static Long serialVersionUID = 1L;

    private Long cinemaId;

    private Long hallId;

    @NotBlank(message = "Hall name cannot be blank")
    private String hallName;

    private String hallCategory;

    // Start numbering of rows: '1' / 'A' etc.
    private String rowStart;

    // Number of rows in the hall
    @Min(value = 3, message = "Number of rows cannot be less than 3")
    @Max(value = 50, message = "Number of rows cannot be more than 50")
    private Integer rowNums;

    // Number of seats per row
    @Min(value = 3, message = "Number of seats per row cannot be less than 3")
    @Max(value = 50, message = "Number of seats per row cannot be more than 50")
    private Integer seatNumsRow;

    // Total number of available seats, seats can be disabled by arranging them
    @Min(value = 9, message = "Number of seats cannot be less than 9")
    @Max(value = 2500, message = "Number of seats cannot be more than 2500")
    private Integer seatNums;

    // State of seats, storing the state of each seat using JSON, where 0 means available, 2 means disabled (1 means sold, counted in the session table)
    private String seatState;

    private Boolean delState;

    // Multi-table connection of the hall table
    private SysCinema sysCinema;
}
