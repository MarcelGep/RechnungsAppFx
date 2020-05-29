package com.gepraegs.rechnungsAppFx.helpers;

import javafx.fxml.FXMLLoader;

public class HelperResourcesLoader {

    private HelperResourcesLoader()
    {
    }

    public static FXMLLoader loadFXML(String resource)
    {
        return new FXMLLoader(new HelperResourcesLoader().getClass().getResource(resource));
    }
}
