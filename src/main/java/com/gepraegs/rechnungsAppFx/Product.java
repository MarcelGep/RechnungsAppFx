package com.gepraegs.rechnungsAppFx;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product extends RecursiveTreeObject<Product> {

    private StringProperty artNr;
    private StringProperty name;
    private StringProperty unit;
    private double priceExcl;
    private double priceIncl;
    private double ust;

    public Product()
    {
        this.artNr = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.unit = new SimpleStringProperty("");
        this.priceExcl = 0.0;
        this.priceIncl = 0.0;
        this.ust = 0.0;
    }

    public Product(String artNr, String name, String unit, double ust, double priceExcl) {
        this.artNr = new SimpleStringProperty(artNr);
        this.name = new SimpleStringProperty(name);
        this.unit = new SimpleStringProperty(unit);
        this.ust = ust;
        this.priceExcl = priceExcl;
        this.priceIncl = ((100 + this.ust) / 100) * this.priceExcl;
    }

    @Override
    public String toString() {
        return this.getName();
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

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
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

    public double getPriceExcl() {
        return priceExcl;
    }

    public void setPriceExcl(double price) {
        this.priceExcl = price;
    }

    public double getPriceIncl() {
        return priceIncl;
    }

    public void setPriceIncl(double price) {
        this.priceIncl = price;
    }

    public double getUst() {
        return ust;
    }

    public void setUst(double ust) {
        this.ust = ust;
    }
}
