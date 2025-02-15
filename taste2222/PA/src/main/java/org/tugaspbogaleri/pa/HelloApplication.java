package org.tugaspbogaleri.pa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {
    Connection con = DB.connectDB();

    static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        if (con != null) {
            System.out.println("Database connection successful.");
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            stage.setTitle("Program galeri");
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        if (con != null) {
            try {
                con.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {launch();}}