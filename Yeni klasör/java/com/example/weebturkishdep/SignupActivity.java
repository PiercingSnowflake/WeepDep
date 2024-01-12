package com.example.weebturkishdep;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextConfirmUsername, editTextPassword, editTextConfirmPassword;
    private Button btnConfirm, btnBack;

    // Handler to update UI from the background thread
    private Handler uiHandler = new Handler(msg -> {
        // Handle UI updates here
        // For simplicity, you can display a Toast message
        showToast("Signup successful: " + msg.obj.toString());
        return true;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Reference UI elements
        editTextUsername = findViewById(R.id.editTextText);
        editTextConfirmUsername = findViewById(R.id.editTextText2);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextTextPassword2);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);

        // Set click listener for the Confirm button
        btnConfirm.setOnClickListener(view -> {
            // Handle signup confirmation
            String username = editTextUsername.getText().toString();
            String confirmUsername = editTextConfirmUsername.getText().toString();
            String password = editTextPassword.getText().toString();
            String confirmPassword = editTextConfirmPassword.getText().toString();

            // Validate input (you can add more validation as needed)
            if (username.isEmpty() || !username.equals(confirmUsername)) {
                showToast("Invalid username or confirmation");
                return;
            }

            if (password.isEmpty() || !password.equals(confirmPassword)) {
                showToast("Invalid password or confirmation");
                return;
            }

            // If input is valid, call the signUp method in a separate thread
            ApiRepository apiRepository = new ApiRepository();
            apiRepository.signUp(Executors.newCachedThreadPool(), uiHandler);

            // Set result and finish activity
            setResult(RESULT_OK);
            finish();

            // Add log message for signup confirmation action
            Log.d("fortytwo", "Signup confirmation requested. Username: " + username);
        });

        // Set click listener for the Back button
        btnBack.setOnClickListener(view -> {
            // Handle back button click
            finish(); // Close the SignupActivity and return to the previous screen

            // Add log message for back button click
            Log.d("fortytwo", "Back button clicked in SignupActivity");
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
