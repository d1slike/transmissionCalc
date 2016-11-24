package ru.disdev.utils;

import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class FieldValidatorUtils {
    private static final UnaryOperator<TextFormatter.Change> NUMERIC_TEXT_VALIDATOR = change -> {
        String content = change.getControlNewText();
        int length = content.length();
        if (length == 0) {
            return change;
        }
        else if (length > 9) {
            return null;
        }
        char lastChar = content.charAt(length - 1);
        boolean validLastChar = (lastChar >= '0' && lastChar <= '9') || lastChar == '.';
        boolean dotCheck = content.indexOf('.') == content.lastIndexOf('.');
        return validLastChar && dotCheck ? change : null;
    };

    private static ValidatorBase requiredFieldValidator;
    private static TextFormatter<Object> numericTextFilter;

    static {
        requiredFieldValidator = new RequiredFieldValidator();
        requiredFieldValidator.setMessage("Обязательно для заполнения");
        numericTextFilter = new TextFormatter<>(NUMERIC_TEXT_VALIDATOR);
    }


    public static ValidatorBase getRequiredFieldValidator() {
        return requiredFieldValidator;
    }

    public static TextFormatter<Object> getNumericTextFilter() {
        return numericTextFilter;
    }
}
