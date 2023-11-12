package com.gepraegs.rechnungsAppFx.model;

import com.gepraegs.rechnungsAppFx.utils.ConstantUtil;

public class ProductTableHeader {
    String pos = ConstantUtil.PRODUCT_TABLE_POS;
    String artNr = ConstantUtil.PRODUCT_TABLE_ART_NR;
    String description = ConstantUtil.PRODUCT_TABLE_DESCRIPTION;
    String quantity = ConstantUtil.PRODUCT_TABLE_QUANTITY;
    String unit = ConstantUtil.PRODUCT_TABLE_UNIT;
    String singlePrice = ConstantUtil.PRODUCT_TABLE_SINGLE_PRICE;
    String totalPrice = ConstantUtil.PRODUCT_TABLE_TOTAL_PRICE;

    public ProductTableHeader setPos(String pos) {
        this.pos = pos;
        return this;
    }

    public String getPos() {
        return pos;
    }

    public ProductTableHeader setArtNr(String artNr) {
        this.artNr = artNr;
        return this;
    }

    public String getArtNr() {
        return artNr;
    }

    public ProductTableHeader setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductTableHeader setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public ProductTableHeader setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public ProductTableHeader setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice;
        return this;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public ProductTableHeader setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public ProductTableHeader build() {
        return this;
    }
}