package ru.disdev.service;

import au.com.bytecode.opencsv.CSVReader;
import javafx.beans.property.Property;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.entity.Column;
import ru.disdev.entity.Enum;
import ru.disdev.entity.Result;
import ru.disdev.entity.Type;
import ru.disdev.utils.NumberUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static ru.disdev.utils.IOUtils.getWin1251FileReader;

public class ImportResultService extends Service<List<Result>> {

    private File source;

    public ImportResultService(File source) {
        this.source = source;
    }

    @Override
    protected Task<List<Result>> createTask() {
        return new Task<List<Result>>() {
            @Override
            protected List<Result> call() throws Exception {
                List<Result> list;
                try (CSVReader reader = new CSVReader(getWin1251FileReader(source), ';')) {
                    list = reader.readAll()
                            .stream()
                            .skip(1)
                            .map(ImportResultService.this::mapResult)
                            .collect(Collectors.toList());
                }
                return list;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private Result mapResult(String[] line) {
        Result result = new Result();
        Field[] fields =
                FieldUtils.getFieldsWithAnnotation(Result.class, Column.class);
        for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            Object property = null;
            if (column.type() == Type.NUMBER) {
                property = NumberUtils.parseDouble(line[i]).orElse(0.);
            } else if (column.type() == Type.BOOLEAN) {
                property = Boolean.parseBoolean(line[i]);
            } else if (column.type() == Type.OBJECT && field.isAnnotationPresent(Enum.class)) {
                Class enumClass = field.getAnnotation(Enum.class).value();
                if (enumClass.isEnum()) {
                    for (Object o : enumClass.getEnumConstants()) {
                        if (o.toString().equals(line[i])) {
                            property = o;
                            break;
                        }
                    }
                }
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
    }
}
