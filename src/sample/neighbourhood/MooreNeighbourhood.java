package sample.neighbourhood;

import sample.grid.BoundaryCondition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MooreNeighbourhood extends Neighbourhood {
    @Override
    public Integer findBestNeighbour(int[][] grid, int x, int y, BoundaryCondition boundaryCondition) {
        int leftIndex = boundaryCondition.getLeftIndex(grid[0].length, x);
        int rightIndex = boundaryCondition.getRightIndex(grid[0].length, x);
        int upperIndex = boundaryCondition.getUpperIndex(grid.length, y);
        int lowerIndex = boundaryCondition.getLowerIndex(grid.length, y);

        Map<Integer, Integer> neighbours = new HashMap<>();
        if (INDEX_CHECK.apply(upperIndex)) {
            addToMap(neighbours, grid[upperIndex][x]);
            if (INDEX_CHECK.apply(leftIndex)) {
                addToMap(neighbours, grid[upperIndex][leftIndex]);
            }
            if (INDEX_CHECK.apply(rightIndex)) {
                addToMap(neighbours, grid[upperIndex][rightIndex]);
            }
        }
        if (INDEX_CHECK.apply(leftIndex)) {
            addToMap(neighbours, grid[y][leftIndex]);
        }
        if (INDEX_CHECK.apply(rightIndex)) {
            addToMap(neighbours, grid[y][rightIndex]);
        }
        if (INDEX_CHECK.apply(lowerIndex)) {
            addToMap(neighbours, grid[lowerIndex][x]);
            if (INDEX_CHECK.apply(leftIndex)) {
                addToMap(neighbours, grid[lowerIndex][leftIndex]);
            }
            if (INDEX_CHECK.apply(rightIndex)) {
                addToMap(neighbours, grid[lowerIndex][rightIndex]);
            }
        }
        return neighbours.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(0);
    }
}
