package com.gepraegs.rechnungsAppFx.controllers;

import com.gepraegs.rechnungsAppFx.Customer;
import com.gepraegs.rechnungsAppFx.Invoice;
import com.gepraegs.rechnungsAppFx.Position;
import com.gepraegs.rechnungsAppFx.Product;
import com.gepraegs.rechnungsAppFx.helpers.HelperDialogs;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

import static com.gepraegs.rechnungsAppFx.helpers.CalculateHelper.*;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.*;

import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showConfirmDialog;
import static java.lang.Integer.MAX_VALUE;

public class InvoiceDialogController implements Initializable {

    @FXML private ComboBox<String> cbPayConditions;
    @FXML private ComboBox<Customer> cbCustomer;

    @FXML private DatePicker dpCreatedDate;
    @FXML private DatePicker dpDeliveryDate;

    @FXML private CheckBox cbDate;

    @FXML private Label lbTitle;
    @FXML private Label lbReNr;
    @FXML private Label lbDueDate;
    @FXML private Label lbPriceExcl;
    @FXML private Label lbPriceIncl;
    @FXML private Label lbPriceUst;
    @FXML private Label lbUst;
    @FXML private Label lbCompany;
    @FXML private Label lbStreet;
    @FXML private Label lbPlzOrt;
    @FXML private Label lbMessageCount;
    @FXML private Label lbMessageMax;
    @FXML private Label lbFooterCount;
    @FXML private Label lbFooterMax;

    @FXML private GridPane gpPositions;
    @FXML private VBox vBoxContact;

    @FXML private Button btnChooseCustomer;
    @FXML private Button btnCancel;

    @FXML private TextArea taMessage;
    @FXML private TextArea taFooter;

    private static final Logger LOGGER = Logger.getLogger(InvoiceDialogController.class.getName());

    private final DbController dbController = DbController.getInstance();

    private ObservableList<Invoice> invoiceData = FXCollections.observableArrayList();

    private List<Product> productData;

    private final SimpleBooleanProperty customerSelected = new SimpleBooleanProperty(false);

    private Stage dialogStage = new Stage();

    private Customer selectedCustomer;

    private Invoice selectedInvoice;
    private Invoice savedInvoice;

    private final int MESSAGE_MAX_COUNT = 3000;
    private final int FOOTER_MAX_COUNT = 250;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vBoxContact.visibleProperty().bind(customerSelected);
        vBoxContact.managedProperty().bind(customerSelected);

        btnChooseCustomer.visibleProperty().bind(customerSelected);
        btnChooseCustomer.managedProperty().bind(customerSelected);

        productData = dbController.readProducts();

        addTextLimiter(taMessage, MESSAGE_MAX_COUNT);
        addTextLimiter(taFooter, FOOTER_MAX_COUNT);

