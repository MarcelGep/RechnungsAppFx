package com.gepraegs.rechnungsAppFx;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Position extends RecursiveTreeObject<Position> {

    private StringProperty artNr;
    private StringProperty rgNr;
    private StringProperty description;
    private StringProperty unit;
    private StringProperty createdDate;
    private double amount;
    private double priceExcl;
    private double priceIncl;
    private double ust;

    public Position()
    {
        this.artNr = new SimpleStringProperty("");
        this.rgNr = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.unit = new SimpleStringProperty("");
        this.createdDate = new SimpleStringProperty("");
        this.amount = 0;
        this.priceExcl = 0.0;
        this.priceIncl = 0.0;
        this.ust = 0.0;
    }

    public Position(String artNr, String rgNr, String description, String unit, String createdDate, int amount, double priceExcl, double priceIncl, double ust) {
        this.artNr = new SimpleStringProperty(artNr);
        this.rgNr = new SimpleStringProperty(rgNr);
        this.description = new SimpleStringProperty(description);
        this.unit = new SimpleStringProperty(unit);
        this.createdDate = new SimpleStringProperty(createdDate);
        this.amount = amount;
        this.priceExcl = priceExcl;
        this.priceIncl = priceIncl;
        this.ust = ust;
    }

    public String getArtNr() {
        return artNr.get();
    }

    public StringProperty artNrProperty() {
        return artNr;
    }

    public void setArtNr(String artNr) {
        this.artNr.set(artNr);
    }

    public String getRgNr() {
        return rgNr.get();
    }

    public StringProperty rgNrProperty() {
        return rgNr;
    }

    public void setRgNr(String rgNr) {
        this.rgNr.set(rgNr);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getUnit() {
        return unit.get();
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public String getCreatedDate() {
        return createdDate.get();
    }

    public StringProperty createdDateProperty() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate.set(createdDate);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPriceExcl() {
        return priceExcl;
    }

    public void setPriceExcl(double priceExcl) {
        this.priceExcl = priceExcl;
    }

    public double getPriceIncl() {
        return priceIncl;
    }

    public void setPriceIncl(double priceIncl) {
        this.priceIncl = priceIncl;
    }

    public double getUst() {
        return ust;
    }

    public void setUst(double ust) {
        this.ust = ust;
    }
}
