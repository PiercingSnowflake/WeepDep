package com.example.weepdep.model;

public class CommentDTO {
    private String id;
    private String comment;
    private boolean isAnon;
    private String username;
    private String anonName;
    private String threadId;
    private String breadId;

    public CommentDTO(String id, String comment, boolean isAnon, String username, String anonName, String threadId, String breadId) {
        this.id = id;
        this.comment = comment;
        this.isAnon = isAnon;
        this.username = username;
        this.anonName = anonName;
        this.threadId = threadId;
        this.breadId = breadId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isAnon() {
        return isAnon;
    }

    public void setAnon(boolean anon) {
        isAnon = anon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAnonName() {
        return anonName;
    }

    public void setAnonName(String anonName) {
        this.anonName = anonName;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getBreadId() {
        return breadId;
    }

    public void setBreadId(String breadId) {
        this.breadId = breadId;
    }
}