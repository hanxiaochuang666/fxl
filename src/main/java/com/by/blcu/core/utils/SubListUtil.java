package com.by.blcu.core.utils;

import java.util.List;

public class SubListUtil<E> {
    private int page; // 当前页
    private int pages; // 总页数
    private int total; // 总条数
    private int pageSize;// 每页显示数
    private List<E> list;// 要进行分页的list

    public SubListUtil(int page, int pageSize, List<E> list) {
        super();
        this.page = page;
        this.pageSize = pageSize;
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        if (this.total % this.pageSize == 0) {
            this.pages = this.total / this.pageSize;
        } else {
            this.pages = this.total / this.pageSize + 1;
        }
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        total = list.size();
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<E> getList() {
        List<E> newList = this.list.subList(this.getPageSize() * (this.getPage() - 1),
                (this.getPageSize() * this.getPage()) > this.getTotal() ? this.getTotal()
                        : (this.getPageSize() * this.getPage()));
        // List<E> newList = this.list.subList(pageSize*(page-1),
        // (pageSize*page)>total?total:(pageSize*page));
        return newList;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

}
