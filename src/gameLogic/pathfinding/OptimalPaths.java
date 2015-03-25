/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.pathfinding;

import gameLogic.Creature;
import gameLogic.gameboard.GameBoard;
import java.util.PriorityQueue;

/**
 *
 * @author User
 */
public class OptimalPaths {

    private final int xDim;
    private final int yDim;
    private DijkstraCoord[][] dCoords;

    public OptimalPaths(int xDim, int yDim) {
        this.xDim = xDim;
        this.yDim = yDim;
        dCoords = new DijkstraCoord[xDim][yDim];
    }

    public void calculateOptimalPathsStartingFromCoordinates(boolean[][] occupiedNodes, Coordinates initCoords) {
        initializeEmptyDijkstraCoords();
        initializeTargetDijkstraCoords(occupiedNodes);
        initializeSourceDijkstraCoords(initCoords); // Adds initial coords to the graph
        calculateDijkstraOptimalPathsForCoordinates(initCoords);
    }

    private void initializeEmptyDijkstraCoords() {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                dCoords[i][j] = null;
            }
        }
    }

    private void initializeTargetDijkstraCoords(boolean[][] occupiedNodes) {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (!occupiedNodes[i][j]) {
                    Coordinates endCoords = new Coordinates(i, j);
                    dCoords[i][j] = new DijkstraCoord(endCoords);
                }
            }
        }
    }

    private void initializeSourceDijkstraCoords(Coordinates initCoords) {
        int xCoord = initCoords.getXCoord();
        int yCoord = initCoords.getYCoord();
        dCoords[xCoord][yCoord] = new DijkstraCoord(initCoords);
    }

    private void calculateDijkstraOptimalPathsForCoordinates(Coordinates initCoords) {
        int xCoord = initCoords.getXCoord();
        int yCoord = initCoords.getYCoord();
        dCoords[xCoord][yCoord].setDistance(0);
        PriorityQueue<DijkstraCoord> queue = generateQueueFromDijkstraCoords();
        while(!queue.isEmpty()) {
            DijkstraCoord dCoord = queue.poll();
            if (dCoord.getDistance() > 63) { 
                break;
            } else {
                Coordinates endCoords = dCoord.getEndCoordinates();
                for (Coordinates coords: endCoords.getFourSurroundingCardinalCoordinates()) {
                    int i = coords.getXCoord();
                    int j = coords.getYCoord();
                    if (GameBoard.tileIsWithinGameBoard(coords) && dCoords[i][j] != null) {
                        int revisedDistance = dCoord.getDistance() + 1;
                        if (revisedDistance < dCoords[i][j].getDistance()) {
                            dCoords[i][j].setDistance(revisedDistance);
                            dCoords[i][j].setSourceCoordinates(endCoords);
                            while(queue.remove(dCoords[i][j])) {
                                // Do nothing
                            }
                            queue.add(dCoords[i][j]);
                        }
                    }
                }
            }
        }  
    }

    private PriorityQueue<DijkstraCoord> generateQueueFromDijkstraCoords() {
        PriorityQueue<DijkstraCoord> queue = new PriorityQueue();
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (dCoords[i][j] != null) {
                    queue.add(dCoords[i][j]);
                }
            }
        }
        return queue;
    }
    
    public void displayOptimalPaths() {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (dCoords[i][j] != null && dCoords[i][j].getSourceCoordinates() != null) {
                    System.out.println(dCoords[i][j]);
                }
            }
        }
    }
    
    public boolean[][] getTilesReachableInAtMostNSteps(int stepCount) { 
        boolean[][] reachableTiles = new boolean[xDim][yDim];
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                reachableTiles[i][j] = (dCoords[i][j] != null) 
                        && (dCoords[i][j].getSourceCoordinates() != null) 
                        && (dCoords[i][j].getDistance() <= stepCount);
            }
        }
        return reachableTiles;
    }
    
    public boolean coordinatesReachableInAtMostDistanceOf(Coordinates coords, int distance) { 
        DijkstraCoord dCoord = coordinatesToDijkstraCoordinates(coords);
        return (dCoord != null) && (dCoord.getDistance() <= distance);
    }
    
    private DijkstraCoord coordinatesToDijkstraCoordinates(Coordinates coords) { 
        int xCoord = coords.getXCoord();
        int yCoord = coords.getYCoord();
        return dCoords[xCoord][yCoord];
    }
    
    public int getCalculatedPathDistanceForCoordinates(Coordinates coords) { 
        DijkstraCoord dCoord = coordinatesToDijkstraCoordinates(coords);
        return dCoord.getDistance();
    }

    public CoordPath getPathForCreatureToCoordinates(Creature creature, Coordinates destCoords) {
        CoordPath path = new CoordPath();
        DijkstraCoord initDCoords = coordinatesToDijkstraCoordinates(destCoords);
        int pathLength = initDCoords.getDistance();
        DijkstraCoord currentDCoords = initDCoords;
        for (int i = 0; i < pathLength; i++) {
            path.pushEndCoordinates(currentDCoords.getEndCoordinates());
            Coordinates nextCoords = currentDCoords.getSourceCoordinates();
            currentDCoords = coordinatesToDijkstraCoordinates(nextCoords);
        }
        path.pushEndCoordinates(currentDCoords.getEndCoordinates());
        return path.generateReversePath();
    }
}
