/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.pathfinding;

/**
 *
 * @author User
 */
public class DijkstraCoord implements Comparable {

    private Coordinates endCoords;
    private Coordinates sourceCoords = null;
    private int distance = 64;

    public DijkstraCoord(Coordinates endCoords) {
        this.endCoords = endCoords;
    }

    public Coordinates getEndCoordinates() {
        return endCoords;
    }

    public Coordinates getSourceCoordinates() {
        return sourceCoords;
    }

    public void setSourceCoordinates(Coordinates sourceCoords) {
        this.sourceCoords = sourceCoords;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    
    public boolean hasSource() {
        return sourceCoords != null;
    }

    public int compareTo(Object o) {
        int comparison = 1;
        if (o == null) {
            throw new NullPointerException();
        } else {
            DijkstraCoord otherDCoord = (DijkstraCoord) o;
            int otherDistance = otherDCoord.getDistance();
            if (distance < otherDistance) {
                comparison = -1;
            } else if (distance == otherDistance) {
                comparison = 0;
            }
            return comparison;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        boolean equality;
        if (o == null || !(o instanceof DijkstraCoord)) {
            equality = false;
        } else {
            DijkstraCoord dCoord = (DijkstraCoord) o;
            equality = endCoords.equals(dCoord.getEndCoordinates());
        }
        return equality;
    }
    
    @Override
    public String toString() {
        return "Distance of " + distance + " to " + endCoords + ", passing through " + sourceCoords;
    }
}
