package com.hnv99.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Encapsulates the conditions for querying movies
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysMovieVo implements Serializable {

    private String movieName;         // Movie name

    private String movieArea;         // Movie production area

    private Long movieCategoryId;     // Movie category ID

    private Date startDate;           // Start date for movie screening

    private Date endDate;             // End date for movie screening

}
