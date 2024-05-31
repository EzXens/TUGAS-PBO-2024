package org.tugaspbogaleri.pa;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardAdminController implements Initializable {
    @FXML private Tab tabAkun;
    @FXML private VBox VboxUserContainerAkun;
    @FXML private Tab tabResep;
    @FXML private Button btntambah;
    @FXML private TextField txtnama;
    @FXML private ComboBox<String> comboketegori;
    @FXML private ComboBox<String> combotags;
    @FXML private TextArea txtdesk;
    @FXML private DatePicker datpiker;
    @FXML private ImageView picturebox;
    @FXML private Button btnunggah;
    @FXML private Tab tabLihatGaleri;
    @FXML private VBox vboxGaleriContainer;

    private File selectedFile;
    private Connection connection;

    @FXML
    void ClickUnggah(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            picturebox.setImage(image);
        }
    }

    @FXML
    void ClickTambahData(ActionEvent event) {
        String namaGambar = txtnama.getText();
        String deskripsiGambar = txtdesk.getText();
        LocalDate tanggalGambar = datpiker.getValue();
        String kategori = comboketegori.getValue();
        String tags = combotags.getValue();

        // Validate input
        if (namaGambar.isEmpty() || deskripsiGambar.isEmpty() || tanggalGambar == null || kategori == null || tags == null || selectedFile == null) {
            // Show error message if any input is empty
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields and select an image.");
            alert.show();
            return;
        }

        try {
            String sql = "INSERT INTO gambar (gambar, nama_gambar, deskripsi_gambar, tanggal_gambar, id_tags, id_kategori) VALUES (?, ?, ?, ?, (SELECT id_tags FROM tags WHERE nama_tags = ?), (SELECT id_kategori FROM kategori WHERE nama_kategori = ?))";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, selectedFile.getAbsolutePath());
            preparedStatement.setString(2, namaGambar);
            preparedStatement.setString(3, deskripsiGambar);
            preparedStatement.setDate(4, Date.valueOf(tanggalGambar));
            preparedStatement.setString(5, tags);
            preparedStatement.setString(6, kategori);
            preparedStatement.executeUpdate();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data added successfully.");
            alert.show();

            // Clear input fields
            txtnama.clear();
            txtdesk.clear();
            datpiker.setValue(null);
            comboketegori.setValue(null);
            combotags.setValue(null);
            picturebox.setImage(null);
            selectedFile = null;
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to add data: " + e.getMessage());
            alert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DB.connectDB();
        populateUserCards();
        populateComboBoxes();
    }

    private void populateUserCards() {
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM akun");
            while (resultSet.next()) {
                int uid = resultSet.getInt("uid");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String izin = resultSet.getString("izin");

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("userCard.fxml"));
                VboxUserContainerAkun.getChildren().add(fxmlLoader.load());

                UserCardController userCardController = fxmlLoader.getController();
                userCardController.setDataUserCard(uid, username, password, email, izin);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateComboBoxes() {
        try {
            ResultSet kategoriSet = connection.createStatement().executeQuery("SELECT nama_kategori FROM kategori");
            while (kategoriSet.next()) {
                comboketegori.getItems().add(kategoriSet.getString("nama_kategori"));
            }

            ResultSet tagsSet = connection.createStatement().executeQuery("SELECT nama_tags FROM tags");
            while (tagsSet.next()) {
                combotags.getItems().add(tagsSet.getString("nama_tags"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTabLihatGaleriSelected(Event event) {
        if (tabLihatGaleri.isSelected()) {
            vboxGaleriContainer.getChildren().clear();
            loadGalleryData();
        }
    }

    private void loadGalleryData() {
        try {
            String sql = "SELECT g.id_gambar, g.gambar, g.nama_gambar, g.deskripsi_gambar, g.tanggal_gambar, k.nama_kategori, t.nama_tags FROM gambar g JOIN kategori k ON g.id_kategori = k.id_kategori JOIN tags t ON g.id_tags = t.id_tags";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            int count = 0;
            HBox currentRow = new HBox(30);  // HBox to hold up to 3 images per row

            while (resultSet.next()) {
                int idGambar = resultSet.getInt("id_gambar");
                String gambarPath = resultSet.getString("gambar");
                String namaGambar = resultSet.getString("nama_gambar");
                String deskripsiGambar = resultSet.getString("deskripsi_gambar");
                Date tanggalGambar = resultSet.getDate("tanggal_gambar");
                String kategori = resultSet.getString("nama_kategori");
                String tags = resultSet.getString("nama_tags");

                VBox imageBox = new VBox(10);
                ImageView imageView = new ImageView(new Image("file:" + gambarPath));
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);

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

                // Add Edit and Delete buttons
                Button btnEdit = new Button("Edit");
                Button btnDelete = new Button("Delete");

                btnEdit.setOnAction(e -> handleEditAction(idGambar, gambarPath, namaGambar, deskripsiGambar, tanggalGambar, kategori, tags));
                btnDelete.setOnAction(e -> handleDeleteAction(idGambar));

                HBox buttonBox = new HBox(10, btnEdit, btnDelete);

                imageBox.getChildren().addAll(imageView, detailsBox, descriptionArea, buttonBox);
                imageBox.setStyle("-fx-padding: 20;"); // Add padding around each imageBox

                currentRow.getChildren().add(imageBox);

                count++;

                if (count == 3) {
                    vboxGaleriContainer.getChildren().add(currentRow);
                    currentRow = new HBox(30);  // create new HBox for next row
                    count = 0;
                }
            }

            // Add the last row if it has any images
            if (count > 0) {
                vboxGaleriContainer.getChildren().add(currentRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteAction(int idGambar) {
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this data?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            try {
                String sql = "DELETE FROM gambar WHERE id_gambar = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, idGambar);
                preparedStatement.executeUpdate();

                // Refresh the gallery after deletion
                handleTabLihatGaleriSelected(null);
            } catch (SQLException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to delete data: " + e.getMessage());
                errorAlert.show();
            }
        }
    }

    private void handleEditAction(int idGambar, String gambarPath, String namaGambar, String deskripsiGambar, Date tanggalGambar, String kategori, String tags) {
        // Load edit.fxml and pass the data to the EditController
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            EditController editController = loader.getController();
            editController.initData(idGambar, gambarPath, namaGambar, deskripsiGambar, tanggalGambar.toLocalDate(), kategori, tags);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
