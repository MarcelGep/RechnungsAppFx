package com.gepraegs.rechnungsAppFx.controllers;

import com.gepraegs.rechnungsAppFx.Customer;
import com.gepraegs.rechnungsAppFx.Invoice;
import com.gepraegs.rechnungsAppFx.InvoiceState;
import com.gepraegs.rechnungsAppFx.Position;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static com.gepraegs.rechnungsAppFx.helpers.CalculateHelper.calculateExclUst;
import static com.gepraegs.rechnungsAppFx.helpers.CalculateHelper.calculateUst;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.*;
import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.*;
import static javax.print.attribute.standard.Chromaticity.COLOR;

public class InvoicesController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger( InvoicesController.class.getName() );

	private final DbController dbController = DbController.getInstance();

	private final ObservableList<Invoice> invoiceData = FXCollections.observableArrayList();
	private final ObservableList<Position> positionData = FXCollections.observableArrayList();

	private final TableColumn<Invoice, String> colReNr = new TableColumn<>("NUMMER");
	private final TableColumn<Invoice, String> colCustomer = new TableColumn<>("KUNDE");
	private final TableColumn<Invoice, String> colCreatedDate = new TableColumn<>("ERSTELLT AM");
	private final TableColumn<Invoice, String> colDueDate = new TableColumn<>("FÄLLIG");
	private final TableColumn<Invoice, String> colPayedDate = new TableColumn<>("BEZAHLT");
	private final TableColumn<Invoice, String> colTotalPrice = new TableColumn<>("GESAMT");
	private final TableColumn<Invoice, String> colState = new TableColumn<>("STATUS");

	private final TableColumn<Position, String> colPosDescription = new TableColumn<>("BESCHREIBUNG");
	private final TableColumn<Position, String> colPosCreatedDate = new TableColumn<>("DATUM");
	private final TableColumn<Position, String> colPosAmount = new TableColumn<>("MENGE");
	private final TableColumn<Position, String> colPosUnit = new TableColumn<>("EINHEIT");
	private final TableColumn<Position, String> colPosPriceExcl = new TableColumn<>("PREIS");
	private final TableColumn<Position, String> colPosUst = new TableColumn<>("UST. %");
	private final TableColumn<Position, String> colPosPriceIncl = new TableColumn<>("GESAMT");

	@FXML private TableView<Invoice> invoiceTable;
	@FXML private TableView<Position> positionTable;

	@FXML private TextField tfSearchInvoice;

	@FXML private Button btnState;

	@FXML private Label lbReNr;
	@FXML private Label lbCustomer;
	@FXML private Label lbPriceExcl;
	@FXML private Label lbUst;
	@FXML private Label lbPriceIncl;
	@FXML private Label lbCreatedDate;
	@FXML private Label lbDueDate;
	@FXML private Label lbPayedDate;
	@FXML private Label lbDeliveryDate;

	@FXML private VBox invoiceDetailsFilled;
	@FXML private VBox invoiceDetailsEmpty;

	@FXML private VBox showDialogLayer;

	@FXML private Button btnClearSearch;

	@Override
	public void initialize( URL location, ResourceBundle resources ) {
		initializeColumns();
		initializePositionColumns();
		loadInvoiceData();
		setRowSelectionListener();
		setTableSortOrder();

		setupInvoiceFilter();

		showDialogLayer.setVisible(false);
	}

	private void loadInvoiceData() {
		//read guests from database
		List<Invoice> invoices = dbController.readInvoices();

		//write guests to data list
		if (invoices != null && !invoices.isEmpty()) {
			invoiceData.addAll(invoices);
		} else {
			LOGGER.warning("No invoice data exist!");
		}

		// set customerData to customerTable
		invoiceTable.setItems(invoiceData);
		positionTable.setItems(positionData);

		clearTableSelection();
	}

	private void initializePositionColumns() {
		positionTable.setPlaceholder(new Label("Keine Einträge vorhanden"));

		// set size of columns
		positionTable.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );

		colPosDescription.setMaxWidth( 1f * Integer.MAX_VALUE * 60);
		colPosCreatedDate.setMaxWidth( 1f * Integer.MAX_VALUE * 15);
		colPosAmount.setMaxWidth( 1f * Integer.MAX_VALUE * 12);
		colPosUnit.setMaxWidth( 1f * Integer.MAX_VALUE * 10);
		colPosPriceExcl.setMaxWidth( 1f * Integer.MAX_VALUE * 16);
		colPosUst.setMaxWidth( 1f * Integer.MAX_VALUE * 10);
		colPosPriceIncl.setMaxWidth( 1f * Integer.MAX_VALUE * 16);

		// set cell value factory
		colPosDescription.setCellValueFactory(param -> param.getValue().descriptionProperty());
		colPosCreatedDate.setCellValueFactory((TableColumn.CellDataFeatures<Position, String> param) ->
				new ReadOnlyStringWrapper(dateFormatter(param.getValue().getCreatedDate())));
		colPosAmount.setCellValueFactory((TableColumn.CellDataFeatures<Position, String> param) ->
				new ReadOnlyStringWrapper(DoubleToNumberStr(param.getValue().getAmount())));
		colPosUnit.setCellValueFactory(param -> param.getValue().unitProperty());
		colPosPriceExcl.setCellValueFactory((TableColumn.CellDataFeatures<Position, String> param) ->
				new ReadOnlyStringWrapper(DoubleToCurrencyString(param.getValue().getPriceExcl())));
		colPosUst.setCellValueFactory((TableColumn.CellDataFeatures<Position, String> param) ->
				new ReadOnlyStringWrapper(DoubleToPercentageString(param.getValue().getUst())));
		colPosPriceIncl.setCellValueFactory((TableColumn.CellDataFeatures<Position, String> param) ->
				new ReadOnlyStringWrapper(DoubleToCurrencyString(param.getValue().getPriceIncl())));

		// add columns to customer table
		positionTable.getColumns().add(colPosDescription);
		positionTable.getColumns().add(colPosCreatedDate);
		positionTable.getColumns().add(colPosAmount);
		positionTable.getColumns().add(colPosUnit);
		positionTable.getColumns().add(colPosPriceExcl);
		positionTable.getColumns().add(colPosUst);
		positionTable.getColumns().add(colPosPriceIncl);
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
		colCustomer.setCellValueFactory(param -> param.getValue().getCustomer().getCompany());
		colCreatedDate.setCellValueFactory((TableColumn.CellDataFeatures<Invoice, String> param) ->
				new ReadOnlyStringWrapper(dateFormatter(param.getValue().getCreateDate())));
		colDueDate.setCellValueFactory((TableColumn.CellDataFeatures<Invoice, String> param) ->
				new ReadOnlyStringWrapper(dateFormatter(param.getValue().getDueDate())));
		colPayedDate.setCellValueFactory((TableColumn.CellDataFeatures<Invoice, String> param) ->
				new ReadOnlyStringWrapper(param.getValue().getPayedDate() == null ? "" : dateFormatter(param.getValue().getPayedDate())));
		colTotalPrice.setCellValueFactory((TableColumn.CellDataFeatures<Invoice, String> param) ->
				new ReadOnlyStringWrapper(DoubleToCurrencyString(param.getValue().getTotalPrice())));
		colState.setCellFactory(new Callback<>() {
			@Override
			public TableCell call(final TableColumn<Invoice, String> param) {
				final TableCell<Invoice, String> cell = new TableCell<>() {

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							Invoice invoice = getTableView().getItems().get(getIndex());

							Label label = new Label();
							label.setText(InvoiceState.getByCode(invoice.getState()).toString());
							label.setTextFill(Color.WHITE);
							label.setStyle("-fx-font-weight: bold");

							Rectangle rectangle = new Rectangle();
							rectangle.setWidth(95);
							rectangle.setHeight(25);
							rectangle.setArcHeight(25);
							rectangle.setArcWidth(25);

							StackPane stack = new StackPane();
							stack.getChildren().addAll(rectangle, label);

							switch (InvoiceState.getByCode(invoice.getState())) {
								case PAYED:
									rectangle.setFill(Color.LIMEGREEN);
									break;

								case DUE:
									rectangle.setFill(Color.RED);
									break;

								default:
									label.setText(InvoiceState.CREATED.toString());
									rectangle.setFill(Color.valueOf("#00b4c2"));
									break;
							}

							setGraphic(stack);
						}
					}
				};
				return cell;
			}
		});

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
		lbCustomer.setText(validateExistingData(invoice.getCustomer().getCompany().getValue()));
		lbCreatedDate.setText(validateExistingData(dateFormatter(invoice.getCreateDate())));
		lbDueDate.setText(validateExistingData(dateFormatter(invoice.getDueDate())));
		lbPayedDate.setText(validateExistingData(dateFormatter(invoice.getPayedDate())));
		lbUst.setText(DoubleToCurrencyString(calculateUst(invoice.getTotalPrice(), invoice.getUst())));
		lbPriceIncl.setText(DoubleToCurrencyString(invoice.getTotalPrice()));
		lbPriceExcl.setText(DoubleToCurrencyString(calculateExclUst(invoice.getTotalPrice(), invoice.getUst())));
		lbDeliveryDate.setText(validateExistingData(dateFormatter(invoice.getDeliveryDate())));

		btnState.setText(InvoiceState.getByCode(invoice.getState()).toString());
		switch (InvoiceState.getByCode(invoice.getState())) {
			case PAYED:
				btnState.setStyle("-fx-background-color: limegreen;");
				break;

			case DUE:
				btnState.setStyle("-fx-background-color: red;");
				break;

			default:
				btnState.setStyle("-fx-background-color: -fx-accent;");
				break;
		}

		showInvoiceDetails(true);
	}

	private void showPositions(Invoice invoice) {
		positionData.clear();
		List<Position> positions = dbController.readPositions(invoice.getReNr());
		positionData.addAll(positions);
	}

	private void clearInvoiceDetails() {
		lbReNr.setText("");
		lbCustomer.setText("");
		lbCreatedDate.setText("");
		lbDueDate.setText("");
		lbPayedDate.setText("");
		lbUst.setText("");
		lbPriceIncl.setText("");
		lbPriceExcl.setText("");
		positionData.clear();

		showInvoiceDetails(false);
	}

	private void clearTableSelection() {
		invoiceTable.getSelectionModel().clearSelection();
		clearInvoiceDetails();
	}

	private void showInvoiceDetails(boolean show) {
		invoiceDetailsFilled.setVisible(show);
		invoiceDetailsEmpty.setVisible(!show);
	}

	private String validateExistingData(String data) {
		return data == null || data.isEmpty() ? "---" : data;
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
				showPositions(newSelection);
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

				String customerName = invoice.getCustomer().getCompany().getValue().toLowerCase();
				String reNr = invoice.getReNr().toLowerCase();

				if (customerName.contains(lowerCaseFilter) || reNr.contains(lowerCaseFilter)) {
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
		clearTableSelection();
	}

	@FXML
	private void onBtnNewInvoiceClicked() {
		showDialogLayer.setVisible(true);

		Invoice invoice = null;

		try {
			// Create Dialog
			invoice = showInvoiceDialog(invoiceData, null);

			if (invoice != null) {
				invoiceTable.sort();

				//scroll to last added invoice, select it and show detail informations
				int lastInvoiceReNr = dbController.readNextId("Invoices") - 1;
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
		showPositions(invoice);
	}

	@FXML
	private void onBtnDeleteInvoiceClicked() {
		try {
			Invoice selectedInvoice = invoiceTable.getSelectionModel().getSelectedItem();
			String content = "Diese Aktion kann später nicht mehr rückgängig gemacht werden.\n\n" +
							 "Möchtest du die Rechnung mit der Nr. " + selectedInvoice.getReNr() +
							 " für den Kunden \"" + selectedInvoice.getCustomer().getCompany().getValue()  + "\" löschen?";

			showDialogLayer.setVisible(true);

			if (showConfirmDialog(content, Arrays.asList("Löschen", "Abbrechen"))) {
				dbController.deleteInvoice(selectedInvoice);
				invoiceData.remove(selectedInvoice);
				clearTableSelection();
			}

			showDialogLayer.setVisible(false);
		} catch (IOException e) {
			LOGGER.warning(e.toString());
		}
	}

	@FXML
	private void onBtnEditInvoiceClicked() {
		try {
			// Create Dialog
			int selectedIndex = invoiceTable.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) {
				showDialogLayer.setVisible(true);
				Invoice invoice = invoiceTable.getItems().get(selectedIndex);
				invoice = showInvoiceDialog(invoiceData, invoice);

				if (invoice != null) {
					showInvoiceInformations(invoice);
					invoiceTable.getSelectionModel().select(selectedIndex);
				}

				showDialogLayer.setVisible(false);
			}
		} catch (IOException e){
			LOGGER.warning(e.toString());
		}
	}

	@FXML
	private void onBtnStateClicked() {
		try {
			// Create Dialog
			int selectedIndex = invoiceTable.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) {
				showDialogLayer.setVisible(true);
				Invoice invoice = invoiceTable.getItems().get(selectedIndex);
				showStateDialog(invoiceData, invoice);
				showDialogLayer.setVisible(false);
			}
		} catch (IOException e){
			LOGGER.warning(e.toString());
		}
	}
}
