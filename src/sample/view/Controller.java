package sample.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import sample.grid.CurrentGrid;
import sample.options.BoundaryCondition;
import sample.options.EmbryoTemplates;
import sample.options.HexagonalNeighbourhoodKind;
import sample.options.Neighbourhoods;
import sample.simulation.CellPainter;
import sample.simulation.GrowthSimulation;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import static java.lang.Math.max;
import static sample.options.BoundaryCondition.getBoundaryConditionForName;
import static sample.options.HexagonalNeighbourhoodKind.getNeighbourhoodKindForName;
import static sample.options.Neighbourhoods.getNeighbourhoodForName;

public class Controller implements Initializable {
    private final Function<String, Boolean> CUSTOM_MODE_ON = "komp. własna"::equals;
    private static int CANVAS_HEIGHT;
    private static int CANVAS_WIDTH;
    private static int SCALE;
    private static String EMBRYO_MODE;

    @FXML
    private Canvas canvas;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button resizeButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button embryosGeneratorButton;
    @FXML
    private TextField widthTextField;
    @FXML
    private TextField heightTextField;
    @FXML
    private Label scaleLabel;
    @FXML
    private TextField embryosNumberTextField;
    @FXML
    private Label embryosNumberLabel;
    @FXML
    private TextField rayTextField;
    @FXML
    private Label rayLabel;
    @FXML
    private TextField neighbourhoodRayTextField;
    @FXML
    private Label neighbourhoodRayLabel;
    @FXML
    private ChoiceBox hexagonalKindChoiceBox;
    @FXML
    private ChoiceBox embryoCreationChoiceBox;
    @FXML
    private ChoiceBox boundaryConditionChoiceBox;
    @FXML
    private ChoiceBox neighbourhoodChoiceBox;

    private CellPainter painter;
    private GraphicsContext gc;
    private CurrentGrid currentGrid;
    private GrowthSimulation growthSimulation;
    private Timeline timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CANVAS_HEIGHT = (int) canvas.getHeight();
        CANVAS_WIDTH = (int) canvas.getWidth();
        SCALE = 10;
        EMBRYO_MODE = "komp. własna";
        gc = canvas.getGraphicsContext2D();
        currentGrid = new CurrentGrid(CANVAS_WIDTH, CANVAS_HEIGHT, SCALE, 0);
        painter = new CellPainter(gc, SCALE);
        clearCanvas();
        widthTextField.setText(currentGrid.getWidth() + "");
        heightTextField.setText(currentGrid.getHeight() + "");
        embryosGeneratorButton.setVisible(false);
        rayLabel.setVisible(false);
        rayTextField.setVisible(false);
        neighbourhoodRayTextField.setVisible(false);
        neighbourhoodRayLabel.setVisible(false);
        hexagonalKindChoiceBox.setVisible(false);

