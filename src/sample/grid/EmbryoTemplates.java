package sample.grid;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EmbryoTemplates {
    private final static List<String> TEMPLATES = Arrays.asList("komp. własna", "jednorodne", "losowe", "z promieniem");

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
            case "z promieniem":
                return null;
            default:
                throw new Exception("Nieznany wzorzec");
        }
    }

    public static String getPromptTextForTemplate(String template) {
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
        int widthDelta = width / (embryosNumber + 1);
        int heightDelta = height / (embryosNumber + 1);
        for (int i = 1; i <= embryosNumber; i++) {
            for (int j = 1; j <= embryosNumber; j++) {
                currentGrid.incrementCellValue(heightDelta * i, widthDelta * j);
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
        }
        return currentGrid;
    }
}