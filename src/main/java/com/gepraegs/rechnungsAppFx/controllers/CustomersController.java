package com.gepraegs.rechnungsAppFx.controllers;

import com.gepraegs.rechnungsAppFx.Customer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.*;

public class CustomersController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger( CustomersController.class.getName() );
	private final DbController dbController = DbController.getInstance();
	private final ObservableList<Customer> customerData = FXCollections.observableArrayList();

	private final JFXTreeTableColumn<Customer, String> colKdNr = new JFXTreeTableColumn<>("NUMMER");
	private final JFXTreeTableColumn<Customer, String> colCompany = new JFXTreeTableColumn<>("KUNDE");
	private final JFXTreeTableColumn<Customer, String> colOpenCosts = new JFXTreeTableColumn<>("OFFENE RECHNUNGEN");
	private final JFXTreeTableColumn<Customer, String> colPayedCosts = new JFXTreeTableColumn<>("BEZAHLTE RECHNUNGEN");

	@FXML private JFXTreeTableView<Customer> customerTable;

	@FXML private JFXButton btnNewCustomer;

	@FXML private Label lbCompany;
	@FXML private Label lbName;
	@FXML private Label lbKdNr;
	@FXML private Label lbPhone;
	@FXML private Label lbHandy;
	@FXML private Label lbFax;
	@FXML private Label lbStreet;
	@FXML private Label lbLocation;
	@FXML private Label lbCountry;
	@FXML private Label lbEmail;
	@FXML private Label lbWebsite;
	@FXML private Label lbDiscount;

	@FXML private VBox customerDetailsFilled;
	@FXML private VBox customerDetailsEmpty;

	@Override
	public void initialize( URL location, ResourceBundle resources ) {

		initializeColumns();
		loadCustomersData();
		showCustomerDetails(false);

		//clear selection if table has no focus
//		customerTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
//			if (!newVal) {
//				customerTable.getSelectionModel().clearSelection();
//				clearCustomerDetails();
//			}
//		});

		customerTable.setRowFactory(tv -> {
			TreeTableRow<Customer> row = new TreeTableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
					Customer customer = customerData.get(row.getIndex());
					showCustomerInformations(customer);
				}
			});
			return row;
		});
	}

	private void loadCustomersData()
	{
		//read guests from database
		List<Customer> customers = dbController.readCustomers();

		//write guests to data list
		if (customers != null && !customers.isEmpty()) {
			customerData.addAll(customers);
		} else {
			LOGGER.warning("No customer data exist!");
		}
	}

	private void initializeColumns() {

		// set size of columns
		customerTable.setColumnResizePolicy( TreeTableView.CONSTRAINED_RESIZE_POLICY );

		colKdNr.setMaxWidth( 1f * Integer.MAX_VALUE * 10);
		colCompany.setMaxWidth( 1f * Integer.MAX_VALUE * 62);
		colCompany.setPrefWidth(900);
		colOpenCosts.setMaxWidth( 1f * Integer.MAX_VALUE * 13.5);
		colPayedCosts.setMaxWidth( 1f * Integer.MAX_VALUE * 14.5);

		// set cell value factory
		colKdNr.setCellValueFactory(param -> param.getValue().getValue().getKdNr());
		colCompany.setCellValueFactory(param -> param.getValue().getValue().getCompany());
		colOpenCosts.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> param) ->
				new ReadOnlyStringWrapper(DoubleToCurrencyString(param.getValue().getValue().getOpenCosts())));
//		colOpenCosts.setStyle("-fx-alignment: CENTER-RIGHT;");
		colPayedCosts.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> param) ->
				new ReadOnlyStringWrapper(DoubleToCurrencyString(param.getValue().getValue().getPayedCosts())));

		// add columns to customer table
		customerTable.getColumns().add(colKdNr);
		customerTable.getColumns().add(colCompany);
		customerTable.getColumns().add(colOpenCosts);
		customerTable.getColumns().add(colPayedCosts);

		// create root item for customer table with the customer data
		final TreeItem<Customer> root = new RecursiveTreeItem<>(customerData, RecursiveTreeObject::getChildren);
		customerTable.setRoot(root);
		customerTable.setShowRoot(false);
	}

	private void showCustomerInformations(Customer customer) {
		lbKdNr.setText("Kundennummer  " + customer.getKdNr().getValue());
		lbCompany.setText(validateExistingData(customer.getCompany().getValue()));
		lbName.setText(customer.getName1().getValue() + " " + customer.getName2().getValue());
		lbStreet.setText(validateExistingData(customer.getStreet().getValue()));
		lbLocation.setText(validateExistingData(customer.getPlz().getValue() + " " + customer.getLocation().getValue()));
		lbCountry.setText(validateExistingData(customer.getCountry().getValue()));
		lbPhone.setText(validateExistingData(customer.getPhone().getValue()));
		lbHandy.setText(validateExistingData(customer.getHandy().getValue()));
		lbFax.setText(validateExistingData(customer.getFax().getValue()));
		lbEmail.setText(validateExistingData(customer.getEmail().getValue()));
		lbWebsite.setText(validateExistingData(customer.getWebsite().getValue()));
		lbDiscount.setText(DoubleToPercentageString(customer.getDiscount()));

		showCustomerDetails(true);
	}

	private void clearCustomerDetails() {
		lbKdNr.setText("");
		lbCompany.setText("");
		lbName.setText("");
		lbStreet.setText("");
		lbLocation.setText("");
		lbCountry.setText("");
		lbPhone.setText("");
		lbHandy.setText("");
		lbFax.setText("");
		lbEmail.setText("");
		lbWebsite.setText("");
		lbDiscount.setText("");

		showCustomerDetails(false);
	}

	private void showCustomerDetails(boolean show) {
		customerDetailsFilled.setVisible(show);
		customerDetailsEmpty.setVisible(!show);
	}

	private String validateExistingData(String data) {
		return data.isEmpty() ? "---" : data;
	}

	@FXML
	private void onBtnNewCustomerClicked() {
		//TODO New customer dialog
		LOGGER.info("NEW CUSTOMER...");
	}

	@FXML
	private void onBtnCloseDetailsClicked() {
		customerTable.getSelectionModel().clearSelection();
		clearCustomerDetails();
	}
}
