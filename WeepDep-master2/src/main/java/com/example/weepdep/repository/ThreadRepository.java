package com.example.weepdep.repository;

import com.example.weepdep.model.Thread;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ThreadRepository extends MongoRepository<Thread, String> {
    Thread findByCustomId(int customId);
    @Query(value = "{'customId': {$exists: true}}", sort = "{'customId': -1}", fields = "{'customId': 1}")
    Integer findMaxCustomId();
}

