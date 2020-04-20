package sample.simulation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.grid.CellCoordinates;
import sample.grid.CurrentGrid;

public class CellPainter {
    private GraphicsContext gc;
    private int scale;

    public CellPainter(GraphicsContext gc, int scale) {
        this.gc = gc;
        this.scale = scale;
    }

    public void repaintCellFromClickedPixel(CurrentGrid currentGrid, int i, int j) {
        int iResidue = i % scale;
        int jResidue = j % scale;
        int iStartPixel = i - iResidue;
        int jStartPixel = j - jResidue;
        int x = iStartPixel / currentGrid.getScale();
        int y = jStartPixel / currentGrid.getScale();
        if (y >= currentGrid.getGrid().length || x >= currentGrid.getGrid()[0].length)
            return;
        currentGrid.incrementCellValue(y, x);
        int value = currentGrid.getCell(y, x);
        paintCell(iStartPixel, jStartPixel, value, currentGrid);
    }

    public void paintCurrentGridCells(CurrentGrid currentGrid) {
        for (int i = 0; i < currentGrid.getHeight(); i++) {
            for (int j = 0; j < currentGrid.getWidth(); j++) {
                int value = currentGrid.getCell(i, j);
                int iStartPixel = i * currentGrid.getScale();
                int jStartPixel = j * currentGrid.getScale();
                paintCell(jStartPixel, iStartPixel, value, currentGrid);
            }
        }
        //paintCircles(currentGrid, 50);
    }

    private void paintCell(int i, int j, int value, CurrentGrid currentGrid) {
        Color color = currentGrid.getColorForValue(value);
        gc.setFill(color);
        gc.fillRect(i, j,
                currentGrid.getScale(), currentGrid.getScale());
    }

    public void paintCircles(CurrentGrid currentGrid, int ray) {
        currentGrid.cells.stream()
                .map(cellCoordinates -> currentGrid.getCellCenter(cellCoordinates.getHeight(), cellCoordinates.getWidth()))
                .forEach(cell -> paintCircle(cell, ray));
    }

    private void paintCircle(CellCoordinates cell, int ray) {
        gc.setFill(Color.BLACK);
        gc.strokeOval(cell.getHeight() - ray, cell.getWidth() - ray, 2 * ray, 2 * ray);
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}