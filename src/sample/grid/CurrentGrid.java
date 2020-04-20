package sample.grid;

import javafx.scene.paint.Color;

import java.util.*;

public class CurrentGrid {
    private int width;
    private int height;
    private int scale;
    private int embryosNumber;
    private Random random;
    private Map<Integer, Color> colorMap;
    public List<CellCoordinates> cells;

    private int[][] grid;

    public CurrentGrid(int width, int height, int scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.embryosNumber = 0;
        this.colorMap = new HashMap<>();
        this.random = new Random();
        cells = new ArrayList<>();
        colorMap.put(0, Color.WHITE);
        this.grid = initiateGrid(this.height, this.width);
    }

    public CurrentGrid(int width, int height, int scale, int embryosNumber) {
        this.width = width/scale;
        this.height = height/scale;
        this.scale = scale;
        this.embryosNumber = embryosNumber;
        this.colorMap = new HashMap<>();
        this.random = new Random();
        colorMap.put(0, Color.WHITE);
        cells = new ArrayList<>();
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

    public int getCell(int height, int width) {
        if (height < 0 || height > this.height - 1 || width < 0 || width > this.width - 1)
            return 0;
        return grid[height][width];
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

    public CellCoordinates getCellCenter(int height, int width) {
        int centerWidth = (int) (width * scale + 0.5 * scale);
        int centerHeight = (int) (height * scale + 0.5 * scale);
        return new CellCoordinates(centerWidth, centerHeight);
    }

    public CellCoordinates getCellRandomPoint(int height, int width) {
        int centerWidth = (int) (width * scale + random.nextDouble() * scale);
        int centerHeight = (int) (height * scale + random.nextDouble() * scale);
        return new CellCoordinates(centerWidth, centerHeight);
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