package sample.neighbourhood;

import sample.grid.CellCoordinates;
import sample.grid.CurrentGrid;
import sample.options.BoundaryCondition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static sample.options.BoundaryCondition.PERIODICAL;

public class RayNeighbourhood extends Neighbourhood {
    private final Function<BoundaryCondition, Boolean> IS_PERIODICAL = PERIODICAL::equals;
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

                CellCoordinates cellRandomPoint = grid.getCellRandomPoint(heighIndex, widthIndex);
                int distance = cellRandomPoint.calculateDistance(cellCenter);
                if (IS_PERIODICAL.apply(boundaryCondition)) {
                    if (heighIndex < 0)
                        heighIndex = grid.getHeight() + heighIndex;
                    else
                        heighIndex = heighIndex % (grid.getHeight() * grid.getScale());
                    if (widthIndex < 0)
                        widthIndex = grid.getWidth() + widthIndex;
                    else
                        widthIndex = widthIndex % (grid.getWidth() * grid.getScale());
                }
                if (distance <= ray) {
                    addToMap(neighbours, grid.getCell(heighIndex, widthIndex));
                }
            }
        }
        return neighbours.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(0);
    }
}
