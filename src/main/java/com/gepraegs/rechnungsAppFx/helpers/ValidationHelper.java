package com.gepraegs.rechnungsAppFx.helpers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.text.Text;

public class ValidationHelper {

    private static final Text WARN_ICON = GlyphsDude.createIcon(FontAwesomeIcon.EXCLAMATION_TRIANGLE, "0.9em");
    private static final String VALIDATION_ERROR = "Eingabe überprüfen!";
    private static final String SHORT_VALIDATION_ERROR = "Fehler!";

    public static void setupValidation(JFXTextField tf) {

        RequiredFieldValidator reqValidator = new RequiredFieldValidator();
        reqValidator.setMessage(VALIDATION_ERROR);
        reqValidator.setIcon(WARN_ICON);

        NumberValidator nrValidator = new NumberValidator();
        nrValidator.setMessage(VALIDATION_ERROR);
        nrValidator.setIcon(WARN_ICON);

        RegexValidator regValidator = new RegexValidator();
        regValidator.setMessage(SHORT_VALIDATION_ERROR);
        regValidator.setIcon(WARN_ICON);

        switch (tf.getId())
        {
            case "tfFirstName":
            case "tfLastName":
            case "tfName":
            case "tfContact":
            case "tfTask":
            case "tfPerson":
                tf.getValidators().add(reqValidator);
                break;

            case "tfPhone":
            case "tfHandy":
                tf.getValidators().add(nrValidator);
                break;

            case "tfDuration":
                tf.getValidators().add(reqValidator);
                tf.getValidators().add(nrValidator);
                break;

            case "tfAge":
                regValidator.setRegexPattern("^(0?[1-9]|[1-9][0-9]|[1][1-9][1-9]|120)$");

                tf.getValidators().add(regValidator);
                break;

            case "tfPlz":
                regValidator.setRegexPattern("^$|[0-9]{5}");

                tf.getValidators().add(regValidator);
                break;

            case "tfEmail":
                regValidator.setRegexPattern("^$|[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]");

                tf.getValidators().add(regValidator);
                break;
        }

        tf.focusedProperty().addListener((arg0, oldVal, newVal) ->
        {
            if (!newVal)
            {
                tf.validate();
            }
            else
            {
                tf.resetValidation();
            }
        });
    }

    public static void addTextLimiter(final JFXTextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) ->
        {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }

    public static void statusValidator(final JFXComboBox cb)
    {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage(VALIDATION_ERROR);
        validator.setIcon(WARN_ICON);

        cb.getValidators().add(validator);

        cb.focusedProperty().addListener((arg0, oldVal, newVal) ->
        {
            if (!newVal)
            {
                cb.validate();
            }
            else
            {
                cb.resetValidation();
            }
        });
    }
}
