package ru.disdev;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.disdev.utils.AlertUtils;

import java.net.URL;
import java.util.function.Consumer;

public class MainApplication extends Application {

    public static final String PROGRAM_NAME = "Transmission Calc";

    private static Stage mainStage;
    private static State currentState;

    public static void main(String[] args) {
        launch(MainApplication.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        mainStage.setTitle(PROGRAM_NAME);
        nextState();
    }

    public static void nextState() {
        if (currentState == null) {
            currentState = State.UPDATE;
        } else {
            currentState = currentState.next();
        }

        try {
            URL url = MainApplication.class.getResource("/fxml/" + currentState.fxmlName);
            Pane pane = FXMLLoader.load(url);
            mainStage.hide();
            mainStage.setScene(new Scene(pane));
            mainStage.sizeToScene();
            mainStage.centerOnScreen();
            Consumer<Stage> stageConfigurationCallback = currentState.getStageConfigurationCallback();
            if (stageConfigurationCallback != null) {
                stageConfigurationCallback.accept(mainStage);
            }
            mainStage.show();
        } catch (Exception e) {
            AlertUtils.showMessageAndCloseProgram(e);
        }

    }

    private enum State {
        UPDATE("update.fxml", stage -> {
            stage.setResizable(false);
            stage.setOnCloseRequest(Event::consume);
        }),
        MAIN("main.fxml", stage -> {
            stage.setResizable(true);
            stage.setOnCloseRequest(null);
        });

        private final String fxmlName;
        private Consumer<Stage> stageConfigurationCallback;

        State(String fxmlName, Consumer<Stage> stageConfigurationCallback) {
            this.fxmlName = fxmlName;
            this.stageConfigurationCallback = stageConfigurationCallback;
        }

        State(String fxmlName) {
            this.fxmlName = fxmlName;
        }

        public State next() {
            return this == values()[values().length - 1] ? this : values()[ordinal() + 1];
        }

        public Consumer<Stage> getStageConfigurationCallback() {
            return stageConfigurationCallback;
        }
    }
}
