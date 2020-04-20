package sample.grid;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class CurrentGrid {
    private int width;
    private int height;
    private int scale;
    private int embryosNumber;
    private Map<Integer, Color> colorMap;

    private int[][] grid;

    public CurrentGrid(int width, int height, int scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.embryosNumber = 0;
        this.colorMap = new HashMap<>();
        colorMap.put(0, Color.WHITE);
        this.grid = initiateGrid(this.height, this.width);
    }

    public CurrentGrid(int width, int height, int scale, int embryosNumber) {
        this.width = width/scale;
        this.height = height/scale;
        this.scale = scale;
        this.embryosNumber = embryosNumber;
        this.colorMap = new HashMap<>();
        colorMap.put(0, Color.WHITE);
        this.grid = initiateGrid(this.height, this.width);
    }

    public void incrementCellValue(int i, int j) {
        int newValue;
        if (grid[i][j] == 0) {
            newValue = ++embryosNumber;
            Color newColor = generateNewColor();
            colorMap.put(newValue, newColor);
        }
        else
            newValue = 0;
        grid[i][j] = newValue;
    }

    public void resizeGrid(int newWidth, int newHeight, int scale) {
        int[][] newGrid = initiateGrid(newHeight, newWidth);

        int widthBorder = Math.min(newWidth, width);
        int heightBorder = Math.min(newHeight, height);

        for (int i = 0; i < heightBorder; i++) {
            for (int j = 0; j < widthBorder; j++) {
                newGrid[i][j] = grid[i][j];
            }
        }
        this.height = newHeight;
        this.width = newWidth;
        this.scale = scale;
        this.grid = newGrid;
    }

    public void resetGrid() {
        this.grid = initiateGrid(height, width);
    }

    private Color generateNewColor() {
        Color color = Color.color(Math.random(), Math.random(), Math.random());
        if(colorMap.containsValue(color))
            return generateNewColor();
        else
            return color;
    }

    private int[][] initiateGrid(int height, int width) {
        int[][]grid = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = 0;
            }
        }
        return grid;
    }

    public Color getColorForValue(int value) {
        return colorMap.get(value);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getScale() {
        return scale;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getCell(int i, int j) {
        return grid[i][j];
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public int getEmbryosNumber() {
        return embryosNumber;
    }

    public void print() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}