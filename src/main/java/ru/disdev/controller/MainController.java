package ru.disdev.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.MainApplication;
import ru.disdev.entity.Column;
import ru.disdev.entity.Result;
import ru.disdev.entity.input.InputData;
import ru.disdev.service.ExportResultService;
import ru.disdev.service.ImportResultService;
import ru.disdev.utils.PopupUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public class MainController implements Controller {

    private static final FileChooser.ExtensionFilter CSV_FILER = new FileChooser.ExtensionFilter("CSV file", "*.csv");
    @FXML
    public BorderPane root;
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
    private InputData lastSavedInputData;


    @Override
    @SuppressWarnings("unchecked")
    public void initialize() {
        resultTable.setItems(results);
        newResultButton.setOnAction(this::onNewResultButtonClick);
        exportButton.setOnAction(this::onExportButtonClick);
        importButton.setOnAction(this::onImportButtonClick);
        directoryChooser.setTitle("Директория для экспорта");
        fileChooser.setTitle("Файл для импорта");
        fileChooser.setSelectedExtensionFilter(CSV_FILER);
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

    private void onNewResultButtonClick(ActionEvent event) {
        Platform.runLater(() -> {
            InputDataController controller =
                    new InputDataController(lastSavedInputData, this::closeInputControllerHandler);
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
                PopupUtils.infoPoup(root, resultTable, "Данные успешно экспортированы", 3);
            });
            service.setOnFailed(e -> {
                updateControlStatus(true);
                spinner.setVisible(false);
                PopupUtils.warningPopup(root, resultTable, "Ошибка при экспорте данных", 3);
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
                PopupUtils.warningPopup(root, resultTable, "Ошибка при импорте данных", 3);
            });
            service.setOnSucceeded(e -> {
                List<Result> value = service.getValue();
                results.clear();
                results.addAll(value);
                updateControlStatus(true);
                spinner.setVisible(false);
                PopupUtils.infoPoup(root, resultTable, "Данные успешно импортированы", 3);
            });
            service.start();
        } else {
            updateControlStatus(true);
        }
        event.consume();
    }

    private void updateControlStatus(boolean enable) {
        Stream.of(exportButton, importButton, newResultButton)
                .forEach(jfxButton -> jfxButton.setDisable(!enable));
    }

    private void closeInputControllerHandler(Result result, InputData inputData) {
        if (result != null) {
            results.add(result);
        }
        lastSavedInputData = inputData;
    }
}
