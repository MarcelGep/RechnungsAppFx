package com.gepraegs.rechnungsAppFx.controllers;

import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showConfirmDialog;
import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showDbCreateErrorDialog;
import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showMainWindow;
import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showMissingDbDialog;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.ini4j.Wini;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StartViewController {

    private Stage dialogStage;

    private String DB_PATH;
    private String DB_LAST_PATH;
    private String DB_LOAD_PATH;

    private static final String DB_MAIN_PATH = "/database/database.db";
    private static final String DB_DEVELOPMENT_PATH = "src/main/resources/database/database.db";

    private static final Logger LOGGER = Logger.getLogger(StartViewController.class.getName());
    private DbController dbController = DbController.getInstance();

    @FXML
    private RadioButton rbDevelopment;
    @FXML
    private RadioButton rbJar;
    @FXML
    private RadioButton rbLastUsedDb;
    @FXML
    private RadioButton rbCreateDb;
    @FXML
    private RadioButton rbLoadDb;
    @FXML
    private RadioButton rbDevelopmentSettings;

    @FXML
    private Button btnSelectDb;

    @FXML
    private Label lbDbPath;

    @FXML
    private VBox showDialogLayer;

    @FXML
    private void onStartButtonClicked() throws Exception {
        if (rbCreateDb.isSelected()) {
            if (!dbController.createNewDatabase(DB_PATH)) {
                showDbCreateErrorDialog();
                return;
            }
        } else if (rbLastUsedDb.isSelected()) {
            DB_PATH = DB_LAST_PATH;
        } else if (rbLoadDb.isSelected()) {
            DB_PATH = DB_LOAD_PATH;
        }

        if (DB_PATH != null && !DB_PATH.isEmpty()) {
            // Initialize Database
            dbController.initDBConnection(DB_PATH);

            // Check database connection
            if (dbController.isDbConnected()) {
                dialogStage.close();

                try {
                    LOGGER.info("Save selected DB-Path to config...");
                    File configIni = new File("config.ini");
                    Wini ini = new Wini(configIni);
                    ini.put("database-path", "last_path", DB_PATH);
                    ini.store();
                    LOGGER.info("...DB-Path saved to config: " + configIni.getAbsolutePath());
                } catch (Exception e) {
                    LOGGER.warning(e.toString());
                }

                showMainWindow();
            } else {
                showMissingDbDialog();
            }
        } else {
            showMissingDbDialog();
            return;
        }
    }

    @FXML
    private void onExitButtonClicked() {
        exitProgram();
    }

    @FXML
    private void onRbDevelopmentClicked() {
        setDbPath(DB_DEVELOPMENT_PATH);
        btnSelectDb.setDisable(true);
    }

    @FXML
    private void onRbJarClicked() {
        setDbPath(DB_MAIN_PATH);
        btnSelectDb.setDisable(true);
    }

    @FXML
    private void onRbLastDbClicked() {
        setLastDbPath(DB_LAST_PATH);
        btnSelectDb.setDisable(true);
        rbDevelopment.setDisable(true);
        rbJar.setDisable(true);
    }

    @FXML
    private void onRbNewDbClicked() {
        btnSelectDb.setDisable(true);
        rbDevelopment.setDisable(false);
        rbJar.setDisable(false);

        setDevPath();
    }

    @FXML
    private void onRbLoadDbClicked() {
        btnSelectDb.setDisable(false);
        rbDevelopment.setDisable(true);
        rbJar.setDisable(true);
        clearDbPath();
    }

    @FXML
    private void onRbDevelopmentSettingsClicked() {
        btnSelectDb.setDisable(true);
        rbDevelopment.setDisable(false);
        rbJar.setDisable(false);
        setDevPath();
    }

    @FXML
    private void onSelectDbButtonClicked() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Wähle Datenbank aus...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SQLite Datenbank", "*.db", "*.sqlite", "*.sqlite3", "*.db3"),
                new FileChooser.ExtensionFilter("Alle Dateien", "*.*"));

        File file = fileChooser.showOpenDialog(dialogStage);

        if (file != null) {
            setLoadDbPath(file.getPath());
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // TODO only for development
    public void setDbPathSetting() {
        setDbPath(DB_DEVELOPMENT_PATH);
        rbDevelopmentSettings.setSelected(true);
    }

    public void setLastDbPath(String path) {
        if (path != null) {
            if (!path.isEmpty()) {
                DB_LAST_PATH = path;
                lbDbPath.setText(DB_LAST_PATH);
                setDbPath(DB_LAST_PATH);
                rbLastUsedDb.setSelected(true);
            } else {
                rbLastUsedDb.setDisable(true);
                rbCreateDb.setSelected(true);
            }
        }
    }

    private void setLoadDbPath(String path) {
        DB_LOAD_PATH = path;
        lbDbPath.setText(DB_LOAD_PATH);
    }

    private void setDbPath(String path) {
        DB_PATH = path;
        lbDbPath.setText(DB_PATH);
    }

    private void setDevPath() {
        if (rbDevelopment.isSelected()) {
            setDbPath(DB_DEVELOPMENT_PATH);
        } else if (rbJar.isSelected()) {
            setDbPath(DB_MAIN_PATH);
        }
    }

    private void clearDbPath() {
        DB_PATH = "";
        lbDbPath.setText(DB_PATH);
    }

    public void exitProgram() {
        try {
            showDialogLayer.setVisible(true);
            String content = "Möchten Sie RechnungsAppFx wirklich beenden?";
            if (showConfirmDialog(content, Arrays.asList("Ja", "Nein"))) {
                Platform.exit();
            }
            showDialogLayer.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
