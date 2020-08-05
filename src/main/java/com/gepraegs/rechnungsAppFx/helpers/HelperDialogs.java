package com.gepraegs.rechnungsAppFx.helpers;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gepraegs.rechnungsAppFx.Customer;
import com.gepraegs.rechnungsAppFx.Product;
import com.gepraegs.rechnungsAppFx.controllers.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import org.ini4j.Ini;

import static com.gepraegs.rechnungsAppFx.Constants.*;
import static com.gepraegs.rechnungsAppFx.helpers.HelperResourcesLoader.*;

public class HelperDialogs {

	public static void showStartDialog( Stage stage ) throws IOException {
		FXMLLoader fxmlLoader = loadFXML( STARTVIEW );

		Parent root = fxmlLoader.load();

//		Image icon = new Image(
//			HelperDialogs.class.getResource( "/icons/cash.png" ).toString() );

		Scene mainScene = new Scene( root );

		stage.setResizable( false );
		stage.setMinWidth( 550 );
		stage.setMinHeight( 590 );
		stage.setTitle( "RechnungsAppFx Start" );
		stage.setScene( mainScene );
//		stage.getIcons().add( icon );

		Ini ini = new Ini( HelperDialogs.class.getClassLoader().getResourceAsStream( "config.ini" ) );
		String lastPath = ini.get( "database-path", "last_path" );

		StartViewController controller = fxmlLoader.getController();
		controller.setDbPathSetting(); // TODO only for development
//		controller.setLastDbPath( lastPath );
		controller.setDialogStage( stage );

		stage.setOnCloseRequest( e -> {
			e.consume();
			AppViewController.exitProgram();
		} );

		stage.show();
	}

	public static void showMainWindow() throws IOException {
		Parent root = loadFXML( MAINVIEW ).load();

//		Image weddingPlanerIcon = new Image(
//			HelperDialogs.class.getResource( "/icons/weddingPlanerIcon.png" ).toString() );

		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		Scene mainScene = new Scene( root, width, height );
		mainScene.getStylesheets()
			.add( HelperDialogs.class.getResource( "/stylesheet/tabPaneStyles.css" ).toExternalForm() );

		Stage mainWindow = new Stage();
		mainWindow.setScene( mainScene );
		mainWindow.setTitle( "RechnungsAppFx" );
		mainWindow.setMaximized( true );
//		mainWindow.getIcons().add( weddingPlanerIcon );
		mainWindow.setOnCloseRequest( e -> {
			e.consume();
			AppViewController.exitProgram();
		} );

		mainWindow.show();
	}
	
	public static void showMissingDbDialog() {
		Image image = new Image( AppViewController.class.getResource( "/icons/database_error.png" ).toString() );
		ImageView imageView = new ImageView( image );
		imageView.setFitHeight( 40 );
		imageView.setFitWidth( 40 );

		Alert alert = new Alert( Alert.AlertType.ERROR, "Es wurde keine Datenbank gefunden!", ButtonType.OK );
		alert.setHeaderText( "Datenbank Fehler" );
		alert.initModality( Modality.APPLICATION_MODAL );
		alert.getDialogPane().setMinHeight( 180 );
		alert.getDialogPane().getStylesheets()
			.add( AppViewController.class.getResource( "/stylesheet/tabPaneStyles.css" ).toExternalForm() );
		alert.getDialogPane().getStyleClass().add( "alertDialog" );
		alert.setGraphic( imageView );
		alert.setResizable( true );
		alert.onShownProperty().addListener( e -> Platform.runLater( () -> alert.setResizable( false ) ) );
		alert.showAndWait();
	}

	public static void showDbCreateErrorDialog() {
		Image image = new Image( AppViewController.class.getResource( "/icons/database_error.png" ).toString() );
		ImageView imageView = new ImageView( image );
		imageView.setFitHeight( 40 );
		imageView.setFitWidth( 40 );

		Alert alert = new Alert( Alert.AlertType.ERROR, "Fehler beim erstellen der Datenbank!", ButtonType.OK );
		alert.setHeaderText( "Datenbank Fehler" );
		alert.initModality( Modality.APPLICATION_MODAL );
		alert.getDialogPane().setMinHeight( 180 );
		alert.getDialogPane().getStylesheets()
			.add( AppViewController.class.getResource( "/stylesheet/tabPaneStyles.css" ).toExternalForm() );
		alert.getDialogPane().getStyleClass().add( "alertDialog" );
		alert.setGraphic( imageView );
		alert.setResizable( true );
		alert.onShownProperty().addListener( e -> Platform.runLater( () -> alert.setResizable( false ) ) );
		alert.showAndWait();
	}

	private static Parent initParent(FXMLLoader fxmlLoader, Stage stage) throws IOException {
		Parent parent = fxmlLoader.load();
		parent.setOnMousePressed(pressEvent -> {
			parent.setOnMouseDragged(dragEvent -> {
				stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
				stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
			});
		});
		return parent;
	}

	public static Customer showCustomerDialog(ObservableList<Customer> customerData, Customer customer) throws IOException
	{
		FXMLLoader fxmlLoader = loadFXML(CUSTOMERDIALOGVIEW);

		Stage dialogStage = new Stage();

		Parent parent = initParent(fxmlLoader, dialogStage);

		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.setResizable(false);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		dialogStage.setScene(new Scene(parent));

		CustomerDialogController dialogController = fxmlLoader.getController();
		dialogController.setDialogStage(dialogStage);
		dialogController.setCustomerData(customerData);

		if (customer != null) {
			dialogController.setDialogTitle("KUNDE BEARBEITEN");
			dialogController.setSelectedCustomer(customer);
		}
		else {
			dialogController.setDialogTitle("NEUER KUNDE");
		}

		dialogStage.showAndWait();

		return dialogController.getSavedCustomer();
	}

	public static Product showProductDialog(ObservableList<Product> productData, Product product) throws IOException
	{
		FXMLLoader fxmlLoader = loadFXML(PRODUCTDIALOGVIEW);

		Stage dialogStage = new Stage();

		Parent parent = initParent(fxmlLoader, dialogStage);

		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.setResizable(false);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		dialogStage.setScene(new Scene(parent));

		ProductDialogController dialogController = fxmlLoader.getController();
		dialogController.setDialogStage(dialogStage);
		dialogController.setProductData(productData);

		if (product != null) {
			dialogController.setDialogTitle("PRODUKT BEARBEITEN");
			dialogController.setSelectedProduct(product);
		}
		else {
			dialogController.setDialogTitle("NEUES PRODUKT");
		}

		dialogStage.showAndWait();

		return dialogController.getSavedProduct();
	}

	public static boolean showConfirmDialog(String content, List<String> buttons) throws IOException {
		FXMLLoader fxmlLoader = loadFXML(CONFIRMDIALOGVIEW);

		Stage dialogStage = new Stage();

		Parent parent = initParent(fxmlLoader, dialogStage);

		dialogStage.setTitle("Neuer Kunde");
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.setResizable(false);
		dialogStage.initStyle(StageStyle.UNDECORATED);
		dialogStage.setScene(new Scene(parent));

		ConfirmDialogController dialogController = fxmlLoader.getController();
		dialogController.setDialogStage(dialogStage);
		dialogController.setContent(content);
		dialogController.setButtons(buttons);

		dialogStage.showAndWait();

		return dialogController.isDialogConfirmed();
	}
}
