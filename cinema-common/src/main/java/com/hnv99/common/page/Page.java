package com.hnv99.common.page;

import com.hnv99.common.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class for retrieving pagination request parameters
 */
@Getter
@Setter
public class Page {

    // Page number
    private Integer pageNum = 1;

    // Page size
    private Integer pageSize = 1000;

    // Column to be sorted by
    private String orderByColumn;

    // Ascending or descending order, "asc" for ascending (default), "desc" for descending
    private String isAsc = "asc";

    /**
     * Concatenates isAsc to orderByColumn
     *
     * @return
     */
    public String getOrderByColumn() {
        if (!StringUtil.isNotEmpty(orderByColumn)) {
            return "";
        }
        return orderByColumn + " " + isAsc;
    }

    public void setIsAsc(String isAsc) {
        if (StringUtil.isNotEmpty(isAsc)) {
            this.isAsc = isAsc;
        }
    }
}
