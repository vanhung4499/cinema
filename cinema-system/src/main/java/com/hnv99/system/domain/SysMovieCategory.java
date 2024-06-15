package com.hnv99.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Represents a category of a movie.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class SysMovieCategory implements Serializable {
    // Serial version UID
    private static final long serialVersionUID = 1L;
    // Movie category ID
    private Long movieCategoryId;
    // Movie category name
    @NotBlank(message = "Movie category name cannot be blank")
    private String movieCategoryName;
}
