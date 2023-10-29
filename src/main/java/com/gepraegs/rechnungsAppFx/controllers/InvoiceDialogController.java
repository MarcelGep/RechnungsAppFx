package com.gepraegs.rechnungsAppFx.controllers;

import static com.gepraegs.rechnungsAppFx.helpers.CalculateHelper.calculateExclUst;
import static com.gepraegs.rechnungsAppFx.helpers.CalculateHelper.calculateInclUst;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.CurrencyStringToDouble;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.DoubleToCurrencyString;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.DoubleToNumberStr;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.DoubleToPercentageString;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.NumberStrToDouble;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.PercentageStringToDouble;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.dateFormatter;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.parseToDate;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.setTfValidState;
import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showConfirmDialog;
import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showInfoDialog;
import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showProductDialog;
import static java.lang.Integer.MAX_VALUE;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;

import com.gepraegs.rechnungsAppFx.Customer;
import com.gepraegs.rechnungsAppFx.Invoice;
import com.gepraegs.rechnungsAppFx.InvoiceState;
import com.gepraegs.rechnungsAppFx.Position;
import com.gepraegs.rechnungsAppFx.Product;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InvoiceDialogController implements Initializable {

    @FXML
    private ComboBox<String> cbPayConditions;
    @FXML
    private ComboBox<Customer> cbCustomer;

    @FXML
    private DatePicker dpCreatedDate;
    @FXML
    private DatePicker dpDeliveryDate;

    @FXML
    private CheckBox cbDate;

    @FXML
    private Label lbTitle;
    @FXML
    private Label lbReNr;
    @FXML
    private Label lbDueDate;
    @FXML
    private Label lbPriceExcl;
    @FXML
    private Label lbPriceIncl;
    @FXML
    private Label lbPriceUst;
    @FXML
    private Label lbUst;
    @FXML
    private Label lbCompany;
    @FXML
    private Label lbStreet;
    @FXML
    private Label lbPlzOrt;
    @FXML
    private Label lbMessageCount;
    @FXML
    private Label lbMessageMax;
    @FXML
    private Label lbFooterCount;
    @FXML
    private Label lbFooterMax;

    @FXML
    private GridPane gpPositions;
    @FXML
    private VBox vBoxContact;

    @FXML
    private Button btnChooseCustomer;
    @FXML
    private Button btnCancel;

    @FXML
    private TextArea taMessage;
    @FXML
    private TextArea taFooter;

    private static final Logger LOGGER = Logger.getLogger(InvoiceDialogController.class.getName());

    private final DbController dbController = DbController.getInstance();

    private ObservableList<Invoice> invoiceData = FXCollections.observableArrayList();
    private ObservableList<Product> productData = FXCollections.observableArrayList();

    private List<Position> positionData;

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

        productData.setAll(dbController.readProducts());

        addTextLimiter(taMessage, MESSAGE_MAX_COUNT);
        addTextLimiter(taFooter, FOOTER_MAX_COUNT);

        initComboBoxes();
        initDatePicker();
        initDueDate();
        initLabels();
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
        lbDueDate.setText(dateFormatter(calcDueDate().toString()));
    }

    private LocalDate calcDueDate() {
        long days = Long.parseLong(cbPayConditions.getSelectionModel().getSelectedItem().split(" ")[0]);
        return dpCreatedDate.getValue().plusDays(days);
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
        this.selectedCustomer = selectedInvoice.getCustomer();
        this.positionData = dbController.readPositions(selectedInvoice.getReNr());

        customerSelected.setValue(true);
        cbCustomer.getSelectionModel().select(selectedInvoice.getCustomer());
        lbCompany.setText(selectedInvoice.getCustomer().getCompany().getValue());
        lbStreet.setText(selectedInvoice.getCustomer().getStreet().getValue());
        lbPlzOrt.setText(selectedInvoice.getCustomer().getPlz().getValue() + " "
                + selectedInvoice.getCustomer().getLocation().getValue());
        lbReNr.setText(selectedInvoice.getReNr());
        dpCreatedDate.setValue(parseToDate(selectedInvoice.getCreateDate()));
        dpDeliveryDate.setValue(parseToDate(selectedInvoice.getDeliveryDate()));
        cbDate.setSelected(selectedInvoice.getCreateDate().equals(selectedInvoice.getDeliveryDate()));
        cbPayConditions.getSelectionModel().select(selectedInvoice.getPayCondition() + " Tage");
        lbDueDate.setText(dateFormatter(selectedInvoice.getDueDate()));

        showPositions();
    }

    public void setInvoiceData(ObservableList<Invoice> invoiceData) {
        this.invoiceData = invoiceData;
    }

    @FXML
    public void handleSave() {
        // if (!validateInputs())
        // {
        // return;
        // }

        String newReNr = selectedInvoice != null ? selectedInvoice.getReNr()
                : String.valueOf(dbController.readNextId("Invoices"));

        LocalDate now = LocalDate.now();
        InvoiceState state = InvoiceState.CREATED;
        String currentDate = now.toString();
        String dueDate = calcDueDate().toString();

        if (dueDate.compareTo(currentDate) <= 0) {
            state = InvoiceState.DUE;
        }

        Invoice newInvoice = new Invoice(newReNr,
                selectedCustomer,
                dpCreatedDate.getValue().toString(),
                calcDueDate().toString(),
                null,
                dpDeliveryDate.getValue().toString(),
                state.getCode(),
                Integer.parseInt(cbPayConditions.getSelectionModel().getSelectedItem().split(" ")[0]),
                CurrencyStringToDouble(lbPriceIncl.getText()),
                PercentageStringToDouble(lbUst.getText()));

        if (selectedInvoice != null) {
            // Edit selected invoice
            invoiceData.set(this.invoiceData.indexOf(selectedInvoice), newInvoice);
            dbController.editInvoice(newInvoice);
            dbController.deletePositionsOfInvoice(newReNr);
            createPositions();
        } else {
            // Add new invoice to invoiceData
            invoiceData.add(newInvoice);
            dbController.createInvoice(newInvoice);
            createPositions();
        }

        savedInvoice = newInvoice;

        dialogStage.close();
    }

    private void createPositions() {
        for (int i = 1; i < gpPositions.getRowCount(); i++) {
            Position position = getPositionByRow(i);
            position.setRgNr(lbReNr.getText());
            position.setCreatedDate(dpCreatedDate.getValue().toString());

            if (!dbController.positionExist(position)) {
                dbController.createPosition(position);
            }
        }
    }

    private void editPositions() {
        for (int i = 1; i < gpPositions.getRowCount(); i++) {
            Position position = getPositionByRow(i);
            position.setRgNr(lbReNr.getText());

            dbController.editPosition(position);
        }
    }

    private void showPositions() {
        for (Position pos : positionData) {
            // Product product = dbController.readProduct(pos.getArtNr());
            String artNr = pos.getArtNr();
            String description = pos.getDescription();
            String amount = DoubleToNumberStr(pos.getAmount());
            String unit = pos.getUnit();
            String priceExcl = DoubleToCurrencyString(pos.getPriceExcl());
            String ust = DoubleToPercentageString(pos.getUst());
            String priceIncl = DoubleToCurrencyString(pos.getPriceIncl());

            addPosition(artNr, description, amount, unit, priceExcl, ust, priceIncl);
        }

        updateTotalPrices();
    }

    @FXML
    public void handleCancel() throws IOException {
        dialogStage.close();
        // if (hasChanges) {
        // String content = "RECHNUNG VOR DEM VERLASSEN SPEICHERN?\n\n" +
        // "Du verlässt nun den Rechnungseditor mit ungesicherten Änderungen an der
        // Rechnung. " +
        // "Möchtest du die Änderungen speichern?";
        // if (showConfirmDialog(content, Arrays.asList("Ja", "Nein"))) {
        // handleSave();
        // } else {
        // dialogStage.close();
        // }
        // }
    }

    @FXML
    private void onBtnAddPositionClicked() {
        addEmptyPosition();
    }

    public void addEmptyPosition() {
        addPosition(null, null, null, null, null, null, null);
    }

    private void addPosition(String artNr, String description, String amount, String unit, String priceExcl, String ust,
            String priceIncl) {
        int rowCount = gpPositions.getRowCount();
        gpPositions.addRow(rowCount, createTfArtNr(artNr), createCbProduct(description), createTfAmount(amount),
                createCbUnit(unit), createTfPriceExcl(priceExcl),
                createCbUst(ust), createTfPriceIncl(priceIncl), createDeleteBtn());
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
                GridPane.setRowIndex(child, r - 1);
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
            if (child instanceof TextField && child.getId() != null) {
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

    private void setPrice(int row, Product product) {
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

    private ComboBox createCbUnit(String value) {
        List<String> unit = Arrays.asList("Stk.", "cm", "h", "kg", "km", "m", "L", "ml");
        ComboBox cb = createCBox(null, unit, 0);
        cb.setId("cbUnit");
        cb.setEditable(false);
        cb.getSelectionModel().select(value != null ? value : "Stk.");
        return cb;
    }

    private TextField createTfPriceExcl(String value) {
        TextField tf = new TextField();
        tf.setId("tfPriceExcl");
        tf.setStyle("-fx-alignment: CENTER-RIGHT;");
        tf.setText(value != null ? value : DoubleToCurrencyString(0.0));
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

    private TextField createTfPriceIncl(String value) {
        TextField tf = new TextField();
        tf.setId("tfPriceIncl");
        tf.setStyle("-fx-alignment: CENTER-RIGHT;");
        tf.setText(value != null ? value : DoubleToCurrencyString(0.0));
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

    private TextField createTfArtNr(String artNr) {
        TextField tf = new TextField();
        tf.setId("tfArtNr");
        tf.setText(artNr);
        tf.setEditable(false);
        return tf;
    }

    private TextField createTfAmount(String value) {
        TextField tf = new TextField();
        tf.setId("tfAmount");
        tf.setText(value != null ? value : DoubleToNumberStr(1.0));
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

        // tf.setOnKeyPressed(event -> {
        // if(event.getCode() == KeyCode.ENTER){
        // try {
        // double amount = NumberStrToDouble(tf.getText());
        // tf.setText(DoubleToNumberStr(amount));
        // updateInclPrice(GridPane.getRowIndex(tf));
        // updateTotalPrices();
        // } catch (Exception e) {
        // tf.setStyle("-fx-border-color: red;");
        // tf.setText("0,00");
        // }
        // }
        // });
    }

    private void editPositionByRow(int row, Product product) {
        TextField tf;
        List<String> textFieldIds = Arrays.asList("tfArtNr", "tfPriceExcl", "tfPriceIncl");
        for (String tfId : textFieldIds) {
            for (Node child : gpPositions.getChildren()) {
                if (child instanceof TextField
                        && child.getId() != null
                        && child.getId().equals(tfId)
                        && row == GridPane.getRowIndex(child)) {

                    switch (tfId) {
                        case "tfArtNr":
                            tf = (TextField) child;
                            tf.setText(product.getArtNr());
                            break;

                        case "tfPriceExcl":
                            tf = (TextField) child;
                            tf.setText(DoubleToCurrencyString(product.getPriceExcl()));
                            break;

                        case "tfPriceIncl":
                            double amount = NumberStrToDouble(getTextFieldById(row, "tfAmount").getText());
                            tf = (TextField) child;
                            tf.setText(DoubleToCurrencyString(product.getPriceIncl() * amount));
                            break;

                        default:
                            break;
                    }
                }
            }

            ComboBox cb;
            List<String> comboBoxIds = Arrays.asList("cbUnit", "cbUst");
            for (String cbId : comboBoxIds) {
                for (Node child : gpPositions.getChildren()) {
                    if (child instanceof ComboBox
                            && child.getId() != null
                            && child.getId().equals(cbId)
                            && row == GridPane.getRowIndex(child)) {

                        switch (cbId) {
                            case "cbUnit":
                                cb = (ComboBox) child;
                                cb.setValue(product.getUnit());
                                break;

                            case "cbUst":
                                cb = (ComboBox) child;
                                cb.setValue(DoubleToPercentageString(product.getUst()));
                                break;

                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private ComboBox createCbProduct(String value) {
        // ComboBox cb = createCBox("Beschreibe oder wähle ein Produkt", null, -1);
        // cb.getItems().setAll(productData);

        ComboBox<String> cb = new ComboBox<>();
        cb.setEditable(true);
        cb.setMaxWidth(MAX_VALUE);
        cb.setId("cbProduct");
        cb.getSelectionModel().select(value);
        cb.setPromptText("Beschreibe oder wähle ein Produkt");

        List<String> productNames = new ArrayList<>();
        for (Product p : productData) {
            productNames.add(p.getName());
        }

        cb.getItems().addAll(productNames);

        new AutoCompleteBox(cb);

        cb.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String product = cb.getSelectionModel().getSelectedItem();
                if (!product.isEmpty()) {
                    if (positionExist(product)) {
                        try {
                            showInfoDialog("Die Position wurde bereits erfasst!", "Abbrechen");
                            cb.setValue(null);
                            cb.requestFocus();
                            cb.getItems().addAll(productNames);
                            cb.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // check if product exist
                        if (dbController.productExist(product)) {
                            Product p = dbController.readProduct(product);
                            editPositionByRow(GridPane.getRowIndex(cb), p);
                        } else {
                            try {
                                if (showConfirmDialog(
                                        "Das Produkt \"" + product
                                                + "\" wurde nicht gefunden, soll dieses angelegt werden?",
                                        Arrays.asList("Ja", "Nein"))) {
                                    showProductDialog(productData, null, product);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    editPositionByRow(GridPane.getRowIndex(cb), new Product());
                }
                updateTotalPrices();
            }
        });

        // cb.getSelectionModel().selectedItemProperty().addListener( (options,
        // oldValue, newValue) -> {
        // if (newValue != null && !newValue.isEmpty()) {
        // // show if position exist in invoice
        // if (positionExist(newValue)) {
        // try {
        // showInfoDialog("Die Position wurde bereits erfasst!", "Abbrechen");
        // //deletePosition(GridPane.getRowIndex(cb));
        // cb.setValue("");
        //// cb.requestFocus();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // } else {
        // // check if product exist
        // if (dbController.productExist(newValue)) {
        // Product product = dbController.readProduct(newValue);
        // editPositionByRow(GridPane.getRowIndex(cb), product);
        // } else {
        // try {
        // if (showConfirmDialog("Das Produkt \"" + newValue + "\" wurde nicht gefunden,
        // soll dieses angelegt werden?", Arrays.asList("Ja", "Nein"))) {
        // showProductDialog(productData, null, newValue);
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }
        // }
        // } else {
        // editPositionByRow(GridPane.getRowIndex(cb), new Product());
        // }
        // updateTotalPrices();
        // });

        return cb;
    }

    private ComboBox createCbUst(String value) {
        List<String> ustArr = Arrays.asList("19 %", "16 %", "7 %", "5 %", "0 %");
        ComboBox cb = createCBox(null, ustArr, 0);
        cb.setId("cbUst");
        cb.getSelectionModel().select(value != null ? value : DoubleToPercentageString(19.0));
        cb.valueProperty().addListener((ov, oldValue, newValue) -> {
            lbUst.setText((String) newValue);
            double ust = PercentageStringToDouble((String) newValue);
            double priceExcl = CurrencyStringToDouble(lbPriceExcl.getText());
            lbPriceUst.setText(DoubleToCurrencyString(priceExcl * (ust / 100)));
            lbPriceIncl.setText(DoubleToCurrencyString(priceExcl * ((ust + 100) / 100)));
        });
        return cb;
    }

    private ComboBox createCBox(String prompt, List<String> content, int selectedIndex) {
        ComboBox cBox = new ComboBox<>();
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

    private boolean positionExist(String name) {
        int posCount = 0;

        if (!name.isEmpty()) {
            for (Node child : gpPositions.getChildren()) {
                if (child instanceof ComboBox
                        && child.getId() != null
                        && child.getId().equals("cbProduct")) {
                    ComboBox cb = (ComboBox) child;
                    if (name.equals(cb.getSelectionModel().getSelectedItem().toString())) {
                        if (posCount == 0) {
                            posCount++;
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private Position getPositionByRow(int row) {
        Position position = new Position();
        TextField tf;
        List<String> textFieldIds = Arrays.asList("tfArtNr", "tfAmount", "tfPriceExcl", "tfPriceIncl");
        for (String tfId : textFieldIds) {
            for (Node child : gpPositions.getChildren()) {
                if (child instanceof TextField
                        && child.getId() != null
                        && child.getId().equals(tfId)
                        && row == GridPane.getRowIndex(child)) {

                    switch (tfId) {
                        case "tfArtNr":
                            tf = (TextField) child;
                            position.setArtNr(tf.getText());
                            break;

                        case "tfAmount":
                            tf = (TextField) child;
                            position.setAmount(NumberStrToDouble(tf.getText()));
                            break;

                        case "tfPriceExcl":
                            tf = (TextField) child;
                            position.setPriceExcl(CurrencyStringToDouble(tf.getText()));
                            break;

                        case "tfPriceIncl":
                            tf = (TextField) child;
                            position.setPriceIncl(CurrencyStringToDouble(tf.getText()));
                            break;

                        default:
                            break;
                    }
                }
            }
        }

        ComboBox cb;
        List<String> comboBoxIds = Arrays.asList("cbProduct", "cbUnit", "cbUst");
        for (String cbId : comboBoxIds) {
            for (Node child : gpPositions.getChildren()) {
                if (child instanceof ComboBox
                        && child.getId() != null
                        && child.getId().equals(cbId)
                        && row == GridPane.getRowIndex(child)) {

                    switch (cbId) {
                        case "cbProduct":
                            cb = (ComboBox) child;
                            position.setDescription(cb.getSelectionModel().getSelectedItem().toString());
                            break;

                        case "cbUnit":
                            cb = (ComboBox) child;
                            position.setUnit(cb.getSelectionModel().getSelectedItem().toString());
                            break;

                        case "cbUst":
                            cb = (ComboBox) child;
                            position.setUst(
                                    PercentageStringToDouble(cb.getSelectionModel().getSelectedItem().toString()));
                            break;

                        default:
                            break;
                    }
                }
            }
        }

        return position;
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
                if (newValue != null) {
                    if (ta.getText().length() > maxLength) {
                        String s = ta.getText().substring(0, maxLength);
                        ta.setText(s);
                    }
                }

            });
        }
    }

    // private boolean validateInputs() {
    // boolean isInputCorrect = true;
    //
    // if (!tfFirstName.validate()) isInputCorrect = false;
    // if (!tfLastName.validate()) isInputCorrect = false;
    // if (!tfAge.validate()) isInputCorrect = false;
    // if (!tfPlz.validate()) isInputCorrect = false;
    // if (!tfPhone.validate()) isInputCorrect = false;
    // if (!tfHandy.validate()) isInputCorrect = false;
    // if (!tfEmail.validate()) isInputCorrect = false;
    // if (!cbState.validate()) {
    // isInputCorrect = false;
    // LOGGER.warning("cbstate error");
    // }
    //
    // // TODO
    //// tpGuestData.getSelectionModel().select(PERSON);
    //
    // return isInputCorrect;
    // }
}
