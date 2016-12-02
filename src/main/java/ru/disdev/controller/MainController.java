package ru.disdev.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import de.jensd.fx.fontawesome.Icon;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.MainApplication;
import ru.disdev.entity.Column;
import ru.disdev.entity.Result;
import ru.disdev.service.ExportResultService;
import ru.disdev.service.ImportResultService;
import ru.disdev.utils.PopupUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public class MainController implements Controller {

    private static final FileChooser.ExtensionFilter CSV_FILER = new FileChooser.ExtensionFilter("CSV file", "*.csv");
    @FXML
    private JFXButton deleteButton;
    @FXML
    private BorderPane root;
    @FXML
    private JFXButton newResultButton;
    @FXML
    private JFXButton importButton;
    @FXML
    private JFXButton exportButton;
    @FXML
    private TableView<Result> resultTable;
    @FXML
    private JFXSpinner spinner;

    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private final FileChooser fileChooser = new FileChooser();

    private ObservableList<Result> results = FXCollections.observableArrayList();
    private int selectedItem;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize() {
        resultTable.setItems(results);
        newResultButton.setOnAction(this::onNewResultButtonClick);
        newResultButton.setGraphic(new Icon("PLUS_CIRCLE"));
        newResultButton.setText("");
        exportButton.setOnAction(this::onExportButtonClick);
        exportButton.setGraphic(new Icon("DOWNLOAD"));
        exportButton.setText("");
        importButton.setOnAction(this::onImportButtonClick);
        importButton.setGraphic(new Icon("UPLOAD"));
        importButton.setText("");
        directoryChooser.setTitle("Директория для экспорта");
        fileChooser.setTitle("Файл для импорта");
        fileChooser.setSelectedExtensionFilter(CSV_FILER);
        deleteButton.setVisible(false);
        Icon trash = new Icon("TRASH");
        trash.setTextFill(Color.RED);
        deleteButton.setGraphic(trash);
        deleteButton.setOnAction(this::onDeleteButtonClick);
        resultTable.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectedItem = newValue.intValue();
            deleteButton.setVisible(selectedItem != -1);
        });
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
                        case OBJECT: {
                            TableColumn<Result, String> column = new TableColumn<>();
                            column.setCellValueFactory(param -> {
                                try {
                                    ObjectProperty<Object> o = (ObjectProperty<Object>) FieldUtils.readField(field, param.getValue());
                                    return new SimpleStringProperty(o.getValue().toString());
                                } catch (IllegalAccessException ignored) {
                                }
                                return new SimpleStringProperty("bad data");
                            });
                            nextColumn = column;
                            break;
                        }
                        case BOOLEAN: {
                            TableColumn<Result, String> column = new TableColumn<>();
                            column.setCellValueFactory(param -> {
                                try {
                                    Property<Boolean> o = (Property<Boolean>) FieldUtils.readField(field, param.getValue());
                                    return new SimpleStringProperty(o.getValue() ? "Да" : "Нет");
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
                    resultTable.getColumns().add(nextColumn);
                });

    }

    private void onDeleteButtonClick(ActionEvent event) {
        if (selectedItem != -1 && !results.isEmpty()) {
            results.remove(selectedItem);
        }
    }

    private void onNewResultButtonClick(ActionEvent event) {
        Platform.runLater(() -> {
            InputDataController controller =
                    new InputDataController(result -> results.add(result));
            controller.initialize();
        });
        event.consume();
    }

    private void onExportButtonClick(ActionEvent event) {
        updateControlStatus(false);
        File directory = directoryChooser.showDialog(MainApplication.getMainStage());
        if (directory != null) {
            ExportResultService service = new ExportResultService(results, directory);
            service.setOnRunning(e -> spinner.setVisible(true));
            service.setOnSucceeded(e -> {
                updateControlStatus(true);
                spinner.setVisible(false);
                PopupUtils.infoPoup(root, spinner, "Данные успешно экспортированы", 3);
            });
            service.setOnFailed(e -> {
                updateControlStatus(true);
                spinner.setVisible(false);
                PopupUtils.warningPopup(root, spinner, "Ошибка при экспорте данных", 3);
            });
            service.start();
        } else {
            updateControlStatus(true);
        }
        event.consume();
    }

    private void onImportButtonClick(ActionEvent event) {
        updateControlStatus(false);
        File source = fileChooser.showOpenDialog(MainApplication.getMainStage());
        if (source != null) {
            ImportResultService service = new ImportResultService(source);
            service.setOnRunning(e -> spinner.setVisible(true));
            service.setOnFailed(e -> {
                updateControlStatus(true);
                spinner.setVisible(false);
                PopupUtils.warningPopup(root, spinner, "Ошибка при импорте данных", 3);
            });
            service.setOnSucceeded(e -> {
                List<Result> value = service.getValue();
                results.clear();
                results.addAll(value);
                updateControlStatus(true);
                spinner.setVisible(false);
                PopupUtils.infoPoup(root, spinner, "Данные успешно импортированы", 3);
            });
            service.start();
        } else {
            updateControlStatus(true);
        }
        event.consume();
    }

    private void updateControlStatus(boolean enable) {
        Stream.of(exportButton, importButton, newResultButton, deleteButton)
                .forEach(jfxButton -> jfxButton.setDisable(!enable));
    }
}
