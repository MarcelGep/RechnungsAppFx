package com.gepraegs.rechnungsAppFx.controllers;

import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.DoubleToCurrencyString;
import static com.gepraegs.rechnungsAppFx.helpers.FormatterHelper.DoubleToPercentageString;
import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showConfirmDialog;
import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showProductDialog;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.gepraegs.rechnungsAppFx.Product;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ProductsController implements Initializable {

	private static final Logger LOGGER = Logger.getLogger(ProductsController.class.getName());

	private final DbController dbController = DbController.getInstance();

	private final ObservableList<Product> productData = FXCollections.observableArrayList();

	private final TableColumn<Product, String> colArtNr = new TableColumn<>("NUMMER");
	private final TableColumn<Product, String> colProductName = new TableColumn<>("PRODUKT");
	private final TableColumn<Product, String> colPriceExcl = new TableColumn<>("PREIS EXKL. UST.");
	private final TableColumn<Product, String> colUst = new TableColumn<>("UST.");
	private final TableColumn<Product, String> colPriceIncl = new TableColumn<>("PREIS INKL. UST.");

	@FXML
	private TableView<Product> productTable;

	@FXML
	private TextField tfSearchProduct;

	@FXML
	private Label lbProduct;
	@FXML
	private Label lbArtNr;
	@FXML
	private Label lbUnit;
	@FXML
	private Label lbPriceExcl;
	@FXML
	private Label lbUst;
	@FXML
	private Label lbPriceIncl;

	@FXML
	private VBox productDetailsFilled;
	@FXML
	private VBox productDetailsEmpty;

	@FXML
	private VBox showDialogLayer;

	@FXML
	private Button btnClearSearch;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeColumns();
		loadProductsData();
		setRowSelectionListener();
		setTableSortOrder();

		setupCustomerFilter();

		showDialogLayer.setVisible(false);
	}

	public void loadProductsData() {
		productData.clear();

		// read guests from database
		List<Product> products = dbController.readProducts();

		// write guests to data list
		if (products != null && !products.isEmpty()) {
			productData.addAll(products);
		} else {
			LOGGER.warning("No products data exist!");
		}

		// set productsData to productTable
		productTable.setItems(productData);

		clearTableSelection();
	}

	private void initializeColumns() {
		productTable.setPlaceholder(new Label("Keine Einträge vorhanden"));

		// set size of columns
		productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		colArtNr.setMaxWidth(1f * Integer.MAX_VALUE * 12);
		colProductName.setMaxWidth(1f * Integer.MAX_VALUE * 58);
		colPriceExcl.setMaxWidth(1f * Integer.MAX_VALUE * 12);
		colUst.setMaxWidth(1f * Integer.MAX_VALUE * 8);
		colPriceIncl.setMaxWidth(1f * Integer.MAX_VALUE * 12);

		// set cell value factory
		colArtNr.setCellValueFactory(param -> param.getValue().artNrProperty());
		colProductName.setCellValueFactory(param -> param.getValue().nameProperty());
		colPriceExcl
				.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> param) -> new ReadOnlyStringWrapper(
						DoubleToCurrencyString(param.getValue().getPriceExcl())));
		colUst.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> param) -> new ReadOnlyStringWrapper(
				DoubleToPercentageString(param.getValue().getUst())));
		colPriceIncl
				.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> param) -> new ReadOnlyStringWrapper(
						DoubleToCurrencyString(param.getValue().getPriceIncl())));

		// add columns to customer table
		productTable.getColumns().add(colArtNr);
		productTable.getColumns().add(colProductName);
		productTable.getColumns().add(colPriceExcl);
		productTable.getColumns().add(colUst);
		productTable.getColumns().add(colPriceIncl);

		productTable.setRowFactory(tv -> {
			TableRow<Product> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					onBtnEditProductClicked();
				}
			});
			return row;
		});
	}

	private void showProductInformations(Product product) {
		lbProduct.setText(validateExistingData(product.getName()));
		lbArtNr.setText(validateExistingData(product.getArtNr()));
		lbUnit.setText(validateExistingData(product.getUnit()));
		lbPriceExcl.setText(validateExistingData(DoubleToCurrencyString(product.getPriceExcl())));
		lbUst.setText(validateExistingData(DoubleToPercentageString(product.getUst())));
		lbPriceIncl.setText(validateExistingData(DoubleToCurrencyString(product.getPriceIncl())));

		showProductDetails(true);
	}

	private void clearCustomerDetails() {
		lbArtNr.setText("");
		lbPriceExcl.setText("");
		lbPriceIncl.setText("");
		lbUnit.setText("");
		lbUst.setText("");
		lbProduct.setText("");

		showProductDetails(false);
	}

	private void clearTableSelection() {
		productTable.getSelectionModel().clearSelection();
		clearCustomerDetails();
	}

	private void showProductDetails(boolean show) {
		productDetailsFilled.setVisible(show);
		productDetailsEmpty.setVisible(!show);
	}

	private String validateExistingData(String data) {
		return data == null || data.isEmpty() ? "---" : data;
	}

	private void scrollToRow(int row) {
		productTable.requestFocus();
		productTable.getSelectionModel().select(row);
		productTable.getFocusModel().focus(row);
		productTable.scrollTo(row);
	}

	private void setTableSortOrder() {
		colProductName.setSortType(TableColumn.SortType.ASCENDING);
		colArtNr.setSortType(TableColumn.SortType.ASCENDING);
		// productTable.getSortOrder().add(colProductName);
		productTable.getSortOrder().add(colArtNr);
	}

	private void setRowSelectionListener() {
		productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				showProductInformations(newSelection);
			}
		});
	}

	private void setupCustomerFilter() {
		FilteredList<Product> filteredData = new FilteredList<>(productData, e -> true);

		tfSearchProduct.textProperty().addListener((observableValue, oldValue, newValue) -> {
			filteredData.setPredicate((Predicate<? super Product>) product -> {
				if (newValue == null) {
					return true;
				}

				btnClearSearch.setVisible(!newValue.isEmpty());

				String lowerCaseFilter = newValue.toLowerCase();

				String productName = product.getName().toLowerCase();

				if (productName.contains(lowerCaseFilter)) {
					return true;
				}

				return false;
			});

			SortedList<Product> sortedData = new SortedList<>(filteredData);
			sortedData.comparatorProperty().bind(productTable.comparatorProperty());
			productTable.setItems(sortedData);
		});
	}

	@FXML
	private void onClearSearchButtonClicked() {
		tfSearchProduct.clear();
		btnClearSearch.setVisible(false);
	}

	@FXML
	private void onBtnCloseDetailsClicked() {
		productTable.getSelectionModel().clearSelection();
		System.out.println(productTable.getSelectionModel().getSelectedIndex());
		clearTableSelection();
	}

	@FXML
	private void onBtnNewProductClicked() {
		try {
			// Create Dialog
			showDialogLayer.setVisible(true);
			Product product = showProductDialog(productData, null, null);

			if (product != null) {
				productTable.sort();

				// scroll to last added product, select it and show detail informations
				int lastProductArtNr = dbController.readNextId("Products") - 1;
				for (int i = 0; i < productTable.getItems().size(); i++) {
					if (Integer.parseInt(productTable.getItems().get(i).getArtNr()) == lastProductArtNr) {
						scrollToRow(i);
						productTable.getSelectionModel().select(i);
						break;
					}
				}
			}

			showDialogLayer.setVisible(false);
		} catch (IOException e) {
			LOGGER.warning(e.toString());
		}
	}

	@FXML
	private void onBtnDeleteCustomerClicked() {
		try {
			String content = "Diese Aktion kann später nicht mehr rückgängig gemacht werden.\n\n" +
					"Möchtest du das Produkt \"" + productTable.getSelectionModel().getSelectedItem().getName()
					+ "\" löschen?";

			showDialogLayer.setVisible(true);

			if (showConfirmDialog(content, Arrays.asList("Löschen", "Abbrechen"))) {
				dbController.deleteProduct(productTable.getSelectionModel().getSelectedItem());
				productData.remove(productTable.getSelectionModel().getSelectedItem());
				clearTableSelection();
			}

			showDialogLayer.setVisible(false);
		} catch (IOException e) {
			LOGGER.warning(e.toString());
		}
	}

	@FXML
	private void onBtnEditProductClicked() {
		try {
			// Create Dialog
			int selectedIndex = productTable.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) {
				showDialogLayer.setVisible(true);
				Product product = productTable.getItems().get(selectedIndex);
				product = showProductDialog(productData, product, null);

				if (product != null) {
					showProductInformations(product);
					productTable.getSelectionModel().select(selectedIndex);
				}

				showDialogLayer.setVisible(false);
			}
		} catch (IOException e) {
			LOGGER.warning(e.toString());
		}
	}
}
