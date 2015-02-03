/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class GameBoard {

    private Tile[][] tiles;
    private final int xDim = 8; // Number of tiles along the X-axis
    private final int yDim = 8; // Number of tiles along the Y-axis

    public GameBoard() {
        tiles = new Tile[xDim][yDim];
        initializeEmptyTiles();
    }

    private void initializeEmptyTiles() {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    public Tile getTileAt(Coordinates coordinates) {
        return getTileAt(coordinates.getXCoord(),coordinates.getYCoord());
    }
    
    public Tile getTileAt(int xCoord, int yCoord) {
        return tiles[xCoord][yCoord];
    }
    
    public static int tileCountBetweenCoordinates
            (int firstXCoord, int firstYCoord, int secondXCoord, int secondYCoord) {
        int diffAlongX = secondXCoord - firstXCoord;
        int diffAlongY = secondYCoord - firstYCoord;
        int tilesAlongX = diffAlongX > 0 ? diffAlongX : diffAlongX * -1;
        int tilesAlongY = diffAlongY > 0 ? diffAlongY : diffAlongY * -1;
        return tilesAlongX + tilesAlongY;
    }

    public void displayCreatureCoordinates(Creature creature) {
        Coordinates coordinates = getCreatureCoordinates(creature);
        String outputIntro = "Creature <" + creature + "> was ";
        if (coordinates != null) {
            System.out.println(outputIntro + "found at "
                    + coordinates);
        } else {
            System.out.println(outputIntro + "not found on the gameboard.");
        }
    }

    public boolean containsCreature(Creature creature) {
        return getCreatureCoordinates(creature) != null;
    }
    
    public Coordinates getCreatureCoordinates(Creature creature) {
        Coordinates coordinates = null;
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (tiles[i][j].isOccupiedBy(creature)) {
                    coordinates = new Coordinates(i,j);
                    break;
                }
            }
        }
        return coordinates;
    }
      
    public void moveCreatureInDirection(Creature creature, String direction) {
        Coordinates coordinates = this.getCreatureCoordinates(creature);
        int xCoord = coordinates.getXCoord();
        int yCoord = coordinates.getYCoord();
        if (validMoveAccordingToDirectionRelativeTo(xCoord, yCoord, direction)) {
            // redundant for the moment - see GameBattle class
            Tile initialTile = tiles[xCoord][yCoord];
            Tile destinationTile = 
                getAdjacentTileAccordingToDirectionRelativeTo(xCoord, yCoord, direction);
            destinationTile.addOccupier(creature);
            initialTile.removeOccupier();
        }
    }
    
    private Tile getAdjacentTileAccordingToDirectionRelativeTo
            (int xCoord, int yCoord, String direction) {
        Tile adjacentTile;
        if (direction.equals("+x")) {
            adjacentTile = this.getTileAt(xCoord + 1, yCoord);
        } else if (direction.equals("+y")) {
            adjacentTile = this.getTileAt(xCoord, yCoord + 1);
        } else if (direction.equals("-x")) {
            adjacentTile = this.getTileAt(xCoord - 1, yCoord);
        } else {    //direction.equals("-y")) {
            adjacentTile = this.getTileAt(xCoord, yCoord - 1); 
        }
        return adjacentTile;
    }

    public boolean validMoveAccordingToDirectionRelativeTo
            (int xCoord, int yCoord, String direction) {
        return directionalMoveStaysWithinGameBoard(xCoord, yCoord, direction) && 
        directionalMoveGoesOntoUnoccupiedTile(xCoord, yCoord, direction);
                
    }
    
    private boolean directionalMoveStaysWithinGameBoard(int xCoord, int yCoord, String direction) {
        boolean movementWithinGameBoard = direction.equals("+x") && xCoord <= 6
                || direction.equals("+y") && yCoord <= 6
                || direction.equals("-x") && xCoord >= 1
                || direction.equals("-y") && yCoord >= 1;
        if (!movementWithinGameBoard) {
            System.out.println("*** Error: attempt to move off the gameboard.");
        }
        return movementWithinGameBoard;
    }
    
    public boolean directionalMoveGoesOntoUnoccupiedTile(int xCoord, int yCoord, String direction) {
        Tile destinationTile = getAdjacentTileAccordingToDirectionRelativeTo(xCoord, yCoord, direction);
        boolean movementOntoUnoccupiedTile = !destinationTile.isOccupied();
        if (!movementOntoUnoccupiedTile) {
            System.out.println("*** Error: attempt to move onto an occupied tile.");
        }
        return movementOntoUnoccupiedTile;
    }

    public List<Creature> getGoodCreatureList() {
        return getCreatureListAccordingToAlignment("good");
    }

    public List<Creature> getBadCreatureList() {
        return getCreatureListAccordingToAlignment("bad");
    }

    public List<Creature> getFullCreatureList() {
        return getCreatureListAccordingToAlignment("all");
    }

    private List<Creature> getCreatureListAccordingToAlignment(String alignment) {
        ArrayList<Creature> creatures = new ArrayList();
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (tiles[i][j].isOccupied()) {
                    Creature creature = tiles[i][j].getOccupier();
                    if (creature.isAlignedTo(alignment)) {
                        creatures.add(creature);
                    }
                }
            }
        }
        return creatures;
    }

    public void insertCreatureAt(Creature creature, int xCoord, int yCoord) {
        Tile targetTile = tiles[xCoord][yCoord];
        assert (!targetTile.isOccupied());
        targetTile.addOccupier(creature);
    }
}
