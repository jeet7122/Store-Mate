package org.storemate.pos.user_interface.pages.settings_pages;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.storemate.pos.utils.Session;

public class EditUsernameView {
    private final VBox root;
    public EditUsernameView(){
        root = new VBox(10);
        Label title = new Label("Edit username");
        root.getChildren().add(title);
        Label existingUsername = new Label("Existing Username");
        Label existingVal = new Label(Session.getInstance().getCurrentUser().getUsername());
        Label newUserNameLabel = new Label("Enter new username");
        TextField usernameTF = new TextField();
        usernameTF.setPromptText("Enter new username");
        Button updateBTN = new Button("Update");

        GridPane grid = new GridPane();
        grid.add(existingUsername, 0, 0);
        grid.add(existingVal, 1, 0);
        grid.add(newUserNameLabel, 0, 1);
        grid.add(usernameTF, 1, 1);
        grid.add(updateBTN,0,2,1,2);

        root.getChildren().add(grid);
    }
    public Parent getView(){
        return root;
    }
}
