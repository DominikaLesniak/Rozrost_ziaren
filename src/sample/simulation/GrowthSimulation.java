package sample.simulation;

import sample.grid.CurrentGrid;
import sample.neighbourhood.HexagonalNeighbourhood;
import sample.neighbourhood.Neighbourhood;
import sample.neighbourhood.PentagonalRandomNeighbourhood;
import sample.options.BoundaryCondition;
import sample.options.HexagonalNeighbourhoodKind;

import java.util.Random;

public class GrowthSimulation {
    private Neighbourhood neighbourhood;
    private BoundaryCondition boundaryCondition;
    private HexagonalNeighbourhoodKind neighbourhoodKind;

    public GrowthSimulation(Neighbourhood neighbourhood, BoundaryCondition boundaryCondition, HexagonalNeighbourhoodKind neighbourhoodKind) {
        this.neighbourhood = neighbourhood;
        this.boundaryCondition = boundaryCondition;
        this.neighbourhoodKind = neighbourhoodKind;
    }

    public void generateNextStep(CurrentGrid currentGrid) {
        int[][] grid = new int[currentGrid.getHeight()][currentGrid.getWidth()];
        if (neighbourhood instanceof PentagonalRandomNeighbourhood) {
            Random random = new Random();
            int randomCase = (int) (random.nextDouble() * 4);
            neighbourhood.setGenerationCase(randomCase);
        } else if (neighbourhood instanceof HexagonalNeighbourhood) {
            if (HexagonalNeighbourhoodKind.RANDOM.equals(neighbourhoodKind)) {
                Random random = new Random();
                int randomCase = random.nextBoolean() ? 0 : 1;
                neighbourhood.setGenerationCase(randomCase);
            } else
                neighbourhood.setGenerationCase(neighbourhoodKind.getIntValue());
        }

        for (int i = 0; i < currentGrid.getHeight(); i++) {
            for (int j = 0; j < currentGrid.getWidth(); j++) {
                if (currentGrid.getCell(i, j) != 0)
                    grid[i][j] = currentGrid.getCell(i, j);
                else {
                    int bestNeighbour = neighbourhood.findBestNeighbour(currentGrid, j, i, boundaryCondition);
                    grid[i][j] = bestNeighbour;
                }
            }
        }
        currentGrid.setGrid(grid);
    }
}