        setButtons();
        prepareChoiceBoxes();
        activateCanvas();
        timeline = new Timeline();
        timeline.setOnFinished(event -> {
            System.err.println("done");
            timeline.stop();
            timeline.getKeyFrames().clear();
        });
    }

    private void setButtons() {
        startButton.setOnAction(event -> {
            timeline.stop();
            String neighbourhood = (String) neighbourhoodChoiceBox.getValue();
            String boundaryCondition = (String) boundaryConditionChoiceBox.getValue();
            String hexagonalKind = (String) hexagonalKindChoiceBox.getValue();
            growthSimulation = new GrowthSimulation(getNeighbourhoodForName(neighbourhood),
                    getBoundaryConditionForName(boundaryCondition),
                    getNeighbourhoodKindForName(hexagonalKind));
            setTimeline();
            timeline.play();
            deactivateCanvas();
        });
        stopButton.setOnAction(event -> {
            timeline.stop();
            timeline.getKeyFrames().clear();
            if (CUSTOM_MODE_ON.apply(EMBRYO_MODE))
                activateCanvas();
        });
        resizeButton.setOnAction(event -> {
            resizeGrid();
        });
        resetButton.setOnAction(event -> {
            clearCanvas();
            currentGrid.resetGrid();
        });
        embryosGeneratorButton.setOnAction(event -> {
            String template = (String) embryoCreationChoiceBox.getValue();
            int embryosNumber = getValueIfNumericAndNotEmpty(embryosNumberTextField.getText());
            int ray = getValueIfNumericAndNotEmpty(rayTextField.getText());
            try {
                currentGrid = EmbryoTemplates.getGridForTemplate(template, currentGrid.getWidth(), currentGrid.getHeight(), SCALE, embryosNumber, ray);
                painter.paintCurrentGridCells(currentGrid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setTimeline() {
        Duration delayBetweenIterations = Duration.millis(500);
        Duration frame = delayBetweenIterations;
        timeline.setCycleCount(Timeline.INDEFINITE);
        int iterations=1;
        for (int i = 0; i < iterations; i++) {
            timeline.getKeyFrames().add(new KeyFrame(frame, e -> growthSimulation.generateNextStep(currentGrid)));
            timeline.getKeyFrames().add(new KeyFrame(frame, e -> painter.paintCurrentGridCells(currentGrid)));
            frame = frame.add(delayBetweenIterations);
        }
    }

    private void prepareChoiceBoxes() {
        prepareEmbryoChoiceBox();
        prepareNeighbourhoodsChoiceBox();
        prepareBoundaryConditionChoiceBox();
        prepareHexagonalKindChoiceBox();
    }

    private void prepareEmbryoChoiceBox() {
        ObservableList embryoTemplates = FXCollections.observableArrayList(EmbryoTemplates.getTemplates());
        embryoCreationChoiceBox.setItems(embryoTemplates);
        embryoCreationChoiceBox.setValue(embryoTemplates.get(0));
        embryoCreationChoiceBox.setOnAction(event -> {
            String template = (String) embryoCreationChoiceBox.getValue();
            EMBRYO_MODE = template;
            boolean customModeOff = !CUSTOM_MODE_ON.apply(EMBRYO_MODE);
            embryosNumberTextField.setVisible(customModeOff);
            embryosGeneratorButton.setVisible(customModeOff);
            if (customModeOff)
                deactivateCanvas();
            else
                activateCanvas();
            boolean rayEmbryoTemplate = "losowe z promieniem".equals(EMBRYO_MODE);
            rayLabel.setVisible(rayEmbryoTemplate);
            rayTextField.setVisible(rayEmbryoTemplate);

            String labelText = EmbryoTemplates.getLabelTextForTemplate(template);
            embryosNumberLabel.setText(labelText);
            embryosNumberLabel.setTextAlignment(TextAlignment.CENTER);
        });
    }

    private void prepareNeighbourhoodsChoiceBox() {
        ObservableList neighbourhoods = FXCollections.observableArrayList(Neighbourhoods.getNeighbourhoods());
        neighbourhoodChoiceBox.setItems(neighbourhoods);
        neighbourhoodChoiceBox.setValue(neighbourhoods.get(0));
        neighbourhoodChoiceBox.setOnAction(event -> {
            String neighbourhood = (String) neighbourhoodChoiceBox.getValue();
            boolean rayNeighbourhoodChosen = "Z promieniem".equals(neighbourhood);

            neighbourhoodRayTextField.setVisible(rayNeighbourhoodChosen);
            boolean hexagonalNeighbourhoodChosen = "Heksagonalne".equals(neighbourhood);
            hexagonalKindChoiceBox.setVisible(hexagonalNeighbourhoodChosen);
            if (hexagonalNeighbourhoodChosen)
                neighbourhoodRayLabel.setText("Rodzaj");
            else
                neighbourhoodRayLabel.setText("Promień");
            neighbourhoodRayLabel.setVisible(rayNeighbourhoodChosen || hexagonalNeighbourhoodChosen);
        });
    }

    private void prepareBoundaryConditionChoiceBox() {
        ObservableList boundaryConditions = FXCollections.observableArrayList(BoundaryCondition.getNames());
        boundaryConditionChoiceBox.setItems(boundaryConditions);
        boundaryConditionChoiceBox.setValue(boundaryConditions.get(0));
    }

    private void prepareHexagonalKindChoiceBox() {
        ObservableList neighbourhoodKinds = FXCollections.observableArrayList(HexagonalNeighbourhoodKind.getNames());
        hexagonalKindChoiceBox.setItems(neighbourhoodKinds);
        hexagonalKindChoiceBox.setValue(neighbourhoodKinds.get(0));
    }

    private void activateCanvas() {
        canvas.setOnMouseClicked(event -> {
            int x = (int) event.getX();
            int y = (int) event.getY();
            MouseButton button = event.getButton();
            if(MouseButton.PRIMARY.equals(button))
                painter.repaintCellFromClickedPixel(currentGrid, x, y);
        });
    }

    private void deactivateCanvas() {
        canvas.setOnMouseClicked(event -> {
        });
    }

    private void resizeGrid() {
        int width = getWidth();
        int height = getHeight();
        int widthScale = CANVAS_WIDTH / width;
        int heightScale = CANVAS_HEIGHT / height;
        SCALE = max(widthScale, heightScale);
        if (SCALE > 20)
            SCALE = 20;
        currentGrid.resizeGrid(width, height, SCALE);
        clearCanvas();
        painter.paintCurrentGridCells(currentGrid);
        scaleLabel.setText(SCALE + " pikseli");
        painter.setScale(SCALE);
    }

    private void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0,
                gc.getCanvas().getWidth(),
                gc.getCanvas().getHeight());
    }

    private Integer getHeight() {
        int height = getValueIfNumericAndNotEmpty(heightTextField.getText());
        if (height <= 0)
            return 1;
        else if (height > CANVAS_HEIGHT)
            return CANVAS_HEIGHT;
        return height;
    }

    private int getWidth() {
        int width = getValueIfNumericAndNotEmpty(widthTextField.getText());
        if (width <= 0)
                return 1;
        else if (width > CANVAS_WIDTH)
            return CANVAS_WIDTH;
        return width;
    }

    private boolean isNumeric(String value) {
        String number = value.replaceAll("\\s+", "");
        for (int i = 0; i < number.length(); i++) {
            if (!((int) number.charAt(i) >= 47 && (int) number.charAt(i) <= 57)) {
                return false;
            }
        }
        return true;
    }

    private int getValueIfNumericAndNotEmpty(String value) {
        if (!value.isEmpty() && isNumeric(value)) {
            return Integer.parseInt(value);
        }
        return 0;
    }
}
