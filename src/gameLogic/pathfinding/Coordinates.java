    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.pathfinding;

/**
 *
 * @author User
 */
public class Coordinates implements Comparable {

    int xCoord = -1;
    int yCoord = -1;

    public Coordinates() {
        xCoord = -1;
        yCoord = -1;
    }

    public Coordinates(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public void setTo(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public int getXCoord() {
        return xCoord;
    }

    public void setXCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setYCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public Coordinates coordinatesOneUnitInDirection(String direction) {
        Coordinates resultantCoordinates = new Coordinates(xCoord, yCoord);
        if (direction.equals("+x")) {
            resultantCoordinates.setXCoord(xCoord + 1);
        } else if (direction.equals("-x")) {
            resultantCoordinates.setXCoord(xCoord - 1);
        } else if (direction.equals("+y")) {
            resultantCoordinates.setYCoord(yCoord + 1);
        } else if (direction.equals("-y")) {
            resultantCoordinates.setYCoord(yCoord - 1);
        } else {
            resultantCoordinates.setTo(-1, -1); // Invalid direction
        }
        return resultantCoordinates;
    }

    @Override
    public String toString() {
        return "(" + xCoord + ", " + yCoord + ")";
    }

    @Override
    public boolean equals(Object o) {
        boolean equality;
        if (o == null || !(o instanceof Coordinates)) {
            equality = false;
        } else {
            Coordinates coordinates = (Coordinates) o;
            equality = xCoord == coordinates.getXCoord()
                    && yCoord == coordinates.getYCoord();
        }
        return equality;
    }

    public boolean quickEquals(Coordinates coord) {
        return xCoord == coord.getXCoord() && yCoord == coord.getYCoord();
    }

    // Coordinates must be adjacent (one of the four surrounding cardinal coords
    public Coordinates getNextCoordinatesInTheDirectionOf(Coordinates coords) {
        Coordinates nextCoords = null;
        if (this.areCardinalCoordinatesAdjacentTo(coords)) {
            int otherXCoord = coords.getXCoord();
            int otherYCoord = coords.getYCoord();
            if (otherXCoord - xCoord > 0) {
                nextCoords = new Coordinates(otherXCoord + 1, yCoord);
            } else if (otherXCoord - xCoord < 0) {
                nextCoords = new Coordinates(otherXCoord - 1, yCoord);
            } else if (otherYCoord - yCoord > 0) {
                nextCoords = new Coordinates(xCoord, otherYCoord + 1);
            } else {
                nextCoords = new Coordinates(xCoord, otherYCoord - 1);
            }
        }
        return nextCoords;
    }

    public String getCardinalDirectionTowards(Coordinates coords) {
        String direction = null;
        if (!this.equals(coords)) {
            int otherXCoord = coords.getXCoord();
            int otherYCoord = coords.getYCoord();
            if (otherXCoord - xCoord > 0) {
                direction = "+x";
            } else if (otherXCoord - xCoord < 0) {
                direction = "-x";
            } else if (otherYCoord - yCoord > 0) {
                direction = "+y";
            } else {
                direction = "-y";
            }
        }
        return direction;
    }

    public Coordinates getAdjacentCoordinatesNearestTo(Coordinates coords) {
        Coordinates adjacentCoords;
        int xDiff = coords.getXCoord() - xCoord;
        int yDiff = coords.getYCoord() - yCoord;
        int xStep = xDiff > 0 ? 1 : -1;
        int yStep = yDiff > 0 ? 1 : -1;
        if (Math.abs(xDiff) > Math.abs(yDiff)) {
            adjacentCoords = new Coordinates(xCoord + xStep, yCoord);
        } else {
            adjacentCoords = new Coordinates(xCoord, yCoord + yStep);
        }
        return adjacentCoords;
    }

    public boolean areCardinalCoordinatesAdjacentTo(Coordinates coords) {
        return (coords.equals(new Coordinates(xCoord, yCoord + 1))
                || coords.equals(new Coordinates(xCoord + 1, yCoord))
                || coords.equals(new Coordinates(xCoord, yCoord - 1))
                || coords.equals(new Coordinates(xCoord - 1, yCoord)));
    }

    public Coordinates[] getFourSurroundingCardinalCoordinates() {
        Coordinates[] surroundingCoords = new Coordinates[4];
        surroundingCoords[0] = new Coordinates(xCoord, yCoord + 1);
        surroundingCoords[1] = new Coordinates(xCoord + 1, yCoord);
        surroundingCoords[2] = new Coordinates(xCoord, yCoord - 1);
        surroundingCoords[3] = new Coordinates(xCoord - 1, yCoord);
        return surroundingCoords;
    }

    public boolean foundIn(Coordinates[] coordinatesArray) {
        boolean found = false;
        for (Coordinates coord : coordinatesArray) {
            if (this.quickEquals(coord)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public int sumOfXYComponentDistancesTo(Coordinates coords) {
        int xDistance = Math.abs(xCoord - coords.getXCoord());
        int yDistance = Math.abs(yCoord - coords.getYCoord());
        return xDistance + yDistance;
    }

    public boolean isDiagonalTo(Coordinates coords) {
        int otherXCoord = coords.getXCoord();
        int otherYCoord = coords.getYCoord();
        int xDiff = xCoord - otherXCoord;
        int yDiff = yCoord - otherYCoord;
        return (yCoord == otherYCoord + xDiff) || (yCoord == otherYCoord - xDiff)
                || (xCoord == otherXCoord + yDiff) || (xCoord == otherXCoord - yDiff);
    }

    public boolean hasXOrYComponentWithinNUnitsOf(int units, Coordinates coords) {
        int xDiff = xCoord - coords.getXCoord();
        int yDiff = yCoord - coords.getYCoord();
        return Math.abs(xDiff) <= units || Math.abs(yDiff) <= units;
    }

    public int compareTo(Object o) {
        int comparison = 0;
        if (o == null) {
            throw new NullPointerException();
        } else {
            Coordinates otherCoords = (Coordinates) o;
            int otherXCoord = otherCoords.getXCoord();
            int otherYCoord = otherCoords.getYCoord();
            if (xCoord < otherXCoord || (xCoord == otherXCoord && yCoord < otherYCoord)) {
                comparison = -1;
            } else if (xCoord > otherXCoord || (xCoord == otherXCoord && yCoord > otherYCoord)) {
                comparison = 1;
            }
        }
        return comparison;
    }
}
