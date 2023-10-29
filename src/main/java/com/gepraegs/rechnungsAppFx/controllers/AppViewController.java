package com.gepraegs.rechnungsAppFx.controllers;

import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showConfirmDialog;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class AppViewController implements Initializable {

	@FXML
	private ProductsController productsController;

	@FXML
	private TabPane tabPaneContent;

	@FXML
	private MenuItem menuItemExit;

	@FXML
	private VBox showDialogLayer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Load initialize tab
		// tabPaneContent.getSelectionModel().selectFirst();

		tabPaneContent.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
			if (newValue.getText().equals("Produkte")) {
				productsController.loadProductsData();
			}
		});

		// Setup menu items
		menuItemExit.setOnAction(e -> exitProgram());
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
