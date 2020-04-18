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
import sample.grid.EmbryoTemplates;
import sample.simulation.CellPainter;
import sample.simulation.LifeSimulation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class Controller implements Initializable {
    private final Function<String, Boolean> CUSTOM_MODE_ON = "komp. własna"::equals;
    private static int CANVAS_HEIGHT;
    private static int CANVAS_WIDTH;
    private static int SCALE;

    @FXML
    private Canvas canvas;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button resizeButton;
    @FXML
    private Button embryosGeneratorButton;
    @FXML
    private TextField widthTextField;
    @FXML
    private TextField heightTextField;
    @FXML
    private TextField embryosNumberTextField;
    @FXML
    private Label embryosNumberLabel;
    @FXML
    private TextField rayTextField;
    @FXML
    private Label rayLabel;
    @FXML
    private ChoiceBox embryoCreationChoiceBox;
    @FXML
    private ChoiceBox boundaryConditionChoiceBox;
    @FXML
    private ChoiceBox neighbourhoodChoiceBox;

    private CellPainter painter;
    private GraphicsContext gc;
    private CurrentGrid currentGrid;
    private LifeSimulation lifeSimulation;
    private Timeline timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CANVAS_HEIGHT = (int) canvas.getHeight();
        CANVAS_WIDTH = (int) canvas.getWidth();
        SCALE = 4;
        gc = canvas.getGraphicsContext2D();
        currentGrid = new CurrentGrid(CANVAS_WIDTH, CANVAS_HEIGHT, SCALE, 0);
        painter = new CellPainter(gc, SCALE);
        lifeSimulation = new LifeSimulation();
        clearCanvas();
        //drawGridLines(CANVAS_WIDTH, CANVAS_HEIGHT);

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
            setNumberOfIterations();
            timeline.play();
            canvas.setOnMouseClicked(event1 -> {
            });
        });
        stopButton.setOnAction(event -> {
            timeline.stop();
            timeline.getKeyFrames().clear();
            //if(embryoCreationChoiceBox.getValue())
            activateCanvas();
        });
        resizeButton.setOnAction(event -> resizeGrid());
        embryosGeneratorButton.setOnAction(event -> {
            String template = (String) embryoCreationChoiceBox.getValue();
            int embryosNumber = getValueIfNumericAndNotEmpty(embryosNumberTextField.getText());
            int ray = getValueIfNumericAndNotEmpty(rayTextField.getText());
            try {
                currentGrid = EmbryoTemplates.getGridForTemplate(template, currentGrid.getWidth(), currentGrid.getHeight(), embryosNumber, ray);
                painter.paintCurrentGridCells(currentGrid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setNumberOfIterations() {
        Duration delayBetweenMessages = Duration.seconds(1);
        Duration frame = delayBetweenMessages;
        timeline.setCycleCount(Timeline.INDEFINITE);
        int iterations=1;
        for (int i = 0; i < iterations; i++) {
            timeline.getKeyFrames().add(new KeyFrame(frame, e -> lifeSimulation.generateNextGeneration(currentGrid)));
            timeline.getKeyFrames().add(new KeyFrame(frame, e -> painter.paintCurrentGridCells(currentGrid)));
            frame = frame.add(delayBetweenMessages);
        }
    }

    private void prepareChoiceBoxes() {
        List<String> templates = EmbryoTemplates.getTemplates();
        ObservableList list = FXCollections.observableArrayList(templates);
        embryoCreationChoiceBox.setItems(list);
        embryoCreationChoiceBox.setValue(list.get(0));
        embryoCreationChoiceBox.setOnAction(event -> {
            String template = (String) embryoCreationChoiceBox.getValue();
            String labelText = EmbryoTemplates.getPromptTextForTemplate(template);
            embryosNumberLabel.setText(labelText);
            embryosNumberLabel.setTextAlignment(TextAlignment.CENTER);
        });
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

    private void resizeGrid() {
        int width = getWidth();
        int height = getHeight();
        if (currentGrid.getWidth() != width / SCALE || currentGrid.getHeight() != height / SCALE)
            currentGrid.resizeGrid(width / SCALE, height / SCALE);
        gc.setFill(Color.WHITE);
        if (width < CANVAS_WIDTH)
            gc.fillRect((width - width%SCALE), 0,
                    CANVAS_WIDTH + (width + width%SCALE),
                    CANVAS_HEIGHT);
        if (height < CANVAS_HEIGHT)
            gc.fillRect(0, (height - height%SCALE),
                    CANVAS_WIDTH,
                    CANVAS_HEIGHT + (height + height%SCALE));
        widthTextField.setText(width - width%SCALE + "");
        heightTextField.setText(height - height%SCALE + "");
    }

    private void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0,
                gc.getCanvas().getWidth(),
                gc.getCanvas().getHeight());
    }

    private Integer getHeight() {
        String input = heightTextField.getText();
        if (!input.isEmpty() && isNumeric(input)) {
            int height = Integer.parseInt(input);
            if (height <= 0)
                return 1;
            if (height <= CANVAS_HEIGHT)
                return height;
        }
        return CANVAS_HEIGHT;
    }

    private int getWidth() {
        String input = widthTextField.getText();
        if (!input.isEmpty() && isNumeric(input)) {
            int width = Integer.parseInt(input);
            if (width <= 0)
                return 1;
            if (width <= CANVAS_WIDTH)
                return width;
        }
        return CANVAS_WIDTH;
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
