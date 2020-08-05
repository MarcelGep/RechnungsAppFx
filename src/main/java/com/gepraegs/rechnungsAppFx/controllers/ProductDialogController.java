package com.gepraegs.rechnungsAppFx.controllers;

import com.gepraegs.rechnungsAppFx.Product;
import com.gepraegs.rechnungsAppFx.helpers.FormatterHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.PercentageStringConverter;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.*;
//import static com.gepraegs.rechnungsAppFx.helpers.InputValidHelper.isInputInvalid;

public class ProductDialogController implements Initializable {

    @FXML private ComboBox<String> cbUnit;
    @FXML private ComboBox<String> cbUst;
    @FXML private ComboBox<String> cbPriceType;

    @FXML private TextField tfName;
    @FXML private TextField tfPriceExcl;
    @FXML private TextField tfPriceIncl;

    @FXML private Label lbTitle;

    private static final Logger LOGGER = Logger.getLogger(ProductDialogController.class.getName());

    private final DbController dbController = DbController.getInstance();

    private ObservableList<Product> productData = FXCollections.observableArrayList();

    private Stage dialogStage = new Stage();

    private Product selectedProduct;
    private Product savedProduct;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComboBoxes();

        tfPriceExcl.textProperty().addListener((observable, oldValue, newValue) -> {
            if (cbPriceType.getSelectionModel().getSelectedIndex() == 1) {
                tfPriceIncl.setText(DoubleToCurrencyString(calculateInclPrice()));
            }
        });

        tfPriceIncl.textProperty().addListener((observable, oldValue, newValue) -> {
            if (cbPriceType.getSelectionModel().getSelectedIndex() == 0) {
                tfPriceExcl.setText(DoubleToCurrencyString(calculateExclPrice()));
            }
        });

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

    private double calculateExclPrice() {
        double valueIncl = CurrencyStringToDouble(tfPriceIncl.getText());
        double valueUst = PercentageStringToDouble(cbUst.getSelectionModel().getSelectedItem());
        return valueIncl / ((100 + valueUst) / 100);
    }

    private double calculateInclPrice() {
        double valueIncl = CurrencyStringToDouble(tfPriceExcl.getText());
        double valueUst = PercentageStringToDouble(cbUst.getSelectionModel().getSelectedItem());
        return valueIncl * ((100 + valueUst) / 100);
    }

    public void setDialogTitle(String title) {
        lbTitle.setText(title);
    }

    public Product getSavedProduct() {
        return savedProduct;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void initComboBoxes() {
        // countries
//        ObservableList<String> countries =
//        cbUnit.getSelectionModel().select("Deutschland");
//        cbUnit.setItems(filteredItems);
//        cbUnit.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
//            final TextField editor = cbUnit.getEditor();
//            final String selected = cbUnit.getSelectionModel().getSelectedItem();
//
//            Platform.runLater(() -> {
//                if (selected == null || !selected.equals(editor.getText())) {
//                    filteredItems.setPredicate(item -> {
//                        if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    });
//                }
//            });
//        });

        // price type ust
        cbPriceType.getItems().addAll("Preis inkl. USt.", "Preis ohne USt.");
        cbPriceType.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (cbPriceType.getSelectionModel().getSelectedIndex() == 0) {
                tfPriceIncl.setDisable(false);
                tfPriceExcl.setDisable(true);
//                if (!tfPriceIncl.getText().isEmpty()) {
//                    tfPriceExcl.setText(DoubleToCurrencyString(calculateExclPrice()));
//                }
            } else {
                tfPriceIncl.setDisable(true);
                tfPriceExcl.setDisable(false);
//                if (!tfPriceExcl.getText().isEmpty()) {
//                    tfPriceIncl.setText(DoubleToCurrencyString(calculateInclPrice()));
//                }
            }
        });
        cbPriceType.getSelectionModel().selectFirst();

        // product unit
        cbUnit.getItems().addAll("Stk.", "cm", "h", "kg", "km", "m", "L", "ml");

        // ust
        cbUst.getItems().addAll("19 %", "16 %", "7 %", "5 %", "0 %");
        cbUst.getSelectionModel().selectFirst();
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;

        tfName.setText(selectedProduct.getName());
        tfPriceExcl.setText(String.valueOf(selectedProduct.getPriceExcl()));
        tfPriceIncl.setText(String.valueOf(selectedProduct.getPriceIncl()));
        cbUnit.getSelectionModel().select(selectedProduct.getUnit());
    }

    public void setProductData(ObservableList<Product> productData) {
        this.productData = productData;
    }

    @FXML
    public void handleSave() {
//        if (!validateInputs())
//        {
//            return;
//        }

        String newArtNr = selectedProduct != null ? selectedProduct.getArtNr() : String.valueOf(dbController.readNextId("Products"));
        Product newProduct = new Product(newArtNr,
                            tfName.getText(),
                            cbUnit.getSelectionModel().getSelectedItem(),
                            PercentageStringToDouble(cbUst.getSelectionModel().getSelectedItem()),
                            CurrencyStringToDouble(tfPriceExcl.getText()));


        if (selectedProduct != null) {
            // Edit selected customer
            productData.set(this.productData.indexOf(selectedProduct), newProduct);
            dbController.editProduct(newProduct);
        } else {
            // Add new customer to customerData
            productData.add(newProduct);
            dbController.createProduct(newProduct);
        }

        savedProduct = newProduct;

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
