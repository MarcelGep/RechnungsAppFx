package com.gepraegs.rechnungsAppFx.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

import com.gepraegs.rechnungsAppFx.*;

import static javafx.scene.control.cell.ComboBoxTableCell.*;

public class GuestListViewController implements Initializable {

    private DbController dbController = DbController.getInstance();
//    private ObservableList<Guest> guestData = FXCollections.observableArrayList();
//    private ObservableList<Guest> removeGuestData = FXCollections.observableArrayList();

    private static final Logger LOGGER = Logger.getLogger(DbController.class.getName());

    @FXML private TextField tfSearchGuest;
//    @FXML private TableView<Guest> tableGuests;

//    @FXML private TableColumn<Guest, Boolean> selectColumn;
//    @FXML private TableColumn<Guest, TrashButton> deleteColumn;
//    @FXML private TableColumn<Guest, Integer> idColumn;
//    @FXML private TableColumn<Guest, String> firstNameColumn;
//    @FXML private TableColumn<Guest, String> lastNameColumn;
//    @FXML private TableColumn<Guest, Integer> ageColumn;
//    @FXML private TableColumn<Guest, GuestStatus> statusColumn;
//    @FXML private TableColumn<Guest, String> streetColumn;
//    @FXML private TableColumn<Guest, Integer> plzColumn;
//    @FXML private TableColumn<Guest, String> ortColumn;
//    @FXML private TableColumn<Guest, String> phoneColumn;
//    @FXML private TableColumn<Guest, String> handyColumn;
//    @FXML private TableColumn<Guest, String> emailColumn;
//    @FXML private TableColumn<Guest, String> commentsColumn;
//    @FXML private TableColumn<Guest, Boolean> inviteColumn;

    @FXML private JFXCheckBox cbOpen;
    @FXML private JFXCheckBox cbInvite;
    @FXML private JFXCheckBox cbComming;
    @FXML private JFXCheckBox cbCancel;
    @FXML private JFXCheckBox chbStreet;

    @FXML private Button btnRemoveSelection;
    @FXML private Button btnDeleteSelection;
    @FXML private Button btnClearSearch;

    @FXML private JFXTextField tfTestGuestsCount;

    @FXML private Label lbInviteGuests;

    private JFXCheckBox cbSelectAll = new JFXCheckBox();

    private static class TrashButton extends Button
    {
        private TrashButton()
        {
            ImageView iv;

            iv = new ImageView(getClass().getResource("/icons/trash.png").toString());
            iv.setFitHeight(17);
            iv.setFitWidth(17);
            iv.setOpacity(0.6);
            this.setGraphic(iv);
            this.setStyle("-fx-background-color: transparent;");
        }
    }

    public static class DeleteButtonTableCell<S> extends TableCell<S, TrashButton>
    {
        private final TrashButton deleteButton;

        private DeleteButtonTableCell(Function< S, S> function)
        {
            this.deleteButton = new TrashButton();
            this.deleteButton.setOnAction((ActionEvent e) -> function.apply(getCurrentItem()));
            this.deleteButton.setOnMouseEntered(mouseEvent -> deleteButton.setCursor(Cursor.HAND));
            this.deleteButton.setMaxWidth(Double.MAX_VALUE);
            this.deleteButton.setAlignment(Pos.CENTER);
        }

        private S getCurrentItem()
        {
            return getTableView().getItems().get(getIndex());
        }

        private static <S> Callback<TableColumn<S, TrashButton>, TableCell<S, TrashButton>> forTableColumn(Function< S, S> function)
        {
            return param -> new DeleteButtonTableCell<>(function);
        }

