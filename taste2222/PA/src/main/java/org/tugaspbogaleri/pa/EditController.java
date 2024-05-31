package org.tugaspbogaleri.pa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import java.io.File;
import java.sql.*;
import java.time.LocalDate;

public class EditController {

    @FXML private DatePicker datpiker;
    @FXML private ComboBox<String> combotags;
    @FXML private ComboBox<String> comboketegori;
    @FXML private TextField txtnama;
    @FXML private Button btntambah;
    @FXML private TextArea txtdesk;
    @FXML private ImageView picturebox;
    @FXML private Button btnunggah;

    private int idGambar;
    private String gambarPath;
    private Connection connection;
    private File selectedFile;

    public void initialize() {
        connection = DB.connectDB();
        populateComboBoxes();
    }

    @FXML
    void ClickTambahData(ActionEvent event) {
        String namaGambar = txtnama.getText();
        String deskripsiGambar = txtdesk.getText();
        LocalDate tanggalGambar = datpiker.getValue();
        String kategori = comboketegori.getValue();
        String tags = combotags.getValue();

        // Validate input
        if (namaGambar.isEmpty() || deskripsiGambar.isEmpty() || tanggalGambar == null || kategori == null || tags == null) {
            // Show error message if any input is empty
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
            alert.show();
            return;
        }

        try {
            String sql = "UPDATE gambar SET gambar = ?, nama_gambar = ?, deskripsi_gambar = ?, tanggal_gambar = ?, id_tags = (SELECT id_tags FROM tags WHERE nama_tags = ?), id_kategori = (SELECT id_kategori FROM kategori WHERE nama_kategori = ?) WHERE id_gambar = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, selectedFile != null ? selectedFile.getAbsolutePath() : gambarPath);
            preparedStatement.setString(2, namaGambar);
            preparedStatement.setString(3, deskripsiGambar);
            preparedStatement.setDate(4, Date.valueOf(tanggalGambar));
            preparedStatement.setString(5, tags);
            preparedStatement.setString(6, kategori);
            preparedStatement.setInt(7, idGambar);
            preparedStatement.executeUpdate();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data updated successfully.");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update data: " + e.getMessage());
            alert.show();
        }
    }

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

    public void initData(int idGambar, String gambarPath, String namaGambar, String deskripsiGambar, LocalDate tanggalGambar, String kategori, String tags) {
        this.idGambar = idGambar;
        this.gambarPath = gambarPath;
        txtnama.setText(namaGambar);
        txtdesk.setText(deskripsiGambar);
        datpiker.setValue(tanggalGambar);
        comboketegori.setValue(kategori);
        combotags.setValue(tags);

        Image image = new Image("file:" + gambarPath);
        picturebox.setImage(image);
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
}
