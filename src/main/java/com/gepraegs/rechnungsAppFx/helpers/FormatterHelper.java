package com.gepraegs.rechnungsAppFx.helpers;

import javafx.scene.control.TextField;

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

//    public static double CurrencyStringToDouble(String value) {
//        String currency = null;
//        try {
//            currency = DecimalFormat.getCurrencyInstance(Locale.getDefault()).parse(value).toString();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return Double.parseDouble(currency);
//    }

    public static double CurrencyStringToDouble(String value) {
        String currency = value.replace(".", "")
                               .replace(",", ".")
                               .replaceAll("[^\\d.-]", "");
        try {
            return Double.parseDouble(currency);
        } catch (Exception e) {
            throw e;
        }
    }

    public static double NumberStrToDouble(String str) {
        return Double.parseDouble(str.replace(".","").replace(",","."));
    }

    public static String DoubleToNumberStr(Double value) {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        return formatter.format(value);
    }

    public static boolean IsDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void setTfValidState(TextField tf, boolean state) {
        if (state) {
            tf.setStyle("-fx-border-width: 0px; -fx-alignment: CENTER-RIGHT;");
        } else {
            tf.setText("0,00 €");
            tf.setStyle("-fx-border-color: red; -fx-alignment: CENTER-RIGHT;");
        }
    }
}