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
    private StringProperty kdNr;
    private StringProperty createDate;
    private StringProperty dueDate;
    private StringProperty payedDate;
    private BooleanProperty state;
    private double totalPrice;
    private double ust;

    public Invoice()
    {
        this.reNr = new SimpleStringProperty("");
        this.kdNr = new SimpleStringProperty("");
        this.createDate = new SimpleStringProperty("");
        this.dueDate = new SimpleStringProperty("");
        this.payedDate = new SimpleStringProperty("");
        this.state = new SimpleBooleanProperty(false);
        this.totalPrice = 0.0;
        this.ust = 0.0;
    }

    public Invoice(String reNr, String kdNr, String createDate, String dueDate, String payedDate, boolean state, double totalPrice, double ust) {
        this.reNr = new SimpleStringProperty(reNr);
        this.kdNr = new SimpleStringProperty(kdNr);
        this.createDate = new SimpleStringProperty(createDate);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.payedDate = new SimpleStringProperty(payedDate);
        this.state = new SimpleBooleanProperty(state);
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

    public String getKdNr() {
        return kdNr.get();
    }

    public StringProperty kdNrProperty() {
        return kdNr;
    }

    public void setKdNr(String kdNr) {
        this.kdNr.set(kdNr);
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

    public boolean isState() {
        return state.get();
    }

    public BooleanProperty stateProperty() {
        return state;
    }

    public void setState(boolean state) {
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
}
