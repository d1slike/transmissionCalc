package ru.disdev.controller;

import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ru.disdev.MainApplication;
import ru.disdev.entity.TableData;
import ru.disdev.service.TableDataService;

import java.util.Map;

public class LoadController implements Controller {

    private static final String INITIAL_MESSAGE = "Загрузка данных";

    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private Label infoLabel;

    @Override
    public void initialize() {
        TableDataService service = new TableDataService();
        service.setOnSucceeded(event -> {
            Map<String, TableData> value = service.getValue();
            //TODO
            MainApplication.nextState();
        });
        service.setInfoLabelUpdateCallback(message -> Platform.runLater(() -> infoLabel.setText(message)));
        progressBar.progressProperty().bind(service.progressProperty());
        infoLabel.setText(INITIAL_MESSAGE);
        service.start();
    }
}
