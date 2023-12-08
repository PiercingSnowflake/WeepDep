
package com.example.weepdep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.weepdep.model.Comments;
import com.example.weepdep.repository.CommentsRepository;

@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentRepository;

    public List<Comments> getCommentsByThreadId(String threadId) {
        return commentRepository.findByThreadId(threadId);
    }

    public List<Comments> getCommentsByBreadId(String breadId) {
        return commentRepository.findByBreadId(breadId);
    }

    public Comments saveComment(Comments comment) {
        // You can add additional logic before saving if needed
        return commentRepository.save(comment);
    }
}