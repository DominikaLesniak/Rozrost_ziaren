package sample.neighbourhood;

import sample.grid.CurrentGrid;
import sample.options.BoundaryCondition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RayNeighbourhood extends Neighbourhood {
    @Override
    public Integer findBestNeighbour(CurrentGrid grid, int x, int y, BoundaryCondition boundaryCondition) {
        Map<Integer, Integer> neighbours = new HashMap<>();


        return neighbours.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(0);
    }
}
