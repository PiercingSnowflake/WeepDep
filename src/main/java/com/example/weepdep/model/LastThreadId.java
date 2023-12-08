package com.example.weepdep.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "last_thread_id")
public class LastThreadId {
    @Id
    private String id;
    private int lastThreadId;

    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLastThreadId() {
        return lastThreadId;
    }

    public void setLastThreadId(int lastThreadId) {
        this.lastThreadId = lastThreadId;
    }
}


