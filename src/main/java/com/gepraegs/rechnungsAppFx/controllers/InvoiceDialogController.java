package com.gepraegs.rechnungsAppFx.controllers;

import com.gepraegs.rechnungsAppFx.Invoice;
import com.gepraegs.rechnungsAppFx.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.*;
import static java.lang.Integer.MAX_VALUE;
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

    @FXML private GridPane gpPositions;

    private static final Logger LOGGER = Logger.getLogger(InvoiceDialogController.class.getName());

    private final DbController dbController = DbController.getInstance();

    private ObservableList<Invoice> invoiceData = FXCollections.observableArrayList();

    private Stage dialogStage = new Stage();

    private Invoice selectedInvoice;
    private Invoice savedInvoice;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComboBoxes();
        initCreatedDate();
        initDueDate();
        initLabels();
//        System.out.println(gpPositions.getColumnCount());
//        int colCount = gpPositions.getColumnCount();
//        gpPositions.getColumnConstraints().removeAll();
//        for (int i = 0; i < colCount; i++) {
//            gpPositions.getColumnConstraints().add(new ColumnConstraints(-1, -1, -1, Priority.ALWAYS, HPos.CENTER, false));
//        }

        addPosition();

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

    private void initLabels() {
        lbPriceExcl.setText(DoubleToCurrencyString(0.0));
        lbPriceIncl.setText(DoubleToCurrencyString(0.0));
        lbPriceUst.setText(DoubleToCurrencyString(0.0));
        lbUst.setText(DoubleToPercentageString(19.0));
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
//        cbUnit.getItems().addAll("Stk.", "cm", "h", "kg", "km", "m", "L", "ml");
//        cbUnit.getSelectionModel().selectFirst();

        // ust
//        cbUst.getItems().addAll("19 %", "16 %", "7 %", "5 %", "0 %");
//        cbUst.getSelectionModel().selectFirst();
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
        double priceExclTotal = 0.0;
        double priceInclTotal = 0.0;

        for (Node child : gpPositions.getChildren()) {
            // handle null values for index=0
            if (child instanceof TextField && child.getId() != null ) {
                if (child.getId().equals("tfPriceExcl")) {
                    priceExclTotal += CurrencyStringToDouble(((TextField) child).getText());
                } else if (child.getId().equals("tfPriceIncl")) {
                    priceInclTotal += CurrencyStringToDouble(((TextField) child).getText());
                }
            }
        }

        lbPriceExcl.setText(DoubleToCurrencyString(priceExclTotal));
        lbPriceIncl.setText(DoubleToCurrencyString(priceInclTotal));
        lbPriceUst.setText(DoubleToCurrencyString(priceInclTotal - priceExclTotal));
    }

    private Button createDeleteBtn() {
        ImageView imageView = new ImageView(new Image(getClass().getResource("/icons/cancel.png").toString()));
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);
        Button deleteBtn = new Button();
        deleteBtn.getStyleClass().clear();
        deleteBtn.getStyleClass().add("back-transparent");
        deleteBtn.setGraphic(imageView);
        deleteBtn.setOnAction(e -> deletePosition(GridPane.getRowIndex(deleteBtn)));
        deleteBtn.setPrefHeight(30);
        return deleteBtn;
    }

    private ComboBox createCbUnit() {
        List<String> unit = Arrays.asList(new String[]{"Stk.", "cm", "h", "kg", "km", "m", "L", "ml"});
        return createCBox(null, unit, 0);
    }

    private TextField createTfPriceExcl() {
        TextField tf = createTextField(DoubleToCurrencyString(0.0));
        tf.setId("tfPriceExcl");
        tf.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                tf.setText(DoubleToCurrencyString(CurrencyStringToDouble(tf.getText())));
                double ust = PercentageStringToDouble(lbUst.getText());
                double priceExcl = CurrencyStringToDouble(tf.getText());

                TextField tfPriceIncl = getTextFieldById(GridPane.getRowIndex(tf), "tfPriceIncl");
                tfPriceIncl.setText(DoubleToCurrencyString(priceExcl * ((ust + 100) / 100)));

                updateTotalPrices();
            }
        });
        return tf;
    }

    private TextField createTfPriceIncl() {
        TextField tf = createTextField(DoubleToCurrencyString(0.0));
        tf.setId("tfPriceIncl");
        tf.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                tf.setText(DoubleToCurrencyString(CurrencyStringToDouble(tf.getText())));
                double ust = PercentageStringToDouble(lbUst.getText());
                double priceIncl = CurrencyStringToDouble(tf.getText());

                TextField tfPriceExcl = getTextFieldById(GridPane.getRowIndex(tf), "tfPriceExcl");
                tfPriceExcl.setText(DoubleToCurrencyString(priceIncl / ((ust + 100) / 100)));

                updateTotalPrices();
            }
        });
        return tf;
    }

    private TextField createTfAmount() {
        return createTextField("1,00");
    }

    private ComboBox createCbProduct() {
        ComboBox cb = createCBox("Beschreibe oder wähle ein Produkt", null, -1 );
        cb.setMaxWidth(MAX_VALUE);
        return cb;
    }

    private ComboBox createCbUst() {
        List<String> ustArr = Arrays.asList(new String[]{"19 %", "16 %", "7 %", "5 %", "0 %"});
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

    private TextField createTextField(String text) {
        TextField tF = new TextField(text);
        tF.setStyle("-fx-alignment: CENTER-RIGHT;");
        return tF;
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
