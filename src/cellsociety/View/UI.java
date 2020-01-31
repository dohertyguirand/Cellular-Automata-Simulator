package cellsociety.View;

import cellsociety.Controller.GameOfLife;
import cellsociety.Controller.XMLParser;
import cellsociety.Model.ArrayGrid;
import cellsociety.Controller.Simulation;
import cellsociety.Model.Grid;
import java.sql.Time;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class UI extends Application {
    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;
    private static String gameOfLifeConfiguration = "./Resources/gameoflife.xml";
    private double timestep = 1000;
    public static final String STYLESHEET = "style.css";

    private Timeline timeline;
    private Text testing;

    GameOfLife gameOfLife = new GameOfLife();
    BorderPane root = new BorderPane();

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        gameOfLife.loadSimulationContents(gameOfLifeConfiguration);
        //Setting the title to Stage.
        primaryStage.setTitle("Simulation");;
        createTimeline(timestep);
        //Adding the scene to Stage
        primaryStage.setScene(makeScene());
        primaryStage.show();
    }

    private static List<String> readText(String fname) throws FileNotFoundException {
        List<String> ret = new ArrayList<>();
        Scanner s = new Scanner(new File(fname));
        while (s.hasNext()){
            ret.add(s.nextLine());
        }
        return ret;
    }

    private Scene makeScene() throws FileNotFoundException {
        root.setTop(makeSimulationToolbar());
        root.setBottom(makeSimulationControls());
        root.setCenter(buildGrid());
        Scene scene = new Scene(root ,WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        scene.setFill(Color.WHITE);
        return scene;
    }

    private Node makeSimulationToolbar() throws FileNotFoundException {
        HBox toolbar = new HBox();
        ComboBox comboBox = new ComboBox();
        testing = new Text();
        testing.setFont(new Font(22));
        testing.setText("yo");
        comboBox.getItems().addAll(readText("Resources/SimulationMenuText.txt"));
        comboBox.getSelectionModel().selectFirst();
        toolbar.getChildren().add(comboBox);
        toolbar.getChildren().add(testing);
        return toolbar;
    }

    private void createTimeline(double milliseconds) {
        if (timeline!= null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), event -> {
            testing.setText(String.valueOf(Math.random()));
            gameOfLife.updateGrid();
            root.setCenter(buildGrid());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private Node makeSimulationControls() {
        Slider slider = new Slider(100,5000, 100);

        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        Button playButton = new Button();
        playButton.setText("Play");
        playButton.setOnAction(e -> timeline.play());
        Button stopButton = new Button();
        stopButton.setText("Stop");
        stopButton.setOnAction(e -> timeline.stop());
        slider.valueProperty().addListener((observable, oldValue, newValue) -> createTimeline((double)newValue));

        HBox controls = new HBox();
        controls.getChildren().add(playButton);
        controls.getChildren().add(stopButton);
        controls.getChildren().add(slider);
        return controls;
    }

    private Node buildGrid() {

        HBox wrapper = new HBox();
        Grid currentGrid = gameOfLife.getGrid();
        TilePane uiGrid = new TilePane();
        Map<Integer, Color> colorMap = gameOfLife.getCellColorMap();

        System.out.println(gameOfLife.getSimulationCols());
        for (int i = 0; i < currentGrid.getSize(); i++) {
            for (int j = 0; j < currentGrid.getSize(); j++) {
                uiGrid.getChildren().add(new Rectangle(30, 30, colorMap.get(gameOfLife.getGrid().getCurrentState(i, j))));
            }
        }
        uiGrid.setHgap(10);
        uiGrid.setVgap(10);
        uiGrid.setPrefColumns(gameOfLife.getSimulationCols());
        uiGrid.setPadding(new Insets(20, 75, 20, 75));
        uiGrid.prefRowsProperty();
        wrapper.getChildren().add(uiGrid);
        return wrapper;
    }

}
