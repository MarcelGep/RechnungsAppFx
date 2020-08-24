package com.gepraegs.rechnungsAppFx;

public enum InvoiceState {

    CREATED("CR", "Erstellt"),
    PAYED("PA", "Bezahlt"),
    UNPAYED("UP", "Nicht bezahlt"),
    DUE("DU", "Überfällig");

    private String code;
    private String text;

    InvoiceState(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static InvoiceState getByCode(String statusCode) {
        for (InvoiceState g : InvoiceState.values()) {
            if (g.code.equals(statusCode)) {
                return g;
            }
        }
        return null;
    }

    public static InvoiceState getByName(String statusText) {
        for (InvoiceState g : InvoiceState.values()) {
            if (g.text.equals(statusText)) {
                return g;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.text;
    }
}