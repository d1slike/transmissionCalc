package ru.disdev.holder;

import ru.disdev.entity.input.enums.Curing;
import ru.disdev.entity.input.enums.LoadType;
import ru.disdev.entity.table.FifthTableData;
import ru.disdev.utils.Rnd;

public class FifthTableHolder {
    private static FifthTableHolder ourInstance = new FifthTableHolder();

    public static FifthTableHolder getInstance() {
        return ourInstance;
    }

    private FifthTableHolder() {
    }

    private FifthTableData _1 = new FifthTableData(240, 4_000_000, 800, 60_000_000);
    private FifthTableData _2 = new FifthTableData(210, 4_000_000, 1100, 120_000_000);
    private FifthTableData _3 = new FifthTableData(165, 4_000_000, 800, 60_000_000);
    private FifthTableData _4 = new FifthTableData(200, 4_000_000, 550, 10_000_000);
    private FifthTableData _5 = new FifthTableData(270, 4_000_000, 900, 80_000_000);

    public FifthTableData get(Curing curing, LoadType type) {
        switch (Rnd.get(1, 5)) {
            case 1:
                return _1;
            case 2:
                return _2;
            case 3:
                return _3;
            case 4:
                return _4;
            case 5:
                return _5;
            default:
                return null;
        }
    }
}
