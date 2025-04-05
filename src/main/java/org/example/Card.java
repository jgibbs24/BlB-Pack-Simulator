package org.example;

public class Card {
    private String name;
    private String rarity;
    private String imgURL;
    private double price;

    public Card(String name, String rarity, String imgURL, double price) {
        this.name = name;
        this.rarity = rarity;
        this.imgURL = imgURL;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

