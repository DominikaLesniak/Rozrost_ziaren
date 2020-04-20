package sample.grid;

import static java.lang.Math.pow;

public class CellCoordinates {
    private int width;
    private int height;

    public int calculateDistance(CellCoordinates cell) {
        return (int) Math.sqrt(pow(this.height - cell.height, 2) + pow(this.width - cell.width, 2));
    }

    public CellCoordinates(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
