package sample.grid;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum BoundaryCondition {
    ABSORBING("absorbujące"),
    PERIODICAL("periodyczne");
    private String name;

    BoundaryCondition(String name) {
        this.name = name;
    }

    public static BoundaryCondition getBoundaryConditionForName(String name) {
        return Arrays.stream(BoundaryCondition.values())
                .filter(boundaryCondition -> boundaryCondition.name.equals(name))
                .findFirst()
                .orElse(PERIODICAL);
    }

    public static List<String> getNames() {
        return Arrays.stream(BoundaryCondition.values())
                .map(boundaryCondition -> boundaryCondition.name)
                .collect(toList());
    }

    public int getLeftIndex(int width, int x) {
        if (x == 0 && this.equals(BoundaryCondition.PERIODICAL)) {
            return width - 1;
        } else {
            return x - 1;
        }
    }

    public int getRightIndex(int width, int x) {
        if (x == width - 1) {
            return this.equals(BoundaryCondition.PERIODICAL) ? 0 : -1;
        } else {
            return x + 1;
        }
    }

    public int getUpperIndex(int height, int y) {
        if (y == 0 && this.equals(BoundaryCondition.PERIODICAL)) {
            return height - 1;
        } else {
            return y - 1;
        }
    }

    public int getLowerIndex(int height, int y) {
        if (y == height - 1) {
            return this.equals(BoundaryCondition.PERIODICAL) ? 0 : -1;
        } else {
            return y + 1;
        }
    }
}
