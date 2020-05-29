package com.gepraegs.rechnungsAppFx;

import javafx.beans.property.SimpleBooleanProperty;

public class Guest {

    private int id;
    private String lastName;
    private String firstName;
    private int age;
    private String status;
    private String phone;
    private String handy;
    private String email;
    private String street;
    private String plz;
    private String ort;
    private String comments;
    private SimpleBooleanProperty selected;
    private SimpleBooleanProperty invite;

    public Guest()
    {
        id = -1;
        lastName = "";
        firstName = "";
        age = 0;
        status = "";
        phone = "";
        handy = "";
        email = "";
        street = "";
        plz = "";
        ort = "";
        comments = "";
        selected = new SimpleBooleanProperty(false);
        invite = new SimpleBooleanProperty(false);
    }

    public Guest(int id, String lastName, String firstName, int age, String status,
                 String phone, String handy, String email, String street, String plz, String ort, String comments, boolean invite)
    {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
        this.status = status;
        this.phone = phone;
        this.handy = handy;
        this.email = email;
        this.street = street;
        this.plz = plz;
        this.ort = ort;
        this.comments = comments;
        this.selected = new SimpleBooleanProperty(false);
        this.invite = new SimpleBooleanProperty(invite);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHandy() {
        return handy;
    }

    public void setHandy(String handy) {
        this.handy = handy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public boolean isSelected() {
        return this.selected.get();
    }

    public boolean isInvite() {
        return invite.get();
    }

    public SimpleBooleanProperty inviteProperty() {
        return invite;
    }

    public void setInvite(boolean invite) {
        this.invite.set(invite);
    }
}
