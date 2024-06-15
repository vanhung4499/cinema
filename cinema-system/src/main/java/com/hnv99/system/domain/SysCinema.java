package com.hnv99.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a cinema entity.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SysCinema implements Serializable {
    private final static Long serialVersionUID = 1L;

    private Long cinemaId;

    @NotBlank(message = "Cinema name cannot be blank")
    private String cinemaName;

    private String hallCategoryList;

    private String cinemaPicture;

    private String cinemaAddress;

    private String cinemaPhone;

    // Work start time of the cinema
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "HH:mm")
    private String workStartTime;

    // Work end time of the cinema
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "HH:mm")
    private String workEndTime;

    // List of all movies currently showing at the cinema. Rule: Movies currently showing include movies scheduled for the next 5 days, including today.
    private List<SysMovie> sysMovieList;
}
