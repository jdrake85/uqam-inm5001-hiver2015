/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.gameboard;

import gameLogic.Creature;
import gameLogic.FakeMain2;
import gameLogic.pathfinding.Coordinates;
import gameLogic.pathfinding.CoordPath;
import gameLogic.skills.*;
import java.util.ArrayList;
import java.util.List;

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

    public void clearGameBoard() {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (tiles[i][j] != null) {
                    Creature occupier = tiles[i][j].getOccupier();
                    if (occupier != null) {
                        tiles[i][j].removeOccupier();
                        occupier.hideCreatureOn3DBoard();
                    }
                }
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
        return getTileAt(coordinates.getXCoord(), coordinates.getYCoord());
    }

    public Tile getTileAt(int xCoord, int yCoord) {
        Tile tile = null;
        if (tileIsWithinGameBoard(xCoord, yCoord)) {
            tile = tiles[xCoord][yCoord];
        }
        return tile;
    }

    public static int tileCountBetweenCoordinates(Coordinates initCoords, Coordinates destCoords) {
        int initXCoord = initCoords.getXCoord();
        int initYCoord = initCoords.getYCoord();
        int destXCoord = destCoords.getXCoord();
        int destYCoord = destCoords.getYCoord();
        return tileCountBetweenCoordinates(initXCoord, initYCoord, destXCoord, destYCoord);
    }

    public int tileCountBetweenCreatureAndCoordinates(Creature creature, Coordinates coords) {
        Coordinates creatureCoords = getCreatureCoordinates(creature);
        int tileCount = 0;
        if (creatureCoords != null) {
            tileCount = tileCountBetweenCoordinates(creatureCoords, coords);
        }
        return tileCount;
    }

    public static int tileCountBetweenCoordinates(int initXCoord, int initYCoord, int destXCoord, int destYCoord) {
        int diffAlongX = destXCoord - initXCoord;
        int diffAlongY = destYCoord - initYCoord;
        int tilesAlongX = diffAlongX > 0 ? diffAlongX : diffAlongX * -1;
        int tilesAlongY = diffAlongY > 0 ? diffAlongY : diffAlongY * -1;
        return tilesAlongX + tilesAlongY;
    }

    public void displayCreatureCoordinates(Creature creature) {
        Coordinates coordinates = getCreatureCoordinates(creature);
        String outputIntro = "Creature '" + creature + '\'';
        if (coordinates != null) {
            System.out.println(outputIntro + " is at " + coordinates);
        } else {
            System.out.println(outputIntro + " was not found on the gameboard.");
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
                    coordinates = new Coordinates(i, j);
                    break;
                }
            }
        }
        return coordinates;
    }

    // Unit movement
    public void moveCreatureInDirection(Creature creature, String direction) {
        Coordinates initialCoords = getCreatureCoordinates(creature);
        if (validDestinationTile(initialCoords, direction)) {
            Coordinates destinationCoords = initialCoords.coordinatesOneUnitInDirection(direction);
            Tile initialTile = getTileAt(initialCoords);
            Tile destinationTile = getTileAt(destinationCoords);
            destinationTile.addOccupier(creature);
            initialTile.removeOccupier();
            //creature.displayCreatureOn3DBoard(destinationCoords.getXCoord(),destinationCoords.getYCoord());           

        } else {
            System.out.println("Error: GameBattle request for invalid move");
        }
    }

    public void moveCreatureTo(Creature creature, Coordinates destCoords) {
        if (validAndEmptyDestinationTileAt(destCoords)) {
            Coordinates initCoords = getCreatureCoordinates(creature);
            System.out.println("GAMEBOARD: Moving from " + initCoords + " to " + destCoords);
            Tile initialTile = getTileAt(initCoords);
            Tile destinationTile = getTileAt(destCoords);
            destinationTile.addOccupier(creature);
            initialTile.removeOccupier();
            //creature.displayCreatureOn3DBoard(destCoords.getXCoord(),destCoords.getYCoord());           

        } else {
            System.out.println("Error: GameBattle request for invalid move");
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
        ArrayList<Creature> creatures = new ArrayList<Creature>();
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

    public void removeDeadCreatures() {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (tiles[i][j].isOccupied()) {
                    Creature creature = tiles[i][j].getOccupier();
                    if (!creature.isAlive()) {
                        creature.hideCreatureOn3DBoard();
                        tiles[i][j].removeOccupier();
                    }
                }
            }
        }
    }

    public void insertCreatureAt(Creature creature, int xCoord, int yCoord) {
        Tile targetTile = tiles[xCoord][yCoord];
        if (!targetTile.isOccupied()) {
            targetTile.addOccupier(creature);
        } else {
            System.out.println("Error: tile at (" + xCoord + ", " + yCoord + ") already occupied");
        }
    }

    public boolean validAndEmptyDestinationTileAt(Coordinates coords) {
        return tileIsWithinGameBoard(coords) && !tileIsOccupied(coords);
    }

    // Unit movement
    public boolean validDestinationTile(Creature creature, String direction) {
        Coordinates coordinates = getCreatureCoordinates(creature);
        return validDestinationTile(coordinates, direction);
    }

    // Unit movement
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

    public static boolean tileIsWithinGameBoard(Coordinates coordinates) {
        int xCoord = coordinates.getXCoord();
        int yCoord = coordinates.getYCoord();
        return tileIsWithinGameBoard(xCoord, yCoord);
    }

    public static boolean tileIsWithinGameBoard(int xCoord, int yCoord) {
        return 0 <= xCoord && xCoord <= 7 && 0 <= yCoord && yCoord <= 7;
    }

    public boolean tileIsOccupied(Coordinates coordinates) {
        Tile tile = getTileAt(coordinates);
        return tile.isOccupied();
    }

    private ArrayList<Coordinates> getValidUnoccupiedCoordinatesAdjacentTo(Coordinates initCoord) {
        ArrayList<Coordinates> unoccupiedCoords = new ArrayList<Coordinates>();
        return unoccupiedCoords;
    }

    public boolean[][] getOccupiedTiles() {
        boolean[][] occupiedTiles = new boolean[xDim][yDim];
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (tiles[i][j].isOccupied()) {
                    occupiedTiles[i][j] = true;
                } else {
                    occupiedTiles[i][j] = false;
                }
            }
        }
        return occupiedTiles;
    }

    public void moveCreatureAlongPath(Creature creature, CoordPath path) {
        Coordinates initCoords = path.popFirstCoordinates();
        Coordinates creatureCoords = getCreatureCoordinates(creature);
        if (!initCoords.equals(creatureCoords)) {
            System.err.println("ERROR: movement failure, path starts at "
                    + initCoords + " but creature is at " + creatureCoords);
        } else {
            Coordinates nextCoords;
            while (!path.isEmpty()) {
                nextCoords = path.popFirstCoordinates();
                moveCreatureFromTo(initCoords, nextCoords);
                initCoords = nextCoords;
            }
        }
    }

    private void knockbackCreatureFromSkill(Creature creature, Skill skill) {
        Coordinates previousCoords = skill.getOriginatingFrom();
        Coordinates coords = getCreatureCoordinates(creature);
        Coordinates nextCoords;
        int knockbackSteps = 2;
        for (int i = 0; i < knockbackSteps; i++) {
            nextCoords = previousCoords.getNextCoordinatesInTheDirectionOf(coords);
            if (validAndEmptyDestinationTileAt(nextCoords)) {
                moveCreatureFromTo(coords, nextCoords);
                previousCoords = coords;
                coords = nextCoords;
                creature.displayCreatureOn3DBoard(nextCoords.getXCoord(), nextCoords.getYCoord());
            } else {
                break;
            }
        }
    }

    private void moveCreatureFromTo(Coordinates initCoords, Coordinates destCoords) {
        if (!tileIsOccupied(initCoords)) {
            System.err.println("ERROR: movement failure, no creature found at " + initCoords);
        } else if (!validAndEmptyDestinationTileAt(destCoords)) {
            System.err.println("ERROR: movement failure, invalid destination tile at " + destCoords);
        } else {
            Tile initialTile = getTileAt(initCoords);
            Tile destinationTile = getTileAt(destCoords);
            Creature creature = initialTile.getOccupier();
            destinationTile.addOccupier(creature);
            initialTile.removeOccupier();
        }
    }

    public void drawWithBlankOverlay() {
        drawWithMovesOverlay(null);
    }

    public void drawWithMovesOverlay(boolean[][] overlay) {
        //System.out.println();
        boolean withOverlay = overlay != null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char tileDrawing = drawTile(i, j);
                if (withOverlay && overlay[i][j]) {
                    if (tileDrawing == ' ') {
                        FakeMain2.g[i][j].setMaterial(FakeMain2.greenMat);
                    } else {
                        FakeMain2.g[i][j].setMaterial(FakeMain2.redMat);
                    }
                }
            }
        }
    }

    public void drawWithSkillOverlay(Creature creature, boolean[][] overlay) {
        if (creature != null && overlay != null) {
            boolean creatureIsGood = creature.isGood();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (overlay[i][j]) {
                        if (tiles[i][j].isOccupied()) {
                            Creature occupier = tiles[i][j].getOccupier();
                            if (creatureIsGood != occupier.isGood()) {
                                FakeMain2.g[i][j].setMaterial(FakeMain2.redMat);
                            } else {
                                FakeMain2.g[i][j].setMaterial(FakeMain2.blueMat);
                            }
                        } else {
                            FakeMain2.g[i][j].setMaterial(FakeMain2.greenMat);
                        }
                    }
                }
            }
        }
    }

    private char drawTile(int i, int j) {
        Tile tile = tiles[i][j];
        char tileDrawing = ' ';
        Creature occupier = tile.getOccupier();

        if (occupier != null) {
            if (occupier.isAlignedTo("good")) {
                tileDrawing = 'H';
            } else {
                tileDrawing = 'Z';
            }
        }
        return tileDrawing;
    }

    public boolean[][] getSkillOverlay(Skill skill) {
        //TODO: distinguish between good and bad creatures
        boolean[][] skillUseOverlay = null;
        if (skill instanceof MeleeSkill) {
            skillUseOverlay = generateMeleeOverlay(skill);
        } else if (skill instanceof RangedSkill) {
            skillUseOverlay = generateRangedOverlay((RangedSkill) skill);
        } else if (skill instanceof EverywhereSkill) {
            skillUseOverlay = generateBadCreaturesOverlay();
        } else if (skill instanceof DirectionnalSkill) {
            skillUseOverlay = generateDirectionnalOverlay((DirectionnalSkill) skill);
        }
        return skillUseOverlay;
    }

    public boolean[][] generateBooleanOverlayWithAllValuesSetTo(boolean setting) {
        boolean[][] overlay = new boolean[xDim][yDim];
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                overlay[i][j] = setting;
            }
        }
        return overlay;
    }

    private boolean[][] generateMeleeOverlay(Skill skill) {
        boolean[][] overlay = generateBooleanOverlayWithAllValuesSetTo(false);
        Coordinates originatingCoords = skill.getOriginatingFrom();
        int xCoord = originatingCoords.getXCoord();
        int yCoord = originatingCoords.getYCoord();
        //skillUseOverlay[xCoord][yCoord] = true; // TODO: USE WHEN FINISHED WITH ASCII GRAPHICS
        if (tileIsWithinGameBoard(xCoord, yCoord + 1)) {
            overlay[xCoord][yCoord + 1] = true;
        }
        if (tileIsWithinGameBoard(xCoord + 1, yCoord)) {
            overlay[xCoord + 1][yCoord] = true;
        }
        if (tileIsWithinGameBoard(xCoord, yCoord - 1)) {
            overlay[xCoord][yCoord - 1] = true;
        }
        if (tileIsWithinGameBoard(xCoord - 1, yCoord)) {
            overlay[xCoord - 1][yCoord] = true;
        }
        return overlay;
    }

    private boolean[][] generateRangedOverlay(RangedSkill skill) {
        boolean[][] overlay = new boolean[xDim][yDim];
        Coordinates originatingCoords = skill.getOriginatingFrom();
        int xCoord = originatingCoords.getXCoord();
        int yCoord = originatingCoords.getYCoord();
        int range = skill.getRange();
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                overlay[i][j] = (Math.abs(i - xCoord) + Math.abs(j - yCoord)) <= range;
            }
        }
        overlay[xCoord][yCoord] = false; // More efficient to negate after loops
        return overlay;
    }

    private boolean[][] generateBadCreaturesOverlay() {
        boolean[][] overlay = new boolean[xDim][yDim];
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                boolean badCreatureFound = false;
                if (tiles[i][j].isOccupied()) {
                    Creature creature = tiles[i][j].getOccupier();
                    badCreatureFound = creature.isAlignedTo("bad");
                }
                overlay[i][j] = badCreatureFound;
            }
        }
        return overlay;
    }

    private boolean[][] generateDirectionnalOverlay(DirectionnalSkill skill) {
        boolean[][] overlay = new boolean[xDim][yDim];
        Coordinates originatingCoords = skill.getOriginatingFrom();
        int xCoord = originatingCoords.getXCoord();
        int yCoord = originatingCoords.getYCoord();
        int range = skill.getRange();
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                int xDiff = Math.abs(xCoord - i);
                int yDiff = Math.abs(yCoord - j);
                overlay[i][j] = !(xDiff > range || yDiff > range || originatingCoords.isDiagonalTo(new Coordinates(i, j)));
            }
        }
        overlay[originatingCoords.getXCoord()][originatingCoords.getYCoord()] = false; // More efficient to negate after loops
        return overlay;
    }

    public List<Creature> performTargetedSkill(Skill skill) {
        Coordinates targetCoords = skill.getTargetCoordinates();
        List<Coordinates> affectedCoords = skill.generateAffectedCoordinatesFrom(targetCoords);
        List<Creature> affectedCreatures = new ArrayList<Creature>();
        for (Coordinates coords : affectedCoords) {
            Tile targetTile = getTileAt(coords);
            if (targetTile != null && targetTile.isOccupied()) {
                Creature target = targetTile.getOccupier();
                int damage = skill.performOn(target);

                if (damage > 0) {
                    affectedCreatures.add(target);
                    System.out.println(target + " receives " + damage + " damage from " + skill + ", is at " + target.getHealth() + "/" + target.getMaxHealth());
                    if (skill.hasKnockback()) {
                        knockbackCreatureFromSkill(target, skill);
                    }
                    if (!target.isAlive()) {
                        System.out.println("...and expires!");

                    }
                } else if (damage == 0) {
                    System.out.println(skill + "misses!");
                } else {
                    System.out.println(target + " regains " + (damage * -1) + " points from " + skill);
                }
            }
        }
        return affectedCreatures;
    }

    public boolean containsTileWithCoordinates(Coordinates coords) {
        int xCoord = coords.getXCoord();
        int yCoord = coords.getYCoord();
        return 0 <= xCoord && xCoord <= 7 && 0 <= yCoord && yCoord <= 7;
    }

    public Creature getCreatureAt(Coordinates coords) {
        Creature creatureFound = null;
        if (containsTileWithCoordinates(coords)) {
            creatureFound = tiles[coords.getXCoord()][coords.getYCoord()].getOccupier();
        }
        return creatureFound;
    }
}
