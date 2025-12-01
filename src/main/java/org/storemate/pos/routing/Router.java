package org.storemate.pos.routing;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.storemate.pos.models.Category;
import org.storemate.pos.models.enums.Permission;
import org.storemate.pos.services.AuthorizationService;
import org.storemate.pos.user_interface.EmployeeLoginFormView;
import org.storemate.pos.user_interface.EmployeeRegistrationFormView;
import org.storemate.pos.user_interface.pages.*;
import org.storemate.pos.user_interface.pages.settings_pages.EditUsernameView;
import org.storemate.pos.user_interface.pages.settings_pages.ForgotPasswordView;
import org.storemate.pos.user_interface.pages.settings_pages.ManageSecurityQuestionsView;
import org.storemate.pos.user_interface.pages.settings_pages.ModifyPasswordPage;
import org.storemate.pos.user_interface.views.*;
import org.storemate.pos.utils.SceneManager;
import org.storemate.pos.utils.Session;
import org.storemate.pos.utils.TransitionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Router {
    private static Stage stage;
    private static final Map<String, Route> routes = new HashMap<>();
    private static final Set<String> publicRoutes = Set.of("/login", "/register");
    public static void init(Stage s){
        stage = s;
        registerRoutes();
    }

    //All the Routes with permissions
    private static void registerRoutes(){
        routes.put("/dashboard", new Route(() -> new DashboardViewForAdmin().getView(), Permission.VIEW_DASHBOARD));
        routes.put("/employees", new Route(() -> new EmployeeDataView().getView(), Permission.MANAGE_EMPLOYEES));
        routes.put("/employees/add", new Route(() -> new EmployeeRegistrationFormView().getView(), Permission.MANAGE_EMPLOYEES));
        routes.put("/employees/modify", new Route(() -> new EmployeeModificationFormView().getView(), Permission.MANAGE_EMPLOYEES));
        routes.put("/employees/delete", new Route(() -> new DeleteEmployeeView().getView(), Permission.MANAGE_EMPLOYEES));
        routes.put("/suppliers", new Route(() -> new SuppliersDataView().getView(), Permission.MANAGE_SUPPLIERS));
        routes.put("/suppliers/add", new Route(() -> new AddSuppliersView().getView(), Permission.MANAGE_SUPPLIERS));
        routes.put("/suppliers/modify", new Route(() -> new ModifySuppliersView().getView(), Permission.MANAGE_SUPPLIERS));
        routes.put("/suppliers/delete", new Route(() -> new DeleteSuppliersView().getView(), Permission.MANAGE_SUPPLIERS));
        routes.put("/products", new Route(() -> new ProductsDataView().getView(), Permission.VIEW_PRODUCTS));
        routes.put("/products/add", new Route(() -> new AddProductsView().getView(), Permission.MANAGE_PRODUCTS));
        routes.put("/products/modify", new Route(() -> new ModifyProductsView().getView(), Permission.MANAGE_PRODUCTS));
        routes.put("/products/delete", new Route(() -> new DeleteProductsView().getView(), Permission.MANAGE_PRODUCTS));
        routes.put("/pos", new Route(() -> new ProcessSalesView().getView(), Permission.PROCESS_SALES));
        routes.put("/purchases", new Route(() -> new PurchaseDataView().getView(), Permission.MANAGE_PURCHASES));
        routes.put("/purchases/order", new Route(() -> new PurchaseOrderScreen().getView(), Permission.MANAGE_PURCHASES));
        routes.put("/reports/sales", new Route(() -> new SalesDataView().getView(), Permission.VIEW_SALES_REPORTS));
        routes.put("/shifts", new Route(() -> new ShiftDataView().getView(), Permission.VIEW_SHIFTS));
        routes.put("/settings/general", new Route(() -> new SettingsTabView().getView(), Permission.MANAGE_SETTINGS));
        routes.put("/settings/general/edit-username", new Route(() -> new EditUsernameView().getView(), Permission.MANAGE_SETTINGS));
        routes.put("/settings/general/modify-password", new Route(() -> new ModifyPasswordPage().getView(), Permission.MANAGE_SETTINGS));
        routes.put("/settings/general/forgot-password", new Route(() -> new ForgotPasswordView().getView(), Permission.MANAGE_SETTINGS));
        routes.put("/settings/general/manage-security-questions", new Route(() -> new ManageSecurityQuestionsView().getView(), Permission.MANAGE_SETTINGS));
        routes.put("/categories", new Route(() -> new CategoriesDataView().getView(), Permission.VIEW_CATEGORIES));
        routes.put("/categories/add", new Route(() -> new AddCategoriesView().getView(), Permission.MANAGE_CATEGORIES));
        routes.put("/categories/modify", new Route(() -> new ModifyCategoriesView().getView(), Permission.MANAGE_CATEGORIES));
        routes.put("/categories/delete", new Route(() -> new DeleteCategoriesView().getView(), Permission.MANAGE_CATEGORIES));
        routes.put("/transactions", new Route(() -> new TransactionsDataView().getView(), Permission.VIEW_TRANSACTIONS));
        routes.put("/transactions/refunds", new Route(() -> new ReturnsDataView().getView(), Permission.PROCESS_REFUND));
        routes.put("/register", new Route(() -> new EmployeeRegistrationFormView().getView(), Permission.MANAGE_USER_PROFILE));
        routes.put("/login", new Route(() -> new EmployeeLoginFormView().getView(), Permission.MANAGE_USER_PROFILE));
    }

    public static void navigate(String routePath){
        if (!routes.containsKey(routePath)){
            System.out.println("Route not found: " + routePath);
            return;
        }
        if (publicRoutes.contains(routePath)){
            Parent view = routes.get(routePath).getViewBuilder().get();
            Scene scene = new Scene(view);
            scene.getStylesheets().add(Router.class.getResource("/css/index.css").toExternalForm());
            stage.setScene(scene);
            return;
        }
        if (!Session.getInstance().isLoggedIn()){
            Parent view = routes.get("/login").getViewBuilder().get();
            stage.setScene(new Scene(view));
            return;
        }

        Route route = routes.get(routePath);
        AuthorizationService authorizationService = new AuthorizationService(Session.getInstance().getCurrentUser().getRole());


        if (!authorizationService.hasPermission(route.getRequiredPermission())){
            SceneManager.getMainLayoutView().getView().setCenter(new AccessDenied().getView());
            return;
        }
        Parent view = route.getViewBuilder().get();
        SceneManager.getMainLayoutView().getView().setCenter(view);
        TransitionUtil.applyTikTokTransition(view);
    }


}
