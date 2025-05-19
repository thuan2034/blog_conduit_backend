package com.blog.conduit.dtos;

import java.util.List;

public class ArticlePageResponseDto {
    private List<ArticleResponseDto> articleResponseDtoList;
    private long totalArticles; // Tổng số bản ghi
    private int totalPages;     // Tổng số trang
    private int currentPage;    // Trang hiện tại (bắt đầu từ 0)
    private int pageSize;       // Kích thước trang

    //constructors...
    public ArticlePageResponseDto(){}
    public ArticlePageResponseDto(List<ArticleResponseDto> articleResponseDtoList, int pageSize, int currentPage, int totalPages, long totalArticles) {
        this.articleResponseDtoList = articleResponseDtoList;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalArticles = totalArticles;
    }

    //getters and setters...

    public List<ArticleResponseDto> getArticleResponseDtoList() {
        return articleResponseDtoList;
    }

    public void setArticleResponseDtoList(List<ArticleResponseDto> articleResponseDtoList) {
        this.articleResponseDtoList = articleResponseDtoList;
    }

    public long getTotalArticles() {
        return totalArticles;
    }

    public void setTotalArticles(long totalArticles) {
        this.totalArticles = totalArticles;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
