package org.storemate.pos.user_interface;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.storemate.pos.models.Employee;
import org.storemate.pos.models.enums.Permission;
import org.storemate.pos.services.AuthorizationService;
import org.storemate.pos.utils.SceneManager;
import org.storemate.pos.utils.Session;

public class RouterPageDemo {
    private final VBox root;
    public RouterPageDemo(){
        Employee employee = Session.getInstance().getCurrentUser();
        AuthorizationService authorizationService = new AuthorizationService(employee.getRole());
        root= new VBox(10);
        Label title = new Label("Home Page");
        Button everyOne = new Button("Open For Everyone");
        everyOne.setOnAction(e -> SceneManager.showAllAccess());
        Button adminOnly = new Button("Admin Only");
        adminOnly.setDisable(!authorizationService.hasPermission(Permission.ADMIN_ONLY));
        adminOnly.setOnAction(e -> SceneManager.showAdminAccessOnly());
        root.getChildren().addAll(title, everyOne, adminOnly);
    }
    public Parent getView(){
        return root;
    }
}
