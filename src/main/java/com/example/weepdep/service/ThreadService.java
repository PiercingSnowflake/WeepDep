package com.example.weepdep.service;

import com.example.weepdep.model.Thread;
import com.example.weepdep.repository.ThreadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreadService {
    private final ThreadRepository threadRepository;

    public ThreadService(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }

    public List<Thread> getAllThreads() {
        return threadRepository.findAll();
    }

    public Thread getThreadByCustomId(int customId) {
        return threadRepository.findByCustomId(customId);
    }

    public Thread createThread(String authorName, String title, String content) {
        Thread newThread = new Thread();
        newThread.setAuthorName(authorName);
        newThread.setTitle(title);
        newThread.setContent(content);
        // Set other fields as needed
        return threadRepository.save(newThread);
    }
}