        initComboBoxes();
        initDatePicker();
        initDueDate();
        initLabels();
        addPosition();
    }

    @FXML
    private void onBtnChooseCustomerClicked() {
        customerSelected.setValue(false);
        cbCustomer.setValue(null);
        btnCancel.requestFocus();
        selectedCustomer = null;
    }

    @FXML
    private void onCbDateAction() {
        if (cbDate.isSelected()) {
            dpDeliveryDate.setValue(dpCreatedDate.getValue());
        }
    }

    @FXML
    private void onMessageTextChanged() {
        lbMessageCount.setText(String.valueOf(taMessage.getText().length()));
    }

    @FXML
    private void onFooterTextChanged() {
        lbFooterCount.setText(String.valueOf(taFooter.getText().length()));
    }

    private void initLabels() {
        lbPriceExcl.setText(DoubleToCurrencyString(0.0));
        lbPriceIncl.setText(DoubleToCurrencyString(0.0));
        lbPriceUst.setText(DoubleToCurrencyString(0.0));
        lbUst.setText(DoubleToPercentageString(19.0));
        lbReNr.setText(String.valueOf(dbController.readNextId("Invoices")));
        lbMessageCount.setText(String.valueOf(taMessage.getText().length()));
        lbMessageMax.setText(String.valueOf(MESSAGE_MAX_COUNT));
        lbFooterCount.setText(String.valueOf(taFooter.getText().length()));
        lbFooterMax.setText(String.valueOf(FOOTER_MAX_COUNT));
    }

    private void initDatePicker() {
        LocalDate createdDate = LocalDate.now();
        dpCreatedDate.setValue(createdDate);
        dpCreatedDate.valueProperty().addListener((ov, oldValue, newValue) -> {
            initDueDate();
        });

        dpDeliveryDate.disableProperty().bind(cbDate.selectedProperty());
        if (cbDate.isSelected()) {
            dpDeliveryDate.setValue(createdDate);
        }
    }

    private void initDueDate() {
        long days = Long.parseLong(cbPayConditions.getSelectionModel().getSelectedItem().split(" ")[0]);
        lbDueDate.setText(dateFormatter(dpCreatedDate.getValue().plusDays(days)));
    }

    public void setDialogTitle(String title) {
        lbTitle.setText(title);
    }

    public Invoice getSavedInvoice() {
        return savedInvoice;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleCbCustomerAction() {
        int selectedIndex = cbCustomer.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Customer customer = cbCustomer.getSelectionModel().getSelectedItem();
            if (customer != null) {
                selectedCustomer = customer;
                customerSelected.setValue(true);

                lbCompany.setText(customer.getCompany().getValue());
                lbStreet.setText(customer.getStreet().getValue());
                lbPlzOrt.setText(customer.getPlz().getValue() + " " + customer.getLocation().getValue());
            }
        }
    }

    private void initComboBoxes() {
        // pay conditions
        cbPayConditions.getItems().addAll("0 Tage", "7 Tage", "14 Tage", "30 Tage", "60 Tage", "90 Tage");
        cbPayConditions.getSelectionModel().select(1);
        cbPayConditions.valueProperty().addListener((ov, oldValue, newValue) -> {
            initDueDate();
        });

        List<Customer> customerData = dbController.readCustomers();
        cbCustomer.getItems().addAll(customerData);
        cbCustomer.visibleProperty().bind(customerSelected.not());
        cbCustomer.managedProperty().bind(customerSelected.not());
    }

    public void setSelectedInvoice(Invoice selectedInvoice) {
        this.selectedInvoice = selectedInvoice;
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

        Invoice newInvoice = new Invoice(newReNr,
                                         selectedCustomer,
                                         dateFormatter(dpCreatedDate.getValue()),
                                         lbDueDate.getText(),
                                         null,
                                         false,
                                         CurrencyStringToDouble(lbPriceIncl.getText()),
                                         PercentageStringToDouble(lbUst.getText()));

        if (selectedInvoice != null) {
            // Edit selected invoice
            invoiceData.set(this.invoiceData.indexOf(selectedInvoice), newInvoice);
            dbController.editInvoice(newInvoice);
        } else {
            // Add new invoice to invoiceData
            invoiceData.add(newInvoice);
            dbController.createInvoice(newInvoice);

            // create positions
            createPositions();
        }

        savedInvoice = newInvoice;

        dialogStage.close();
    }

    private void createPositions() {
        Position position = new Position();

        for (int i = 0; i < gpPositions.getRowCount(); i++) {
            for (Node child : gpPositions.getChildren()) {
                if (GridPane.getRowIndex(child) == i) {
                    //TODO set position infos
                }
            }
        }

        dbController.createPosition(position);
    }

    @FXML
    public void handleCancel() throws IOException {
        String content = "RECHNUNG VOR DEM VERLASSEN SPEICHERN?\n\n" +
                         "Du verlässt nun den Rechnungseditor mit ungesicherten Änderungen an der Rechnung. " +
                         "Möchtest du die Änderungen speichern?";
        if (showConfirmDialog(content, Arrays.asList("Ja", "Nein"))) {
            handleSave();
        } else {
            dialogStage.close();
        }
    }

    @FXML
    private void onBtnAddPositionClicked() {
        addPosition();
    }

    private void addPosition() {
        int rowCount = gpPositions.getRowCount();
        gpPositions.addRow(rowCount, createCbProduct(), createTfAmount(),
                            createCbUnit(), createTfPriceExcl(),
                            createCbUst(), createTfPriceIncl(), createDeleteBtn());
    }

    private void deletePosition(int row) {
        Set<Node> deleteNodes = new HashSet<>();
        for (Node child : gpPositions.getChildren()) {
            // get index from child
            Integer rowIndex = GridPane.getRowIndex(child);

            // handle null values for index=0
            int r = rowIndex == null ? 0 : rowIndex;

            if (r > row) {
                // decrement rows for rows after the deleted row
                GridPane.setRowIndex(child, r-1);
            } else if (r == row) {
                // collect matching rows for deletion
                deleteNodes.add(child);
            }
        }

        gpPositions.getChildren().removeAll(deleteNodes);

        updateTotalPrices();
    }

    private void updateTotalPrices() {
        double priceInclTotal = 0.0;
        double ust = PercentageStringToDouble(lbUst.getText());

        for (Node child : gpPositions.getChildren()) {
            // handle null values for index=0
            if (child instanceof TextField && child.getId() != null ) {
                if (child.getId().equals("tfPriceIncl")) {
                    priceInclTotal += CurrencyStringToDouble(((TextField) child).getText());
                }
            }
        }

        double priceExclTotal = calculateExclUst(priceInclTotal, ust);

        lbPriceExcl.setText(DoubleToCurrencyString(priceExclTotal));
        lbPriceIncl.setText(DoubleToCurrencyString(priceInclTotal));
        lbPriceUst.setText(DoubleToCurrencyString(priceInclTotal - priceExclTotal));
    }

    private void updateInclPrice(int row) {
        TextField tfPriceIncl = getTextFieldById(row, "tfPriceIncl");
        TextField tfPriceExcl = getTextFieldById(row, "tfPriceExcl");
        TextField tfAmount = getTextFieldById(row, "tfAmount");
        double ust = PercentageStringToDouble(lbUst.getText());
        double priceExcl = CurrencyStringToDouble(tfPriceExcl.getText());
        double priceIncl = calculateInclUst(priceExcl, ust);
        double amount = NumberStrToDouble(tfAmount.getText());
        tfPriceIncl.setText(DoubleToCurrencyString(amount * priceIncl));
    }

    private void updateExclPrice(int row) {
        TextField tfPriceExcl = getTextFieldById(row, "tfPriceExcl");
        TextField tfPriceIncl = getTextFieldById(row, "tfPriceIncl");
        TextField tfAmount = getTextFieldById(row, "tfAmount");
        double ust = PercentageStringToDouble(lbUst.getText());
        double priceIncl = CurrencyStringToDouble(tfPriceIncl.getText());
        double amount = NumberStrToDouble(tfAmount.getText());
        double priceExcl = calculateExclUst(priceIncl, ust) / amount;
        tfPriceExcl.setText(DoubleToCurrencyString(priceExcl));
    }

    private void setProductData(int row, Product product) {
        TextField tfPriceExcl = getTextFieldById(row, "tfPriceExcl");
        TextField tfPriceIncl = getTextFieldById(row, "tfPriceIncl");
        tfPriceExcl.setText(DoubleToCurrencyString(product.getPriceExcl()));
        tfPriceIncl.setText(DoubleToCurrencyString(product.getPriceIncl()));
    }

    private Button createDeleteBtn() {
        ImageView imageView = new ImageView(new Image(getClass().getResource("/icons/cancel.png").toString()));
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);
        Button deleteBtn = new Button();
        deleteBtn.getStyleClass().clear();
        deleteBtn.getStyleClass().add("back-transparent");
        deleteBtn.getStyleClass().add("hover-hand");
        deleteBtn.setGraphic(imageView);
        deleteBtn.setOnAction(e -> deletePosition(GridPane.getRowIndex(deleteBtn)));
        deleteBtn.setPrefHeight(30);
        return deleteBtn;
    }

    private ComboBox createCbUnit() {
        List<String> unit = Arrays.asList("Stk.", "cm", "h", "kg", "km", "m", "L", "ml");
        ComboBox cb =  createCBox(null, unit, 0);
        cb.setEditable(false);
        return cb;
    }

    private TextField createTfPriceExcl() {
        TextField tf = new TextField(DoubleToCurrencyString(0.0));
        tf.setId("tfPriceExcl");
        tf.setStyle("-fx-alignment: CENTER-RIGHT;");
        tf.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    double priceExcl = CurrencyStringToDouble(tf.getText());
                    tf.setText(DoubleToCurrencyString(priceExcl));
                } catch (Exception e) {
                    setTfValidState(tf, false);
                }
                updateInclPrice(GridPane.getRowIndex(tf));
                updateTotalPrices();
            } else {
                setTfValidState(tf, true);
            }
        });
        return tf;
    }

    private TextField createTfPriceIncl() {
        TextField tf = new TextField(DoubleToCurrencyString(0.0));
        tf.setId("tfPriceIncl");
        tf.setStyle("-fx-alignment: CENTER-RIGHT;");
        tf.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    double priceIncl = CurrencyStringToDouble(tf.getText());
                    tf.setText(DoubleToCurrencyString(priceIncl));
                } catch (Exception e) {
                    setTfValidState(tf, false);
                }
                updateExclPrice(GridPane.getRowIndex(tf));
                updateTotalPrices();
            } else {
                setTfValidState(tf, true);
            }
        });
        return tf;
    }

    private TextField createTfAmount() {
        TextField tf = new TextField("1,00");
        tf.setId("tfAmount");
        tf.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    double amount = NumberStrToDouble(tf.getText());
                    tf.setText(DoubleToNumberStr(amount));
                } catch (Exception e) {
                    tf.setStyle("-fx-border-color: red;");
                    tf.setText("0,00");
                }
                updateInclPrice(GridPane.getRowIndex(tf));
                updateTotalPrices();
            } else {
                tf.setStyle("-fx-border-width: 0px;");

            }
        });
        return tf;

