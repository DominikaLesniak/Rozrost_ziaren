package sample.neighbourhood;

import sample.grid.CurrentGrid;
import sample.options.BoundaryCondition;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class HexagonalNeighbourhood extends Neighbourhood {
    private final static int LEFT_CASE = 0;
    private final static int RIGHT_CASE = 1;

    @Override
    public Integer findBestNeighbour(CurrentGrid grid, int x, int y, BoundaryCondition boundaryCondition) {
        int leftIndex = boundaryCondition.getLeftIndex(grid.getWidth(), x);
        int rightIndex = boundaryCondition.getRightIndex(grid.getWidth(), x);
        int upperIndex = boundaryCondition.getUpperIndex(grid.getHeight(), y);
        int lowerIndex = boundaryCondition.getLowerIndex(grid.getHeight(), y);

        Map<Integer, Integer> neighbours;
        switch (generationCase) {
            case LEFT_CASE:
                neighbours = verticalCase(grid, x, y, rightIndex, leftIndex, upperIndex, lowerIndex);
                break;
            case RIGHT_CASE:
                neighbours = verticalCase(grid, x, y, leftIndex, rightIndex, upperIndex, lowerIndex);
                break;
            default:
                neighbours = Collections.emptyMap();
        }

        return neighbours.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(0);
    }

    private Map<Integer, Integer> verticalCase(CurrentGrid grid, int x, int y, int side1Index, int side2Index, int upperIndex, int lowerIndex) {
        Map<Integer, Integer> neighbours = new HashMap<>();
        if (INDEX_CHECK.apply(upperIndex)) {
            addToMap(neighbours, grid.getCell(upperIndex, x));
            if (INDEX_CHECK.apply(side1Index)) {
                addToMap(neighbours, grid.getCell(upperIndex, side1Index));
            }
        }
        if (INDEX_CHECK.apply(side1Index)) {
            addToMap(neighbours, grid.getCell(y, side1Index));
        }
        if (INDEX_CHECK.apply(side2Index)) {
            addToMap(neighbours, grid.getCell(y, side2Index));
        }
        if (INDEX_CHECK.apply(lowerIndex)) {
            addToMap(neighbours, grid.getCell(lowerIndex, x));
            if (INDEX_CHECK.apply(side2Index)) {
                addToMap(neighbours, grid.getCell(lowerIndex, side2Index));
            }
        }
        return neighbours;
    }
}
