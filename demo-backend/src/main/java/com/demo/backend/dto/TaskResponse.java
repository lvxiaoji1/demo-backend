package com.demo.backend.dto;

import com.demo.backend.entity.Task;

import java.time.LocalDateTime;

public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private Boolean done;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TaskResponse from(Task t) {
        TaskResponse r = new TaskResponse();
        r.id = t.getId();
        r.title = t.getTitle();
        r.description = t.getDescription();
        r.done = t.getDone();
        r.createdAt = t.getCreatedAt();
        r.updatedAt = t.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Boolean getDone() { return done; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
