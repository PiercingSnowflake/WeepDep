package com.example.weepdep.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "threads")
public class Thread {
    @Id
    private String id;
    private int customId;
    private String title;
    private String content;
    boolean  isAnon;
    
    @DBRef
    private User user;  // Reference to the user who created the thread

    @DBRef
    private List<Comments> comments = new ArrayList<>();  // List of comments in the thread

    // getters and setters

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

    public String getAuthorName() {
        return user.getUsername();
    }

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
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
    
    public void addComment(Comments comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

    // Method to get comments of the thread
    public List<Comments> getComments() {
        return comments;
    }
    
    public void setAnon(boolean isAnon) {
        this.isAnon = isAnon;
    }
    
    public boolean getAnon() {

         return isAnon;
    }
    
    public String getAnonName () {
        return user.getAnon();
    }

}
