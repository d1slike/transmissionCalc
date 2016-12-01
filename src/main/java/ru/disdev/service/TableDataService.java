package ru.disdev.service;

import au.com.bytecode.opencsv.CSVReader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import ru.disdev.entity.input.enums.CircleType;
import ru.disdev.entity.input.enums.Material;
import ru.disdev.entity.input.enums.ShaftPosition;
import ru.disdev.entity.table.FirstTableData;
import ru.disdev.entity.table.ThirdTableData;
import ru.disdev.holder.FirstTableHolder;
import ru.disdev.holder.FourthTableHolder;
import ru.disdev.holder.SecondTableHolder;
import ru.disdev.holder.ThirdTableHolder;
import ru.disdev.utils.MD5Utils;
import ru.disdev.utils.NumberUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static ru.disdev.utils.IOUtils.getResourceAsReader;

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
                loadFourth();
                updateProgress(4, 4);
                return null;
            }

            protected void updateProgress(long workDone, long max) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ignored) {
                }
                super.updateProgress(workDone, max);
            }
        };
    }

    private void loadFirst() throws IOException {
        try (CSVReader reader = new CSVReader(getResourceAsReader("/1.csv"), ';')) {
            Set<Double> primaryKeys = new HashSet<>();
            reader.readAll().forEach(line -> {
                double key = Double.parseDouble(line[0]);
                primaryKeys.add(key);
                ShaftPosition position = ShaftPosition.valueOf(line[1]);
                double khl = NumberUtils.parseDouble(line[4]).orElse(1.);
                double kfl = NumberUtils.parseDouble(line[5]).orElse(1.);
                FirstTableData row = new FirstTableData(khl, kfl);
                String pk = MD5Utils.get(key, position, line[2], line[3]);
                FirstTableHolder.getInstance().put(pk, row);
            });
            FirstTableHolder.getInstance().initKeys(primaryKeys);
        }
    }

    private void loadSecond() throws IOException {
        try (CSVReader reader = new CSVReader(getResourceAsReader("/2.csv"), ';')) {
            Set<Double> values = new HashSet<>();
            reader.readAll().forEach(line -> {
                values.add(Double.parseDouble(line[1]));
            });
            SecondTableHolder.getInstance().initValues(values);
        }
    }

    private void loadThird() throws IOException {
        try (CSVReader reader = new CSVReader(getResourceAsReader("/3.csv"), ';')) {
            reader.readAll().forEach(line -> {
                Material material = Material.valueOf(line[0]);
                CircleType type = CircleType.valueOf(line[1]);
                double Ka = Double.parseDouble(line[2]);
                double Kb = Double.parseDouble(line[3]);
                double Zm = Double.parseDouble(line[4]);
                ThirdTableData data = new ThirdTableData(Ka, Kb, Zm);
                ThirdTableHolder.getInstance().put(MD5Utils.get(material, type), data);
            });
        }
    }

    private void loadFourth() throws IOException {
        try (CSVReader reader = new CSVReader(getResourceAsReader("/4.csv"), ';')) {
            Set<Double> xVariants = new HashSet<>();
            Set<Double> zVariants = new HashSet<>();
            reader.readAll().forEach(line -> {
                double z = Double.parseDouble(line[0]);
                zVariants.add(z);
                double x = Double.parseDouble(line[1]);
                xVariants.add(x);
                double value = NumberUtils.parseDouble(line[2]).orElse(3.);
                FourthTableHolder.getInstance().put(MD5Utils.get(z, x), value);
            });
            FourthTableHolder.getInstance().init(zVariants, xVariants);
        }
    }


}

