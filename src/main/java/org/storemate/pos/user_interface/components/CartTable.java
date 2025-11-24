package org.storemate.pos.user_interface.components;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import org.storemate.pos.dto.CartDTO;
import org.storemate.pos.dto.Cart_Item_DTO;

import java.math.BigDecimal;

public class CartTable {

    private final CartDTO cart;
    private final TableView<Cart_Item_DTO> cartTable;
    private final ObservableList<Cart_Item_DTO> observableItems = FXCollections.observableArrayList();


    // Labels for totals (should be linked in your UI)
    private final Label subtotalLabel;
    private final Label totalTaxLabel;
    private final Label totalWithTaxLabel;

    public CartTable(CartDTO cart, Label subtotalLabel, Label totalTaxLabel, Label totalWithTaxLabel) {
        this.cart = cart;
        this.subtotalLabel = subtotalLabel;
        this.totalTaxLabel = totalTaxLabel;
        this.totalWithTaxLabel = totalWithTaxLabel;

        cartTable = new TableView<>();
        setupTable();
    }

    public TableView<Cart_Item_DTO> getCartTable() {
        return cartTable;
    }

    private void setupTable() {
        // Product ID
        TableColumn<Cart_Item_DTO, String> idCol = new TableColumn<>("Product ID");
        idCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getProduct_id()));

        // Name
        TableColumn<Cart_Item_DTO, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getProduct_name()));

        // Tax Category
        TableColumn<Cart_Item_DTO, String> taxCol = new TableColumn<>("Tax Cat");
        taxCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getTaxCategory().name()));

        // Unit Price
        TableColumn<Cart_Item_DTO, BigDecimal> priceCol = new TableColumn<>("Unit Price");
        priceCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getUnitPrice()));

        // Quantity (editable)
        TableColumn<Cart_Item_DTO, Integer> qtyCol = createQuantityColumn(cart, this::updateTotalsLabels);

        // Tax Amount
        TableColumn<Cart_Item_DTO, BigDecimal> taxAmountCol = new TableColumn<>("Tax Amount");
        taxAmountCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTaxAmount()));

        // Total
        TableColumn<Cart_Item_DTO, BigDecimal> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTotalAmount()));

        // Remove Button
        TableColumn<Cart_Item_DTO, Void> removeCol = createRemoveColumn();

        // Add columns
        cartTable.getColumns().addAll(idCol, nameCol, taxCol, priceCol, qtyCol, taxAmountCol, totalCol, removeCol);

        cartTable.setEditable(true);
        observableItems.setAll(cart.getCartItems());
        cartTable.setItems(observableItems);
        // Optional: resize columns to fit content
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Initialize totals
        updateTotalsLabels();
    }

    // Call this whenever cart changes
    private void updateTotalsLabels() {
        subtotalLabel.setText("Subtotal: $" + cart.getSubtotal().setScale(2, BigDecimal.ROUND_HALF_UP));
        totalTaxLabel.setText("Total Tax: $" + cart.getTotalTax().setScale(2, BigDecimal.ROUND_HALF_UP));
        totalWithTaxLabel.setText("Total Price: $" + cart.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));

        // Clear old style classes
        subtotalLabel.getStyleClass().removeAll("label-total-positive", "label-total-negative", "label-mixed-total");
        totalWithTaxLabel.getStyleClass().removeAll("label-total-positive", "label-total-negative", "label-mixed-total");

        // Apply style based on cart state
        if (cart.isMixed()) {
            subtotalLabel.getStyleClass().add("label-mixed-total");
            totalWithTaxLabel.getStyleClass().add("label-mixed-total");
        } else if (cart.isReturnMode()) {
            subtotalLabel.getStyleClass().add("label-total-negative");
            totalWithTaxLabel.getStyleClass().add("label-total-negative");
        } else {
            subtotalLabel.getStyleClass().add("label-total-positive");
            totalWithTaxLabel.getStyleClass().add("label-total-positive");
        }
    }


    // Call this method to refresh the table (after adding new item)
    public void refreshTable() {
        observableItems.setAll(cart.getCartItems());
        updateTotalsLabels();

        // Apply row styles based on cart item type
        cartTable.setRowFactory(tv -> new TableRow<Cart_Item_DTO>() {
            @Override
            protected void updateItem(Cart_Item_DTO item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setStyle("");
                    getStyleClass().removeAll("table-row-sale", "table-row-return", "table-row-mixed");
                } else {
                    getStyleClass().removeAll("table-row-sale", "table-row-return", "table-row-mixed");

                    if (item.isReturn()) {
                        getStyleClass().add("table-row-return"); // light red row
                    } else if (item.isSale()) {
                        getStyleClass().add("table-row-sale");   // default blue-ish row
                    } else {
                        getStyleClass().add("table-row-mixed");  // light orange for mixed
                    }
                }
            }
        });
    }

    public static TableColumn<Cart_Item_DTO, Integer> createQuantityColumn(CartDTO cart, Runnable updateTotalsLabels) {
        TableColumn<Cart_Item_DTO, Integer> qtyCol = new TableColumn<>("Quantity");

        // Value factory
        qtyCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getQuantity()));

        // Editable cell
        qtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        qtyCol.setOnEditCommit(event -> {
            Cart_Item_DTO item = event.getRowValue();
            item.setQuantity(event.getNewValue());
            cart.recalculateCartTotal();
            updateTotalsLabels.run();// call UI refresh
            event.getTableView().refresh();
        });

        return qtyCol;
    }

    private TableColumn<Cart_Item_DTO, Void> createRemoveColumn() {

        TableColumn<Cart_Item_DTO, Void> removeCol = new TableColumn<>("Remove");

        removeCol.setCellFactory(col -> new TableCell<>() {

            private final Button removeBtn = new Button("X");

            {
                removeBtn.setOnAction(e -> {
                    Cart_Item_DTO item = getTableView().getItems().get(getIndex());

                    // Remove from backend DTO
                    cart.removeItem(item);

                    // Remove from UI table
                    cartTable.getItems().remove(item);

                    // Update totals
                    updateTotalsLabels();
                });

                // Optional styling
                removeBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeBtn);
                }
            }
        });

        return removeCol;
    }

}
