package sample.neighbourhood;

import sample.grid.CurrentGrid;
import sample.options.BoundaryCondition;

import java.util.Map;

public abstract class Neighbourhood {
    int generationCase;

    public abstract Integer findBestNeighbour(CurrentGrid grid, int x, int y, BoundaryCondition boundaryCondition);

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