package org.storemate.pos.user_interface.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.storemate.pos.dto.PurchaseDTO;
import org.storemate.pos.dto.Purchase_Item_DTO;

import java.math.BigDecimal;

public class PurchaseCartTable {
    private final TableView<Purchase_Item_DTO> table;
    private final PurchaseDTO purchaseCart;
    private final ObservableList<Purchase_Item_DTO> rows = FXCollections.observableArrayList();
    public PurchaseCartTable(PurchaseDTO purchaseCart) {
        this.purchaseCart = purchaseCart;
        table = new TableView<>();
        table.setItems(rows);

        TableColumn<Purchase_Item_DTO, String> nameCol = new TableColumn<>("Product");
        nameCol.setCellValueFactory(c -> javafx.beans.property.SimpleStringProperty.stringExpression(
                javafx.beans.binding.Bindings.createStringBinding(() -> c.getValue().getProduct().getProduct_name())
        ));
        nameCol.setPrefWidth(250);

        TableColumn<Purchase_Item_DTO, BigDecimal> unitCol = new TableColumn<>("Unit Cost");
        unitCol.setCellValueFactory(new PropertyValueFactory<>("costPrice"));
        unitCol.setPrefWidth(100);

        TableColumn<Purchase_Item_DTO, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setPrefWidth(60);

        TableColumn<Purchase_Item_DTO, BigDecimal> taxCol = new TableColumn<>("Tax");
        taxCol.setCellValueFactory(new PropertyValueFactory<>("taxAmount"));
        taxCol.setPrefWidth(100);

        TableColumn<Purchase_Item_DTO, BigDecimal> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalCol.setPrefWidth(120);

        table.getColumns().addAll(nameCol, unitCol, qtyCol, taxCol, totalCol);
        refresh();
    }

    public TableView<Purchase_Item_DTO> getTable() {
        return table;
    }

    public void refresh() {
        purchaseCart.recalcALL();
        rows.setAll(purchaseCart.getCart());
    }
}
