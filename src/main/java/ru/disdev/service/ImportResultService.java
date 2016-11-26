package ru.disdev.service;

import au.com.bytecode.opencsv.CSVReader;
import javafx.beans.property.Property;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.entity.Column;
import ru.disdev.entity.Result;
import ru.disdev.entity.Type;
import ru.disdev.utils.NumberUtils;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class ImportResultService extends Service<List<Result>> {

    private File source;

    public ImportResultService(File source) {
        this.source = source;
    }

    @Override
    protected Task<List<Result>> createTask() {
        return new Task<List<Result>>() {
            @Override
            @SuppressWarnings("unchecked")
            protected List<Result> call() throws Exception {
                List<Result> list;
                try (CSVReader reader = new CSVReader(new FileReader(source), ';')) {
                    list = reader.readAll()
                            .stream()
                            .skip(1)
                            .map(line -> {
                                Result result = new Result();
                                Field[] fields =
                                        FieldUtils.getFieldsWithAnnotation(Result.class, Column.class);
                                for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
                                    Field field = fields[i];
                                    field.setAccessible(true);
                                    Column column = field.getAnnotation(Column.class);
                                    Object property;
                                    if (column.type() == Type.NUMBER) {
                                        property = NumberUtils.parseDouble(line[i]).orElse(0.);
                                    } else {
                                        property = line[i];
                                    }
                                    try {
                                        Property<Object> prop = (Property<Object>) FieldUtils.readField(field, result);
                                        prop.setValue(property);
                                    } catch (IllegalAccessException ignored) {

                                    }
                                }
                                return result;
                            }).collect(Collectors.toList());
                }
                return list;
            }
        };
    }
}
