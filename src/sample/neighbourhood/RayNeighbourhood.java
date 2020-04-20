package sample.neighbourhood;

import sample.grid.CellCoordinates;
import sample.grid.CurrentGrid;
import sample.options.BoundaryCondition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RayNeighbourhood extends Neighbourhood {
    private int ray;

    public RayNeighbourhood(int ray) {
        this.ray = ray;
    }

    @Override
    public Integer findBestNeighbour(CurrentGrid grid, int x, int y, BoundaryCondition boundaryCondition) {
        Map<Integer, Integer> neighbours = new HashMap<>();
        int rangeOfCells = ray / grid.getScale();
        CellCoordinates cellCenter = grid.getCellCenter(y, x);

        for (int i = -rangeOfCells; i <= rangeOfCells; i++) {
            for (int j = -rangeOfCells; j <= rangeOfCells; j++) {
                int heighIndex = y + i;
                int widthIndex = x + j;
                if (BoundaryCondition.PERIODICAL.equals(boundaryCondition)) {
                    if (heighIndex < 0)
                        heighIndex = grid.getHeight() - heighIndex;
                    else
                        heighIndex = heighIndex % grid.getHeight();
                    if (widthIndex < 0)
                        widthIndex = grid.getWidth() - widthIndex;
                    else
                        widthIndex = widthIndex % grid.getWidth();
                }
                CellCoordinates cellRandomPoint = grid.getCellRandomPoint(heighIndex, widthIndex);
                if (cellRandomPoint.calculateDistance(cellCenter) <= ray)
                    addToMap(neighbours, grid.getCell(heighIndex, widthIndex));
            }
        }
        return neighbours.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(0);
    }
}
