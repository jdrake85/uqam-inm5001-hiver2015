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
        initializeTiles();
    }

    private void initializeTiles() {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                tiles[i][j] = new Tile(i, j);
            }
        }
    }

    public Tile getTileAt(int xCoord, int yCoord) {
        return tiles[xCoord][yCoord];
    }

    public void displayCreatureCoordinates(Creature creature) {
        Tile creatureTile = getTileContainingCreature(creature);
        String outputIntro = "Creature <" + creature + "> was ";
        if (creatureTile != null) {
            System.out.println(outputIntro + "found at "
                    + creatureTile.getCondensedCoordinates());
        } else {
            System.out.println(outputIntro + "not found on the gameboard.");
        }
    }

    public boolean containsCreature(Creature creature) {
        boolean creatureFound = false;
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (tiles[i][j].isOccupiedBy(creature)) {
                    creatureFound = true;
                    break;
                }
            }
        }
        return creatureFound;
    }
    
    public int getCreatureXCoordinate(Creature creature) {
        int creatureXCoordinate = -1;
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (tiles[i][j].isOccupiedBy(creature)) {
                    creatureXCoordinate = i;
                    break;
                }
            }
        }
        return creatureXCoordinate;
    }
    
    public int getCreatureYCoordinate(Creature creature) {
        int creatureXCoordinate = -1;
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (tiles[i][j].isOccupiedBy(creature)) {
                    creatureXCoordinate = j;
                    break;
                }
            }
        }
        return creatureXCoordinate;
    }

    private Tile getTileContainingCreature(Creature creature) {
        Tile creatureTile = null;
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (tiles[i][j].isOccupiedBy(creature)) {
                    creatureTile = tiles[i][j];
                    break;
                }
            }
        }
        return creatureTile;
    }

    // Does not take into account occupied positions yet
    public boolean validMoveDirectionAccordingToPosition(int xCoord, int yCoord, String direction) {
        boolean validMoveDirection =
                direction.equals("+x") && xCoord <= 6
                || direction.equals("+y") && yCoord <= 6
                || direction.equals("-x") && xCoord >= 1
                || direction.equals("-y") && yCoord >= 1;
        return validMoveDirection;
    }
    
    public void moveCreatureInDirection(Creature creature, String direction) {
        
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
