package com.example.weepdep;

import com.example.weepdep.controller.AuthController;
import com.example.weepdep.model.LoginRequest;
import com.example.weepdep.model.SignUpRequest;
import com.example.weepdep.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

import java.util.Scanner;

@SpringBootApplication
public class WeepDepApplication implements CommandLineRunner {

    private final AuthController authController;

    public WeepDepApplication(AuthController authController) {
        this.authController = authController;
    }

    public static void main(String[] args) {
        SpringApplication.run(WeepDepApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
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


    private void login(Scanner scanner) {
        LoginRequest request = new LoginRequest(scanner);

        ResponseEntity<String> response = authController.login(request);
        System.out.println("Server Response:");
        System.out.println(response.getBody()); // Use getBody() to get the response body
    }




}
