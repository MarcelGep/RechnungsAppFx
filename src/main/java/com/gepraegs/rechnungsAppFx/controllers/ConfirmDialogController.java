package com.gepraegs.rechnungsAppFx.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConfirmDialogController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ConfirmDialogController.class.getName());
    private Stage dialogStage = new Stage();
    private boolean dialogConfirmed = false;

    @FXML
    private Label lbContent;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setButtons(List<String> buttons) {
        if (buttons != null && !buttons.isEmpty()) {
            btnSave.setText(buttons.get(0));
            btnCancel.setText(buttons.get(1));
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
