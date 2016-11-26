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
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.MainApplication;
import ru.disdev.entity.Result;
import ru.disdev.entity.Type;
import ru.disdev.entity.input.*;
import ru.disdev.entity.input.conditional.Condition;
import ru.disdev.entity.input.conditional.DependOn;
import ru.disdev.entity.input.conditional.ElementsList;
import ru.disdev.utils.AlertUtils;
import ru.disdev.utils.FieldValidatorUtils;
import ru.disdev.utils.NumberUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class InputDataController implements Controller {

    private InputData inputData;
    private List<JFXTextField> fields = new ArrayList<>();
    private Map<Integer, ElementsList> stateMap = new HashMap<>();
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
        Stage stage = MainApplication.newChildStage();
        stage.initModality(Modality.WINDOW_MODAL);
        GridPane content = new GridPane();
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        BorderPane root = new BorderPane(content);
        root.setBottom(makeCalcButton(stage));
        mapContent(content);
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.centerOnScreen();
        stateMap.forEach((state, list) -> {
            list.getDisable().forEach(node -> node.setDisable(false));
            list.getEnable().forEach(node -> node.setDisable(true));
        });
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
                stateMap.forEach((integer, elementsList) -> elementsList.clear());
                stateMap.clear();
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
                    if (field.isAnnotationPresent(DependOn.class)) {
                        DependOn dependOn = field.getAnnotation(DependOn.class);
                        ElementsList elementsList = stateMap.get(dependOn.id());
                        if (elementsList == null) {
                            elementsList = new ElementsList();
                            stateMap.put(dependOn.id(), elementsList);
                        }
                        if (dependOn.showOn() == CheckBoxState.CHECKED) {
                            elementsList.addToEnable(nextElement);
                        } else {
                            elementsList.addToDisable(nextElement);
                        }
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
    private VBox mapTextField(TextField annotation, Field field) throws IllegalAccessException {
        VBox box = new VBox();
        JFXTextField textField = new JFXTextField();
        textField.setPromptText(annotation.name());
        textField.setTooltip(new Tooltip(annotation.description()));
        textField.setAlignment(Pos.BOTTOM_LEFT);
        Label label = new Label(annotation.name());
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setFont(Font.font(12));
        box.getChildren().addAll(label, textField);
        if (annotation.isRequired()) {
            textField.setValidators(FieldValidatorUtils.getRequiredFieldValidator());
            textField.focusedProperty().addListener((o, oldVal, newVal) -> {
                if (!newVal) textField.validate();
            });
        }
        switch (annotation.type()) {
            case NUMBER:
                textField.setTextFormatter(FieldValidatorUtils.getNumericTextFilter());
                try {
                    textField.setText(((Property<Number>) FieldUtils.readField(field, inputData)).getValue().toString());
                } catch (Exception ignored) {
                }
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    NumberUtils.parseDouble(newValue).ifPresent(value -> {
                        Property<Number> numberProperty = null;
                        try {
                            numberProperty = (Property<Number>) FieldUtils.readField(field, inputData);
                        } catch (Exception ignored) {
                        }
                        if (numberProperty != null) {
                            numberProperty.setValue(value);
                        }
                    });
                });
                break;
            case STRING:
                textField.textProperty()
                        .bindBidirectional((Property<String>) FieldUtils.readField(field, inputData));
                break;
        }
        fields.add(textField);
        return box;
    }

    @SuppressWarnings("unchecked")
    private JFXCheckBox mapCheckBox(CheckBox checkBox, Field field) throws IllegalAccessException {
        JFXCheckBox box = new JFXCheckBox();
        box.setText(checkBox.name());
        box.setTooltip(new Tooltip(checkBox.description()));
        box.selectedProperty().bindBidirectional((Property<Boolean>) FieldUtils.readField(field, inputData));
        if (field.isAnnotationPresent(Condition.class)) {
            Condition condition = field.getAnnotation(Condition.class);
            if (!stateMap.containsKey(condition.value())) {
                stateMap.put(condition.value(), new ElementsList());
            }
            box.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (!stateMap.containsKey(condition.value())) {
                    return;
                }
                ElementsList elementsList = stateMap.get(condition.value());
                elementsList.getEnable().forEach(node -> node.setDisable(!newValue));
                elementsList.getDisable().forEach(node -> node.setDisable(newValue));
            });
        }
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
            label.setAlignment(Pos.CENTER_LEFT);
            label.setPadding(new Insets(0, 10, 0, 0));
            box.getChildren().addAll(label, newBox);
        }

        return box;
    }
}
