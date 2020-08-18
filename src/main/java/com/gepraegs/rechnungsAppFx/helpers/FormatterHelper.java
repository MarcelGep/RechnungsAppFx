package com.gepraegs.rechnungsAppFx.helpers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class FormatterHelper {

    public static String DoubleToPercentageString(double value) {
        NumberFormat nf = DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        return nf.format(value) + " %";
    }

    public static String DoubleToCurrencyString(double value) {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return currency.format(value);
    }

    public static double PercentageStringToDouble(String value) {
        String percentage = value.replace("%", "").trim();
        return Double.parseDouble(percentage);
    }

    public static double CurrencyStringToDouble(String value) {
        String currency = value;
        try {
            currency = DecimalFormat.getCurrencyInstance(Locale.getDefault()).parse(value).toString();
        } catch (ParseException e) {

        }
        return Double.parseDouble(currency);
    }
}