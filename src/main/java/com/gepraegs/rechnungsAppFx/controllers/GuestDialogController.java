package com.gepraegs.rechnungsAppFx.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static com.gepraegs.rechnungsAppFx.helpers.ValidationHelper.*;

public class GuestDialogController implements Initializable {

    @FXML public JFXTabPane tpGuestData;

    @FXML public JFXTextField tfFirstName;
    @FXML public JFXTextField tfLastName;
    @FXML public JFXTextField tfAge;
    @FXML public JFXTextField tfPhone;
    @FXML public JFXTextField tfHandy;
    @FXML public JFXTextField tfEmail;
    @FXML public JFXTextField tfStreet;
    @FXML public JFXTextField tfPlz;
    @FXML public JFXTextField tfOrt;

    @FXML public JFXTextArea taComments;

//    @FXML public JFXComboBox<GuestStatus> cbState;

    private static final Logger LOGGER = Logger.getLogger(GuestDialogController.class.getName());
    private DbController dbController = DbController.getInstance();

    private Stage dialogStage;

//    private ObservableList<Guest> guestData = FXCollections.observableArrayList();
//
//    private Guest selectedGuest;

    private final int PERSON = 0;
    private final int ADDRESS = 1;
    private final int CONTACT = 2;
	@Override
	public void initialize( URL location, ResourceBundle resources ) {
		// TODO Auto-generated method stub
		
	}

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle)
//    {
//        cbState.getItems().addAll(GuestStatus.values());
//
//        addTextLimiter(tfFirstName, 29);
//        addTextLimiter(tfLastName, 29);
//        addTextLimiter(tfAge, 3);
//        addTextLimiter(tfStreet, 30);
//        addTextLimiter(tfPlz, 5);
//        addTextLimiter(tfOrt, 22);
//        addTextLimiter(tfPhone, 30);
//        addTextLimiter(tfHandy, 30);
//        addTextLimiter(tfEmail, 30);
//
//        setupValidation(tfFirstName);
//        setupValidation(tfLastName);
//        setupValidation(tfAge);
//        setupValidation(tfPlz);
//        setupValidation(tfPhone);
//        setupValidation(tfHandy);
//        setupValidation(tfEmail);
//        statusValidator(cbState);
//    }

//    @FXML
//    public void handleSave()
//    {
//        if (!validateInputs())
//        {
//            return;
//        }
//
//        // TODO type as variable
//        int newId = selectedGuest != null ? selectedGuest.getId() : dbController.readNextId("Guests");
//        boolean inviteStatus = selectedGuest != null ? selectedGuest.isInvite() : false;
//
//        Guest newGuest = new Guest(newId,
//                                    tfLastName.getText(),
//                                    tfFirstName.getText(),
//                                    Integer.parseInt(tfAge.getText()),
//                                    Objects.requireNonNull(GuestStatus.getByName(cbState.getSelectionModel().getSelectedItem().toString())).getCode(),
//                                    tfPhone.getText(),
//                                    tfHandy.getText(),
//                                    tfEmail.getText(),
//                                    tfStreet.getText(),
//                                    tfPlz.getText(),
//                                    tfOrt.getText(),
//                                    taComments.getText(),
//                                    inviteStatus);
//
//        if (selectedGuest != null)
//        {
//            // Edit selected guest
//            guestData.set(this.guestData.indexOf(selectedGuest), newGuest);
//            dbController.editGuest(newGuest);
//        }
//        else
//        {
//            // Add new guest to list
//            guestData.add(newGuest);
//            dbController.createGuest(newGuest);
//        }
//
//        updateGuestDataFromDb();
//
//    	dialogStage.close();
//    }
//    
//    @FXML
//    public void handleCancel()
//    {
//    	dialogStage.close();
//    }
//
//    public void setDialogStage(Stage dialogStage) 
//    {
//        this.dialogStage = dialogStage;
//    }
//    
//    public void setGuest(Guest selectedGuest)
//    {
//    	this.selectedGuest = selectedGuest;
//    	
//    	tfFirstName.setText(selectedGuest.getFirstName());
//    	tfLastName.setText(selectedGuest.getLastName());
//    	tfAge.setText(String.valueOf(selectedGuest.getAge()));
//    	cbState.getSelectionModel().select(GuestStatus.getByCode(selectedGuest.getStatus()));
//    	tfPhone.setText(selectedGuest.getPhone());
//    	tfHandy.setText(selectedGuest.getHandy());
//    	tfEmail.setText(selectedGuest.getEmail());
//    	tfStreet.setText(selectedGuest.getStreet());
//    	tfPlz.setText(selectedGuest.getPlz() == null ? "" : selectedGuest.getPlz());
//    	tfOrt.setText(selectedGuest.getOrt());
//    	taComments.setText(selectedGuest.getComments());
//    }
//
//    public void setGuestData(ObservableList<Guest> guestData)
//    {
//        this.guestData = guestData;
//    }
//
//    private boolean validateInputs()
//    {
//        boolean isInputCorrect = true;
//
//        if (!tfFirstName.validate()) isInputCorrect = false;
//        if (!tfLastName.validate()) isInputCorrect = false;
//        if (!tfAge.validate()) isInputCorrect = false;
//        if (!tfPlz.validate()) isInputCorrect = false;
//        if (!tfPhone.validate()) isInputCorrect = false;
//        if (!tfHandy.validate()) isInputCorrect = false;
//        if (!tfEmail.validate()) isInputCorrect = false;
//        if (!cbState.validate()) {
//            isInputCorrect = false;
//            LOGGER.warning("cbstate error");
//        }
//
//        // TODO
////            tpGuestData.getSelectionModel().select(PERSON);
//
//        return isInputCorrect;
//    }
}
