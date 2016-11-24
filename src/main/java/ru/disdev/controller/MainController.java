package ru.disdev.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import ru.disdev.utils.DaemonThreadPool;

import java.io.File;
import java.util.concurrent.TimeUnit;
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
        InputDataController controller =
                new InputDataController(lastSavedInputData, this::closeInputControllerHandler);
        controller.initialize();
        event.consume();
    }

    private void onExportButtonClick(ActionEvent event) {
        updateControlStatus(false);
        File directory = directoryChooser.showDialog(MainApplication.getMainStage());
        if (directory != null) {
            ExportResultService service = new ExportResultService(results, directory);
            service.setOnSucceeded(e -> {
                updateControlStatus(true);
                showPopup(new Label("Успешно экспортировано"), exportButton);
            });
            service.setOnFailed(e -> {
                updateControlStatus(true);
                showPopup(new Label("Возникла ошибка при экспорте"), exportButton);
            });
            service.start();
        } else {
            updateControlStatus(true);
            showPopup(new Label("Не указана директория для экспорта"), exportButton);
        }
        event.consume();
        //TODO кастомизировать, вынести в константы
    }

    private void showPopup(Label text, Node source) {
        text.setPadding(new Insets(20));
        text.setAlignment(Pos.CENTER);
        JFXPopup popup = new JFXPopup(root, text);
        popup.setSource(source);
        popup.show(JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.RIGHT);
        DaemonThreadPool.schedule(() -> Platform.runLater(popup::close), 1, TimeUnit.SECONDS);
    }

    private void onImportButtonClick(ActionEvent event) {

    }

    private void updateControlStatus(boolean enable) {
        spinner.setVisible(!enable);
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
