package com.gepraegs.rechnungsAppFx.helpers;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatterHelper {

    public static String DoubleToPercentageString(double value) {
        NumberFormat format = NumberFormat.getPercentInstance(Locale.GERMANY);
        return format.format(value);
    }

    public static String DoubleToCurrencyString(double value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return format.format(value);
    }
}
