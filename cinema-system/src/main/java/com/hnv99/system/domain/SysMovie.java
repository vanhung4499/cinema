package com.hnv99.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Represents a movie entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysMovie implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long movieId;

    // Movie name
    @NotBlank(message = "Movie name cannot be blank")
    private String movieName;

    // Movie duration
    private Integer movieLength;

    // Movie poster
    private String moviePoster;

    private String movieArea;

    // Release date
    private Date releaseDate;

    // Total box office of the movie
    private Double movieBoxOffice;

    // Movie introduction
    private String movieIntroduction;

    // Movie gallery
    private String moviePictures;

    // Movie category
    private List<SysMovieCategory> movieCategoryList;

}
