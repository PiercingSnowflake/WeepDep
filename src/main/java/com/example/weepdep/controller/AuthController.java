package com.example.weepdep.controller;

import com.example.weepdep.model.Comments;
import com.example.weepdep.model.LoginRequest;
import com.example.weepdep.model.SignUpRequest;
import com.example.weepdep.model.User;
import com.example.weepdep.model.Thread;
import com.example.weepdep.model.Bread;
import com.example.weepdep.service.CommentsService;
import com.example.weepdep.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.weepdep.service.BreadService;
import com.example.weepdep.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final ThreadService threadService;
    private final BreadService breadService;

    public AuthController(UserService userService, BreadService breadService, ThreadService threadService) {
        this.userService = userService;
        this.breadService = breadService;
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
        try {
            User user = userService.getUserByUsername(request.getUsername());

            if (user != null && user.getPassword() != null && user.getPassword().equals(request.getPassword())) {
                return ResponseEntity.ok("You successfully logged in as " + user.getUsername());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login: " + e.getMessage());
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
            Thread newThread = threadService.createThread(thread.getAuthorName(), thread.getTitle(), thread.getContent());
            return ResponseEntity.ok("Thread created with ID: " + newThread.getCustomId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating thread: " + e.getMessage());
        }
    }

    @PostMapping("/create-bread")
    public ResponseEntity<String> createBread(@RequestBody Bread bread) {
        try {
            Bread newBread = breadService.createBread(bread.getAuthorName(), bread.getTitle(), bread.getContent());
            return ResponseEntity.ok("Bread created with ID: " + newBread.getCustomId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating bread: " + e.getMessage());
        }
    }

    @GetMapping("/breads")
    public List<Bread> getAllBreads() {
        return breadService.getAllBreads();
    }


    @GetMapping("/breads/{customId}")
    public ResponseEntity<Bread> getBreadByCustomId(@PathVariable int customId) {
        Bread bread = breadService.getBreadByCustomId(customId);
        return ResponseEntity.ok(bread);
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
        public ResponseEntity<List<Comments>> getCommentsByThreadId(@PathVariable String threadId) {
            List<Comments> comments = commentService.getCommentsByThreadId(threadId);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }

        // Endpoint to add a comment to a specific thread
        @PostMapping("/{threadId}/comments")
        public ResponseEntity<Comments> addCommentToThread(@PathVariable int threadId, @RequestBody Comments comment) {
            // You might want to set the thread reference in the comment before saving
            Thread thread = threadService.getThreadByCustomId(threadId);
            comment.setThread(thread);

            Comments savedComment = commentService.saveComment(comment);
            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        }
    }

    @RestController
    @RequestMapping("/api/breads")  // Assuming your existing controller handles threads
    public class BreadController {
        @Autowired
        private BreadService breadService;

        @Autowired
        private CommentsService commentService;

        // Your existing endpoints for threads

        // Endpoint to get comments for a specific thread
        @GetMapping("/{breadId}/comments")
        public ResponseEntity<List<Comments>> getCommentsByBreadId(@PathVariable String breadId) {
            List<Comments> comments = commentService.getCommentsByBreadId(breadId);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }

        // Endpoint to add a comment to a specific thread
        // Endpoint to add a comment to a specific bread
        @PostMapping("/{breadId}/comments")
        public ResponseEntity<Comments> addCommentToBread(@PathVariable int breadId, @RequestBody Comments comment) {
            Bread bread = breadService.getBreadByCustomId(breadId);
            comment.setBread(bread);

            Comments savedComment = commentService.saveComment(comment);
            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        }

    }



}