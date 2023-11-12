package com.gepraegs.rechnungsAppFx.model;

import com.gepraegs.rechnungsAppFx.utils.ConstantUtil;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;

public class AddressDetails {
    private String billingCompany = ConstantUtil.EMPTY;
    private String billingContactPerson = ConstantUtil.EMPTY;
    private String billingAddress = ConstantUtil.EMPTY;
    private String billingZipCity = ConstantUtil.EMPTY;

    private String shippingCompany = ConstantUtil.EMPTY;
    private String shippingContactPerson = ConstantUtil.EMPTY;
    private String shippingAddress = ConstantUtil.EMPTY;
    private String shippingZipCity = ConstantUtil.EMPTY;
    private String shippingPhone = ConstantUtil.EMPTY;
    private String shippingEmail = ConstantUtil.EMPTY;

    private Color borderColor = ColorConstants.GRAY;

    public AddressDetails setBillingCompany(String billingCompany) {
        this.billingCompany = billingCompany;
        return this;
    }

    public String getBillingCompany() {
        return billingCompany;
    }

    public AddressDetails setBillingContactPerson(String billingName) {
        this.billingContactPerson = billingName;
        return this;
    }

    public String getBillingContactPersion() {
        return billingContactPerson;
    }

    public AddressDetails setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public AddressDetails setBillingZipCity(String billingEmailText) {
        this.billingZipCity = billingEmailText;
        return this;
    }

    public String getBillingZipCity() {
        return billingZipCity;
    }

    public AddressDetails setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
        return this;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public AddressDetails setShippingContactPerson(String shippingContactPerson) {
        this.shippingContactPerson = shippingContactPerson;
        return this;
    }

    public String getShippingContactPerson() {
        return shippingContactPerson;
    }

    public AddressDetails setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public AddressDetails setShippingZipCity(String shippingZipCity) {
        this.shippingZipCity = shippingZipCity;
        return this;
    }

    public String getShippingZipCity() {
        return shippingZipCity;
    }

    public AddressDetails setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
        return this;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public AddressDetails setShippingEmail(String shippingEmail) {
        this.shippingEmail = shippingEmail;
        return this;
    }

    public String getShippingEmail() {
        return shippingEmail;
    }

    public AddressDetails setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public AddressDetails build() {
        return this;
    }

}