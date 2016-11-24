package ru.disdev.controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.entity.Column;
import ru.disdev.entity.Result;

public class MainController implements Controller {

    private ObservableList<Result> results = FXCollections.observableArrayList();

    @FXML
    public JFXButton newResultButton;
    @FXML
    public JFXButton importButton;
    @FXML
    public JFXButton exportButton;
    @FXML
    private TableView<Result> resultTable;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize() {
        resultTable.setItems(results);
        FieldUtils.getFieldsListWithAnnotation(Result.class, Column.class)
                .forEach(field -> {
                    field.setAccessible(true);
                    Column annotation = field.getAnnotation(Column.class);
                    TableColumn nextColumn = null;
                    switch (annotation.type()) {
                        case NUMBER: {
                            TableColumn<Result, Number> column = new TableColumn<>();
                            column.setCellValueFactory(param -> {
                                try {
                                    return (ObservableValue<Number>) FieldUtils.readField(field, param.getValue());
                                } catch (IllegalAccessException ignored) {
                                }
                                return new SimpleIntegerProperty(Integer.MIN_VALUE);
                            });
                            nextColumn = column;
                            break;
                        }
                        case STRING: {
                            TableColumn<Result, String> column = new TableColumn<>();
                            column.setCellValueFactory(param -> {
                                try {
                                    return (ObservableValue<String>) FieldUtils.readField(field, param.getValue());
                                } catch (IllegalAccessException ignored) {
                                }
                                return new SimpleStringProperty("bad data");
                            });
                            nextColumn = column;
                            break;
                        }
                    }
                    Label label = new Label(annotation.name());
                    Tooltip tooltip = new Tooltip(annotation.description());
                    label.setTooltip(tooltip);
                    nextColumn.setGraphic(label);
                    nextColumn.setResizable(false);
                    resultTable.getColumns().add(nextColumn);
                });

    }
}
