package org.tugaspbogaleri.pa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.tugaspbogaleri.pa.Important.SesiUser;

import java.io.IOException;
import java.sql.*;

public class LoginController{
    @FXML private Button btnLogin;

    @FXML private PasswordField txtPassword;

    @FXML private Label txtWarning;

    @FXML private Hyperlink hyperRegister;

    @FXML private TextField txtUsername;

    static Stage loginStage;

    private final Connection connection = DB.connectDB();

    //ACTION
    public void btnLoginClicked(ActionEvent actionEvent) throws IOException {Login(actionEvent);}
    public void hyperRegisterClicked(ActionEvent actionEvent){openRegisterPopup(actionEvent);}

    //METHOD
    private void Login(ActionEvent actionEvent) throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM akun WHERE username=? AND password=?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                SesiUser.LoginSession = 1;
                SesiUser.uid = resultSet.getInt("uid");
                SesiUser.username = resultSet.getString("username");
                SesiUser.email = resultSet.getString("email");
                SesiUser.izin = resultSet.getString("izin");

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login Berhasil!",ButtonType.CLOSE);
                alert.setTitle("Notifikasi");alert.setHeaderText("Notifikasi");alert.showAndWait();
                Stage stage = CloseLoginStage(actionEvent);

                HelloApplication.primaryStage.close();

                loginStage = stage;
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Galeri Art");
                stage.setScene(scene);
                stage.show();

            } else {
                txtWarning.setVisible(true);
                txtWarning.setText("Username atau Password salah !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openRegisterPopup(ActionEvent actionEvent) {
        CloseLoginStage(actionEvent);

        Popup REGpopup = new Popup();
        REGpopup.setAutoHide(true);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage POPstage = new Stage();
            POPstage.setScene(scene);
            POPstage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage CloseLoginStage (ActionEvent actionEvent){
        Node source = (Node) actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
        return stage;
    }
}
