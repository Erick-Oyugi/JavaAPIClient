/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cblnotification;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.SSLSocketFactory;

public class IPNServices {
    public static void main(String[] args) {
        try {
            // Define the URL of the server
            String serverUrl = "https://webhook-server.creditbank.co.ke:3001/notification";
          
            // Create the HttpURLConnection object
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            
            // Set the request method to POST
            connection.setRequestMethod("POST");
            connection.setSSLSocketFactory(new InsecureSSLSocketFactory());


            
            // Set the request headers (if needed)
            connection.setRequestProperty("Content-Type", "application/json");
            
            // Enable input and output streams
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            // Create the request body
            String requestBody = "{\"key\": \"value\"}";
            
            // Write the request body
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();
            
            // Get the response code
            int responseCode = connection.getResponseCode();
            
            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            reader.close();
            
            // Print the response
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + response.toString());
            
            // Disconnect the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

