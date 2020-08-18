package com.gepraegs.rechnungsAppFx.controllers;

import com.gepraegs.rechnungsAppFx.Customer;
import com.gepraegs.rechnungsAppFx.Invoice;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.DoubleToCurrencyString;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.DoubleToPercentageString;
import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.*;

public class InvoicesController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger( InvoicesController.class.getName() );

	private final DbController dbController = DbController.getInstance();

	private final ObservableList<Invoice> invoiceData = FXCollections.observableArrayList();

	private final TableColumn<Invoice, String> colReNr = new TableColumn<>("NUMMER");
	private final TableColumn<Invoice, String> colCustomer = new TableColumn<>("KUNDE");
	private final TableColumn<Invoice, String> colCreatedDate = new TableColumn<>("ERSTELLT AM");
	private final TableColumn<Invoice, String> colDueDate = new TableColumn<>("FÄLLIG");
	private final TableColumn<Invoice, String> colPayedDate = new TableColumn<>("BEZAHLT");
	private final TableColumn<Invoice, String> colTotalPrice = new TableColumn<>("GESAMT");
	private final TableColumn<Invoice, String> colState = new TableColumn<>("STATUS");

	@FXML private TableView<Invoice> invoiceTable;
	@FXML private TextField tfSearchInvoice;

	@FXML private Label lbReNr;
	@FXML private Label lbCustomer;
	@FXML private Label lbPriceExcl;
	@FXML private Label lbUst;
	@FXML private Label lbPriceIncl;
	@FXML private Label lbCreatedDate;
	@FXML private Label lbDueDate;
	@FXML private Label lbPayedDate;

	@FXML private VBox invoiceDetailsFilled;
	@FXML private VBox invoiceDetailsEmpty;

	@FXML private VBox showDialogLayer;

	@FXML private Button btnClearSearch;

	@Override
	public void initialize( URL location, ResourceBundle resources ) {
		initializeColumns();
		loadCustomersData();
		setRowSelectionListener();
		setTableSortOrder();

		setupInvoiceFilter();

		showDialogLayer.setVisible(false);
	}

	private void loadCustomersData() {
		//read guests from database
		List<Invoice> invoices = dbController.readInvoices();

		//write guests to data list
		if (invoices != null && !invoices.isEmpty()) {
			invoiceData.addAll(invoices);
		} else {
			LOGGER.warning("No customer data exist!");
		}

		// set customerData to customerTable
		invoiceTable.setItems(invoiceData);

		clearTableSelection();
	}

	private void initializeColumns() {
		invoiceTable.setPlaceholder(new Label("Keine Einträge vorhanden"));

		// set size of columns
		invoiceTable.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );

		colReNr.setMaxWidth( 1f * Integer.MAX_VALUE * 16);
		colCustomer.setMaxWidth( 1f * Integer.MAX_VALUE * 43);
		colCreatedDate.setMaxWidth( 1f * Integer.MAX_VALUE * 18);
		colDueDate.setMaxWidth( 1f * Integer.MAX_VALUE * 15);
		colPayedDate.setMaxWidth( 1f * Integer.MAX_VALUE * 15);
		colTotalPrice.setMaxWidth( 1f * Integer.MAX_VALUE * 15);
		colState.setMaxWidth( 1f * Integer.MAX_VALUE * 15);

		// set cell value factory
		colReNr.setCellValueFactory(param -> param.getValue().reNrProperty());
		colCustomer.setCellValueFactory(param -> param.getValue().kdNrProperty());
		colCreatedDate.setCellValueFactory(param -> param.getValue().createDateProperty());
		colDueDate.setCellValueFactory(param -> param.getValue().dueDateProperty());
		colPayedDate.setCellValueFactory(param -> param.getValue().payedDateProperty());
		colState.setCellValueFactory(param -> param.getValue().payedDateProperty());
		colTotalPrice.setCellValueFactory((TableColumn.CellDataFeatures<Invoice, String> param) ->
				new ReadOnlyStringWrapper(DoubleToCurrencyString(param.getValue().getTotalPrice())));

		// add columns to customer table
		invoiceTable.getColumns().add(colReNr);
		invoiceTable.getColumns().add(colCustomer);
		invoiceTable.getColumns().add(colCreatedDate);
		invoiceTable.getColumns().add(colDueDate);
		invoiceTable.getColumns().add(colPayedDate);
		invoiceTable.getColumns().add(colTotalPrice);
		invoiceTable.getColumns().add(colState);
	}

	private void showInvoiceInformations(Invoice invoice) {
		lbReNr.setText(invoice.getReNr());
		lbCustomer.setText(validateExistingData(invoice.getKdNr()));
		lbCreatedDate.setText(validateExistingData(invoice.getCreateDate()));
		lbDueDate.setText(validateExistingData(invoice.getDueDate()));
		lbPayedDate.setText(validateExistingData(invoice.getPayedDate()));
		lbUst.setText(DoubleToPercentageString(invoice.getUst()));
		lbPriceIncl.setText(DoubleToCurrencyString(invoice.getTotalPrice()));
		lbPriceExcl.setText(DoubleToCurrencyString(invoice.getTotalPrice() / ((100 + invoice.getUst()) / 100)));

		showInvoiceDetails(true);
	}

	private void clearCustomerDetails() {
		lbReNr.setText("");
		lbCustomer.setText("");
		lbCreatedDate.setText("");
		lbDueDate.setText("");
		lbPayedDate.setText("");
		lbUst.setText("");
		lbPriceIncl.setText("");
		lbPriceExcl.setText("");

		showInvoiceDetails(false);
	}

	private void clearTableSelection() {
		invoiceTable.getSelectionModel().clearSelection();
		clearCustomerDetails();
	}

	private void showInvoiceDetails(boolean show) {
		invoiceDetailsFilled.setVisible(show);
		invoiceDetailsEmpty.setVisible(!show);
	}

	private String validateExistingData(String data) {
		return data.isEmpty() ? "---" : data;
	}

	private void scrollToRow(int row)
	{
		invoiceTable.requestFocus();
		invoiceTable.getSelectionModel().select(row);
		invoiceTable.getFocusModel().focus(row);
		invoiceTable.scrollTo(row);
	}

	private void setTableSortOrder() {
		colCustomer.setSortType(TableColumn.SortType.ASCENDING);
		colReNr.setSortType(TableColumn.SortType.ASCENDING);
		invoiceTable.getSortOrder().add(colReNr);
//		customerTable.getSortOrder().add(colCustomer);
	}

	private void setRowSelectionListener() {
		invoiceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				showInvoiceInformations(newSelection);
			}
		});
	}

	private void setupInvoiceFilter() {
		FilteredList<Invoice> filteredData = new FilteredList<>(invoiceData, e -> true);

		tfSearchInvoice.textProperty().addListener((observableValue, oldValue, newValue) ->
		{
			filteredData.setPredicate((Predicate<? super Invoice>) invoice ->
			{
				if (newValue == null) {
					return true;
				}

				btnClearSearch.setVisible(!newValue.isEmpty());

				String lowerCaseFilter = newValue.toLowerCase();

				String customerName = invoice.getKdNr().toLowerCase();

				if (customerName.contains(lowerCaseFilter)) {
					return true;
				}

				return false;
			});

			SortedList<Invoice> sortedData = new SortedList<>(filteredData);
			sortedData.comparatorProperty().bind(invoiceTable.comparatorProperty());
			invoiceTable.setItems(sortedData);
		});
	}

	@FXML
	private void onClearSearchButtonClicked() {
		tfSearchInvoice.clear();
		btnClearSearch.setVisible(false);
	}

	@FXML
	private void onBtnCloseDetailsClicked() {
		invoiceTable.getSelectionModel().clearSelection();
		System.out.println(invoiceTable.getSelectionModel().getSelectedIndex());
		clearTableSelection();
	}

	@FXML
	private void onBtnNewInvoiceClicked() {
		showDialogLayer.setVisible(true);

		try {
			// Create Dialog
			Invoice invoice = showInvoiceDialog(invoiceData, null);

			if (invoice != null) {
				invoiceTable.sort();

				//scroll to last added customer, select it and show detail informations
				int lastInvoiceReNr = dbController.readNextId("Customers") - 1;
				for (int i = 0; i < invoiceTable.getItems().size(); i++) {
					if (Integer.parseInt(invoiceTable.getItems().get(i).getReNr()) == lastInvoiceReNr) {
						scrollToRow(i);
						invoiceTable.getSelectionModel().select(i);
						break;
					}
				}
			}
		} catch (IOException e){
			LOGGER.warning(e.toString());
		}

		showDialogLayer.setVisible(false);
	}

	@FXML
	private void onBtnDeleteInvoiceClicked() {
//		try {
//			String content = "Diese Aktion kann später nicht mehr rückgängig gemacht werden.\n\n" +
//							 "Möchtest du den Kunden \"" + customerTable.getSelectionModel().getSelectedItem().getCompany().getValue()  + "\" löschen?";
//
//			showDialogLayer.setVisible(true);
//
//			if (showConfirmDialog(content, Arrays.asList("Löschen", "Abbrechen"))) {
//				dbController.deleteCustomer(customerTable.getSelectionModel().getSelectedItem());
//				customerData.remove(customerTable.getSelectionModel().getSelectedItem());
//				clearTableSelection();
//			}
//
//			showDialogLayer.setVisible(false);
//		} catch (IOException e) {
//			LOGGER.warning(e.toString());
//		}
	}

	@FXML
	private void onBtnEditCustomerClicked() {
//		try {
//			// Create Dialog
//			int selectedIndex = customerTable.getSelectionModel().getSelectedIndex();
//			if (selectedIndex != -1) {
//				showDialogLayer.setVisible(true);
//				Customer customer = customerTable.getItems().get(selectedIndex);
//				customer = showCustomerDialog(customerData, customer);
//
//				if (customer != null) {
//					showInvoiceInformations(customer);
//					customerTable.getSelectionModel().select(selectedIndex);
//				}
//
//				showDialogLayer.setVisible(false);
//			}
//		} catch (IOException e){
//			LOGGER.warning(e.toString());
//		}
	}
}
