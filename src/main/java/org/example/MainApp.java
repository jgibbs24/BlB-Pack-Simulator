package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import java.util.List;

public class MainApp extends Application {

    private VBox mainLayout = new VBox(10);
    private Button openAnotherPackButton;
    private Button openPackButton;
    private FlowPane cardsLayout;
    private HBox cardDetailLayout = new HBox(10);
    private double totalValue = 0.0;

    private int packsOpened = 0;
    private double totalCardValue = 0.0;


    @Override
    public void start(Stage primaryStage) {
        // Create initial stage
        ImageView packImage = new ImageView(
                getClass().getResource("/images/blb_pack_wrapper.JPG").toExternalForm());
        packImage.setFitWidth(200);
        packImage.setPreserveRatio(true);

        // Open pack Button
        Button openPackButton = new Button("Open Pack");
        openPackButton.setOnAction(e -> openPackAction(primaryStage, packImage));

        VBox initialLayout = new VBox(10);
        initialLayout.getChildren().addAll(packImage, openPackButton);
        initialLayout.setAlignment(Pos.CENTER);

        // Background
        initialLayout.setStyle("-fx-background-image: url('" +
                getClass().getResource("/images/bloomburrow_background.jpg").toExternalForm() +
                "'); -fx-background-size: cover; -fx-background-position: center;");
        // Set stage
        Scene initialScene = new Scene(initialLayout, 1920, 1080);
        primaryStage.setTitle("Bloomburrow Pack Opener");
        primaryStage.setScene(initialScene);
        primaryStage.show();

    }

    private void openPackAction(Stage primaryStage, ImageView packImage) {
        // Open pack and fetch cards
        Pack pack = new Pack();
        pack.openPack();
        List<Card> openedPack = pack.getPack();

        // Replace wrapper with card Images
        packImage.setImage(null);

        // Create FlowPane for cards
        cardsLayout = new FlowPane(10,10);
        cardsLayout.setAlignment(Pos.CENTER);

        cardsLayout.getChildren().clear();

        // Add card details
        double packVal = 0.0;
        for(Card card: openedPack) {
            // Individual card details
            ImageView cardImage = new ImageView(card.getImgURL());
            cardImage.fitWidthProperty().bind(cardsLayout.widthProperty().divide(10));  // Adjust the divisor for the size you want
            cardImage.setPreserveRatio(true);

            // layout for card details
            VBox cardBox = new VBox(5);
            cardBox.getChildren().addAll(cardImage, new Button(card.getName() +
                    " - $" + card.getPrice()));

            cardsLayout.getChildren().add(cardBox);
            packVal += card.getPrice();
        }

        // Create button for new pack
        openAnotherPackButton = new Button("Open Another Pack?");
        openAnotherPackButton.setOnAction(e -> openAnotherPackAction(primaryStage));

        // Display total value
        String totalPackValue = "Total Pack Value: " + String.format("%.2f", packVal);
        Button totalValueButton = new Button(totalPackValue);

        cardDetailLayout.setAlignment(Pos.BOTTOM_LEFT);
        cardDetailLayout.getChildren().clear();
        cardDetailLayout.getChildren().addAll(openAnotherPackButton);

        HBox totalValueLayout = new HBox(10);
        totalValueLayout.setAlignment(Pos.BOTTOM_RIGHT);
        totalValueLayout.getChildren().add(totalValueButton);

        // Create box for up / down on value
        packsOpened++;
        totalCardValue += packVal;
        HBox compareBox = getValueCompareBox();

        // Final Layout w/ cards
        VBox finalLayout = new VBox(10);
        finalLayout.getChildren().addAll(cardsLayout, cardDetailLayout, totalValueLayout, compareBox);
        finalLayout.setAlignment(Pos.CENTER);

        finalLayout.setStyle("-fx-background-image: url('" +
                getClass().getResource("/images/bloomburrow_background.jpg").toExternalForm() +
                "'); -fx-background-size: cover; -fx-background-position: center;");
        // Set Final Scene
        Scene finalScene = new Scene(finalLayout,1920,1080);
        primaryStage.setScene(finalScene);
    }

    private void openAnotherPackAction(Stage primaryStage) {
        start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private HBox getValueCompareBox() {
        double totalSpent = packsOpened * 4.99;
        String result = String.format("Spent: $%.2f / Value: $%.2f",
                totalSpent, totalCardValue);
        Button resultButton = new Button(result);

        if(totalCardValue > totalSpent) {
            resultButton.setStyle("-fx-text-fill: green;");
        } else if(totalCardValue == totalSpent) {
            resultButton.setStyle("-fx-text-fill: yellow;");
        } else if(totalCardValue < totalSpent) {
            resultButton.setStyle("-fx-text-fill: red;");
        } else {
            resultButton.setStyle("-fx-text-fill: black;");
        }
        HBox resultBox = new HBox(resultButton);
        resultBox.setAlignment(Pos.TOP_RIGHT);
        return resultBox;

    }
}
