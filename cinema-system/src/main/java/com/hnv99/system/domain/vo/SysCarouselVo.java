package com.hnv99.system.domain.vo;


import com.hnv99.system.domain.SysBill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysCarouselVo implements Serializable {

    private String carouselPicture;  // Carousel picture
    private String carouselUrl;      // Carousel link
    private Boolean isShow;           // Whether to display the carousel

}