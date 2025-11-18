package org.storemate.pos.user_interface;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.storemate.pos.models.Employee;
import org.storemate.pos.services.AuthorizationService;
import org.storemate.pos.utils.SceneManager;
import org.storemate.pos.utils.Session;

public class DemoUIADMINONLY {
    private final VBox root;
    public DemoUIADMINONLY(){
        Employee employee = Session.getInstance().getCurrentUser();
        AuthorizationService authorizationService = new AuthorizationService(employee.getRole());
        root = new VBox(10);
        Label title = new Label("Only For Admins");
        Button everyOne = new Button("Open For Everyone");
        everyOne.setOnAction(e -> SceneManager.navigate("/dashboard"));
        Button home = new Button("Home");
        home.setOnAction(e -> SceneManager.showHomePage());

        root.getChildren().addAll(title, everyOne, home);
    }
    public Parent getView(){
        return root;
    }
}
