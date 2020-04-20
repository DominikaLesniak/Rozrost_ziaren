package sample.neighbourhood;

import sample.grid.CurrentGrid;
import sample.options.BoundaryCondition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class VonNeumannNeighbourhood extends Neighbourhood {
    private Function<Integer, Boolean> INDEX_CHECK = x -> x >= 0;

    @Override
    public Integer findBestNeighbour(CurrentGrid grid, int x, int y, BoundaryCondition boundaryCondition) {
        int leftIndex = boundaryCondition.getLeftIndex(grid.getWidth(), x);
        int rightIndex = boundaryCondition.getRightIndex(grid.getWidth(), x);
        int upperIndex = boundaryCondition.getUpperIndex(grid.getHeight(), y);
        int lowerIndex = boundaryCondition.getLowerIndex(grid.getHeight(), y);

        Map<Integer, Integer> neighbours = new HashMap<>();
        if (INDEX_CHECK.apply(upperIndex)) {
            addToMap(neighbours, grid.getCell(upperIndex, x));
        }
        if (INDEX_CHECK.apply(leftIndex)) {
            addToMap(neighbours, grid.getCell(y, leftIndex));
        }
        if (INDEX_CHECK.apply(rightIndex)) {
            addToMap(neighbours, grid.getCell(y, rightIndex));
        }
        if (INDEX_CHECK.apply(lowerIndex)) {
            addToMap(neighbours, grid.getCell(lowerIndex, x));
        }
        return neighbours.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(0);
    }
}
