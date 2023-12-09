package com.example.weepdep.model;

import java.util.Scanner;

public class LoginRequest {
    private String username;
    private String password;
    
    public LoginRequest() {
        // Default constructor needed for JSON deserialization
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }


    // Constructor to prompt user input
    public LoginRequest(Scanner scanner) {
        promptUserInput(scanner);
    }
    
    private void promptUserInput(Scanner scanner) {
        System.out.print("Enter username: ");
        this.username = scanner.nextLine();

        System.out.print("Enter password: ");
        this.password = scanner.nextLine();
    }


    // getters and setters (you can generate them using your IDE)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
