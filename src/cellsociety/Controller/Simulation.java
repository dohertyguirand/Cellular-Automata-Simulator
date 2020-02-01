package cellsociety.Controller;

import cellsociety.Model.ArrayGrid;
import cellsociety.Model.Grid;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;

public abstract class Simulation {

  public String SIMULATION_NAME;
  public int GRID_WIDTH;
  public int GRID_HEIGHT;
  public Grid simulationGrid;
  public Map<Integer, Color> cellColorMap;

  abstract void loadSimulationContents(String filepath);

  abstract void updateGrid();

  public Grid getGrid(){
    return simulationGrid;
  }

  abstract int getSimulationCols();

  abstract void initializeColorMap();

  public Map<Integer, Color> getCellColorMap(){
    return cellColorMap;
  }

}
