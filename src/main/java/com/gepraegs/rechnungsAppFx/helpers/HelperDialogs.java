package com.gepraegs.rechnungsAppFx.helpers;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.ini4j.Ini;

import com.gepraegs.rechnungsAppFx.controllers.AppViewController;
import com.gepraegs.rechnungsAppFx.controllers.StartViewController;

import static com.gepraegs.rechnungsAppFx.Constants.*;
import static com.gepraegs.rechnungsAppFx.helpers.HelperResourcesLoader.*;

public class HelperDialogs {


	public static void showStartDialog( Stage stage ) throws IOException {
		FXMLLoader fxmlLoader = loadFXML( STARTVIEW );

		Parent root = fxmlLoader.load();

//		Image weddingPlanerIcon = new Image(
//			HelperDialogs.class.getResource( "/icons/weddingPlanerIcon.png" ).toString() );

		Scene mainScene = new Scene( root );

		stage.setResizable( false );
		stage.setMinWidth( 550 );
		stage.setMinHeight( 590 );
		stage.setTitle( "RechnungsAppFx Start" );
		stage.setScene( mainScene );
//		stage.getIcons().add( weddingPlanerIcon );

		Ini ini = new Ini( HelperDialogs.class.getClassLoader().getResourceAsStream( "config.ini" ) );
		String lastPath = ini.get( "database-path", "last_path" );

		StartViewController controller = fxmlLoader.getController();
		controller.setDbPathSetting(); // TODO only for development
		controller.setLastDbPath( lastPath );
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
		mainWindow.setTitle( "WeddingPlaner" );
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

//	public static void showGuestDialog( ObservableList<Guest> guestData, Guest guest ) throws IOException {
//		FXMLLoader fxmlLoader = loadFXML( GUESTDIALOGVIEW );
//
//		Parent page = fxmlLoader.load();
//
//		Stage dialogStage = new Stage();
//		dialogStage.setTitle( "Gast" );
//		dialogStage.initModality( Modality.APPLICATION_MODAL );
//		dialogStage.setResizable( false );
//		dialogStage.setMinWidth( 560 );
//		dialogStage.setMinHeight( 420 );
//
//		Scene scene = new Scene( page );
//		dialogStage.setScene( scene );
//
//		Image weddingPlanerIcon = new Image(
//			HelperDialogs.class.getResource( "/icons/weddingPlanerIcon.png" ).toString() );
//		dialogStage.getIcons().add( weddingPlanerIcon );
//
//		GuestDialogController dialogController = fxmlLoader.getController();
//		dialogController.setDialogStage( dialogStage );
//		dialogController.setGuestData( guestData );
//
//		if ( guest != null ) {
//			dialogController.setGuest( guest );
//		}
//
//		dialogStage.showAndWait();
//	}

//	public static boolean showDeleteGuestDialog( String text ) {
//		ButtonType yes = new ButtonType( "Ja" );
//		ButtonType no = new ButtonType( "Nein" );
//
//		Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
//		alert.setTitle( "Löschen" );
//		alert.setHeaderText( text );
//		alert.setContentText( "Möchten Sie die Auswahl wirklich aus der Gästeliste löschen?" );
//		alert.getDialogPane().getStylesheets()
//			.add( HelperDialogs.class.getResource( "/stylesheet/tabPaneStyles.css" ).toExternalForm() );
//		alert.getDialogPane().getStyleClass().add( "alertDialog" );
//		alert.getDialogPane().setPrefHeight( 180 );
//		alert.setResizable( true );
//		alert.getButtonTypes().clear();
//		alert.getButtonTypes().addAll( yes, no );
//		alert.onShownProperty().addListener( e -> Platform.runLater( () -> alert.setResizable( false ) ) );
//
//		Button yesBtn = ( Button ) alert.getDialogPane().lookupButton( alert.getButtonTypes().get( 0 ) );
//		yesBtn.setId( "save_button" );
//		Button noBtn = ( Button ) alert.getDialogPane().lookupButton( alert.getButtonTypes().get( 1 ) );
//		noBtn.setId( "cancel_button" );
//
//		Image image = new Image( HelperDialogs.class.getResource( "/icons/customer_delete.png" ).toString() );
//		ImageView imageView = new ImageView( image );
//		imageView.setFitHeight( 50 );
//		imageView.setFitWidth( 50 );
//		alert.setGraphic( imageView );
//
//		Image weddingPlanerIcon = new Image(
//			HelperDialogs.class.getResource( "/icons/weddingPlanerIcon.png" ).toString() );
//		Stage stage = ( Stage ) alert.getDialogPane().getScene().getWindow();
//		stage.getIcons().add( weddingPlanerIcon );
//
//		Optional<ButtonType> option = alert.showAndWait();
//
//		return option.isPresent() && option.get() == yes;
//	}

}
