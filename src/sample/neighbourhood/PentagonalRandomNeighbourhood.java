package sample.neighbourhood;

import sample.options.BoundaryCondition;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PentagonalRandomNeighbourhood extends Neighbourhood {
    @Override
    public Integer findBestNeighbour(int[][] grid, int x, int y, BoundaryCondition boundaryCondition) {
        int leftIndex = boundaryCondition.getLeftIndex(grid[0].length, x);
        int rightIndex = boundaryCondition.getRightIndex(grid[0].length, x);
        int upperIndex = boundaryCondition.getUpperIndex(grid.length, y);
        int lowerIndex = boundaryCondition.getLowerIndex(grid.length, y);

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

    private Map<Integer, Integer> verticalCase(int[][] grid, int x, int y, int leftIndex, int rightIndex, int verticalIndex) {
        Map<Integer, Integer> neighbours = new HashMap<>();
        if (INDEX_CHECK.apply(verticalIndex)) {
            addToMap(neighbours, grid[verticalIndex][x]);
            if (INDEX_CHECK.apply(leftIndex)) {
                addToMap(neighbours, grid[verticalIndex][leftIndex]);
            }
            if (INDEX_CHECK.apply(rightIndex)) {
                addToMap(neighbours, grid[verticalIndex][rightIndex]);
            }
        }
        if (INDEX_CHECK.apply(leftIndex)) {
            addToMap(neighbours, grid[y][leftIndex]);
        }
        if (INDEX_CHECK.apply(rightIndex)) {
            addToMap(neighbours, grid[y][rightIndex]);
        }
        return neighbours;
    }

    private Map<Integer, Integer> horizontalCase(int[][] grid, int x, int y, int upperIndex, int lowerIndex, int horizontalIndex) {
        Map<Integer, Integer> neighbours = new HashMap<>();
        if (INDEX_CHECK.apply(upperIndex)) {
            addToMap(neighbours, grid[upperIndex][x]);
            if (INDEX_CHECK.apply(horizontalIndex)) {
                addToMap(neighbours, grid[upperIndex][horizontalIndex]);
            }
        }
        if (INDEX_CHECK.apply(horizontalIndex)) {
            addToMap(neighbours, grid[y][horizontalIndex]);
        }
        if (INDEX_CHECK.apply(lowerIndex)) {
            addToMap(neighbours, grid[lowerIndex][x]);
            if (INDEX_CHECK.apply(horizontalIndex)) {
                addToMap(neighbours, grid[lowerIndex][horizontalIndex]);
            }
        }
        return neighbours;
    }
}