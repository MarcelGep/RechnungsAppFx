package com.gepraegs.rechnungsAppFx.controllers;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gepraegs.rechnungsAppFx.Customer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

//import static com.gepraegs.rechnungsAppFx.helpers.InputValidHelper.isInputInvalid;

public class CustomerDialogController implements Initializable {

    @FXML
    private ComboBox<String> cbCustomerTyp;
    @FXML
    private ComboBox<String> cbCountry;

    @FXML
    private TextField tfCompany;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfStreet;
    @FXML
    private TextField tfPlz;
    @FXML
    private TextField tfLocation;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfFax;
    @FXML
    private TextField tfHandy;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfWebsite;

    @FXML
    private Label lbTitle;

    @FXML
    private Spinner<Integer> spDiscount;

    private static final Logger LOGGER = Logger.getLogger(CustomerDialogController.class.getName());
    private final DbController dbController = DbController.getInstance();
    private ObservableList<Customer> customerData = FXCollections.observableArrayList();
    private Stage dialogStage = new Stage();
    private Customer selectedCustomer;
    private Customer savedCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSpinner();
        initComboBox();

        // addTextLimiter(tfFirstName, 29);
        // addTextLimiter(tfLastName, 29);
        // addTextLimiter(tfAge, 3);
        // addTextLimiter(tfPhone, 30);
        // addTextLimiter(tfHandy, 30);
        // addTextLimiter(tfEmail, 30);
        // addTextLimiter(tfStreet, 30);
        // addTextLimiter(tfPlz, 5);
        // addTextLimiter(tfOrt, 22);

        // setupFocusedProperty();
    }

    public void setDialogTitle(String title) {
        lbTitle.setText(title);
    }

    public Customer getSavedCustomer() {
        return savedCustomer;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void initSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        valueFactory.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return value.toString() + " %";
            }

            @Override
            public Integer fromString(String string) {
                String valueWithoutUnits = string.replaceAll("%", "").trim();
                if (valueWithoutUnits.isEmpty()) {
                    return 0;
                } else {
                    return Integer.valueOf(valueWithoutUnits);
                }
            }
        });
        spDiscount.setValueFactory(valueFactory);
    }

    private void initComboBox() {
        // countries
        ObservableList<String> countries = Stream.of(Locale.getISOCountries())
                .map(locales -> new Locale("", locales))
                .map(Locale::getDisplayCountry)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        FilteredList<String> filteredItems = new FilteredList<>(countries, p -> true);

        cbCountry.getSelectionModel().select("Deutschland");
        cbCountry.setItems(filteredItems);
        cbCountry.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = cbCountry.getEditor();
            final String selected = cbCountry.getSelectionModel().getSelectedItem();

            Platform.runLater(() -> {
                if (selected == null || !selected.equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                        if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
        });

        // customer type
        cbCustomerTyp.getItems().addAll("Neuer Firmenkunde", "Neuer Privatkunde");
        cbCustomerTyp.getSelectionModel().selectFirst();
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;

        tfCompany.setText(selectedCustomer.getCompany().getValue());
        tfFirstName.setText(selectedCustomer.getName1().getValue());
        tfLastName.setText(selectedCustomer.getName2().getValue());
        tfStreet.setText(selectedCustomer.getStreet().getValue());
        tfPlz.setText(selectedCustomer.getPlz().getValue());
        tfLocation.setText(selectedCustomer.getLocation().getValue());
        tfPhone.setText(selectedCustomer.getPhone().getValue());
        tfFax.setText(selectedCustomer.getFax().getValue());
        tfHandy.setText(selectedCustomer.getHandy().getValue());
        tfEmail.setText(selectedCustomer.getEmail().getValue());
        tfWebsite.setText(selectedCustomer.getWebsite().getValue());
        cbCountry.getSelectionModel().select(selectedCustomer.getCountry().getValue());
        spDiscount.getValueFactory().setValue(selectedCustomer.getDiscount());
    }

    public void setCustomerData(ObservableList<Customer> customerData) {
        this.customerData = customerData;
    }

    @FXML
    public void handleSave() {
        // if (!validateInputs())
        // {
        // return;
        // }

        String newKdNr = selectedCustomer != null ? selectedCustomer.getKdNr().getValue()
                : String.valueOf(dbController.readNextId("Customers"));

        Customer newCustomer = new Customer(newKdNr,
                tfCompany.getText(),
                tfFirstName.getText(),
                tfLastName.getText(),
                tfStreet.getText(),
                tfPlz.getText(),
                tfLocation.getText(),
                cbCountry.getSelectionModel().getSelectedItem(),
                tfPhone.getText(),
                tfFax.getText(),
                tfHandy.getText(),
                tfEmail.getText(),
                tfWebsite.getText(),
                spDiscount.getValue());

        if (selectedCustomer != null) {
            // Edit selected customer
            customerData.set(this.customerData.indexOf(selectedCustomer), newCustomer);
            dbController.editCustomer(newCustomer);
        } else {
            // Add new customer to customerData
            customerData.add(newCustomer);
            dbController.createCustomer(newCustomer);
        }

        savedCustomer = newCustomer;

        dialogStage.close();
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    // public void addTextLimiter(final TextField tf, final int maxLength) {
    // tf.textProperty().addListener((ov, oldValue, newValue) ->
    // {
    // if (tf.getText().length() > maxLength && !tf.getText().equals(INPUT_ERROR)) {
    // String s = tf.getText().substring(0, maxLength);
    // tf.setText(s);
    // }
    // });
    // }

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
