package ru.disdev.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.MainApplication;
import ru.disdev.entity.Result;
import ru.disdev.entity.Type;
import ru.disdev.entity.input.CheckBox;
import ru.disdev.entity.input.ComboBox;
import ru.disdev.entity.input.InputData;
import ru.disdev.entity.input.TextField;
import ru.disdev.utils.AlertUtils;
import ru.disdev.utils.FieldValidatorUtils;
import ru.disdev.utils.NumberUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class InputDataController implements Controller {

    private InputData inputData;
    private List<JFXTextField> fields = new ArrayList<>();
    private final BiConsumer<Result, InputData> closeCallback;

    public InputDataController(InputData inputData, BiConsumer<Result, InputData> closeCallback) {
        this.inputData = inputData;
        this.closeCallback = closeCallback;
    }

    @Override
    public void initialize() {
        if (inputData == null) {
            inputData = new InputData();
        }
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        MainApplication.injectMainStage(stage);
        GridPane content = new GridPane();
        BorderPane root = new BorderPane(content);
        root.setBottom(makeCalcButton(stage));
        mapContent(content);
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.showAndWait();
    }

    private Node makeCalcButton(Stage stage) {
        JFXButton calcButton = new JFXButton("РАССЧИТАТЬ");
        calcButton.setOnAction(event -> {
            boolean checked = fields.stream().allMatch(JFXTextField::validate);
            if (checked) {
                //TODO calc logic here
                closeCallback.accept(new Result(), inputData);
                fields.clear();
                stage.close();
            }
            event.consume();
        });
        calcButton.setAlignment(Pos.CENTER);
        HBox box = new HBox(calcButton);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void mapContent(GridPane contentPane) {
        int i = 0, j = 0;
        try {
            for (Field field : FieldUtils.getAllFields(InputData.class)) {
                field.setAccessible(true);
                Region nextElement = null;
                if (field.isAnnotationPresent(TextField.class)) {
                    nextElement = mapTextField(field.getAnnotation(TextField.class), field);
                } else if (field.isAnnotationPresent(CheckBox.class)) {
                    nextElement = mapCheckBox(field.getAnnotation(CheckBox.class), field);
                } else if (field.isAnnotationPresent(ComboBox.class)) {
                    nextElement = mapComboBox(field.getAnnotation(ComboBox.class), field);
                }
                if (nextElement != null) {
                    if (i == 5) {
                        j++;
                        i = 0;
                    }
                    nextElement.setPadding(new Insets(15));
                    contentPane.add(nextElement, j, i++);
                }
            }
        } catch (Exception ex) {
            AlertUtils.showMessageAndCloseProgram(ex);
        }
    }

    @SuppressWarnings("unchecked")
    private JFXTextField mapTextField(TextField annotation, Field field) throws IllegalAccessException {
        JFXTextField textField = new JFXTextField();
        textField.setPromptText(annotation.name());
        textField.setTooltip(new Tooltip(annotation.description()));
        if (annotation.isRequired()) {
            textField.setValidators(FieldValidatorUtils.getRequiredFieldValidator());
            textField.focusedProperty().addListener((o, oldVal, newVal) -> {
                if (!newVal) textField.validate();
            });
        }
        switch (annotation.type()) {
            case NUMBER:
                textField.setTextFormatter(FieldValidatorUtils.getNumericTextFilter());
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    Double value = NumberUtils.parseDoubleORNull(newValue);
                    if (value == null) {
                        return;
                    }
                    Property<Number> numberProperty = null;
                    try {
                        numberProperty = (Property<Number>) FieldUtils.readField(field, inputData);
                    } catch (Exception ignored) {
                    }
                    if (numberProperty != null) {
                        numberProperty.setValue(value);
                    }
                });
                break;
            case STRING:
                textField.textProperty()
                        .bindBidirectional((Property<String>) FieldUtils.readField(field, inputData));
                break;
        }

        fields.add(textField);
        return textField;
    }

    @SuppressWarnings("unchecked")
    private JFXCheckBox mapCheckBox(CheckBox checkBox, Field field) throws IllegalAccessException {
        JFXCheckBox box = new JFXCheckBox();
        box.setText(checkBox.name());
        box.setTooltip(new Tooltip(checkBox.description()));
        box.selectedProperty().bindBidirectional((Property<Boolean>) FieldUtils.readField(field, inputData));
        return box;
    }

    @SuppressWarnings("unchecked")
    private HBox mapComboBox(ComboBox comboBox, Field field) throws IllegalAccessException {
        HBox box = new HBox();
        JFXComboBox newBox = null;
        if (comboBox.enumClass().equals(Type.class)) {
            JFXComboBox<Type> jfxComboBox = new JFXComboBox<>();
            jfxComboBox.getItems().addAll(Type.values());
            jfxComboBox.valueProperty().bindBidirectional((Property<Type>) FieldUtils.readField(field, inputData));
            newBox = jfxComboBox;
        }
        if (newBox != null) {
            newBox.setValue(newBox.getItems().get(0));
            Tooltip tooltip = new Tooltip(comboBox.description());
            newBox.setTooltip(tooltip);
            Label label = new Label(comboBox.name());
            label.setLabelFor(newBox);
            label.setTooltip(tooltip);
            label.setPadding(new Insets(0, 10, 0, 0));
            box.getChildren().addAll(label, newBox);
        }

        return box;
    }
}
