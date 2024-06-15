package com.hnv99.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Represents a many-to-many relationship between movies and movie categories.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysMovieToCategory implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Long movieId;

    private Long movieCategoryId;

}
