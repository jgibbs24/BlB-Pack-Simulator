package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.Random;

public class API {
    // Base URL
    private static final String SCRYFALL_API_URL =
            "https://api.scryfall.com/cards/search?q=set:bloomburrow+rarity:";

    // Fetch a card of a specific rarity
    public static Card fetchCard(String rarity) throws IOException {
        // Adjust URL and fetch
        String url = SCRYFALL_API_URL + rarity.toLowerCase() + "+-type:basic";
        return fetchFromURL(url);
    }

    // Fetch a land card
    public static Card fetchLand() throws IOException {
        // Adjust URL and fetch
        String url = "https://api.scryfall.com/cards/search?q=set:bloomburrow+type:land";
        return fetchFromURL(url);
    }

    private static Card fetchFromURL(String url) throws IOException {
        // Create an HttpClient instance
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            // Prepare the GET request
            HttpGet request = new HttpGet(url);

            // **Add required headers for Scryfall**
            request.setHeader("User-Agent", "BloomburrowPackOpener/1.0 (your-email@example.com)");
            request.setHeader("Accept", "application/json");

            // Execute request and get response
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Get the response body as a String
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JsonObject root = JsonParser.parseString(jsonResponse).getAsJsonObject();

                // Handle error response from API
                if (root.has("object") && root.get("object").getAsString().equals("error")) {
                    throw new IOException("Scryfall API error: " + root.get("details").getAsString());
                }

                // Get the 'data' array safely
                JsonArray cardsArr = root.has("data") ? root.getAsJsonArray("data") : null;

                // Error handling for invalid or empty array
                if (cardsArr == null || cardsArr.isEmpty()) {
                    throw new IOException("No cards found for URL: " + url);
                }

                // Randomize from API pull
                JsonObject randomCard = cardsArr
                        .get(new Random().nextInt(cardsArr.size()))
                        .getAsJsonObject();

                // Extract card details
                String name = randomCard.get("name").getAsString();
                String cardRarity = randomCard.get("rarity").getAsString();

                // Safely get image URL (some cards may not have 'image_uris')
                String imgURL = "";
                if (randomCard.has("image_uris") &&
                        randomCard.get("image_uris").getAsJsonObject().has("normal")) {
                    imgURL = randomCard.getAsJsonObject("image_uris").get("normal").getAsString();
                }

                // Extract price if available
                double price = 0.0;
                if (randomCard.has("prices")) {
                    JsonObject prices = randomCard.getAsJsonObject("prices");
                    if (prices.has("usd") && !prices.get("usd").isJsonNull()) {
                        price = prices.get("usd").getAsDouble();
                    }
                }

                return new Card(name, cardRarity, imgURL, price);
            }
        }
    }
}