package com.truextend.dao;

import com.truextend.exception.BusinessException;

public class Pagination {
    private Integer pageNumber;
    private int pageSize;
    public static final int DEFAULT_PAGE_SIZE  = 10;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Pagination(Integer pageNumber, Integer pageSize) {
        if(pageNumber != null && pageNumber <= 0){
            throw new BusinessException("pageNumber must be a positive number or null for no pagination");
        }
        if(pageSize != null && (pageSize < 1)) {
            throw new BusinessException("pageSize must be a positive number");
        }
        this.pageNumber = pageNumber;
        this.pageSize = pageSize == null? DEFAULT_PAGE_SIZE : pageSize;
    }

    public int calculateOffset(){
        return (this.pageNumber - 1) * this.pageSize;
    }

}
