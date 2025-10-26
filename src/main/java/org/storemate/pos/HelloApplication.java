package org.storemate.pos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Label label = new Label("Hello World!");
        Scene scene = new Scene(label, 300, 300);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
