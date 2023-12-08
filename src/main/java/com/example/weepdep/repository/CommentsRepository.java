package com.example.weepdep.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.weepdep.model.Comments;

public interface CommentsRepository extends MongoRepository<Comments, String> {
    List<Comments> findByThreadId(String threadId);
}
