package com.example.weebturkishdep;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;

public class ApiRepository {

    // Replace the endpoint with your actual endpoint
    private static final String API_ENDPOINT = "http://10.0.2.2:8080/api/auth/signup";

    public void signUp(ExecutorService executorService, Handler uiHandler) {
        executorService.execute(() -> {
            try {
                // Create a URL object with the API endpoint
                URL url = new URL(API_ENDPOINT);

                // Open a connection to the specified URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to POST (since you want to create an account)
                conn.setRequestMethod("POST");

                // Set other required headers if needed
                // conn.setRequestProperty("Content-Type", "application/json");

                // Access input and output streams using the conn object
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                // Read the response
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                // Disconnect after reading the response
                conn.disconnect();

                // Create a Message object to send the response to the UI thread
                Message msg = new Message();
                msg.obj = buffer.toString();
                uiHandler.sendMessage(msg);

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
