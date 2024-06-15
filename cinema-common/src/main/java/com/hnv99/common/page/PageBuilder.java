package com.hnv99.common.page;

import com.hnv99.common.utils.ServletUtil;
import com.hnv99.common.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Build a page object to reduce the number of request parameters on Controller methods through ServletUtil
 */
public class PageBuilder {

    // Current page number
    public static final String PAGE_NUM = "pageNum";

    // Page size
    public static final String PAGE_SIZE = "pageSize";

    // Total number of records
    public static final String TOTAL = "total";

    // Column to be sorted by
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    // Ascending or descending order
    public static final String IS_ASC = "isAsc";

    public static Page buildPage() {
        Page page = new Page();
        HttpServletRequest request = ServletUtil.getRequest();

        String pageNum = request.getParameter(PAGE_NUM);
        if (StringUtil.isNotEmpty(pageNum)) {
            page.setPageNum(Integer.parseInt(pageNum));
        }

        String pageSize = request.getParameter(PAGE_SIZE);
        if (StringUtil.isNotEmpty(pageSize)) {
            page.setPageSize(Integer.parseInt(pageSize));
        }

        page.setIsAsc(request.getParameter(IS_ASC));
        page.setOrderByColumn(StringUtil.toUnderScoreCase(request.getParameter(ORDER_BY_COLUMN)));
        return page;
    }
}
