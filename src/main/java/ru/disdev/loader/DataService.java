package ru.disdev.loader;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import ru.disdev.entity.TableData;

import java.util.Map;
import java.util.function.Consumer;

public class DataService extends Service<Map<String, TableData>> {

    private Consumer<String> infoLabelUpdateCallback;

    @Override
    protected Task<Map<String, TableData>> createTask() {
        return new LoadTask();
    }

    public void setInfoLabelUpdateCallback(Consumer<String> infoLabelUpdateCallback) {
        this.infoLabelUpdateCallback = infoLabelUpdateCallback;
    }


    public class LoadTask extends Task<Map<String, TableData>> {
        @Override
        protected Map<String, TableData> call() throws Exception {
            return null;
        }
    }
}