        @Override
        public void updateItem(TrashButton item, boolean empty)
        {
            super.updateItem(item, empty);

            if (empty)
            {
                setGraphic(null);
            }
            else
            {
                setGraphic(deleteButton);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set guestData to the table view
//        tableGuests.setItems(guestData);

        //init columns
//        showMainColumns(true);
//        showCommentsColumn(false);
//        showAddressColumns(false);
//        showContactColumns(false);

//        updateInviteGuests();

        //set up the columns in the table
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
//        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
//        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
//        streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
//        plzColumn.setCellValueFactory(new PropertyValueFactory<>("plz"));
//        ortColumn.setCellValueFactory(new PropertyValueFactory<>("ort"));
//        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
//        handyColumn.setCellValueFactory(new PropertyValueFactory<>("handy"));
//        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
//        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));

//        deleteColumn.setSortable(false);
//        deleteColumn.setResizable(false);
//        deleteColumn.setCellFactory(DeleteButtonTableCell.forTableColumn((Guest guest) ->
//        {
//            if (deleteGuest(guest))
//            {
//                if (removeGuestData.contains(guest))
//                {
//                    removeGuestData.remove(guest);
//                }
//            }
//
//            return guest;
//        }));

        cbSelectAll.setOnAction(event ->
        {
            boolean isCbSelected = cbSelectAll.isSelected();
            LOGGER.info(String.format("All guests %s!", isCbSelected ? "selected" : "unselected"));
//            handleSelectAllCheckbox(isCbSelected);
        });

//        inviteColumn.setSortable(false);
//        inviteColumn.setResizable(false);
//        inviteColumn.setCellValueFactory(p -> p.getValue().inviteProperty());
//        inviteColumn.setCellFactory(p -> {
//            JFXCheckBox checkBox = new JFXCheckBox();
//
//            TableCell<Guest, Boolean> cell = new TableCell<>() {
//                @Override
//                public void updateItem(Boolean item, boolean empty) {
//                    if (empty) {
//                        setGraphic(null);
//                    } else {
//                        checkBox.setSelected(item);
//                        setGraphic(checkBox);
//                    }
//                }
//            };
//
//            checkBox.setOnAction(event ->
//            {
//                Guest guest = cell.getTableRow().getItem();
//
//                if (guest != null)
//                {
//                    guest.setInvite(!guest.isInvite());
//                    dbController.setInvite(guest);
//                    updateInviteGuests();
//                }
//            });
//
//            return cell ;
//        });

//        selectColumn.setGraphic(cbSelectAll);
//        selectColumn.setSortable(false);
//        selectColumn.setResizable(false);
//        selectColumn.setCellValueFactory(p -> p.getValue().selectedProperty());
//        selectColumn.setCellFactory(p -> {
//            JFXCheckBox checkBox = new JFXCheckBox();
//
//            TableCell<Guest, Boolean> cell = new TableCell<>() {
//                @Override
//                public void updateItem(Boolean item, boolean empty) {
//                    if (empty) {
//                        setGraphic(null);
//                    } else {
//                        checkBox.setSelected(item);
//                        setGraphic(checkBox);
//                        if (item) {
//                            cbSelectAll.setSelected(true);
//                        }
//                    }
//                }
//            };

//            checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) ->
//            {
//                Guest guest = cell.getTableRow().getItem();
//
//                if (guest != null)
//                {
//                    guest.setSelected(isSelected);
//
//                    if (isSelected)
//                    {
//                        if (!removeGuestData.contains(guest))
//                        {
//                            removeGuestData.add(guest);
//                        }
//                    }
//                    else
//                    {
//                        if (removeGuestData.contains(guest))
//                        {
//                            removeGuestData.remove(guest);
//                        }
//                    }
//
//                    if (removeGuestData.size() == 0)
//                    {
//                        showEditListButtons(false);
//                        cbSelectAll.setSelected(false);
//                    }
//
//                    if (removeGuestData.size() == 1)
//                    {
//                        showEditListButtons(true);
//                    }
//
//                    LOGGER.fine("\"" + guest.getFirstName() + " " + guest.getLastName() + "\"" + " selected: " + isSelected);
//                }
//            });
//
//            return cell ;
//        });

//        statusColumn.setCellValueFactory(param ->
//        {
//            Guest guest = param.getValue();
//            GuestStatus guestStatus = GuestStatus.getByCode(guest.getStatus());
//            return new SimpleObjectProperty<>(guestStatus);
//        });
//
//        ObservableList<GuestStatus> statusList = FXCollections.observableArrayList(GuestStatus.values());
//        statusColumn.setCellFactory(forTableColumn(statusList));

//        // After user edit on cell, update to Model.
//        statusColumn.setOnEditCommit(event ->
//        {
//            Guest guest = event.getRowValue();
//            GuestStatus newStatus = event.getNewValue();
//            guest.setStatus(newStatus.getCode());
//
//            dbController.editGuest(guest);
//
//            updateGuestDataFromDb();
//        });
//
//        //read all existing guests from database
//        loadGuests();
//
//        //create column selection context menu
//        GuestListContextMenuHelper contextMenu = new GuestListContextMenuHelper();
//        contextMenu.initialize(tableGuests);
////        tableGuests.setOnContextMenuRequested(event -> {
////            createColumnContextMenu().show(tableGuests, event.getScreenX(), event.getScreenY());
////        });
//
//        tableGuests.setRowFactory(tv -> {
//            TableRow<Guest> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1)
//                {
//                    int col = tableGuests.getSelectionModel().getSelectedCells().get(0).getColumn();
//                    if (col != -1)
//                    {
//                        String tableTitel = tableGuests.getSelectionModel().getSelectedCells().get(0).getTableColumn().getText();
//                        if (tableTitel.equals("Status") ||
//                            tableTitel.equals("Einladung") ||
//                            col < 2)
//                        {
//                            return;
//                        }
//                    }
//
//                    editGuest();
//                }
//            });
//
//            return row;
//        });
//
//        //configure guest filter
//        setupGuestFilters();
//
//        //sort table by lastname ASC
//        lastNameColumn.setSortType(TableColumn.SortType.ASCENDING);
//        firstNameColumn.setSortType(TableColumn.SortType.ASCENDING);
//        tableGuests.getSortOrder().add(lastNameColumn);
//        tableGuests.getSortOrder().add(firstNameColumn);
//
//        //set guest filter
//        tfSearchGuest.setText(getGuestFilterName());
//        cbOpen.setSelected(isGuestFilterOpen());
//        cbInvite.setSelected(isGuestFilterOpen());
//        cbComming.setSelected(isGuestFilterComming());
//        cbCancel.setSelected(isGuestFilterCancel());
//
//        tableGuests.getSelectionModel().clearSelection();
    }

//    @FXML
//    private void onDeleteGuestsBtnClicked()
//    {
//        String text = "Auswahl löschen?";
//
//        LOGGER.warning("Delete count of Guests: " + removeGuestData.size());
//
//        if (showDeleteGuestDialog(text)) {
//            if (!removeGuestData.isEmpty()) {
//                for (Guest g : removeGuestData) {
//                    if (guestData.contains(g)) {
//                        guestData.remove(g);
//                        deleteGuestFromDb(g);
//                    }
//                }
//
//                //Update guest lists & guest counter
//                removeGuestData.clear();
//                tableGuests.refresh();
//
//                //Update button visible states
//                showEditListButtons(false);
//            }
//        }
//    }

//    @FXML
//    private void onRemoveSelectionBtnClicked()
//    {
//        for (Guest g : guestData)
//        {
//            g.setSelected(false);
//        }
//
//        cbSelectAll.setSelected(false);
//    }

    @FXML
    private void onClearSearchButtonClicked()
    {
        tfSearchGuest.clear();
        btnClearSearch.setVisible(false);
    }

    @FXML
    private void onAddGuestBtnClicked()
    {
//        addGuest();
    }

    

//    private boolean deleteGuest(Guest guestToDelete)
//    {
//        String guestName = guestToDelete.getFirstName() + " " + guestToDelete.getLastName();
//
//        if (showDeleteGuestDialog("\"" + guestName + "\"" + " löschen?"))
//        {
//            //delete guest from list
//            guestData.remove(guestToDelete);
//
//            //delete guest from database
//            deleteGuestFromDb(guestToDelete);
//
//            return true;
//        }
//
//        return false;
//    }
//
//    private void deleteGuestFromDb(Guest guestToDelete)
//    {
//        dbController.deleteGuest(guestToDelete);
//
//        updateGuestDataFromDb();
//    }
//
//    private void setupGuestFilters()
//    {
//        FilteredList<Guest> filteredData = new FilteredList<>(guestData, e -> true);
//
//        // set up searching for guests
//        cbComming.selectedProperty().addListener((observable, oldValue, newValue) ->
//        {
//            filteredData.setPredicate((Predicate<? super Guest>) guest->
//            {
//                String searchStr = tfSearchGuest.getText().toLowerCase();
//                boolean foundGuest = (guest.getFirstName().toLowerCase().contains(searchStr) ||
//                                      guest.getLastName().toLowerCase().contains(searchStr));
//
//                if(newValue == null) {
//                    return true;
//                }
//
//                if ((guest.getStatus().equals(GuestStatus.DECLINE.getCode()) && !cbCancel.isSelected()) ||
//                    (guest.getStatus().equals(GuestStatus.INVITE.getCode()) && !cbInvite.isSelected()) ||
//                    (guest.getStatus().equals(GuestStatus.OPEN.getCode()) && !cbOpen.isSelected()))
//                {
//                    return false;
//                }
//
//                if (!newValue)
//                {
//                    if (guest.getStatus().equals(GuestStatus.CONFIRM.getCode()) && removeGuestData.contains(guest))
//                    {
//                        removeGuestData.remove(guest);
//                    }
//
//                    return foundGuest && !guest.getStatus().equals(GuestStatus.CONFIRM.getCode());
//                }
//                else
//                {
//                    if (guest.getStatus().equals(GuestStatus.CONFIRM.getCode())
//                        && guest.isSelected()
//                        && foundGuest
//                        && !removeGuestData.contains(guest))
//                    {
//                        removeGuestData.add(guest);
//                    }
//
//                    return foundGuest;
//                }
//
//            });
//
//            SortedList<Guest> sortedData = new SortedList<>(filteredData);
//            sortedData.comparatorProperty().bind(tableGuests.comparatorProperty());
//            tableGuests.setItems(sortedData);
//
//            //set edit guest list buttons invisible if no guest are shown
//            if (sortedData.isEmpty() && removeGuestData.isEmpty())
//            {
//                showEditListButtons(false);
//            }
//            else if (!sortedData.isEmpty() && !removeGuestData.isEmpty())
//            {
//                showEditListButtons(true);
//            }
//
//            //save status to helper class
//            setGuestFilterComming(newValue);
//        });
//
//        cbCancel.selectedProperty().addListener((observable, oldValue, newValue) ->
//        {
//            filteredData.setPredicate((Predicate<? super Guest>) guest->
//            {
//                String searchStr = tfSearchGuest.getText().toLowerCase();
//                boolean foundGuest = guest.getFirstName().toLowerCase().contains(searchStr) ||
//                        guest.getLastName().toLowerCase().contains(searchStr);
//
//                if(newValue == null) {
//                    return true;
//                }
//
//                if ((guest.getStatus().equals(GuestStatus.OPEN.getCode()) && !cbOpen.isSelected()) ||
//                    (guest.getStatus().equals(GuestStatus.INVITE.getCode()) && !cbInvite.isSelected()) ||
//                    (guest.getStatus().equals(GuestStatus.CONFIRM.getCode()) && !cbComming.isSelected()))
//                {
//                    return false;
//                }
//
//                if (!newValue)
//                {
//                    if (guest.getStatus().equals(GuestStatus.DECLINE.getCode()) && removeGuestData.contains(guest))
//                    {
//                        removeGuestData.remove(guest);
//                    }
//
//                    return foundGuest && !guest.getStatus().equals(GuestStatus.DECLINE.getCode());
//                }
//                else
//                {
//                    if (guest.getStatus().equals(GuestStatus.DECLINE.getCode())
//                        && guest.isSelected()
//                        && foundGuest
//                        && !removeGuestData.contains(guest))
//                    {
//                        removeGuestData.add(guest);
//                    }
//
//                    return foundGuest;
//                }
//            });
//
//            SortedList<Guest> sortedData = new SortedList<>(filteredData);
//            sortedData.comparatorProperty().bind(tableGuests.comparatorProperty());
//            tableGuests.setItems(sortedData);
//
//            //set edit guest list buttons invisible if no guest are shown
//            if (sortedData.isEmpty() && removeGuestData.isEmpty())
//            {
//                showEditListButtons(false);
//            }
//            else if (!sortedData.isEmpty() && !removeGuestData.isEmpty())
//            {
//                showEditListButtons(true);
//            }
//            //save status to helper class
//            setGuestFilterCancel(newValue);
//        });
//
//        cbOpen.selectedProperty().addListener((observable, oldValue, newValue) ->
//        {
//            filteredData.setPredicate((Predicate<? super Guest>) guest->
//            {
//                String searchStr = tfSearchGuest.getText().toLowerCase();
//                boolean foundGuest = guest.getFirstName().toLowerCase().contains(searchStr) ||
//                        guest.getLastName().toLowerCase().contains(searchStr);
//
//                if (newValue == null) {
//                    return true;
//                }
//
//                if ((guest.getStatus().equals(GuestStatus.DECLINE.getCode()) && !cbCancel.isSelected()) ||
//                        (guest.getStatus().equals(GuestStatus.INVITE.getCode()) && !cbInvite.isSelected()) ||
//                        (guest.getStatus().equals(GuestStatus.CONFIRM.getCode()) && !cbComming.isSelected())) {
//                    return false;
//                }
//
//                if (!newValue) {
//                    if (guest.getStatus().equals(GuestStatus.OPEN.getCode()) && removeGuestData.contains(guest)) {
//                        removeGuestData.remove(guest);
//                    }
//
//                    return foundGuest && !guest.getStatus().equals(GuestStatus.OPEN.getCode());
//                }
//                else
//                {
//                    if (guest.getStatus().equals(GuestStatus.OPEN.getCode())
//                        && guest.isSelected()
//                        && foundGuest
//                        && !removeGuestData.contains(guest))
//                    {
//                        removeGuestData.add(guest);
//                    }
//
//                    return foundGuest;
//                }
//            });
//
//            SortedList<Guest> sortedData = new SortedList<>(filteredData);
//            sortedData.comparatorProperty().bind(tableGuests.comparatorProperty());
//            tableGuests.setItems(sortedData);
//
//            //set edit guest list buttons invisible if no guest are shown
//            if (sortedData.isEmpty() && removeGuestData.isEmpty())
//            {
//                showEditListButtons(false);
//            }
//            else if (!sortedData.isEmpty() && !removeGuestData.isEmpty())
//            {
//                showEditListButtons(true);
//            }
//
//            //save status to helper class
//            setGuestFilterOpen(newValue);
//        });
//
//        cbInvite.selectedProperty().addListener((observable, oldValue, newValue) ->
//        {
//            filteredData.setPredicate((Predicate<? super Guest>) guest->
//            {
//                String searchStr = tfSearchGuest.getText().toLowerCase();
//                boolean foundGuest = guest.getFirstName().toLowerCase().contains(searchStr) ||
//                        guest.getLastName().toLowerCase().contains(searchStr);
//
//                if (newValue == null) {
//                    return true;
//                }
//
//                if ((guest.getStatus().equals(GuestStatus.DECLINE.getCode()) && !cbCancel.isSelected()) ||
//                    (guest.getStatus().equals(GuestStatus.OPEN.getCode()) && !cbOpen.isSelected()) ||
//                    (guest.getStatus().equals(GuestStatus.CONFIRM.getCode()) && !cbComming.isSelected())) {
//                    return false;
//                }
//
//                if (!newValue) {
//                    if (guest.getStatus().equals(GuestStatus.INVITE.getCode()) && removeGuestData.contains(guest)) {
//                        removeGuestData.remove(guest);
//                    }
//
//                    return foundGuest && !guest.getStatus().equals(GuestStatus.INVITE.getCode());
//                }
//                else
//                {
//                    if (guest.getStatus().equals(GuestStatus.INVITE.getCode())
//                            && guest.isSelected()
//                            && foundGuest
//                            && !removeGuestData.contains(guest))
//                    {
//                        removeGuestData.add(guest);
//                    }
//
//                    return foundGuest;
//                }
//            });
//
//            SortedList<Guest> sortedData = new SortedList<>(filteredData);
//            sortedData.comparatorProperty().bind(tableGuests.comparatorProperty());
//            tableGuests.setItems(sortedData);
//
//            //set edit guest list buttons invisible if no guest are shown
//            if (sortedData.isEmpty() && removeGuestData.isEmpty())
//            {
//                showEditListButtons(false);
//            }
//            else if (!sortedData.isEmpty() && !removeGuestData.isEmpty())
//            {
//                showEditListButtons(true);
//            }
//
//            //save status to helper class
//            setGuestFilterOpen(newValue);
//        });
//
//        tfSearchGuest.textProperty().addListener((observableValue, oldValue, newValue) ->
//        {
//            filteredData.setPredicate((Predicate<? super Guest>) guest ->
//            {
//                //save status to helper class
//                setGuestFilterName(newValue);
//
//                if (newValue == null) {
//                    return true;
//                }
//
//                btnClearSearch.setVisible(!newValue.isEmpty());
//
//                String lowerCaseFilter = newValue.toLowerCase();
//
//                if ((guest.getStatus().equals(GuestStatus.DECLINE.getCode()) && !cbCancel.isSelected()) ||
//                    (guest.getStatus().equals(GuestStatus.CONFIRM.getCode()) && !cbComming.isSelected()) ||
//                    (guest.getStatus().equals(GuestStatus.INVITE.getCode()) && !cbInvite.isSelected()) ||
//                    (guest.getStatus().equals(GuestStatus.OPEN.getCode()) && !cbOpen.isSelected())) {
//                    return false;
//                }
//
//                String firstLastName = guest.getFirstName().toLowerCase() + " " + guest.getLastName().toLowerCase();
//                String lastFirstName = guest.getLastName().toLowerCase() + " " + guest.getFirstName().toLowerCase();
//
//                if (firstLastName.contains(lowerCaseFilter) || lastFirstName.contains(lowerCaseFilter) )
//                {
//                    if ( guest.isSelected() && !removeGuestData.contains(guest))
//                    {
//                        removeGuestData.add(guest);
//                    }
//                    return true;
//                }
//
//                if (removeGuestData.contains(guest))
//                {
//                    removeGuestData.remove(guest);
//                }
//
//                return false;
//            });
//
//            SortedList<Guest> sortedData = new SortedList<>(filteredData);
//            sortedData.comparatorProperty().bind(tableGuests.comparatorProperty());
//            tableGuests.setItems(sortedData);
//
//            //set edit guest list buttons invisible if no guest are shown
//            if (sortedData.isEmpty() && removeGuestData.isEmpty())
//            {
//                showEditListButtons(false);
//            }
//            else if (!sortedData.isEmpty() && !removeGuestData.isEmpty())
//            {
//                showEditListButtons(true);
//            }
//        });
//    }
//
//    private void addGuest()
//    {
//        try {
//            // Clear old selection on the guest list.
//            tableGuests.getSelectionModel().clearSelection();
//            onRemoveSelectionBtnClicked();
//
//            // Create Dialog
//            showGuestDialog(guestData, null);
//
//            tableGuests.sort();
//
//            int lastGuestId = guestData.get(guestData.size() - 1).getId();
//
//            for (int i = 0; i < tableGuests.getItems().size(); i++) {
//                if (tableGuests.getItems().get(i).getId() == lastGuestId) {
//                    scrollToRow(i);
//                    break;
//                }
//            }
//
//        } catch (IOException e){
//            LOGGER.info(e.toString());
//        }
//    }
//
//    private void editGuest()
//    {
//        try {
//            // Create Dialog
//            Guest selectedGuest = tableGuests.getSelectionModel().getSelectedItem();
//
//            if (selectedGuest != null )
//            {
//                showGuestDialog(guestData, selectedGuest);
//
//                tableGuests.sort();
//            }
//        } catch (IOException e){
//            LOGGER.info(e.toString());
//        }
//    }
//
//    private void loadGuests()
//    {
//    	if (tableGuests.getItems().size() > 0 )
//    	{
//        	tableGuests.getItems().clear();
//    	}
//
//    	//read guests from database
//        List<Guest> guestList = dbController.readGuests();
//
//    	//write guests to list
//        guestData.addAll(guestList);
//
//        tableGuests.getSelectionModel().clearSelection();
//    }
//
//    private void showEditListButtons(Boolean isVisible)
//    {
//        btnRemoveSelection.setVisible(isVisible);
//        btnDeleteSelection.setVisible(isVisible);
//    }
//
//    private void scrollToRow(int row)
//    {
//        tableGuests.requestFocus();
//        tableGuests.getSelectionModel().select(row);
//        tableGuests.getFocusModel().focus(row);
//        tableGuests.scrollTo(row);
//    }
//
//    private void updateInviteGuests()
//    {
//        lbInviteGuests.setText(dbController.readInviteCount());
//    }
}