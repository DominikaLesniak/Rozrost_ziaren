package sample.simulation;

import sample.grid.BoundaryCondition;
import sample.grid.CurrentGrid;
import sample.neighbourhood.Neighbourhood;

public class LifeSimulation {
    private Neighbourhood neighbourhood;
    private BoundaryCondition boundaryCondition;

    public LifeSimulation(Neighbourhood neighbourhood, BoundaryCondition boundaryCondition) {
        this.neighbourhood = neighbourhood;
        this.boundaryCondition = boundaryCondition;
    }

    public void generateNextGeneration(CurrentGrid currentGrid) {
        int[][] oldGrid = currentGrid.getGrid();
        int[][] grid = new int[currentGrid.getHeight()][currentGrid.getWidth()];

        for (int i = 0; i < currentGrid.getHeight(); i++) {
            for (int j = 0; j < currentGrid.getWidth(); j++) {
                if (oldGrid[i][j] != 0)
                    grid[i][j] = oldGrid[i][j];
                else {
                    int bestNeighbour = neighbourhood.findBestNeighbour(oldGrid, j, i, boundaryCondition);
                    grid[i][j] = bestNeighbour;
                }
            }
        }
        currentGrid.setGrid(grid);
    }

    public void setNeighbourhood(Neighbourhood neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public void setBoundaryCondition(BoundaryCondition boundaryCondition) {
        this.boundaryCondition = boundaryCondition;
    }
}
