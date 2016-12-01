package ru.disdev.holder;


import ru.disdev.entity.input.enums.ShaftPosition;
import ru.disdev.entity.input.enums.StrengthTeeth;
import ru.disdev.entity.table.FirstTableData;
import ru.disdev.utils.ArrayUtils;
import ru.disdev.utils.MD5Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FirstTableHolder {

    private static FirstTableHolder ourInstance = new FirstTableHolder();

    public static FirstTableHolder getInstance() {
        return ourInstance;
    }

    private Map<String, FirstTableData> dataMap = new HashMap<>();
    private Set<Double> primaryKeys = new HashSet<>();


    public void put(String key, FirstTableData value) {
        dataMap.put(key, value);
    }


    public void initKeys(Set<Double> keys) {
        primaryKeys.addAll(keys);
    }

    public FirstTableData find(double b, ShaftPosition position, StrengthTeeth teeth) {
        double key = ArrayUtils.findNearest(b, primaryKeys);
        String jv = "-";
        if (position == ShaftPosition.NOT_SYM) {
            jv = "Жесткий вал";
        }
        String hb = ">350";
        if (teeth == StrengthTeeth.LT_350) {
            hb = "<350";
        }
        return dataMap.get(MD5Utils.get(key, position, jv, hb));
    }

    private FirstTableHolder() {

    }
}
