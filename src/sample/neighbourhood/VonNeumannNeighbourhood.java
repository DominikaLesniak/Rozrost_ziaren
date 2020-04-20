package sample.neighbourhood;

import sample.grid.CurrentGrid;
import sample.options.BoundaryCondition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class VonNeumannNeighbourhood extends Neighbourhood {
    @Override
    public Integer findBestNeighbour(CurrentGrid grid, int x, int y, BoundaryCondition boundaryCondition) {
        int leftIndex = boundaryCondition.getLeftIndex(grid.getWidth(), x);
        int rightIndex = boundaryCondition.getRightIndex(grid.getWidth(), x);
        int upperIndex = boundaryCondition.getUpperIndex(grid.getHeight(), y);
        int lowerIndex = boundaryCondition.getLowerIndex(grid.getHeight(), y);

        Map<Integer, Integer> neighbours = new HashMap<>();
        addToMap(neighbours, grid.getCell(upperIndex, x));
        addToMap(neighbours, grid.getCell(y, leftIndex));
        addToMap(neighbours, grid.getCell(y, rightIndex));
        addToMap(neighbours, grid.getCell(lowerIndex, x));

        return neighbours.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(0);
    }
}