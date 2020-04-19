package sample.neighbourhood;

import sample.grid.BoundaryCondition;

import java.util.Map;
import java.util.function.Function;

public abstract class Neighbourhood {
    final Function<Integer, Boolean> INDEX_CHECK = x -> x >= 0;
    int generationCase;

    public abstract Integer findBestNeighbour(int[][] grid, int x, int y, BoundaryCondition boundaryCondition);

    void addToMap(Map<Integer, Integer> neighbours, int id) {
        if (id != 0) {
            if (neighbours.containsKey(id))
                neighbours.replace(id, neighbours.get(id) + 1);
            else
                neighbours.put(id, 1);
        }
    }

    public void setGenerationCase(int generationCase) {
        this.generationCase = generationCase;
    }
}
