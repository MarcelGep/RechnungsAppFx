package com.gepraegs.rechnungsAppFx.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class InfoDialogController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(InfoDialogController.class.getName());
    private Stage dialogStage = new Stage();
    private boolean dialogConfirmed = false;

    @FXML
    private Label lbContent;

    @FXML
    private Button btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setButton(String button) {
        if (button != null && !button.isEmpty()) {
            btnCancel.setText(button);
        }
    }

    public boolean isDialogConfirmed() {
        return dialogConfirmed;
    }

    public void setContent(String content) {
        lbContent.setText(content);
        lbContent.setWrapText(true);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleSave() {
        dialogConfirmed = true;
        dialogStage.close();
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
}
