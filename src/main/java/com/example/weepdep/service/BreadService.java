package com.example.weepdep.service;

import com.example.weepdep.model.LastBreadId;
import com.example.weepdep.model.Bread;
import com.example.weepdep.repository.LastBreadIdRepository;
import com.example.weepdep.repository.BreadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BreadService {
    private final  BreadRepository breadRepository;
    private final LastBreadIdRepository lastBreadIdRepository;

    public BreadService(BreadRepository breadRepository, LastBreadIdRepository lastBreadIdRepository) {
        this.breadRepository = breadRepository;
        this.lastBreadIdRepository = lastBreadIdRepository;
    }

    public List<Bread> getAllBreads() {
        return breadRepository.findAll();
    }

    public Bread getBreadByCustomId(int customId) {
        return breadRepository.findByCustomId(customId);
    }

    public Bread createBread(String authorName, String title, String content) {
        int newCustomId = getNextCustomId();

        Bread newBread = new Bread();
        newBread.setAuthorName(authorName);
        newBread.setTitle(title);
        newBread.setContent(content);
        newBread.setCustomId(newCustomId);

        breadRepository.save(newBread);

        // Fetch the last assigned ID from the database
        LastBreadId lastBreadIdEntity = lastBreadIdRepository.findById("1").orElseGet(() -> {
            // If the entity does not exist, create a new one
            LastBreadId newEntity = new LastBreadId();
            newEntity.setId("1");
            newEntity.setLastBreadId(0);
            return newEntity;
        });

        // Increment the ID for the next bread
        int lastAssignedId = lastBreadIdEntity.getLastBreadId();
        int nextId = lastAssignedId + 1;

        // Update the entity with the new last assigned ID
        lastBreadIdEntity.setLastBreadId(nextId);
        lastBreadIdRepository.save(lastBreadIdEntity);

        return newBread;
    }

    private int getNextCustomId() {
        // Fetch the last assigned ID from the database
        LastBreadId lastBreadIdEntity = lastBreadIdRepository.findById("1").orElseGet(() -> {
            // If the entity does not exist, create a new one
            LastBreadId newEntity = new LastBreadId();
            newEntity.setId("1");
            newEntity.setLastBreadId(0);
            return newEntity;
        });

        int lastAssignedId = lastBreadIdEntity.getLastBreadId();

        // Increment the ID for the next bread
        int nextId = lastAssignedId + 1;

        // Update the entity with the new last assigned ID
        lastBreadIdEntity.setLastBreadId(nextId);

        return nextId;
    }
}

