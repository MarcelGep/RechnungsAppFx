package com.gepraegs.rechnungsAppFx.helpers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import java.io.IOException;

public class DynamicView {

    private DynamicView()
    {
    }

    public static void loadTabContent(Tab tab, String resource) throws IOException {
        tab.setContent(FXMLLoader.load(new DynamicView().getClass().getResource(resource)));
    }
}
