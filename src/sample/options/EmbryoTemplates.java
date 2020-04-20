package sample.options;

import sample.grid.CellCoordinates;
import sample.grid.CurrentGrid;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EmbryoTemplates {
    private final static List<String> TEMPLATES = Arrays.asList("komp. własna", "jednorodne", "losowe", "losowe z promieniem");

    public static List<String> getTemplates() {
        return TEMPLATES;
    }

    public static CurrentGrid getGridForTemplate(String template, int width, int height, int scale, int embryosNumber, int ray) throws Exception {
        switch (template) {
            case "komp. własna":
                return null;
            case "jednorodne":
                return getHomogeneousGrid(width, height, scale, embryosNumber);
            case "losowe":
                return getRandomGrid(width, height, scale, embryosNumber);
            case "losowe z promieniem":
                return getRayGrid(width, height, scale, embryosNumber, ray);
            default:
                throw new Exception("Nieznany wzorzec");
        }
    }

    public static String getLabelTextForTemplate(String template) {
        if ("jednorodne".equals(template)) {
            return "Ilość w rzędzie/kolumnie";
        }
        return "Liczba zarodków";
    }

    private static CurrentGrid getHomogeneousGrid(int width, int height, int scale, int embryosNumber) {
        if (width < embryosNumber)
            width = embryosNumber;
        if (height < embryosNumber)
            height = embryosNumber;
        CurrentGrid currentGrid = new CurrentGrid(width, height, scale);
        double widthDelta = (double) width / (double) (embryosNumber + 1);
        double heightDelta = (double) height / (double) (embryosNumber + 1);
        for (int i = 1; i <= embryosNumber; i++) {
            for (int j = 1; j <= embryosNumber; j++) {
                currentGrid.incrementCellValue((int) (heightDelta * i), (int) (widthDelta * j));
            }
        }
        return currentGrid;
    }

    private static CurrentGrid getRandomGrid(int width, int height, int scale, int embryosNumber) {
        CurrentGrid currentGrid = new CurrentGrid(width, height, scale);
        Random random = new Random();
        int numOfRandoms = embryosNumber * 2;
        double[] randomDoubles = random.doubles(numOfRandoms).toArray();
        for (int i = 0; i < randomDoubles.length - 1; i+=2) {
            int x = (int) (randomDoubles[i]*height);
            int y = (int) (randomDoubles[i+1] * width);
            currentGrid.incrementCellValue(x, y);
            currentGrid.cells.add(new CellCoordinates(x, y));
        }
        return currentGrid;
    }

    private static CurrentGrid getRayGrid(int width, int height, int scale, int embryosNumber, int ray) {
        CurrentGrid currentGrid = new CurrentGrid(width, height, scale);
        Random random = new Random();
        int rangeOfCells = ray / scale;
        if (ray % scale > scale / 2)
            rangeOfCells++;
        int failedTrials = 0;

        while (currentGrid.getEmbryosNumber() < embryosNumber && failedTrials < 100) {
            int x = (int) (random.nextDouble() * width);
            int y = (int) (random.nextDouble() * height);
            boolean spaceAvailableForEmbryo = true;
            for (int j = -rangeOfCells; j <= rangeOfCells; j++) {
                for (int k = -rangeOfCells; k <= rangeOfCells; k++) {
                    int heighIndex = y + j;
                    int widthIndex = x + k;
                    if (heighIndex < 0 || widthIndex < 0 || heighIndex > height - 1 || widthIndex > width - 1)
                        continue;
                    if (currentGrid.getCell(heighIndex, widthIndex) != 0) {
                        spaceAvailableForEmbryo = false;
                        break;
                    }
                }
                if (!spaceAvailableForEmbryo)
                    break;
            }
            if (spaceAvailableForEmbryo) {
                currentGrid.incrementCellValue(y, x);
                currentGrid.cells.add(new CellCoordinates(x, y));
                failedTrials = 0;
            } else
                failedTrials++;
        }
        return currentGrid;
    }
}