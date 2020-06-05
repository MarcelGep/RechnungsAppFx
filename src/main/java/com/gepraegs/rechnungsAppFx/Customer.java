package com.gepraegs.rechnungsAppFx;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer extends RecursiveTreeObject<Customer> {

    private StringProperty kdNr;
    private StringProperty company;
    private StringProperty name1;
    private StringProperty name2;
    private StringProperty street;;
    private StringProperty plz;
    private StringProperty location;
    private StringProperty country;
    private StringProperty phone;
    private StringProperty handy;
    private StringProperty fax;
    private StringProperty email;
    private StringProperty website;
    private StringProperty informations;
    private double discount;
    private double openCosts;
    private double payedCosts;

    public Customer()
    {
        this.kdNr = new SimpleStringProperty("");
        this.company = new SimpleStringProperty("");
        this.name1 = new SimpleStringProperty("");
        this.name2 = new SimpleStringProperty("");
        this.street = new SimpleStringProperty("");
        this.plz = new SimpleStringProperty("");
        this.location = new SimpleStringProperty("");
        this.country = new SimpleStringProperty("");
        this.phone = new SimpleStringProperty("");
        this.handy = new SimpleStringProperty("");
        this.fax = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.website = new SimpleStringProperty("");
        this.informations = new SimpleStringProperty("");
        this.discount = 0.0;
        this.openCosts = 0.0;
        this.payedCosts = 0.0;
    }

    public Customer(String kdNr, String company, String name1, String name2, String street, String plz, String location, String country, String phone, String handy, String fax, String email, String website, String informations, double discount, double openCosts, double payedCosts) {
        this.kdNr = new SimpleStringProperty(kdNr);
        this.company = new SimpleStringProperty(company);
        this.name1 = new SimpleStringProperty(name1);
        this.name2 = new SimpleStringProperty(name2);
        this.street = new SimpleStringProperty(street);
        this.plz = new SimpleStringProperty(plz);
        this.location = new SimpleStringProperty(location);
        this.country = new SimpleStringProperty(country);
        this.phone = new SimpleStringProperty(phone);
        this.handy = new SimpleStringProperty(handy);
        this.fax = new SimpleStringProperty(fax);
        this.email = new SimpleStringProperty(email);
        this.website = new SimpleStringProperty(website);
        this.informations = new SimpleStringProperty(informations);
        this.discount = discount;
        this.openCosts = openCosts;
        this.payedCosts = payedCosts;
    }

    public StringProperty getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = new SimpleStringProperty(location);
    }

    public StringProperty getKdNr() {
        return kdNr;
    }

    public void setKdNr(String kdNr) {
        this.kdNr = new SimpleStringProperty(kdNr);
    }

    public StringProperty getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = new SimpleStringProperty(company);
    }

    public StringProperty getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = new SimpleStringProperty(name1);
    }

    public StringProperty getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = new SimpleStringProperty(name2);
    }

    public StringProperty getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = new SimpleStringProperty(street);
    }

    public StringProperty getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = new SimpleStringProperty(plz);
    }

    public StringProperty getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = new SimpleStringProperty(country);
    }

    public StringProperty getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = new SimpleStringProperty(phone);
    }

    public StringProperty getHandy() {
        return handy;
    }

    public void setHandy(String handy) {
        this.handy = new SimpleStringProperty(handy);
    }

    public StringProperty getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = new SimpleStringProperty(fax);
    }

    public StringProperty getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = new SimpleStringProperty(email);
    }

    public StringProperty getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = new SimpleStringProperty(website);
    }

    public StringProperty getInformations() {
        return informations;
    }

    public void setInformations(String informations) {
        this.informations = new SimpleStringProperty(informations);
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getOpenCosts() {
        return openCosts;
    }

    public void setOpenCosts(double openCosts) {
        this.openCosts = openCosts;
    }

    public double getPayedCosts() {
        return payedCosts;
    }

    public void setPayedCosts(double payedCosts) {
        this.payedCosts = payedCosts;
    }
}
