package org.example;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

public class API {
    private static final String SCRYFALL_API_URL =
            "https://api.scryfall.com/cards/search?q=set:bloomburrow";

    // Fetch Card
    public static Card fetchCard(String name) throws IOException {
        String url = SCRYFALL_API_URL + name.replace(" ", "+");

        // Create an HttpClient instance
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            // Prepare the GET request with the URL
            HttpGet request = new HttpGet(url);

            // Execute the request and get the response
            try (CloseableHttpResponse response = httpClient.execute(request)) {

                // Get the response body as a String
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Parse the JSON response into a Card object using Gson
                Gson gson = new Gson();
                Card card = gson.fromJson(jsonResponse, Card.class);

                return card; // Return the fetched card
            }
        }
    }
}