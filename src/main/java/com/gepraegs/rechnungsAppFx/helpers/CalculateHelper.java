package com.gepraegs.rechnungsAppFx.helpers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CalculateHelper {

    public static double calculateExclUst(double valueIncl, double ust) {
        return valueIncl / ( (100 + ust) / 100);
    }

    public static double calculateInclUst(double valueExcl, double ust) {
        return valueExcl * ( (100 + ust) / 100);
    }

    public static double calculateUst(double valueIncl, double ust) {
        return (valueIncl * ust) / (100 + ust);
    }
}