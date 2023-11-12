package com.gepraegs.rechnungsAppFx.model;

import com.gepraegs.rechnungsAppFx.utils.ConstantUtil;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;

public class InvoiceDetails {
    String invoiceTitle = ConstantUtil.INVOICE_TITLE;
    String invoiceNoText = ConstantUtil.INVOICE_NO_TEXT;
    String invoiceDateText = ConstantUtil.INVOICE_DATE_TEXT;
    String shippingDateText = ConstantUtil.SHIPPING_DATE_TEXT;
    String customerNoText = ConstantUtil.CUSTOMER_NO_TEXT;

    String invoiceNo = ConstantUtil.EMPTY;
    String invoiceDate = ConstantUtil.EMPTY;
    String shippingDate = ConstantUtil.EMPTY;
    String customerNo = ConstantUtil.EMPTY;

    Color borderColor = ColorConstants.GRAY;

    public InvoiceDetails setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
        return this;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public InvoiceDetails setInvoiceNoText(String invoiceNoText) {
        this.invoiceNoText = invoiceNoText;
        return this;
    }

    public String getInvoiceNoText() {
        return invoiceNoText;
    }

    public InvoiceDetails setInvoiceDateText(String invoiceDateText) {
        this.invoiceDateText = invoiceDateText;
        return this;
    }

    public String getInvoiceDateText() {
        return invoiceDateText;
    }

    public InvoiceDetails setShippingDateText(String shippingDateText) {
        this.shippingDateText = shippingDateText;
        return this;
    }

    public String getShippingDateText() {
        return shippingDateText;
    }

    public InvoiceDetails setCustomerNoText(String customerNoText) {
        this.customerNoText = customerNoText;
        return this;
    }

    public String getCustomerNoText() {
        return customerNoText;
    }

    public InvoiceDetails setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public InvoiceDetails setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public InvoiceDetails setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
        return this;
    }

    public String getShippingDate() {
        return shippingDate;
    }

    public InvoiceDetails setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
        return this;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public InvoiceDetails setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public Color getBoderColor() {
        return borderColor;
    }

    public InvoiceDetails build() {
        return this;
    }
}
