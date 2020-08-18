package com.gepraegs.rechnungsAppFx.controllers;

import com.gepraegs.rechnungsAppFx.Customer;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.*;
import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.*;

public class CustomersController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger( CustomersController.class.getName() );

	private final DbController dbController = DbController.getInstance();

	private final ObservableList<Customer> customerData = FXCollections.observableArrayList();

	private final TableColumn<Customer, String> colKdNr = new TableColumn<>("NUMMER");
	private final TableColumn<Customer, String> colCompany = new TableColumn<>("KUNDE");
	private final TableColumn<Customer, String> colOpenCosts = new TableColumn<>("OFFENE\nRECHNUNGEN");
	private final TableColumn<Customer, String> colPayedCosts = new TableColumn<>("BEZAHLTE\nRECHNUNGEN");

	@FXML private TableView<Customer> customerTable;

	@FXML private TextField tfSearchCustomer;

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
	@FXML private Label lbCustomerCount;

	@FXML private VBox customerDetailsFilled;
	@FXML private VBox customerDetailsEmpty;

	@FXML private VBox showDialogLayer;

	@FXML private Button btnClearSearch;

	@Override
	public void initialize( URL location, ResourceBundle resources ) {
		initializeColumns();
		loadCustomersData();
		setRowSelectionListener();
		setTableSortOrder();

		setupCustomerFilter();

		showDialogLayer.setVisible(false);
	}

	private void loadCustomersData() {
		//read guests from database
		List<Customer> customers = dbController.readCustomers();

		//write guests to data list
		if (customers != null && !customers.isEmpty()) {
			customerData.addAll(customers);
		} else {
			LOGGER.warning("No customer data exist!");
		}

		// set customerData to customerTable
		customerTable.setItems(customerData);

		// set count of all customers
		lbCustomerCount.setText(String.valueOf(customerData.size()));

		clearTableSelection();
	}

	private void initializeColumns() {
		customerTable.setPlaceholder(new Label("Keine Einträge vorhanden"));

		// set size of columns
		customerTable.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );

		colKdNr.setMaxWidth( 1f * Integer.MAX_VALUE * 12);
		colCompany.setMaxWidth( 1f * Integer.MAX_VALUE * 58);
		colOpenCosts.setMaxWidth( 1f * Integer.MAX_VALUE * 15);
		colPayedCosts.setMaxWidth( 1f * Integer.MAX_VALUE * 15);

		// set cell value factory
		colKdNr.setCellValueFactory(param -> param.getValue().getKdNr());
		colCompany.setCellValueFactory(param -> param.getValue().getCompany());
		colCompany.setCellValueFactory((TableColumn.CellDataFeatures<Customer, String> param) ->
					new ReadOnlyStringWrapper(param.getValue().getCompany().getValue() + "\n" +
														param.getValue().getName1().getValue() + " " +
														param.getValue().getName2().getValue()));

		colCompany.setCellFactory(column-> new TableCell<>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if(item == null || empty) {
					setGraphic(null);
				} else {
					VBox vbox = new VBox();
					vbox.setAlignment(Pos.CENTER_LEFT);
					List<String> textList = Arrays.asList(item.split("\n"));
					for(int i = 0; i < textList.size() ; i++) {
						Text lbl = new Text(textList.get(i));
						if (i % 2 == 0) {
							lbl.setStyle("-fx-font-weight: bold");
						}
						vbox.getChildren().add(lbl);
					}
					setGraphic(vbox);
				}
			}
		});

		colOpenCosts.setCellValueFactory((TableColumn.CellDataFeatures<Customer, String> param) ->
				new ReadOnlyStringWrapper(DoubleToCurrencyString(param.getValue().getOpenCosts())));
