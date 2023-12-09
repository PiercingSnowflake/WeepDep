package com.example.weepdep.controller;

import com.example.weepdep.model.Comments;
import com.example.weepdep.model.LoginRequest;
import com.example.weepdep.model.SignUpRequest;
import com.example.weepdep.model.User;
import com.example.weepdep.model.Thread;
import com.example.weepdep.model.ThreadDTO;
import com.example.weepdep.model.Bread;
import com.example.weepdep.model.BreadDTO;
import com.example.weepdep.model.CommentDTO;
import com.example.weepdep.service.CommentsService;
import com.example.weepdep.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.weepdep.service.BreadService;
import com.example.weepdep.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



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
            Thread newThread = threadService.createThread(thread.getUser(), thread.getTitle(), thread.getContent(), thread.getAnon());
            return ResponseEntity.ok("Thread created with ID: " + newThread.getCustomId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating thread: " + e.getMessage());
        }
    }

    @PostMapping("/create-bread")
    public ResponseEntity<String> createBread(@RequestBody Bread bread) {
        try {
            Bread newBread = breadService.createBread(bread.getUser(), bread.getTitle(), bread.getContent(),bread.getAnon());
            return ResponseEntity.ok("Bread created with ID: " + newBread.getCustomId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating bread: " + e.getMessage());
        }
    }

    

    @RestController
    @RequestMapping("/api/auth/threads")
    public class ThreadController {
        @Autowired
        private ThreadService threadService;

        @Autowired
        private CommentsService commentService;

        @GetMapping("/{threadCustomId}/comments")
        public ResponseEntity<List<Comments>> getCommentsByThreadCustomId(@PathVariable(name = "threadCustomId") int threadCustomId) {
            Thread thread = threadService.getThreadByCustomId(threadCustomId);

            if (thread == null) {
                // Handle case where the thread is not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            List<Comments> comments = commentService.getCommentsByThreadId(thread.getId());
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }

     // Endpoint to add a comment to a specific thread
        @PostMapping("/{threadCustomId}/comments")
        public ResponseEntity<Comments> addCommentToThread(@PathVariable(name = "threadCustomId") int threadCustomId, @RequestBody Map<String, Object> commentData) {
            Thread thread = threadService.getThreadByCustomId(threadCustomId);

            if (thread == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Extract relevant data from the request body
            String commentText = (String) commentData.get("comment");
            boolean isAnon = (boolean) commentData.get("isAnon");

            // Assuming "user" is an object within commentData
            Map<String, Object> userData = (Map<String, Object>) commentData.get("user");
            String userId = (String) userData.get("id");

            // Get the user by ID from the database
            User user = userService.getUserById(userId);

            // Create a new Comments object with the correct user and thread references
            Comments newComment = new Comments(user, commentText, thread, null, isAnon);

            // Save the new comment
            Comments savedComment = commentService.saveComment(newComment);

            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        }
    }

    /*@RestController
    @RequestMapping("/api/breads")
    public class BreadController {
        @Autowired
        private BreadService breadService;

        @Autowired
        private CommentsService commentService;

        // Your existing endpoints for breads

        // Endpoint to get comments for a specific bread
        @GetMapping("/{breadCustomId}/comments")
        public ResponseEntity<List<Comments>> getCommentsByBreadId(@PathVariable(name = "breadCustomId") int breadCustomId) {
            Bread bread = breadService.getBreadByCustomId(breadCustomId);

            if (bread == null) {
                // Handle case where the bread is not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            List<Comments> comments = commentService.getCommentsByBreadId(bread.getId());
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }

        // Endpoint to add a comment to a specific bread
        @PostMapping("/{breadCustomId}/comments")
        public ResponseEntity<Comments> addCommentToBread(@PathVariable(name = "breadCustomId") int breadCustomId, @RequestBody Map<String, Object> commentData) {
            Bread bread = breadService.getBreadByCustomId(breadCustomId);

            if (bread == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Extract relevant data from the request body
            String commentText = (String) commentData.get("comment");
            boolean isAnon = (boolean) commentData.get("isAnon");

            // Assuming "user" is an object within commentData
            Map<String, Object> userData = (Map<String, Object>) commentData.get("user");
            String userId = (String) userData.get("id");

            // Get the user by ID from the database
            User user = userService.getUserById(userId);

            // Create a new Comments object with the correct user and bread references
            Comments newComment = new Comments(user, commentText,null, bread, isAnon);

            // Save the new comment
            Comments savedComment = commentService.saveComment(newComment);

            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        }
        
    }
    
    @GetMapping("/breads")
    public List<Bread> getAllBreads() {
        return breadService.getAllBreads();
    }


    @GetMapping("/breads/{customId}")
    public ResponseEntity<Bread> getBreadByCustomId(@PathVariable(name = "customId") int customId) {
        Bread bread = breadService.getBreadByCustomId(customId);
        return ResponseEntity.ok(bread);
    }
    */
    
    @RestController
    @RequestMapping("/api/auth/breads")
    public class BreadController {

        @Autowired
        private BreadService breadService;

        @Autowired
        private CommentsService commentService;

        @GetMapping("/{breadCustomId}/comments")
        public ResponseEntity<List<Comments>> getCommentsByBreadId(@PathVariable(name = "breadCustomId") int breadCustomId) {
            Bread bread = breadService.getBreadByCustomId(breadCustomId);

            if (bread == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            List<Comments> comments = commentService.getCommentsByBreadId(bread.getId());
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }

        @PostMapping("/{breadCustomId}/comments")
        public ResponseEntity<Comments> addCommentToBread(@PathVariable(name = "breadCustomId") int breadCustomId, @RequestBody Map<String, Object> commentData) {
            Bread bread = breadService.getBreadByCustomId(breadCustomId);

            if (bread == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String commentText = (String) commentData.get("comment");
            boolean isAnon = (boolean) commentData.get("isAnon");

            Map<String, Object> userData = (Map<String, Object>) commentData.get("user");
            String userId = (String) userData.get("id");

            User user = userService.getUserById(userId);

            Comments newComment = new Comments(user, commentText, null, bread, isAnon);
            Comments savedComment = commentService.saveComment(newComment);

            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        }

        @GetMapping
        public List<BreadDTO> getAllBreads() {
            List<Bread> breads = breadService.getAllBreads();
            return breads.stream().map(this::convertToBreadDTO).collect(Collectors.toList());
        }

        @GetMapping("/{customId}")
        public ResponseEntity<BreadDTO> getBreadByCustomId(@PathVariable(name = "customId") int customId) {
            Bread bread = breadService.getBreadByCustomId(customId);
            if (bread == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            BreadDTO breadDTO = convertToBreadDTO(bread);
            return ResponseEntity.ok(breadDTO);
        }

        private BreadDTO convertToBreadDTO(Bread bread) {
            List<CommentDTO> commentDTOs = bread.getComments().stream().map(this::convertToCommentDTO).collect(Collectors.toList());
            return new BreadDTO(bread.getId(), bread.getCustomId(), bread.getTitle(), bread.getContent(), bread.getAnon(), commentDTOs);
        }

        private CommentDTO convertToCommentDTO(Comments comment) {
            return new CommentDTO(comment.getId(), comment.getComment(), comment.getAnon(), comment.getUsername(), comment.getAnonName(), comment.getThread().getId(), comment.getBread().getId());
        }
    }
    
    

    /*
      @GetMapping("/threads")
    public List<Thread> getAllThreads() {
        return threadService.getAllThreads();
    }

    @GetMapping("/threads/{customId}")
    public ResponseEntity<Thread> getThreadByCustomId(@PathVariable(name = "customId") int customId) {
        Thread thread = threadService.getThreadByCustomId(customId);
        return ResponseEntity.ok(thread);
    }*/

    @GetMapping("/threads")
    public List<ThreadDTO> getAllThreads() {
        List<Thread> threads = threadService.getAllThreads();
        return threads.stream().map(this::convertToThreadDTO).collect(Collectors.toList());
    }

    @GetMapping("/threads/{customId}")
    public ResponseEntity<ThreadDTO> getThreadByCustomId(@PathVariable(name = "customId") int customId) {
        Thread thread = threadService.getThreadByCustomId(customId);
        if (thread == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ThreadDTO threadDTO = convertToThreadDTO(thread);
        return ResponseEntity.ok(threadDTO);
    }

    // ... existing code ...

    private ThreadDTO convertToThreadDTO(Thread thread) {
        List<CommentDTO> commentDTOs = thread.getComments().stream().map(this::convertToCommentDTO).collect(Collectors.toList());
        return new ThreadDTO(thread.getId(), thread.getCustomId(), thread.getTitle(), thread.getContent(), thread.getAnon(), commentDTOs);
    }

    private CommentDTO convertToCommentDTO(Comments comment) {
        return new CommentDTO(comment.getId(), comment.getComment(), comment.getAnon(), comment.getUsername(), comment.getAnonName(), comment.getThread().getId(), comment.getBread().getId());
    }

}