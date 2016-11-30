package ru.disdev.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import ru.disdev.entity.TableData;

import java.util.Map;
import java.util.function.Consumer;

public class TableDataService extends Service<Void> {

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 4);
                loadFirst();
                updateProgress(1, 4);
                loadSecond();
                updateProgress(2, 4);
                loadThird();
                updateProgress(3, 4);
                loadLast();
                updateProgress(4, 4);
                return null;
            }
        };
    }

    private void loadFirst() {

    }

    private void loadSecond() {

    }

    private void loadThird() {

    }

    private void loadLast() {

    }

}

