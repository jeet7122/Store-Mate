package org.storemate.pos.user_interface.views;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;
import org.kordamp.ikonli.materialdesign2.MaterialDesignT;
import org.storemate.pos.routing.Router;

public class EmployeeDataView {
    private final VBox root;
    public EmployeeDataView(){
        root = new VBox(10);
        Label title = new Label("Employee Data View");
        root.getChildren().add(title);
        HBox topOptionBox = new HBox(10);

        //-----------ADD EMPLOYEE BOX-----------//
        HBox addEmployeeBox = new HBox(10);
        FontIcon addIcon = new FontIcon(MaterialDesignP.PLUS);
        addIcon.setIconColor(Color.DARKCYAN);
        addIcon.setIconSize(50);
        Label addEmployeeLabel = new Label("Add Employee");
        addEmployeeBox.getChildren().addAll(addIcon, addEmployeeLabel);
        addEmployeeBox.setOnMouseClicked(e -> Router.navigate("/employees/add"));

        //----------EDIT-EMPLOYEE-BOX--------//
        HBox editEmployeeBox = new HBox(10);
        FontIcon editIcon = new FontIcon(MaterialDesignP.PEN);
        editIcon.setIconColor(Color.DARKCYAN);
        editIcon.setIconSize(50);
        Label editEmployeeLabel = new Label("Modify Employee");
        editEmployeeBox.getChildren().addAll(editIcon, editEmployeeLabel);
        editEmployeeBox.setOnMouseClicked(e -> Router.navigate("/employees/modify"));

        //-------DELETE-EMPLOYEE-BOX--------//
        HBox deleteEmployeeBox = new HBox(10);
        FontIcon deleteIcon = new FontIcon(MaterialDesignT.TRASH_CAN_OUTLINE);
        deleteIcon.setIconColor(Color.DARKCYAN);
        deleteIcon.setIconSize(50);
        Label deleteEmployeeLabel = new Label("Remove Employee");
        deleteEmployeeBox.getChildren().addAll(deleteIcon, deleteEmployeeLabel);
        deleteEmployeeBox.setOnMouseClicked(e -> Router.navigate("/employees/delete"));

        topOptionBox.getChildren().addAll(addEmployeeBox, editEmployeeBox, deleteEmployeeBox);

        root.getChildren().add(topOptionBox);
    }
    public Parent getView(){
        return root;
    }
}
