package com.hnv99.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Session entity class
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysSession implements Serializable {

    private static final long serialVersionUID = 1L;

    // Session ID
    private Long sessionId;

    // Hall ID
    @NotNull(message = "Session must be in a hall")
    private Long hallId;

    // Language version of this session
    @NotBlank(message = "Language version of session cannot be blank")
    private String languageVersion;

    // Movie ID
    @NotNull(message = "Movie for session cannot be null")
    private Long movieId;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "HH:mm")
    private String playTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "HH:mm")
    private String endTime;

    // Deadline, movie and hall information cannot be deleted or modified before this time
    private String deadline;

    // Session date
    @NotNull(message = "Session date cannot be null")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate sessionDate;

    // Session price
    @NotNull(message = "Session price cannot be null")
    @Size(min = 0, message = "Session price cannot be negative")
    private Double sessionPrice;

    // Session tips
    private String sessionTips;

    // Session seat information
    @NotBlank(message = "Session seat information cannot be blank")
    private String sessionSeats;

    private Integer seatNums;

    // Number of seats sold
    private Integer sallNums;

    private SysHall sysHall;

    private SysMovie sysMovie;
}
