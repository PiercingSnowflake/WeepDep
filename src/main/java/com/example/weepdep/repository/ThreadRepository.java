package com.example.weepdep.repository;

import com.example.weepdep.model.Thread;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThreadRepository extends MongoRepository<Thread, String> {
    Thread findByCustomId(int customId);
}

