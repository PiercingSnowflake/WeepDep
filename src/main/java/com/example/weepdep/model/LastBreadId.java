package com.example.weepdep.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "last_bread_id")
public class LastBreadId {
    @Id
    private String id;
    private int lastBreadId;

    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLastBreadId() {
        return lastBreadId;
    }

    public void setLastBreadId(int lastBreadId) {
        this.lastBreadId = lastBreadId;
    }
}


