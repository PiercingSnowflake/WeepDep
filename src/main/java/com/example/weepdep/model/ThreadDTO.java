package com.example.weepdep.model;
import java.util.List;

public class ThreadDTO {
    private String id;
    private int customId;
    private String title;
    private String content;
    private boolean isAnon;
    private List<CommentDTO> comments;

    public ThreadDTO(String id, int customId, String title, String content, boolean isAnon, List<CommentDTO> comments) {
        this.id = id;
        this.customId = customId;
        this.title = title;
        this.content = content;
        this.isAnon = isAnon;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAnon() {
        return isAnon;
    }

    public void setAnon(boolean anon) {
        isAnon = anon;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}