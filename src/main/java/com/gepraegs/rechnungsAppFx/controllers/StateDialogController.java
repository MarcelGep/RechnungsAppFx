package com.gepraegs.rechnungsAppFx.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;

import com.gepraegs.rechnungsAppFx.Invoice;
import com.gepraegs.rechnungsAppFx.InvoiceState;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class StateDialogController implements Initializable {

    @FXML
    private ComboBox<InvoiceState> cbNewState;
    @FXML
    private DatePicker dpPayedDate;

    @FXML
    private Label lbTitle;

    @FXML
    private GridPane gpState;

    private static final Logger LOGGER = Logger.getLogger(StateDialogController.class.getName());

    private final DbController dbController = DbController.getInstance();

    private ObservableList<Invoice> invoiceData = FXCollections.observableArrayList();
    private Invoice selectedInvoice;

    private Stage dialogStage = new Stage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setDialogTitle(String title) {
        lbTitle.setText(title);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSelectedInvoice(Invoice selectedInvoice) {
        this.selectedInvoice = selectedInvoice;

        if (InvoiceState.getByCode(selectedInvoice.getState()) == InvoiceState.CREATED ||
                InvoiceState.getByCode(selectedInvoice.getState()) == InvoiceState.DUE) {
            cbNewState.getItems().addAll(InvoiceState.PAYED);
            LocalDate createdDate = LocalDate.now();
            dpPayedDate.setValue(createdDate);
        } else {
            cbNewState.getItems().addAll(InvoiceState.UNPAYED);
            Set<Node> deleteNodes = new HashSet<>();
            for (Node node : gpState.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(node);
                int r = rowIndex == null ? 0 : rowIndex;
                if (r > 1) {
                    // decrement rows for rows after the deleted row
                    GridPane.setRowIndex(node, r - 1);
                } else if (r == 1) {
                    // collect matching rows for deletion
                    deleteNodes.add(node);
                }
            }
            gpState.getChildren().removeAll(deleteNodes);

        }

        cbNewState.getSelectionModel().selectFirst();
    }

    public void setInvoiceData(ObservableList<Invoice> invoiceData) {
        this.invoiceData = invoiceData;
    }

    @FXML
    public void handleSave() {
        if (selectedInvoice != null) {
            InvoiceState newState = cbNewState.getSelectionModel().getSelectedItem();

            if (newState == InvoiceState.PAYED) {
                selectedInvoice.setState(newState.getCode());
                selectedInvoice.setPayedDate(dpPayedDate.getValue().toString());
            } else if (newState == InvoiceState.UNPAYED) {
                String stateCode = InvoiceState.CREATED.getCode();

                if (selectedInvoice.getDueDate().compareTo(LocalDate.now().toString()) <= 0) {
                    stateCode = InvoiceState.DUE.getCode();
                }

                selectedInvoice.setState(stateCode);
                selectedInvoice.setPayedDate(null);
            }

            invoiceData.set(this.invoiceData.indexOf(selectedInvoice), selectedInvoice);
            dbController.editInvoice(selectedInvoice);
        }

        dialogStage.close();
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
}
