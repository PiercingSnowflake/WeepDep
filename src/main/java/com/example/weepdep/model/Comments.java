package com.example.weepdep.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
public class Comments {

    @Id
    private String id;
    private String comment;

    @DBRef
    private User user;  // Reference to the user who posted the comment

    @DBRef
    private Thread thread;  // Reference to the thread to which the comment belongs


    @DBRef
    private Bread bread;

    public Comments(){

    }
    public Comments(User user, String comment, Thread thread){
        this.comment = comment;
        this.user = user;
        this.thread = thread;
    }

    public String getComment () {
        return comment;
    }

    public void setComment (String comment) {
        this.comment = comment;
    }

    public String getUsername () {
        return user.getUsername();
    }

    public void setUser (User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {

        this.thread = thread;

    }

    public Bread getBread() {
        return bread;
    }

    public void setBread(Bread bread) {

        this.bread = bread;
   }
}