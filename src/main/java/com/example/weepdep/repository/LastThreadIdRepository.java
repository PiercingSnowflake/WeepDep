package com.example.weepdep.repository;

import com.example.weepdep.model.LastThreadId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LastThreadIdRepository extends MongoRepository<LastThreadId, String> {
}

