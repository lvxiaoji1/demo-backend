package com.demo.backend.dto;

import com.demo.backend.entity.Article;
import java.time.LocalDateTime;

public class ArticleResponse {

    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createTime;

    public static ArticleResponse from(Article article) {
        ArticleResponse r = new ArticleResponse();
        r.id = article.getId();
        r.title = article.getTitle();
        r.content = article.getContent();
        r.authorId = article.getUser().getId();
        r.authorName = article.getUser().getName();
        r.createTime = article.getCreateTime();
        if (article.getCategory() != null) {
            r.categoryId = article.getCategory().getId();
            r.categoryName = article.getCategory().getName();
        }
        return r;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Long getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
    public Long getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public LocalDateTime getCreateTime() { return createTime; }
}