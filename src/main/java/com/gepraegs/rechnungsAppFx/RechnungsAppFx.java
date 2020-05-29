package com.gepraegs.rechnungsAppFx;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.*;

import static com.gepraegs.rechnungsAppFx.helpers.HelperDialogs.showStartDialog;

public class RechnungsAppFx extends Application {

    static {
        InputStream stream = RechnungsAppFx.class.getClassLoader().getResourceAsStream("logging.properties");

        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        showStartDialog(primaryStage);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}


