package com.example.weepdep;

import com.example.weepdep.controller.AuthController;
import com.example.weepdep.model.Comments;
import com.example.weepdep.model.LoginRequest;
import com.example.weepdep.model.SignUpRequest;
import com.example.weepdep.model.Thread;
import com.example.weepdep.model.User;
import com.example.weepdep.service.CommentsService;
import com.example.weepdep.service.ThreadService;
import com.example.weepdep.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class WeepDepApplication implements CommandLineRunner {

    private final AuthController authController;
    private final ThreadService threadService;
    private final UserService userService;
    private final CommentsService commentService;
    private User currentUser;

    public WeepDepApplication(AuthController authController, ThreadService threadService, UserService userService,CommentsService commentService) {
        this.authController = authController;
        this.threadService = threadService;
        this.userService = userService;
        this.commentService = commentService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WeepDepApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (currentUser == null) {
                handleInitialState(scanner);
            } else {
                handleLoggedInState(scanner);
            }
        }
    }

    private void handleInitialState(Scanner scanner) {
        System.out.println("Choose an option:");
        System.out.println("1. Create Account");
        System.out.println("2. Log In");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                // Call your create account method
                createAccount(scanner);
                break;
            case 2:
                // Call your login method
                login(scanner);
                break;
            case 3:
                System.out.println("Exiting application. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please choose again.");
        }
    }

    private void handleLoggedInState(Scanner scanner) {
        System.out.println("Logged in as: " + currentUser.getUsername());
        List<Thread> threads= displayThreads(scanner);
        
        
        
        String userCommand;
        if (threads.isEmpty()) {
        	System.out.println("Choose an option:");
            System.out.println("c. Create Thread");
            System.out.println("l. Logout");

            userCommand = scanner.nextLine();
        } 
        else {
        	
        	System.out.print("Enter the custom ID of the thread to view, create a new thread(c) or logout(l): ");
        	userCommand = scanner.nextLine();

            
        }
		
		if("c".equals(userCommand)) {
			createThread(scanner);
		}
		else if("l".equals(userCommand)) {
			// Log out the user
            currentUser = null;
		}
        else {
        	enterThread(scanner, Integer.parseInt(userCommand));
        };
    }



    private void createThread(Scanner scanner) {
        System.out.print("Enter thread title: ");
        String title = scanner.nextLine();

        System.out.print("Enter thread content: ");
        String content = scanner.nextLine();
        
        System.out.print("Is it anonymus?(true/false): ");
        String anon = scanner.nextLine();

        Thread newThread = new Thread();
        newThread.setUser(currentUser);
        newThread.setTitle(title);
        newThread.setContent(content);
        newThread.setAnon(Boolean.valueOf(anon));

        ResponseEntity<String> response = authController.createThread(newThread);
        System.out.println("Server Response:");
        System.out.println(response.getBody());
    }





    private void createAccount(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        SignUpRequest request = new SignUpRequest();
        request.setUsername(username);
        request.setPassword(password);

        ResponseEntity<String> response = authController.signUp(request);
        System.out.println("Server Response:");
        System.out.println(response.getBody());
    }




    private List<Thread> displayThreads(Scanner scanner) {
        List<Thread> threads = threadService.getAllThreads(); // You need to create this service

        if (threads.isEmpty()) {
            System.out.println("\nThere are no threads in the app. Be the first to create the first thread.");
        } else {
            System.out.println("\nThreads:");
            for (Thread thread : threads) {
            	
            	if(thread.getAnon()) {
            		System.out.println(thread.getCustomId() + ". " + thread.getTitle() + " by " + thread.getAnonName());
         		}
         		else {
         			System.out.println(thread.getCustomId() + ". " + thread.getTitle() + " by " + thread.getAuthorName());
         		}
            }
            
        }
		return threads;
    }
    private void enterThread(Scanner scanner, int customId) {
        // Implement logic to enter the specified thread
        // For example, display the details of the selected thread
        Thread selectedThread = threadService.getThreadByCustomId(customId);
        if (selectedThread != null) {
            System.out.println("\nThread Details:");
            System.out.println("Author: " + selectedThread.getAuthorName());
            System.out.println("Title: " + selectedThread.getTitle());
            System.out.println("Content: " + selectedThread.getContent());
            
            List <Comments> comments = commentService.getCommentsByThreadId(selectedThread.getId());
            displayComments(scanner, comments,selectedThread);
           
            
        } else {
            System.out.println("Thread not found.");
        }
        
        
    }
    
    private void displayComments(Scanner scanner, List <Comments> comments, Thread selectedThread) {
    	 if(comments.isEmpty()) {
         	System.out.printf("There are no comments in \"%s\". Be the first to create the first comment.\n", selectedThread.getTitle());
         }else {
         	for (Comments comment : comments) {
         		if(comment.getAnon()) {
         			System.out.println(comment.getAnonName() + ": " + comment.getComment());
         		}
         		else {
         			System.out.println(comment.getUsername() + ": " + comment.getComment());
         		}
                
             }
         }
    	 
    	 addCom(scanner,selectedThread);
    	 
    }
    
    private void addCom(Scanner scanner, Thread selectedThread) {
    	System.out.println("Choose an option:");
        System.out.println("1. Add comment");
        System.out.println("2. Back");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
            	System.out.print("Enter comment: ");
                String content = scanner.nextLine();
                
                System.out.print("Is it anonymus?(true/false): ");
                String anon = scanner.nextLine();
                Comments comment = new Comments(currentUser,content, selectedThread, Boolean.valueOf(anon));
                commentService.saveComment(comment);                
                selectedThread = threadService.saveComment(comment, selectedThread);

                // Display the updated thread details with comments
                enterThread(scanner, selectedThread.getCustomId());
                break;
            case 2:
                break;
            default:
                System.out.println("Invalid choice. Please choose again.");
        }
    }
    


    private void login(Scanner scanner) {
        LoginRequest request = new LoginRequest(scanner);

        ResponseEntity<String> response = authController.login(request);
        System.out.println("Server Response:");
        System.out.println(response.getBody());

        if (response.getStatusCode().is2xxSuccessful()) {
            // Login successful, set the currentUser
            currentUser = userService.getUserByUsername(request.getUsername());
        }
    }




}
