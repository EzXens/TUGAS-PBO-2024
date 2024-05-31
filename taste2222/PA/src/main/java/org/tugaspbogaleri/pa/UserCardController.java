package org.tugaspbogaleri.pa;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

public class UserCardController {
    @FXML private Label txtUidDB;

    @FXML private Label txtUsernameDB;

    @FXML private Label txtPasswordDB;

    @FXML private Label txtEmailDB;

    @FXML private Label txtIzinDB;

    @FXML private Button btnEdit;

    public void setDataUserCard(Integer uid, String username, String password, String email, String izin) {
        txtUidDB.setText(uid.toString());
        txtUsernameDB.setText(username);
        txtPasswordDB.setText(password);
        txtEmailDB.setText(email);
        txtIzinDB.setText(izin);

        btnEdit.setOnAction(actionEvent -> openEditPopup(uid));
    }

    public void openEditPopup(int uid){
        System.out.println( uid);
    }
}
