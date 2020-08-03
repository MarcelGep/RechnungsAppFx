package com.gepraegs.rechnungsAppFx.helpers;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatterHelper {

    public static String DoubleToPercentageString(double value) {
        NumberFormat percent = NumberFormat.getPercentInstance(Locale.GERMANY);
        return percent.format(value/100);
    }

    public static String DoubleToCurrencyString(double value) {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return currency.format(value);
    }
}
