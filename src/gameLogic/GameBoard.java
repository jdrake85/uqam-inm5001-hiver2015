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
    
    public int getDimensionAlongXAxis() {
        return xDim;
    }
    
    public int getDimensionAlongYAxis() {
        return yDim;
    }

    public Tile getTileAt(Coordinates coordinates) {
        return getTileAt(coordinates.getXCoord(),coordinates.getYCoord());
    }
    
    public Tile getTileAt(int xCoord, int yCoord) {
        return tiles[xCoord][yCoord];
    }
    
    public static int tileCountBetweenCoordinates(Coordinates initCoords, Coordinates destCoords) {
        int initXCoord = initCoords.getXCoord();
        int initYCoord = initCoords.getYCoord();
        int destXCoord = destCoords.getXCoord();
        int destYCoord = destCoords.getYCoord();
        return tileCountBetweenCoordinates(initXCoord, initYCoord, destXCoord, destYCoord);
    }
    
    public static int tileCountBetweenCoordinates
            (int initXCoord, int initYCoord, int destXCoord, int destYCoord) {
        int diffAlongX = destXCoord - initXCoord;
        int diffAlongY = destYCoord - initYCoord;
        int tilesAlongX = diffAlongX > 0 ? diffAlongX : diffAlongX * -1;
        int tilesAlongY = diffAlongY > 0 ? diffAlongY : diffAlongY * -1;
        return tilesAlongX + tilesAlongY;
    }

    public void displayCreatureCoordinates(Creature creature) {
        Coordinates coordinates = getCreatureCoordinates(creature);
        String outputIntro = "Creature '" + creature + "' was ";
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
        Coordinates initialCoords = getCreatureCoordinates(creature);
        if (validDestinationTile(initialCoords, direction)) {
            Coordinates destinationCoords = initialCoords.coordinatesOneUnitInDirection(direction);
            Tile initialTile = getTileAt(initialCoords);
            Tile destinationTile = getTileAt(destinationCoords);
            destinationTile.addOccupier(creature);
            initialTile.removeOccupier();
        } else {
            System.out.println("Error: GameBoard request for invalid move");
        }
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
        if (!targetTile.isOccupied()) {
            targetTile.addOccupier(creature);
        } else {
            System.out.println("Error: tile at (" + xCoord + ", " + yCoord + ") already occupied");
        }
    }

    public boolean validDestinationTile(Creature creature, String direction) {
        Coordinates coordinates = getCreatureCoordinates(creature);
        return validDestinationTile(coordinates, direction);
    }
    
    public boolean validDestinationTile(Coordinates coordinates, String direction) {
        Coordinates coordsAfterMove = coordinates.coordinatesOneUnitInDirection(direction);
        // Outputs for debugging purposes
        if (!tileIsWithinGameBoard(coordsAfterMove)) {
            System.out.println("Error: tile at " + coordsAfterMove + " is outside of the gameboard");
        } else if (tileIsOccupied(coordsAfterMove)) {
            Tile targetTile = getTileAt(coordsAfterMove);
            System.out.println("Error: tile at " + coordsAfterMove + " is already occupied by " + targetTile.getOccupier());
        }
        return tileIsWithinGameBoard(coordsAfterMove) && !tileIsOccupied(coordsAfterMove);
    }

    private boolean tileIsWithinGameBoard(Coordinates coordinates) {
       int xCoord = coordinates.getXCoord();
       int yCoord = coordinates.getYCoord();
       return 0 <= xCoord && xCoord <= 7 && 0 <= yCoord && yCoord <= 7;
    }

    public boolean tileIsOccupied(Coordinates coordinates) {
        Tile tile = getTileAt(coordinates);
        return tile.isOccupied();
    }
    
    public void draw() {
        draw(null);
    }
    
    public void draw(boolean[][] overlay) {
        boolean withOverlay = overlay != null;
        for (int j = 7; j >= 0; j--) {
            String lineDrawing = j + " | ";
            for (int i = 0; i < 8; i++) {
                char tileDrawing = drawTile(i, j);
                if (tileDrawing == ' ' && withOverlay && overlay[i][j]) {
                    lineDrawing += '*'; // Move is allowed according to overlay
                } else {
                    lineDrawing += drawTile(i, j); 
                }
                lineDrawing += " | ";
            }
            System.out.println(lineDrawing);
        }
        drawXAxisDetails();
    }
    
    private void drawXAxisDetails() {
        String xAxis = "  |";
        String xLabels = "   ";
        for (int n = 0; n < 8; n++) {
            xAxis += "----";
            xLabels += " " + n + " |";
        }
        System.out.println(xAxis);
        System.out.println(xLabels);
    }

    private char drawTile(int i, int j) {
        Tile tile = tiles[i][j];
        char tileDrawing = ' ';
        Creature occupier = tile.getOccupier();
        
        if (occupier != null) {
            if (occupier.isGood) {
                tileDrawing = 'H';
            } else {
                tileDrawing = 'Z';
            }
        } 
        return tileDrawing;
    }

    
}
