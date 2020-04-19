package sample.grid;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum HexagonalNeighbourhoodKind {
    LEFT("lewe", 0),
    RIGHT("prawe", 1),
    RANDOM("losowe", 2);

    private String name;
    private int intValue;

    HexagonalNeighbourhoodKind(String name, int intValue) {
        this.name = name;
        this.intValue = intValue;
    }

    public static List<String> getNames() {
        return Arrays.stream(HexagonalNeighbourhoodKind.values())
                .map(neighbourhoodKind -> neighbourhoodKind.name)
                .collect(toList());
    }

    public static HexagonalNeighbourhoodKind getNeighbourhoodKindForName(String name) {
        return Arrays.stream(HexagonalNeighbourhoodKind.values())
                .filter(neighbourhoodKind -> neighbourhoodKind.name.equals(name))
                .findFirst()
                .orElse(RANDOM);
    }

    public int getIntValue() {
        return intValue;
    }
}
