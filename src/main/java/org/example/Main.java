package org.example;

public class Main {
    public static void main(String[] args) {
        // Make a Pack
        Pack pack = new Pack();

        pack.openPack();

        for(Card card: pack.getPack()) {
            System.out.println("Name " + card.getName() +
                    " | Rarity: " + card.getRarity() +
                    " | Price: " + card.getPrice() + " | Image Url: "
                    + card.getImgURL());
        }
    }
}