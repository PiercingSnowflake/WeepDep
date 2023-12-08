package com.example.weepdep.service;

import com.example.weepdep.model.Comments;
import com.example.weepdep.model.LastThreadId;
import com.example.weepdep.model.Thread;
import com.example.weepdep.model.User;
import com.example.weepdep.repository.CommentsRepository;
import com.example.weepdep.repository.LastThreadIdRepository;
import com.example.weepdep.repository.ThreadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ThreadService {
    private final ThreadRepository threadRepository;
    private final LastThreadIdRepository lastThreadIdRepository;
    private final CommentsRepository commentsRepository;

    public ThreadService(ThreadRepository threadRepository, LastThreadIdRepository lastThreadIdRepository, CommentsRepository commentsRepository) {
        this.threadRepository = threadRepository;
        this.lastThreadIdRepository = lastThreadIdRepository;
		this.commentsRepository = commentsRepository;
    }

    public List<Thread> getAllThreads() {
        return threadRepository.findAll();
    }

    public Thread getThreadByCustomId(int customId) {
        return threadRepository.findByCustomId(customId);
    }

    public Thread createThread(User user, String title, String content) {
        int newCustomId = getNextCustomId();

        Thread newThread = new Thread();
        newThread.setUser(user);
        newThread.setTitle(title);
        newThread.setContent(content);
        newThread.setCustomId(newCustomId);

        threadRepository.save(newThread);

        // Fetch the last assigned ID from the database
        LastThreadId lastThreadIdEntity = lastThreadIdRepository.findById("1").orElseGet(() -> {
            // If the entity does not exist, create a new one
            LastThreadId newEntity = new LastThreadId();
            newEntity.setId("1");
            newEntity.setLastThreadId(0);
            return newEntity;
        });

        // Increment the ID for the next thread
        int lastAssignedId = lastThreadIdEntity.getLastThreadId();
        int nextId = lastAssignedId + 1;

        // Update the entity with the new last assigned ID
        lastThreadIdEntity.setLastThreadId(nextId);
        lastThreadIdRepository.save(lastThreadIdEntity);

        return newThread;
    }

    private int getNextCustomId() {
        // Fetch the last assigned ID from the database
        LastThreadId lastThreadIdEntity = lastThreadIdRepository.findById("1").orElseGet(() -> {
            // If the entity does not exist, create a new one
            LastThreadId newEntity = new LastThreadId();
            newEntity.setId("1");
            newEntity.setLastThreadId(0);
            return newEntity;
        });

        int lastAssignedId = lastThreadIdEntity.getLastThreadId();

        // Increment the ID for the next thread
        int nextId = lastAssignedId + 1;

        // Update the entity with the new last assigned ID
        lastThreadIdEntity.setLastThreadId(nextId);

        return nextId;
    }
    
    public Thread saveComment(Comments comment, Thread thread) {
        // You can add additional logic before saving if needed
        Comments savedComment = commentsRepository.save(comment);

        // Add the comment to the thread's list of comments
        thread.getComments().add(savedComment);

        // Update the thread in the database
        threadRepository.save(thread);

        return thread;
    }
}

