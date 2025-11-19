package org.storemate.pos.user_interface.components;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.routing.Router;

public class Cards {
    public HBox createCategoryCard(String categoryName, int productCount, Runnable onClickAction) {
        HBox card = new HBox(15);
        card.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #d3d3d3;" +
                        "-fx-border-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        Label nameLabel = new Label(categoryName);
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label countLabel = new Label(productCount + " products");
        countLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 14px;");

        VBox labelBox = new VBox(nameLabel, countLabel);

        card.getChildren().add(labelBox);

        // CLICK HANDLER
        card.setOnMouseClicked(e -> onClickAction.run());

        // Hover styling
        card.setOnMouseEntered(e ->
                card.setStyle(
                        "-fx-background-color: #ffffff;" +
                                "-fx-padding: 15;" +
                                "-fx-background-radius: 10;" +
                                "-fx-border-color: #00bcd4;" +
                                "-fx-border-radius: 10;" +
                                "-fx-cursor: hand;"
                )
        );
        card.setOnMouseExited(e ->
                card.setStyle(
                        "-fx-background-color: #f5f5f5;" +
                                "-fx-padding: 15;" +
                                "-fx-background-radius: 10;" +
                                "-fx-border-color: #d0d0d0;" +
                                "-fx-border-radius: 10;"
                )
        );

        return card;
    }
    public Parent createCategoryList() {
        VBox cardContainer = new VBox(15); // spacing between cards

        // Dummy data (replace with DB data later)
        cardContainer.getChildren().add(createCategoryCard("Electronics", 42, () -> showProducts("Electronics")));
        cardContainer.getChildren().add(createCategoryCard("Shoes", 18, () -> showProducts("Shoes")));
        cardContainer.getChildren().add(createCategoryCard("Furniture", 8, () -> showProducts("Furniture")));
        cardContainer.getChildren().add(createCategoryCard("Toys", 22, () -> showProducts("Toys")));

        ScrollPane scrollPane = new ScrollPane(cardContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        return scrollPane;
    }
    private void showProducts(String categoryName) {
        Router.navigate("/products?category=" + categoryName);
    }



}
