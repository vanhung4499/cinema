package com.hnv99.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysCarousel implements Serializable {
    private static final Long serialVersionUID = 1L;

    private Long carouselId;

    private String carouselPicture;

    private String carouselUrl;

    private Boolean isShow;
}
