package com.gepraegs.rechnungsAppFx.controllers;

import com.gepraegs.rechnungsAppFx.Customer;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class CustomersController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger( CustomersController.class.getName() );
	private final DbController dbController = DbController.getInstance();
	private final ObservableList<Customer> customerData = FXCollections.observableArrayList();

	private final JFXTreeTableColumn<Customer, String> colKdNr = new JFXTreeTableColumn<>("Nummer");
	private final JFXTreeTableColumn<Customer, String> colCompany = new JFXTreeTableColumn<>("Kunde	");
	private final JFXTreeTableColumn<Customer, String> colName1 = new JFXTreeTableColumn<>("Name 1");
	private final JFXTreeTableColumn<Customer, String> colName2 = new JFXTreeTableColumn<>("Name 2");
	private final JFXTreeTableColumn<Customer, String> colStreet = new JFXTreeTableColumn<>("Strasse");
	private final JFXTreeTableColumn<Customer, String> colPlz = new JFXTreeTableColumn<>("PLZ");
	private final JFXTreeTableColumn<Customer, String> colLocation = new JFXTreeTableColumn<>("Ort");
	private final JFXTreeTableColumn<Customer, String> colCountry = new JFXTreeTableColumn<>("Land");
	private final JFXTreeTableColumn<Customer, String> colPhone = new JFXTreeTableColumn<>("Telefon");
	private final JFXTreeTableColumn<Customer, String> colHandy = new JFXTreeTableColumn<>("Handy");
	private final JFXTreeTableColumn<Customer, String> colFax = new JFXTreeTableColumn<>("Fax");
	private final JFXTreeTableColumn<Customer, String> colEmail = new JFXTreeTableColumn<>("E-Mail");
	private final JFXTreeTableColumn<Customer, String> colWebsite  = new JFXTreeTableColumn<>("Website");
	private final JFXTreeTableColumn<Customer, String> colDiscount = new JFXTreeTableColumn<>("Rabatt");
	private final JFXTreeTableColumn<Customer, String> colAccountBalance = new JFXTreeTableColumn<>("Kontostand");

	@FXML private JFXTreeTableView<Customer> customerTable;

	@Override
	public void initialize( URL location, ResourceBundle resources ) {

		initializeColumns();
		loadCustomersData();

		//clear selection if table has no focus
		customerTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				customerTable.getSelectionModel().clearSelection();
			}
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

		customerTable.setColumnResizePolicy( TreeTableView.CONSTRAINED_RESIZE_POLICY );
		colKdNr.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );
		colCompany.setMaxWidth( 1f * Integer.MAX_VALUE * 70 );
		colAccountBalance.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );

		colKdNr.setPrefWidth(150);
		colKdNr.setCellValueFactory(param -> param.getValue().getValue().getKdNr());
		colKdNr.setStyle("-fx-alignment: CENTER-LEFT;");

		colCompany.setPrefWidth(700);
		colCompany.setCellValueFactory(param -> param.getValue().getValue().getCompany());

		colName1.setPrefWidth(150);
		colName1.setCellValueFactory(param -> param.getValue().getValue().getName1());

		colName2.setPrefWidth(150);
		colName2.setCellValueFactory(param -> param.getValue().getValue().getName2());

		colStreet.setPrefWidth(150);
		colStreet.setCellValueFactory(param -> param.getValue().getValue().getStreet());

		colPlz.setPrefWidth(150);
		colPlz.setCellValueFactory(param -> param.getValue().getValue().getPlz());
		colPlz.setStyle("-fx-alignment: CENTER;");

		colLocation.setPrefWidth(150);
		colLocation.setCellValueFactory(param -> param.getValue().getValue().getLocation());

		colCountry.setPrefWidth(150);
		colCountry.setCellValueFactory(param -> param.getValue().getValue().getCountry());

		colPhone.setPrefWidth(150);
		colPhone.setCellValueFactory(param -> param.getValue().getValue().getPhone());

		colHandy.setPrefWidth(150);
		colHandy.setCellValueFactory(param -> param.getValue().getValue().getHandy());

		colFax.setPrefWidth(150);
		colFax.setCellValueFactory(param -> param.getValue().getValue().getFax());

		colEmail.setPrefWidth(150);
		colEmail.setCellValueFactory(param -> param.getValue().getValue().getEmail());

		colWebsite.setPrefWidth(150);
		colWebsite.setCellValueFactory(param -> param.getValue().getValue().getWebsite());

		colDiscount.setPrefWidth(150);
		colDiscount.setStyle("-fx-alignment: CENTER-RIGHT;");
		colDiscount.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> param) -> {
			NumberFormat format = NumberFormat.getPercentInstance(Locale.GERMANY);
			String percent = format.format(param.getValue().getValue().getDiscount());
			return new ReadOnlyStringWrapper(percent);
		});

		colAccountBalance.setPrefWidth(150);
		colAccountBalance.setStyle("-fx-alignment: CENTER-LEFT;");
		colAccountBalance.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> param) -> {
			NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
			String currency = format.format(param.getValue().getValue().getAccountBalance());
		 	return new ReadOnlyStringWrapper(currency);
		});

		customerTable.getColumns().add(colKdNr);
		customerTable.getColumns().add(colCompany);
//		customerTable.getColumns().add(colName1);
//		customerTable.getColumns().add(colName2);
//		customerTable.getColumns().add(colStreet);
//		customerTable.getColumns().add(colPlz);
//		customerTable.getColumns().add(colLocation);
//		customerTable.getColumns().add(colCountry);
//		customerTable.getColumns().add(colPhone);
//		customerTable.getColumns().add(colHandy);
//		customerTable.getColumns().add(colFax);
//		customerTable.getColumns().add(colEmail);
//		customerTable.getColumns().add(colWebsite);
//		customerTable.getColumns().add(colDiscount);
		customerTable.getColumns().add(colAccountBalance);

		final TreeItem<Customer> root = new RecursiveTreeItem<>(customerData, RecursiveTreeObject::getChildren);
		customerTable.setRoot(root);
		customerTable.setShowRoot(false);
	}
//
//	public class PercentCell extends TreeTableCell<Customer, String> {
//
//		PercentCell() {
//		}
//
//		@Override
//		protected void updateItem(String t, boolean empty) {
//			super.updateItem(t, empty);
//			if (!empty) {
//
//			}
//		}
//	}
}
