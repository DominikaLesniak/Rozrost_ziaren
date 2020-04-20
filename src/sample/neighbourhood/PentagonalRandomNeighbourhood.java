package sample.neighbourhood;

import sample.grid.CurrentGrid;
import sample.options.BoundaryCondition;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PentagonalRandomNeighbourhood extends Neighbourhood {
    @Override
    public Integer findBestNeighbour(CurrentGrid grid, int x, int y, BoundaryCondition boundaryCondition) {
        int leftIndex = boundaryCondition.getLeftIndex(grid.getWidth(), x);
        int rightIndex = boundaryCondition.getRightIndex(grid.getWidth(), x);
        int upperIndex = boundaryCondition.getUpperIndex(grid.getHeight(), y);
        int lowerIndex = boundaryCondition.getLowerIndex(grid.getHeight(), y);

        Map<Integer, Integer> neighbours;
        switch (generationCase) {
            case 0:
                neighbours = verticalCase(grid, x, y, leftIndex, rightIndex, upperIndex);
                break;
            case 1:
                neighbours = verticalCase(grid, x, y, leftIndex, rightIndex, lowerIndex);
                break;
            case 2:
                neighbours = horizontalCase(grid, x, y, upperIndex, lowerIndex, rightIndex);
                break;
            case 3:
                neighbours = horizontalCase(grid, x, y, upperIndex, lowerIndex, leftIndex);
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

    private Map<Integer, Integer> verticalCase(CurrentGrid grid, int x, int y, int leftIndex, int rightIndex, int verticalIndex) {
        Map<Integer, Integer> neighbours = new HashMap<>();
        addToMap(neighbours, grid.getCell(verticalIndex, x));
        addToMap(neighbours, grid.getCell(verticalIndex, leftIndex));
        addToMap(neighbours, grid.getCell(verticalIndex, rightIndex));
        addToMap(neighbours, grid.getCell(y, leftIndex));
        addToMap(neighbours, grid.getCell(y, rightIndex));
        return neighbours;
    }

    private Map<Integer, Integer> horizontalCase(CurrentGrid grid, int x, int y, int upperIndex, int lowerIndex, int horizontalIndex) {
        Map<Integer, Integer> neighbours = new HashMap<>();
        addToMap(neighbours, grid.getCell(upperIndex, x));
        addToMap(neighbours, grid.getCell(upperIndex, horizontalIndex));
        addToMap(neighbours, grid.getCell(y, horizontalIndex));
        addToMap(neighbours, grid.getCell(lowerIndex, x));
        addToMap(neighbours, grid.getCell(lowerIndex, horizontalIndex));
        return neighbours;
    }
}