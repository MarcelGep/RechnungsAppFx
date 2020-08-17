package com.gepraegs.rechnungsAppFx.controllers;

import com.gepraegs.rechnungsAppFx.Invoice;
import com.gepraegs.rechnungsAppFx.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.*;
//import static com.gepraegs.rechnungsAppFx.helpers.InputValidHelper.isInputInvalid;

public class InvoiceDialogController implements Initializable {

    @FXML private ComboBox<String> cbCustomer;
    @FXML private ComboBox<String> cbUst;
    @FXML private ComboBox<String> cbUnit;
    @FXML private ComboBox<Product> cbProduct;
    @FXML private ComboBox<String> cbPayConditions;

    @FXML private DatePicker dpCreatedDate;

    @FXML private TextField tfAmount;
    @FXML private TextField tfPriceExcl;
    @FXML private TextField tfPriceIncl;

    @FXML private Label lbTitle;
    @FXML private Label lbReNr;
    @FXML private Label lbDueDate;
    @FXML private Label lbPriceExcl;
    @FXML private Label lbPriceIncl;
    @FXML private Label lbPriceUst;
    @FXML private Label lbUst;

    private static final Logger LOGGER = Logger.getLogger(InvoiceDialogController.class.getName());

    private final DbController dbController = DbController.getInstance();

    private ObservableList<Invoice> invoiceData = FXCollections.observableArrayList();

    private Stage dialogStage = new Stage();

    private Invoice selectedInvoice;
    private Invoice savedInvoice;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComboBoxes();
        initTextFields();
        initCreatedDate();
        initDueDate();

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

    private void initCreatedDate() {
        LocalDate createdDate = LocalDate.now();
        dpCreatedDate.setValue(createdDate);
        dpCreatedDate.valueProperty().addListener((ov, oldValue, newValue) -> {
            initDueDate();
        });
    }

    private void initDueDate() {
        long days = Long.parseLong(cbPayConditions.getSelectionModel().getSelectedItem().split(" ")[0]);
        String dueDate = dpCreatedDate.getValue().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        lbDueDate.setText(dueDate);
    }

//    private double calculateExclPrice() {
//        double valueIncl = CurrencyStringToDouble(tfPriceIncl.getText());
//        double valueUst = PercentageStringToDouble(cbUst.getSelectionModel().getSelectedItem());
//        return valueIncl / ((100 + valueUst) / 100);
//    }
//
//    private double calculateInclPrice() {
//        double valueIncl = CurrencyStringToDouble(tfPriceExcl.getText());
//        double valueUst = PercentageStringToDouble(cbUst.getSelectionModel().getSelectedItem());
//        return valueIncl * ((100 + valueUst) / 100);
//    }

    public void setDialogTitle(String title) {
        lbTitle.setText(title);
    }

    public Invoice getSavedInvoice() {
        return savedInvoice;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void initComboBoxes() {
        // pay conditions
        cbPayConditions.getItems().addAll("0 Tage", "7 Tage", "14 Tage", "30 Tage", "60 Tage", "90 Tage");
        cbPayConditions.getSelectionModel().select(1);
        cbPayConditions.valueProperty().addListener((ov, oldValue, newValue) -> {
            initDueDate();
        });

        // product unit
        cbUnit.getItems().addAll("Stk.", "cm", "h", "kg", "km", "m", "L", "ml");
        cbUnit.getSelectionModel().selectFirst();

        // ust
        cbUst.getItems().addAll("19 %", "16 %", "7 %", "5 %", "0 %");
        cbUst.getSelectionModel().selectFirst();
    }

    private void initTextFields() {
        tfPriceExcl.setText(DoubleToCurrencyString(0.0));
        tfPriceIncl.setText(DoubleToCurrencyString(0.0));
        tfAmount.setText("1,00");

//        tfPriceIncl.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal) {
//                tfPriceIncl.setText(DoubleToCurrencyString(Double.parseDouble(tfPriceIncl.getText())));
//            }
//        });
//
//        tfPriceExcl.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal) {
//                tfPriceExcl.setText(DoubleToCurrencyString(Double.parseDouble(tfPriceExcl.getText())));
//            }
//        });
//
//        tfPriceExcl.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (cbPriceType.getSelectionModel().getSelectedIndex() == 1) {
//                tfPriceIncl.setText(DoubleToCurrencyString(calculateInclPrice()));
//            }
//        });
//
//        tfPriceIncl.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (cbPriceType.getSelectionModel().getSelectedIndex() == 0) {
//                tfPriceExcl.setText(DoubleToCurrencyString(calculateExclPrice()));
//            }
//        });
    }

    public void setSelectedInvoice(Invoice selectedInvoice) {
        this.selectedInvoice = selectedInvoice;

//        tfName.setText(selectedInvoice.getReNr());
//        tfPriceExcl.setText(DoubleToCurrencyString(selectedInvoice.getPriceExcl()));
//        tfPriceIncl.setText(DoubleToCurrencyString(selectedInvoice.getPriceIncl()));
//        cbUnit.getSelectionModel().select(selectedInvoice.getUnit());
    }

    public void setInvoiceData(ObservableList<Invoice> invoiceData) {
        this.invoiceData = invoiceData;
    }

    @FXML
    public void handleSave() {
//        if (!validateInputs())
//        {
//            return;
//        }

        String newReNr = selectedInvoice != null ? selectedInvoice.getReNr() : String.valueOf(dbController.readNextId("Invoices"));
        Invoice newInvoice = new Invoice();

        if (selectedInvoice != null) {
            // Edit selected customer
            invoiceData.set(this.invoiceData.indexOf(selectedInvoice), newInvoice);
            dbController.editInvoice(newInvoice);
        } else {
            // Add new customer to customerData
            invoiceData.add(newInvoice);
            dbController.createInvoice(newInvoice);
        }

        savedInvoice = newInvoice;

        dialogStage.close();
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void onPriceInclEnter(ActionEvent ae){
        tfPriceIncl.setText(DoubleToCurrencyString(Double.parseDouble(tfPriceIncl.getText())));
    }

    @FXML
    private void onPriceExclEnter(ActionEvent ae){
        tfPriceExcl.setText(DoubleToCurrencyString(Double.parseDouble(tfPriceExcl.getText())));
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

//    private boolean validateInputs() {
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
