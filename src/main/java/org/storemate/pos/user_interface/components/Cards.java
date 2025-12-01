package org.storemate.pos.user_interface.components;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.storemate.pos.models.Category;
import org.storemate.pos.routing.Router;

import java.util.List;


/**
 * Utility class for creating UI card components and lists for the application.
 * <p>
 * This class provides methods to create category cards with click and hover effects,
 * and to display a scrollable list of categories with their status.
 * </p>
 * @author Khushi, Jeet
 */
public class Cards {

    /**
     * Creates a visual card representing a category with a name and product count.
     * <p>
     * The card has hover effects and triggers a specified action when clicked.
     * </p>
     *
     * @param categoryName  the name of the category
     * @param productCount  the number of products in the category
     * @param onClickAction a {@link Runnable} to execute when the card is clicked
     * @return an {@link HBox} representing the category card
     */
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

    /**
     * Creates a scrollable list of categories with their status.
     * <p>
     * Each category is displayed as a row containing the category name and status.
     * </p>
     *
     * @param categories a list of {@link Category} objects to display
     * @return a {@link Parent} node containing the scrollable category list
     */
    public Parent createCategoryList(List<Category> categories) {

        VBox box = new VBox(10);

        for (Category c : categories) {
            HBox row = new HBox(10);
            row.getChildren().addAll(
                    new Label(c.getCategory_name()),
                    new Label(c.getStatus().name())
            );
            box.getChildren().add(row);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(box);

        return scrollPane;
    }

    /**
     * Navigates to the products view filtered by the given category name.
     * <p>
     * This method is private and used internally by category cards to show products
     * belonging to a specific category.
     * </p>
     *
     * @param categoryName the category name used for filtering products
     */
    private void showProducts(String categoryName) {
        Router.navigate("/products?category=" + categoryName);
    }



}
