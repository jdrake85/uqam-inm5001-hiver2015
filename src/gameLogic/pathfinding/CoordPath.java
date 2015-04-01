/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.pathfinding;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class CoordPath {
    private ArrayList<Coordinates> pathElements;
    
    public CoordPath() {
        pathElements = new ArrayList<Coordinates>();
    }
    
    public boolean isEmpty() {
        return length() == 0;
    }
    
    public int length() {
        return pathElements.size();
    }

    public void pushEndCoordinates(Coordinates nextCoord) {
        pathElements.add(nextCoord);
    }
    
    public Coordinates popEndCoordinates() {
        int elementCount = pathElements.size();
        Coordinates endCoords = null;
        if (elementCount > 0) { 
            endCoords = pathElements.remove(elementCount - 1);
        }
        return endCoords;
    }
    
    public void pushFirstCoordinates(Coordinates firstCoord) { 
        pathElements.add(0, firstCoord);
    }
    
    public Coordinates popFirstCoordinates() {
        return pathElements.remove(0);
    }
    
    @Override
    public String toString() {
        String message = "Path: ";
        if (isEmpty()) {
            message += "empty";
        } else {
            for (Coordinates coords: pathElements) {
                message += " -> " + coords;
            }
        }
        return message;
    }
    
    public CoordPath generateReversePath() { 
        CoordPath reversePath = new CoordPath();
        for (int i = pathElements.size() - 1; i >= 0 ; i--) {
            Coordinates coords = pathElements.get(i);
            reversePath.pushEndCoordinates(coords);
        }
        return reversePath;
    }
    
    public static CoordPath generateStraightPathOfLengthNFromAdjacentCoordinates(Coordinates firstCoords, Coordinates secondCoords, int length) {
        CoordPath path = null;
        if (secondCoords.areCardinalCoordinatesAdjacentTo(firstCoords)) {
            int initXCoord = firstCoords.getXCoord();
            int initYCoord = firstCoords.getYCoord();
            int xStep = secondCoords.getXCoord() - initXCoord;
            int yStep = secondCoords.getYCoord() - initYCoord;
            path = new CoordPath();
            path.pushEndCoordinates(firstCoords);
            path.pushEndCoordinates(secondCoords);
            for (int i = 2; i < length; i++) {
                path.pushEndCoordinates(
                        new Coordinates(initXCoord + (i * xStep), initYCoord + (i * yStep)));
            }
        }
        return path;
    }
}
