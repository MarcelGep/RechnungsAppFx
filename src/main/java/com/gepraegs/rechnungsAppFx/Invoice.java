package com.gepraegs.rechnungsAppFx;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Invoice extends RecursiveTreeObject<Invoice> {

    private StringProperty reNr;
    private Customer customer;
    private StringProperty createDate;
    private StringProperty dueDate;
    private StringProperty payedDate;
    private StringProperty deliveryDate;
    private StringProperty state;
    private int payCondition;
    private double totalPrice;
    private double ust;

    public Invoice()
    {
        this.reNr = new SimpleStringProperty("");
        this.customer = new Customer();
        this.createDate = new SimpleStringProperty("");
        this.dueDate = new SimpleStringProperty("");
        this.payedDate = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("CR");
        this.deliveryDate = new SimpleStringProperty("");
        this.payCondition = 0;
        this.totalPrice = 0.0;
        this.ust = 0.0;
    }

    public Invoice(String reNr, Customer customer, String createDate, String dueDate, String payedDate, String deliveryDate, String state, int payCondition, double totalPrice, double ust) {
        this.reNr = new SimpleStringProperty(reNr);
        this.customer = customer;
        this.createDate = new SimpleStringProperty(createDate);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.payedDate = new SimpleStringProperty(payedDate);
        this.deliveryDate = new SimpleStringProperty(deliveryDate);
        this.state = new SimpleStringProperty(state);
        this.payCondition = payCondition;
        this.totalPrice = totalPrice;
        this.ust = ust;
    }

    public String getReNr() {
        return reNr.get();
    }

    public StringProperty reNrProperty() {
        return reNr;
    }

    public void setReNr(String reNr) {
        this.reNr.set(reNr);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCreateDate() {
        return createDate.get();
    }

    public StringProperty createDateProperty() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate.set(createDate);
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate.set(dueDate);
    }

    public String getPayedDate() {
        return payedDate.get();
    }

    public StringProperty payedDateProperty() {
        return payedDate;
    }

    public void setPayedDate(String payedDate) {
        this.payedDate.set(payedDate);
    }

    public String getState() {
        return state.get();
    }

    public StringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getUst() {
        return ust;
    }

    public void setUst(double ust) {
        this.ust = ust;
    }

    public String getDeliveryDate() {
        return deliveryDate.get();
    }

    public StringProperty deliveryDateProperty() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate.set(deliveryDate);
    }

    public int getPayCondition() {
        return payCondition;
    }

    public void setPayCondition(int payCondition) {
        this.payCondition = payCondition;
    }
}
