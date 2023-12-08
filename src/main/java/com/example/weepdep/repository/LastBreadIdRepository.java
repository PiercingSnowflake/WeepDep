package com.example.weepdep.repository;

import com.example.weepdep.model.LastBreadId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LastBreadIdRepository extends MongoRepository<LastBreadId, String> {
}

