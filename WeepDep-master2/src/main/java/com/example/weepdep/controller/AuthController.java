package com.example.weepdep.controller;

import com.example.weepdep.model.Comments;
import com.example.weepdep.model.LoginRequest;
import com.example.weepdep.model.SignUpRequest;
import com.example.weepdep.model.User;
import com.example.weepdep.model.Thread;
import com.example.weepdep.service.CommentsService;
import com.example.weepdep.service.ThreadService;
import com.example.weepdep.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final ThreadService threadService;

    public AuthController(UserService userService, ThreadService threadService) {
        this.userService = userService;
        this.threadService = threadService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) {
        try {
            User existingUser = userService.getUserByUsername(request.getUsername());
            if (existingUser != null) {
                throw new RuntimeException("Username already exists");
            }

            User newUser = userService.createUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok("Account created for user: " + newUser.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating account: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        User user = userService.getUserByUsername(request.getUsername());

        if (user != null && user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.ok("You successfully logged in as " + user.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }




    @GetMapping("/threads")
    public List<Thread> getAllThreads() {
        return threadService.getAllThreads();
    }

    @GetMapping("/threads/{customId}")
    public ResponseEntity<Thread> getThreadByCustomId(@PathVariable int customId) {
        Thread thread = threadService.getThreadByCustomId(customId);
        return ResponseEntity.ok(thread);
    }
/*
    @PostMapping("/threads")
    public ResponseEntity<String> createThread(@RequestBody Thread thread) {
        try {
            Thread newThread = threadService.createThread(thread.getAuthorName(), thread.getTitle(), thread.getContent());
            return ResponseEntity.ok("Thread created with ID: " + newThread.getCustomId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating thread: " + e.getMessage());
        }
    }
*/
    @PostMapping("/create-thread")
    public ResponseEntity<String> createThread(@RequestBody Thread thread) {
        try {
            Thread newThread = threadService.createThread(thread.getUser(), thread.getTitle(), thread.getContent());
            return ResponseEntity.ok("Thread created with ID: " + newThread.getCustomId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating thread: " + e.getMessage());
        }
    }
    
    @RestController
    @RequestMapping("/api/threads")  // Assuming your existing controller handles threads
    public class ThreadController {
        @Autowired
        private ThreadService threadService;
        
        @Autowired
        private CommentsService commentService;

        // Your existing endpoints for threads

        // Endpoint to get comments for a specific thread
        @GetMapping("/{threadId}/comments")
        public ResponseEntity<List<Comments>> getCommentsByThreadId(@PathVariable String customId) {
            List<Comments> comments = commentService.getCommentsByThreadId(customId);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }

        // Endpoint to add a comment to a specific thread
        @PostMapping("/{threadId}/comments")
        public ResponseEntity<Comments> addCommentToThread(@PathVariable int customId, @RequestBody Comments comment) {
            // You might want to set the thread reference in the comment before saving
            Thread thread = threadService.getThreadByCustomId(customId);
            comment.setThread(thread);

            Comments savedComment = commentService.saveComment(comment);
            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        }
    }





}