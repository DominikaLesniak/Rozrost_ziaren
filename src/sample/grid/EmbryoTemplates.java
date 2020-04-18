package sample.grid;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EmbryoTemplates {
    private final static List<String> TEMPLATES = Arrays.asList("komp. własna", "jednorodne", "losowe", "z promieniem");

    public static List<String> getTemplates() {
        return TEMPLATES;
    }

    public static CurrentGrid getGridForTemplate(String template, int width, int height, int embryosNumber) throws Exception {
        return getGridForTemplate(template, width, height, embryosNumber, 0);
    }

    public static CurrentGrid getGridForTemplate(String template, int width, int height, int embryosNumber, int ray) throws Exception {
        switch (template) {
            case "komp. własna":
                return null;
            case "jednorodne":
                return getUnchangeableGrid(width, height);
            case "losowe":
                return getGliderGrid(width, height);
            case "z promieniem":
                return getOscilatorGrid(width, height);
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

    private static CurrentGrid getUnchangeableGrid(int width, int height) {
        if(width < 6)
            width = 6;
        if(height < 5)
            height = 5;
        CurrentGrid currentGrid = new CurrentGrid(width, height);
        int widthHalf = width/2;
        int heightHalf = height/2-1;
        currentGrid.incrementCellValue(heightHalf, widthHalf);
        currentGrid.incrementCellValue(heightHalf, widthHalf+1);
        currentGrid.incrementCellValue(heightHalf+1, widthHalf-1);
        currentGrid.incrementCellValue(heightHalf+1, widthHalf+2);
        currentGrid.incrementCellValue(heightHalf+2, widthHalf);
        currentGrid.incrementCellValue(heightHalf+2, widthHalf+1);
        return currentGrid;
    }

    private static CurrentGrid getGliderGrid(int width, int height){
        if(width < 5)
            width = 5;
        if(height < 5)
            height = 5;
        CurrentGrid currentGrid = new CurrentGrid(width, height);
        int widthHalf = width/2;
        int heightHalf = height/2-1;
        currentGrid.incrementCellValue(heightHalf, widthHalf+1);
        currentGrid.incrementCellValue(heightHalf, widthHalf+2);
        currentGrid.incrementCellValue(heightHalf+1, widthHalf);
        currentGrid.incrementCellValue(heightHalf+1, widthHalf+1);
        currentGrid.incrementCellValue(heightHalf+2, widthHalf+2);
        return currentGrid;
    }

    private static CurrentGrid getOscilatorGrid(int width, int height) {
        if(width < 3)
            width = 3;
        if(height < 5)
            height = 5;
        CurrentGrid currentGrid = new CurrentGrid(width, height);
        int widthHalf = width/2;
        int heightHalf = height/2-1;
        currentGrid.incrementCellValue(heightHalf, widthHalf+1);
        currentGrid.incrementCellValue(heightHalf+1, widthHalf+1);
        currentGrid.incrementCellValue(heightHalf+2, widthHalf+1);
        return currentGrid;
    }

    private static CurrentGrid getRandomGrid(int width, int height) {
        CurrentGrid currentGrid = new CurrentGrid(width, height);
        Random random = new Random();
        int numOfRandoms = width*height / 3;
        double[] randomDoubles = random.doubles(numOfRandoms).toArray();
        for (int i = 0; i < randomDoubles.length - 1; i+=2) {
            int x = (int) (randomDoubles[i]*height);
            int y = (int) (randomDoubles[i+1] * width);
            currentGrid.incrementCellValue(x, y);
        }
        return currentGrid;
    }
}