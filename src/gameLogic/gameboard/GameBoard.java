package gameLogic.gameboard;

import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import gameLogic.Creature;
import gameLogic.Main;
import static gameLogic.Main.greyMat;
import gameLogic.pathfinding.Coordinates;
import gameLogic.pathfinding.CoordPath;
import gameLogic.skills.*;
import gameLogic.skills.nurse.Heal;
import java.util.ArrayList;
import java.util.LinkedList;
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

    public void moveCreatureTo(Creature creature, Coordinates destCoords) {
        if (validAndEmptyDestinationTileAt(destCoords)) {
            Coordinates initCoords = getCreatureCoordinates(creature);
            Tile initialTile = getTileAt(initCoords);
            Tile destinationTile = getTileAt(destCoords);
            destinationTile.addOccupier(creature);
            initialTile.removeOccupier();       

        } else {
            System.err.println("Error: GameBattle request for invalid move");
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
            System.err.println("Error: tile at (" + xCoord + ", " + yCoord + ") already occupied");
        }
    }

    public boolean validAndEmptyDestinationTileAt(Coordinates coords) {
        return tileIsWithinGameBoard(coords) && !tileIsOccupied(coords);
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Main.g[i][j].setMaterial(greyMat);
                Main.g[i][j].setQueueBucket(RenderQueue.Bucket.Translucent);
            }
        }
    }

    public void drawWithMovesOverlay(boolean[][] overlay) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char tileDrawing = drawTile(i, j);
                if (overlay[i][j]) {
                    if (tileDrawing == ' ') {
                        Main.g[i][j].setMaterial(Main.greenMat);
                    } else {
                        Main.g[i][j].setMaterial(Main.redMat);
                    }
                }
            }
        }
    }

    public void drawWithGeneralSkillOverlay(boolean skillTargetsZombies, boolean[][] overlay) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (overlay[i][j]) {
                    if (tiles[i][j].isOccupied()) {
                        Creature occupier = tiles[i][j].getOccupier();
                        if (occupier.isGood() != skillTargetsZombies) {
                            Main.g[i][j].setMaterial(Main.redMat);
                        } else {
                            Main.g[i][j].setMaterial(Main.blueMat);
                        }
                    } else {
                        Main.g[i][j].setMaterial(Main.greenMat);
                    }
                }
            }
        }
    }

    public void drawWithAreaOfEffectSkillOverlay(boolean[][] overlay) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (overlay[i][j]) {
                    Main.g[i][j].setMaterial(Main.redMat);
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
        if (skill instanceof Heal) {
            overlay[xCoord][yCoord] = true; // A creature can heal themselves
        }

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

    public void performTargetedSkill(Skill skill, LinkedList<Node> damageNodes, LinkedList<MotionEvent> damageMotions) {
        boolean skillTargetsZombies = skill.getTargetsZombies();
        Coordinates targetCoords = skill.getTargetCoordinates();
        List<Coordinates> affectedCoords = skill.generateAffectedCoordinatesFrom(targetCoords);
        for (Coordinates coords : affectedCoords) {
            Tile targetTile = getTileAt(coords);
            if (targetTile != null && targetTile.isOccupied()) {
                Node damagePannel = new Node("damagePannel");
                MotionEvent motionControl;

                Creature target = targetTile.getOccupier();
                int damage = skill.performOn(target);

                if (damage == 0 && (skillTargetsZombies == target.isGood())) {
                    // Do nothing
                } else {
                    if (damage > 0 && skill.hasKnockback()) {
                            knockbackCreatureFromSkill(target, skill);
                        
                    } else if (damage < 0) {
                        damage *= -1;
                    } 

                    damagePannel.setLocalTranslation(target.geometry3D.getLocalTranslation());

                    Box b1 = new Box(0.1f, 0.2f, 0f);
                    Geometry tens = new Geometry("Box1", b1);
                    tens.setMaterial(Main.numberMat[damage / 10]);
                    tens.setLocalTranslation(tens.getLocalTranslation().add(new Vector3f(-0.15f, 0f, 0.5f)));

                    Box b2 = new Box(0.1f, 0.2f, 0f);
                    Geometry units = new Geometry("Box2", b2);
                    units.setMaterial(Main.numberMat[damage % 10]);
                    units.setLocalTranslation(units.getLocalTranslation().add(new Vector3f(+0.15f, 0f, 0.5f)));

                    damagePannel.attachChild(tens);
                    damagePannel.attachChild(units);

                    Main.charNode.attachChild(damagePannel);

                    MotionPath path = new MotionPath();
                    path.addWayPoint(damagePannel.getLocalTranslation());
                    path.addWayPoint(damagePannel.getLocalTranslation().add(new Vector3f(0f, 1.5f, 0f)));

                    motionControl = new MotionEvent(damagePannel, path);
                    motionControl.setDirectionType(MotionEvent.Direction.None);
                    motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));//???
                    motionControl.setSpeed(10f);

                    motionControl.play();

                    damageNodes.add(damagePannel);
                    damageMotions.add(motionControl);

                }
            }
        }
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
