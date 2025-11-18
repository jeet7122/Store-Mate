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

public class DemoUIGeneral {
    private final VBox root;
    public DemoUIGeneral(){
        Employee employee = Session.getInstance().getCurrentUser();
        AuthorizationService authorizationService = new AuthorizationService(employee.getRole());
        root = new VBox(10);
        Label title = new Label("Everyone can access");
        Button adminOnly = new Button("Admin Only");
        adminOnly.setDisable(!authorizationService.hasPermission(Permission.ADMIN_ONLY));

        adminOnly.setOnAction(e -> SceneManager.showAdminAccessOnly());
        Button home = new Button("Home");
        home.setOnAction(e -> SceneManager.showHomePage());

        root.getChildren().addAll(title, adminOnly, home);
    }
    public Parent getView(){
        return root;
    }
}