//		colOpenCosts.setStyle("-fx-alignment: CENTER-RIGHT;");
		colPayedCosts.setCellValueFactory((TableColumn.CellDataFeatures<Customer, String> param) ->
				new ReadOnlyStringWrapper(DoubleToCurrencyString(param.getValue().getPayedCosts())));

		// add columns to customer table
		customerTable.getColumns().add(colKdNr);
		customerTable.getColumns().add(colCompany);
		customerTable.getColumns().add(colOpenCosts);
		customerTable.getColumns().add(colPayedCosts);
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

	private void clearTableSelection() {
		customerTable.getSelectionModel().clearSelection();
		clearCustomerDetails();
	}

	private void showCustomerDetails(boolean show) {
		customerDetailsFilled.setVisible(show);
		customerDetailsEmpty.setVisible(!show);
	}

	private String validateExistingData(String data) {
		return data.isEmpty() ? "---" : data;
	}

	private void scrollToRow(int row)
	{
		customerTable.requestFocus();
		customerTable.getSelectionModel().select(row);
		customerTable.getFocusModel().focus(row);
		customerTable.scrollTo(row);
	}

	private void setTableSortOrder() {
		colCompany.setSortType(TableColumn.SortType.ASCENDING);
		colKdNr.setSortType(TableColumn.SortType.ASCENDING);
		customerTable.getSortOrder().add(colCompany);
//		customerTable.getSortOrder().add(colKdNr);
	}

	private void setRowSelectionListener() {
		customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				showCustomerInformations(newSelection);
			}
		});
	}

	private void setupCustomerFilter() {
		FilteredList<Customer> filteredData = new FilteredList<>(customerData, e -> true);

		tfSearchCustomer.textProperty().addListener((observableValue, oldValue, newValue) ->
		{
			filteredData.setPredicate((Predicate<? super Customer>) customer ->
			{
				if (newValue == null) {
					return true;
				}

				btnClearSearch.setVisible(!newValue.isEmpty());

				String lowerCaseFilter = newValue.toLowerCase();

				String firstLastName = customer.getName1().toString().toLowerCase() + " " +
						               customer.getName2().toString().toLowerCase();
				String lastFirstName = customer.getName2().toString().toLowerCase() + " " +
						               customer.getName1().toString().toLowerCase();
				String companyName = customer.getCompany().toString().toLowerCase();

				if (firstLastName.contains(lowerCaseFilter) ||
					lastFirstName.contains(lowerCaseFilter) ||
					companyName.contains(lowerCaseFilter)) {
					return true;
				}

				return false;
			});

			SortedList<Customer> sortedData = new SortedList<>(filteredData);
			sortedData.comparatorProperty().bind(customerTable.comparatorProperty());
			customerTable.setItems(sortedData);
		});
	}

	@FXML
	private void onClearSearchButtonClicked() {
		tfSearchCustomer.clear();
		btnClearSearch.setVisible(false);
	}

	@FXML
	private void onBtnCloseDetailsClicked() {
		customerTable.getSelectionModel().clearSelection();
		System.out.println(customerTable.getSelectionModel().getSelectedIndex());
		clearTableSelection();
	}

	@FXML
	private void onBtnNewCustomerClicked() {
		try {
			// Create Dialog
			showDialogLayer.setVisible(true);
			Customer customer = showCustomerDialog(customerData, null);

			if (customer != null) {
				customerTable.sort();

				// scroll to last added customer, select it and show detail informations
				int lastCustomerKdNr = dbController.readNextId("Customers") - 1;
				for (int i = 0; i < customerTable.getItems().size(); i++) {
					if (Integer.parseInt(customerTable.getItems().get(i).getKdNr().getValue()) == lastCustomerKdNr) {
						scrollToRow(i);
						customerTable.getSelectionModel().select(i);
						break;
					}
				}
			}

			showDialogLayer.setVisible(false);
		} catch (IOException e){
			LOGGER.warning(e.toString());
		}
	}

	@FXML
	private void onBtnDeleteCustomerClicked() {
		try {
			String content = "Diese Aktion kann später nicht mehr rückgängig gemacht werden.\n\n" +
							 "Möchtest du den Kunden \"" + customerTable.getSelectionModel().getSelectedItem().getCompany().getValue()  + "\" löschen?";

			showDialogLayer.setVisible(true);

			if (showConfirmDialog(content, Arrays.asList("Löschen", "Abbrechen"))) {
				dbController.deleteCustomer(customerTable.getSelectionModel().getSelectedItem());
				customerData.remove(customerTable.getSelectionModel().getSelectedItem());
				clearTableSelection();
			}

			showDialogLayer.setVisible(false);
		} catch (IOException e) {
			LOGGER.warning(e.toString());
		}
	}

	@FXML
	private void onBtnEditCustomerClicked() {
		try {
			// Create Dialog
			int selectedIndex = customerTable.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) {
				showDialogLayer.setVisible(true);
				Customer customer = customerTable.getItems().get(selectedIndex);
				customer = showCustomerDialog(customerData, customer);

				if (customer != null) {
					showCustomerInformations(customer);
					customerTable.getSelectionModel().select(selectedIndex);
				}

				showDialogLayer.setVisible(false);
			}
		} catch (IOException e){
			LOGGER.warning(e.toString());
		}
	}
}
