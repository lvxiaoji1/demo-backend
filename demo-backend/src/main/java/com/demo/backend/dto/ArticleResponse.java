package com.demo.backend.dto;

import com.demo.backend.entity.Article;
import java.time.LocalDateTime;

public class ArticleResponse {

    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;
    private LocalDateTime createTime;

    public static ArticleResponse from(Article article) {
        ArticleResponse r = new ArticleResponse();
        r.id = article.getId();
        r.title = article.getTitle();
        r.content = article.getContent();
        r.authorId = article.getUser().getId();
        r.authorName = article.getUser().getName();
        r.createTime = article.getCreateTime();
        return r;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Long getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
    public LocalDateTime getCreateTime() { return createTime; }
}