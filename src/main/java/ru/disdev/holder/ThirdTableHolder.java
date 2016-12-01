package ru.disdev.holder;

import ru.disdev.entity.input.enums.CircleType;
import ru.disdev.entity.input.enums.Material;
import ru.disdev.entity.table.ThirdTableData;
import ru.disdev.utils.MD5Utils;

import java.util.HashMap;
import java.util.Map;

public class ThirdTableHolder {
    private static ThirdTableHolder ourInstance = new ThirdTableHolder();

    public static ThirdTableHolder getInstance() {
        return ourInstance;
    }

    private Map<String, ThirdTableData> dataMap = new HashMap<>();

    public void put(String key, ThirdTableData value) {
        dataMap.put(key, value);
    }

    public ThirdTableData find(Material material, CircleType type) {
        return dataMap.get(MD5Utils.get(material, type));
    }

    private ThirdTableHolder() {
    }
}