//        tf.setOnKeyPressed(event -> {
//            if(event.getCode() == KeyCode.ENTER){
//                try {
//                    double amount = NumberStrToDouble(tf.getText());
//                    tf.setText(DoubleToNumberStr(amount));
//                    updateInclPrice(GridPane.getRowIndex(tf));
//                    updateTotalPrices();
//                } catch (Exception e) {
//                    tf.setStyle("-fx-border-color: red;");
//                    tf.setText("0,00");
//                }
//            }
//        });
    }

    private ComboBox createCbProduct() {
        ComboBox cb = createCBox("Beschreibe oder wähle ein Produkt", null, -1 );
        cb.setMaxWidth(MAX_VALUE);
        cb.getItems().setAll(productData);
        cb.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue instanceof Product) {
                setProductData(GridPane.getRowIndex(cb), (Product) newValue);
                updateTotalPrices();
            }
        });
        return cb;
    }

    private ComboBox createCbUst() {
        List<String> ustArr = Arrays.asList("19 %", "16 %", "7 %", "5 %", "0 %");
        ComboBox cb = createCBox(null, ustArr, 0);
        cb.valueProperty().addListener((ov, oldValue, newValue) -> {
            lbUst.setText((String) newValue);
            double ust = PercentageStringToDouble((String) newValue);
            double priceExcl = CurrencyStringToDouble(lbPriceExcl.getText());
            lbPriceUst.setText(DoubleToCurrencyString(priceExcl *  (ust / 100)));
            lbPriceIncl.setText(DoubleToCurrencyString(priceExcl *  ((ust + 100) / 100)));
        });
        return cb;
    }

    private ComboBox createCBox(String prompt, List<String> content, int selectedIndex) {
        ComboBox cBox = new ComboBox<String>();
        cBox.setEditable(true);

        if (prompt != null) {
            cBox.setPromptText(prompt);
        }

        if (content != null) {
            cBox.getItems().addAll(content);
        }

        if (selectedIndex != -1) {
            cBox.getSelectionModel().select(selectedIndex);
        }

        return cBox;
    }

    private TextField getTextFieldById(int row, String id) {
        TextField tf = null;
        for (Node child : gpPositions.getChildren()) {
            if (child instanceof TextField
                    && child.getId() != null
                    && child.getId().equals(id)
                    && row == GridPane.getRowIndex(child)) {
                tf = (TextField) child;
            }
        }
        return tf;
    }

    public void addTextLimiter(final Object obj, final int maxLength) {
        if (obj instanceof TextField) {
            TextField tf = (TextField) obj;
            tf.textProperty().addListener((ov, oldValue, newValue) -> {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            });
        } else if (obj instanceof TextArea) {
            TextArea ta = (TextArea) obj;
            ta.textProperty().addListener((ov, oldValue, newValue) -> {
                String currentText = ta.getText();
                if(newValue != null) {
                    if (ta.getText().length() > maxLength) {
                        String s = ta.getText().substring(0, maxLength);
                        ta.setText(s);
                    }

//                    if(newValue.length() <= 5){
//                        ta.setText(currentText.substring(0, ta.getText().length()-1));
//                    }
                }

            });
        }
    }

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
