package org.example;

import com.google.gson.Gson;
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
    private static final String SCRYFALL_API_URL =
            "https://api.scryfall.com/cards/search?q=set:bloomburrow+rarity:";

    public static Card fetchCard(String rarity) throws IOException {
        // Adjust URL and fetch
        String url = SCRYFALL_API_URL + rarity.toLowerCase();
        return fetchFromURL(url);
    }

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

            // Execute request and get response
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Get the response body as a String
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JsonObject root = JsonParser.parseString(jsonResponse).getAsJsonObject();
                JsonArray cardsArr = root.getAsJsonArray("data");

                // Error handling for invalid array size
                if (cardsArr.isEmpty()) {
                    throw new IOException("No cards found for rarity");
                }

                // Randomize from API pull
                JsonObject randomCard = cardsArr
                        .get(new Random().nextInt(cardsArr.size()))
                        .getAsJsonObject();

                String name = randomCard.get("name").getAsString();
                String cardRarity = randomCard.get("rarity").getAsString();
                String imgURL = randomCard.getAsJsonObject("image_uris").
                        get("normal").getAsString();
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