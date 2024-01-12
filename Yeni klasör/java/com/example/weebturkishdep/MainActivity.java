package com.example.weebturkishdep;
// MainActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button btnLogin, btnSignup;
    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference UI elements
        editTextUsername = findViewById(R.id.NameSpace);
        editTextPassword = findViewById(R.id.PwSpace);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        welcomeMessage = findViewById(R.id.WelcomeMessage);

        // Set welcome message
        welcomeMessage.setText("Welcome to James Webb");

        // Set click listeners for buttons
        btnLogin.setOnClickListener(view -> {
            // Handle login button click
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();
            // Implement your login logic here
            // For simplicity, you can start a new activity or display a message
        });

        btnSignup.setOnClickListener(view -> {
            // Handle signup button click
            // Start the SignupActivity and clear the back stack
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
