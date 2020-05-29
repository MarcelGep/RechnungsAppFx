package com.gepraegs.rechnungsAppFx.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AppViewController implements Initializable {

	@FXML
	private TabPane tabPaneContent;

	@FXML
	private MenuItem menuItemExit;

	@Override
	public void initialize( URL location, ResourceBundle resources ) {
		// Load initialize tab
		tabPaneContent.getSelectionModel().selectFirst();

		// Setup menu items
		menuItemExit.setOnAction( e -> exitProgram() );

	}

	public static void exitProgram() {
		ButtonType yes = new ButtonType( "Ja" );
		ButtonType no = new ButtonType( "Nein" );

		Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
		alert.setHeaderText( "Programm beenden?" );
		alert.setContentText( "Möchten Sie RechnungsAppFx wirklich beenden?" );
		alert.getDialogPane().getStylesheets()
			.add( AppViewController.class.getResource( "/stylesheet/buttonStyles.css" ).toExternalForm() );
		alert.getDialogPane().getStylesheets()
			.add( AppViewController.class.getResource( "/stylesheet/jfoenixStyles.css" ).toExternalForm() );
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll( yes, no );
		alert.getDialogPane().setPrefHeight( 180 );
		alert.setResizable( true );
		alert.onShownProperty().addListener( e -> {
			Platform.runLater( () -> alert.setResizable( false ) );
		} );

		Button yesBtn = ( Button ) alert.getDialogPane().lookupButton( alert.getButtonTypes().get( 0 ) );
		yesBtn.setId( "save_button" );
		Button noBtn = ( Button ) alert.getDialogPane().lookupButton( alert.getButtonTypes().get( 1 ) );
		noBtn.setId( "cancel_button" );

		Image image = new Image( AppViewController.class.getResource( "/icons/log-out.png" ).toString() );
		ImageView imageView = new ImageView( image );
		imageView.setFitHeight( 50 );
		imageView.setFitWidth( 50 );
		alert.setGraphic( imageView );

//		Image weddingPlanerIcon = new Image(
//			AppViewController.class.getResource( "/icons/weddingPlanerIcon.png" ).toString() );
//		Stage stage = ( Stage ) alert.getDialogPane().getScene().getWindow();
//		stage.getIcons().add( weddingPlanerIcon );

		Optional<ButtonType> option = alert.showAndWait();

		if ( option.isPresent() && option.get() == yes ) {
			Platform.exit();
		}
	}
}
