package com.hnv99.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hnv99.common.page.Page;
import com.hnv99.common.page.PageBuilder;
import com.hnv99.common.response.ResponseResult;

import java.util.List;

import static com.hnv99.common.page.PageBuilder.*;

/**
 * Extracts repetitive functions into a base class
 */
public class BaseController {

    /**
     * Enable pagination
     */
    public void startPage() {
        Page page = PageBuilder.buildPage();
        Integer pageNum = page.getPageNum();
        Integer pageSize = page.getPageSize();
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize, page.getOrderByColumn());
        }
    }

    /**
     * Returns response message based on the number of modified rows
     * @param rows
     * @return
     */
    public ResponseResult getResult(int rows) {
        return rows == 0 ? ResponseResult.error() : ResponseResult.success();
    }

    /**
     * Pagination response message
     * @param data data
     * @return pagination response message
     */
    public ResponseResult getResult(List<?> data) {
        PageInfo pageInfo = new PageInfo(data);
        ResponseResult responseResult = ResponseResult.success(data);
        responseResult.put(PAGE_NUM, pageInfo.getPageNum());
        responseResult.put(PAGE_SIZE, pageInfo.getPageSize());
        responseResult.put(TOTAL, pageInfo.getTotal());
        return responseResult;
    }

    /**
     * Object type response message
     * @param data data
     * @return object type response message
     */
    public ResponseResult getResult(Object data) {
        return ResponseResult.success(data);
    }

}
