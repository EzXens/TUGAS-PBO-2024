package org.tugaspbogaleri.pa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.tugaspbogaleri.pa.Important.*;
import org.tugaspbogaleri.pa.Important.SesiUser;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private HBox HboxContainer;

    @FXML private Label txtNamaUSer;

    @FXML private HBox HboxResepBtn;

    @FXML private HBox HboxBookmarkBtn;

    @FXML private HBox HboxRankBtn;

    @FXML private HBox HboxDashboardBtn;

    @FXML private GridPane resepContainer;

    @FXML private VBox VboxLobbyContainer;

    @FXML private VBox MainContainer;

    @FXML private Button btnOpenLogin;

    @FXML private Label txtHomeBtn;

    static Stage PopLoginStage;

    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        if (SesiUser.LoginSession == 1) {
            txtNamaUSer.setText(SesiUser.username);
            btnOpenLogin.setText("Logout");
            if (SesiUser.izin.equals("admin")) {
                HboxDashboardBtn.setVisible(true);
                HboxDashboardBtn.setDisable(false);
            }
        } else {
            HboxDashboardBtn.setVisible(false);
            HboxDashboardBtn.setDisable(true);
            btnOpenLogin.setText("Login");
            txtNamaUSer.setText("Nama");
        }

        Popup OpenLoginPopup = new Popup();
        Stage popLoginStage = new Stage();
        OpenLoginPopup.setAutoHide(true);
        btnOpenLogin.setOnAction(event -> openLoginPopup(OpenLoginPopup, popLoginStage));

        HboxDashboardBtn.setOnMouseClicked(mouseEvent -> openAdminDashboard());

        txtHomeBtn.setOnMouseClicked(mouseEvent -> openHome());

        connection = DB.connectDB();
        loadGalleryData();
    }

    private void openHome() {
        returnColor();
        MainContainer.getChildren().clear();
        MainContainer.getChildren().addFirst(VboxLobbyContainer);
    }

    private void openLoginPopup(Popup popup, Stage stage) {
        if (btnOpenLogin.getText().equals("Logout")) {
            SesiUser.LoginSession = 0;

            LoginController.loginStage.close();
            HelloApplication.primaryStage.show();
        } else {
            PopLoginStage = stage;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            try {
                if (!popup.isShowing()) {
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openAdminDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("dashboardAdmin.fxml"));

            returnColor();
            HboxDashboardBtn.setStyle("-fx-background-color: #277dba;");
            MainContainer.getChildren().clear();
            MainContainer.getChildren().addFirst(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void returnColor() {
        HboxDashboardBtn.setStyle("-fx-background-color: white;");
        HboxResepBtn.setStyle("-fx-background-color: white;");
        HboxBookmarkBtn.setStyle("-fx-background-color: white;");
    }

    private void loadGalleryData() {
        try {
            String sql = "SELECT g.id_gambar, g.gambar, g.nama_gambar, g.deskripsi_gambar, g.tanggal_gambar, k.nama_kategori, t.nama_tags FROM gambar g JOIN kategori k ON g.id_kategori = k.id_kategori JOIN tags t ON g.id_tags = t.id_tags";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            VBox currentRow = new VBox(10);  // VBox to hold rows of HBox

            HBox currentHBox = new HBox(30);  // HBox to hold images per row
            int imageCount = 0;

            while (resultSet.next()) {
                String gambarPath = resultSet.getString("gambar");
                String namaGambar = resultSet.getString("nama_gambar");
                String deskripsiGambar = resultSet.getString("deskripsi_gambar");
                String tanggalGambar = resultSet.getString("tanggal_gambar");
                String kategori = resultSet.getString("nama_kategori");
                String tags = resultSet.getString("nama_tags");

                VBox imageBox = new VBox(10);
                ImageView imageView = new ImageView(new Image("file:" + gambarPath));
                imageView.setFitHeight(200);  // Set image height
                imageView.setFitWidth(200);   // Set image width

                VBox detailsBox = new VBox(5);
                Label nameLabel = new Label("Nama: " + namaGambar);
                Label dateLabel = new Label("Tanggal: " + tanggalGambar);
                Label categoryLabel = new Label("Kategori: " + kategori);
                Label tagsLabel = new Label("Tags: " + tags);

                detailsBox.getChildren().addAll(nameLabel, dateLabel, categoryLabel, tagsLabel);

                TextArea descriptionArea = new TextArea(deskripsiGambar);
                descriptionArea.setPrefRowCount(5);
                descriptionArea.setWrapText(true);
                descriptionArea.setEditable(false);
                descriptionArea.setMaxHeight(100);
                descriptionArea.setMaxWidth(150);

                imageBox.getChildren().addAll(imageView, detailsBox, descriptionArea);
                imageBox.setStyle("-fx-padding: 20;"); // Add padding around each imageBox

                currentHBox.getChildren().add(imageBox);
                imageCount++;

                if (imageCount == 3) {
                    currentRow.getChildren().add(currentHBox);
                    currentHBox = new HBox(30);  // create new HBox for next row
                    imageCount = 0;
                }
            }

            // Add the last HBox if it has any images
            if (!currentHBox.getChildren().isEmpty()) {
                currentRow.getChildren().add(currentHBox);
            }

            HboxContainer.getChildren().add(currentRow);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
