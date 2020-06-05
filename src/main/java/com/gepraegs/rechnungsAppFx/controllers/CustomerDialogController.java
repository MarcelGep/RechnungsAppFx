package com.gepraegs.rechnungsAppFx.controllers;

import com.gepraegs.rechnungsAppFx.Customer;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

//import static com.gepraegs.rechnungsAppFx.helpers.InputValidHelper.isInputInvalid;

public class CustomerDialogController implements Initializable {

    @FXML private JFXComboBox<String> cbCustomerTyp;
    @FXML private JFXTextField tfCompany;
    @FXML private TextField tfFirstName;

    private static final Logger LOGGER = Logger.getLogger(CustomerDialogController.class.getName());
    private final DbController dbController = DbController.getInstance();
    private ObservableList<Customer> customerData = FXCollections.observableArrayList();
    private Stage dialogStage = new Stage();
    private Customer selectedCustomer;
    private Customer savedCustomer;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbCustomerTyp.getItems().addAll("Neuer Firmenkunde", "Neuer Privatkunde");
        cbCustomerTyp.getSelectionModel().selectFirst();

//        addTextLimiter(tfFirstName, 29);
//        addTextLimiter(tfLastName, 29);
//        addTextLimiter(tfAge, 3);
//        addTextLimiter(tfPhone, 30);
//        addTextLimiter(tfHandy, 30);
//        addTextLimiter(tfEmail, 30);
//        addTextLimiter(tfStreet, 30);
//        addTextLimiter(tfPlz, 5);
//        addTextLimiter(tfOrt, 22);

//        setupFocusedProperty();
    }

    public Customer getSavedCustomer() {
        return savedCustomer;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;

//        tfFirstName.setText(selectedCustomer.getName1().getValue());
//        tfCompany.setText(selectedCustomer.getCompany().getValue());
//        tfAge.setText(String.valueOf(selectedGuest.getAge()));
//        cbState.getSelectionModel().select(GuestStatus.getByCode(selectedGuest.getStatus()));
//        tfPhone.setText(selectedGuest.getPhone());
//        tfHandy.setText(selectedGuest.getHandy());
//        tfEmail.setText(selectedGuest.getEmail());
//        tfStreet.setText(selectedGuest.getStreet());
//        tfPlz.setText(selectedGuest.getPlz() == null ? "" : selectedGuest.getPlz());
//        tfOrt.setText(selectedGuest.getOrt());
//        taComments.setText(selectedGuest.getComments());
    }

    public void setCustomerData(ObservableList<Customer> customerData) {
        this.customerData = customerData;
    }

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

    @FXML
    public void handleSave() {
//        if (!validateInputs())
//        {
//            return;
//        }

        int newKdNr = dbController.readNextId("Customers");

        Customer newCustomer = new Customer();
        newCustomer.setKdNr(String.valueOf(newKdNr));
        newCustomer.setName1(tfFirstName.getText());
        newCustomer.setCompany(tfCompany.getText());

//        Customer customer = new Customer(newKdNr,
//                                tfCompany.getText(),
//                                tfFirstName.getText(),
//            					Integer.parseInt(tfAge.getText()),
//                                Objects.requireNonNull(GuestStatus.getByName(cbState.getSelectionModel().getSelectedItem().toString())).getCode(),
//                                tfPhone.getText(),
//                                tfHandy.getText(),
//                                tfEmail.getText(),
//                                tfStreet.getText(),
//                                tfPlz.getText(),
//                                tfOrt.getText(),
//                                "No comment!");

        if (selectedCustomer != null)
        {
            // Edit selected customer
            customerData.set(this.customerData.indexOf(selectedCustomer), newCustomer);
            dbController.editGuest(newCustomer);
        }
        else
        {
            // Add new customer to customerData
            customerData.add(newCustomer);
            dbController.createCustomer(newCustomer);
            savedCustomer = newCustomer;
        }

        dialogStage.close();
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

//    public void addTextLimiter(final TextField tf, final int maxLength) {
//        tf.textProperty().addListener((ov, oldValue, newValue) ->
//        {
//            if (tf.getText().length() > maxLength && !tf.getText().equals(INPUT_ERROR)) {
//                String s = tf.getText().substring(0, maxLength);
//                tf.setText(s);
//            }
//        });
//    }
}
