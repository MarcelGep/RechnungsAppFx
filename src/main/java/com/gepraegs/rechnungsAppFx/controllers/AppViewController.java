package com.gepraegs.rechnungsAppFx.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showConfirmDialog;

public class AppViewController implements Initializable {

	@FXML private TabPane tabPaneContent;

	@FXML private MenuItem menuItemExit;

	@FXML private VBox showDialogLayer;

	@Override
	public void initialize( URL location, ResourceBundle resources ) {
		// Load initialize tab
		tabPaneContent.getSelectionModel().selectFirst();

		// Setup menu items
		menuItemExit.setOnAction( e -> {
			exitProgram();
		});
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
