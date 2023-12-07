package com.example.weepdep;

import com.example.weepdep.controller.AuthController;
import com.example.weepdep.model.LoginRequest;
import com.example.weepdep.model.SignUpRequest;
import com.example.weepdep.model.Thread;
import com.example.weepdep.model.User;
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
    private User currentUser;

    public WeepDepApplication(AuthController authController, ThreadService threadService, UserService userService) {
        this.authController = authController;
        this.threadService = threadService;
        this.userService = userService;
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
        displayThreads(scanner);

        System.out.println("Choose an option:");
        System.out.println("1. Create Thread");
        System.out.println("2. Logout");

        String userCommand = scanner.nextLine();

        switch (userCommand) {
            case "1":
                createThread(scanner);
                break;
            case "2":
                // Log out the user
                currentUser = null;
                break;
            default:
                System.out.println("Invalid choice. Please choose again.");
        }
    }



    private void createThread(Scanner scanner) {
        System.out.print("Enter thread title: ");
        String title = scanner.nextLine();

        System.out.print("Enter thread content: ");
        String content = scanner.nextLine();

        Thread newThread = new Thread();
        newThread.setAuthorName(currentUser.getUsername());
        newThread.setTitle(title);
        newThread.setContent(content);

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




    private void displayThreads(Scanner scanner) {
        List<Thread> threads = threadService.getAllThreads(); // You need to create this service

        if (threads.isEmpty()) {
            System.out.println("There are no threads in the app. Be the first to create the first thread.");
        } else {
            System.out.println("Threads:");
            for (Thread thread : threads) {
                System.out.println(thread.getCustomId() + ". " + thread.getTitle() +
                        " by " + thread.getAuthorName());
            }
            System.out.print("Enter the custom ID of the thread to view or type 'logout': ");
            String userInput = scanner.nextLine();

            if (!"logout".equalsIgnoreCase(userInput)) {
                // Handle entering the thread by calling another method
                enterThread(Integer.parseInt(userInput));
            }
        }
    }
    private void enterThread(int customId) {
        // Implement logic to enter the specified thread
        // For example, display the details of the selected thread
        Thread selectedThread = threadService.getThreadByCustomId(customId);
        if (selectedThread != null) {
            System.out.println("Thread Details:");
            System.out.println("Author: " + selectedThread.getAuthorName());
            System.out.println("Title: " + selectedThread.getTitle());
            System.out.println("Content: ");
            displayWrappedContent(selectedThread.getContent());
        } else {
            System.out.println("Thread not found.");
        }
    }


    private void displayWrappedContent(String content) {
        // Split the content into words
        String[] words = content.split("\\s+");

        // Initialize variables to keep track of line length and maximum line length
        int lineLength = 0;
        int maxLineLength = 40; // You can adjust this value based on your preference

        // Iterate through words and print them, wrapping to the next line when needed
        for (String word : words) {
            if (lineLength + word.length() <= maxLineLength) {
                // If the word can fit on the current line, print it
                System.out.print(word + " ");
                lineLength += word.length() + 1; // Add 1 for the space between words
            } else {
                // If the word doesn't fit, start a new line and print the word
                System.out.println();
                System.out.print(word + " ");
                lineLength = word.length() + 1; // Reset line length
            }
        }
        System.out.println("\n");
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
