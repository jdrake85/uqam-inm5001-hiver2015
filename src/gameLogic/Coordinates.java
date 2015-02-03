/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

/**
 *
 * @author User
 */
public class Coordinates {

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
    public boolean equals(Object coordinates) {
        boolean validObject = coordinates != null & coordinates instanceof Coordinates;
        boolean matching = false;
        if (validObject) {
            Coordinates trueCoordinates = (Coordinates) coordinates;
            matching = this.xCoord == trueCoordinates.getXCoord()
                    && this.yCoord == trueCoordinates.getYCoord();
        }
        return matching;
    }
}
