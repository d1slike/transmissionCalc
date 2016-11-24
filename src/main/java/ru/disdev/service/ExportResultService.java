package ru.disdev.service;

import au.com.bytecode.opencsv.CSVWriter;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.entity.Column;
import ru.disdev.entity.Result;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;

public class ExportResultService extends Service<Void> {

    private final List<Result> resultsToExport;
    private final File file;

    public ExportResultService(List<Result> resultsToExport, File file) {
        this.resultsToExport = resultsToExport;
        this.file = file;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                File target = new File(file.getAbsolutePath() + "/results.csv");
                try (CSVWriter writer = new CSVWriter(new FileWriter(target), ';')) {
                    resultsToExport.stream()
                            .map(ExportResultService.this::toCSVRow)
                            .forEach(writer::writeNext);
                }
                return null;
            }
        };
    }

    private String[] toCSVRow(Result result) {
        List<String> list = FieldUtils.getFieldsListWithAnnotation(Result.class, Column.class)
                .stream()
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        return FieldUtils.readField(field, result).toString();
                    } catch (IllegalAccessException ignored) {
                    }
                    return "";
                }).collect(Collectors.toList());
        String[] array = new String[list.size()];
        return list.toArray(array);
    }
}
