package org.example;

import java.util.*;
import java.net.*;
import java.io.*;

public class Pack {
    private List<Card> pack;
    public static final int NUM_CARDS = 15;

    public Pack() {
        this.pack = new ArrayList<>();
    }

    public List<Card> getPack() {
        return this.pack;
    }

    public void addCard(Card card) {
        pack.add(card);
    }

    public void openPack() {
        pack.clear();

        for(int i = 0; i < 10; i++) {
            // Fetch 10 Commons
            pack.add(fetchCard("Common"));
        }

        for(int i = 0; i < 3; i++) {
            // Fetch 3 Uncommons
            pack.add(fetchCard("Uncommon"));
        }

        // Fetch Rare / Mythic
        String rareOrMythic = generateRarity();
        pack.add(fetchCard(rareOrMythic));

        // Fetch Land
        pack.add(fetchLand());

        // Sort
        sortRarity();
    }

    private String generateRarity() {
        int roll = (int) (Math.random() * 100);
        if(roll < 15) {
            return "Mythic";

        } else {
            return "Rare";
        }
    }

    private int rarityVal(String rarity) {
        switch (rarity) {
            case "Common":
                return 1;
            case "Uncommon":
                return 2;
            case "Rare":
                return 3;
            case "Mythic":
                return 4;
            default:
                return 0;
        }
    }

    public void sortRarity() {
        pack.sort(new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return Integer.compare(rarityVal(c1.getRarity()),rarityVal(c2.getRarity()));
            }
        });
    }

    private Card fetchCard(String rarity) {
        // Fetch from API
        try {
            return API.fetchCard(rarity);
        } catch (IOException e) {
            e.printStackTrace();
            return new Card("Failed to fetch..", rarity, "", 0.0);
        }
    }

    private Card fetchLand() {
        // Fetch from API
        try {
            return API.fetchLand();
        } catch (IOException e) {
            e.printStackTrace();
            return new Card("failed to fetch..", "Land", "", 0.0);
        }
    }

    public double getTotalPackVal() {
        double total = 0.0;
        for(Card card: pack) {
            total += card.getPrice();
        }
        return total;
    }
}

