/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gameLogic.gameboard.GameBoard;
import gameLogic.gameboard.Tile;
import gameLogic.pathfinding.Coordinates;
import gameLogic.pathfinding.CoordPath;
import gameLogic.pathfinding.OptimalPaths;
import gameLogic.skills.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author User
 */
public class GameBattle {

    private Creature creaturePlayingTurn; // TODO: Not used or implemented yet
    private List<Creature> creatureList; // Fast access to creatures on gameboard
    private OptimalPaths paths;
    private GameBoard gameboard;
    private boolean[][] currentOverlay = null;
    private PriorityQueue<CreatureSpeedTurnTriplet> creaturePriority = new PriorityQueue<CreatureSpeedTurnTriplet>();
    private int turnCounter = 1;

    public GameBattle() {
        paths = new OptimalPaths(8, 8);
        gameboard = new GameBoard();
    }

    public boolean[][] getOverlayForCreatureSkill(Creature creature, int skillNumber) {
        Skill skill = creature.prepareSkill(skillNumber);
        Coordinates originatingCoords = gameboard.getCreatureCoordinates(creature);
        skill.setOriginatingFrom(originatingCoords);
        return gameboard.getSkillOverlay(skill);
    }

    public boolean[][] getOverlayForCreatureMoves(Creature creature) {
        calculateOptimalPathsForCreature(creature);
        int stepCount = creature.maximumStepsAbleToWalk();
        return paths.getTilesReachableInAtMostNSteps(stepCount);
    }

    private void calculateOptimalPathsForCreature(Creature creature) {
        Coordinates initCoords = gameboard.getCreatureCoordinates(creature);
        boolean[][] occupiedTiles = gameboard.getOccupiedTiles();
        paths.calculateOptimalPathsStartingFromCoordinates(occupiedTiles, initCoords);
    }

    public void displayOptimalPaths() {
        paths.displayOptimalPaths();
    }

    public void refreshCreatureList() {
        gameboard.removeDeadCreatures();
        creatureList = gameboard.getFullCreatureList();
        //System.out.println("Refreshing list of ALL creatures on the map: " + creatureList);
    }

    public boolean containsGoodCreatures() {
        List<Creature> goodCreatures = gameboard.getGoodCreatureList();
        return !goodCreatures.isEmpty();
    }

    public boolean containsBadCreatures() {
        List<Creature> badCreatures = gameboard.getBadCreatureList();
        return !badCreatures.isEmpty();
    }

    public boolean containsCreature(Creature targetCreature) {
        refreshCreatureList(); //precaution
        boolean creatureFound = false;
        for (Creature creature : creatureList) {
            if (creature.equals(targetCreature)) {
                creatureFound = true;
                break;
            }
        }
        return creatureFound;
    }

    public void displayCreatureCoordinates(Creature creature) {
        gameboard.displayCreatureCoordinates(creature);
    }

    public void draw() {
        currentOverlay = null;
        gameboard.draw();
    }

    public void drawWithOverlayForCreatureMoves(Creature creature) {
        currentOverlay = getOverlayForCreatureMoves(creature);
        gameboard.draw(currentOverlay);
    }

    public void drawWithOverlayForCreatureSkill(Creature creature, int skillNumber) {
        currentOverlay = getOverlayForCreatureSkill(creature, skillNumber);
        gameboard.draw(currentOverlay);
    }

    public void insertCreatureAt(Creature creature, int xCoord, int yCoord) {
        gameboard.insertCreatureAt(creature, xCoord, yCoord);
        creature.displayCreatureOn3DBoard(xCoord, yCoord);
        refreshCreatureList();
    }

    // Quick implementation
    public void removeCreatureAt(int xCoord, int yCoord) {
        Tile tile = gameboard.getTileAt(xCoord, yCoord);
        tile.removeOccupier();
        //TODO creature.hideCreatureOn3DBoard();
    }

    // Unit movement
    public void moveCreatureInDirection(Creature creature, String direction) {
        if (creatureCanMoveInDirection(creature, direction)) {
            creature.consumeEnergyForSteps(1);
            gameboard.moveCreatureInDirection(creature, direction);
        } // Outputs for debugging purposes handled by Creature/Gameboard classes
    }

    // Unit movement
    public boolean creatureCanMoveInDirection(Creature creature, String direction) {
        boolean validMove = false;
        // Outputs for debugging purposes
        if (!creature.canPayEnergyCostForSteps(1)) {
            System.out.println("Error: creature does not have energy to move");
        } else { // Outputs handled by gameboard methods
            validMove = gameboard.validDestinationTile(creature, direction);
        }
        return validMove;
    }

    public void moveCreatureTo(Creature creature, Coordinates destCoords) {
        if (creatureCanMoveTo(creature, destCoords)) {

            CoordPath pathChosen = paths.getPathForCreatureToCoordinates(creature, destCoords);
            creature.consumeEnergyForSteps(pathChosen.length() - 1);
            System.out.println(pathChosen);

            gameboard.moveCreatureTo(creature, destCoords);
            creature.displayCreatureOn3DBoard(destCoords.getXCoord(), destCoords.getYCoord());

        } else {
            System.out.println("Error: creature cannot move to " + destCoords);
        }
    }

    // TODO: implement pathfinding
    private boolean creatureCanMoveTo(Creature creature, Coordinates destCoords) {
        int availableSteps = creature.maximumStepsAbleToWalk();
        return paths.coordinatesReachableInAtMostDistanceOf(destCoords, availableSteps);
    }

    public void useCreatureSkillAt(Creature creature, int skillNumber, Coordinates coords) {
        Skill skill = creature.prepareSkill(skillNumber);
        Coordinates originatingCoords = gameboard.getCreatureCoordinates(creature);
        skill.setOriginatingFrom(originatingCoords);
        if (creatureCanUseSkillAt(creature, skillNumber, coords)) {
            creature.consumeEnergyForSkillNumber(skillNumber);
            skill.setTargetCoordinates(coords);
            gameboard.performTargetedSkill(skill);
            removeDeadCreaturesFromTurnOrder();
        } else if (!creature.canPayEnergyCostForSkillNumber(skillNumber)) {
            System.out.println("Not enough energy!");
        }
    }

    private boolean creatureCanUseSkillAt(Creature creature, int skillNumber, Coordinates coords) {
        Skill skill = creature.prepareSkill(skillNumber);
        currentOverlay = gameboard.getSkillOverlay(skill);
        int xCoord = coords.getXCoord();
        int yCoord = coords.getYCoord();
        return creature.canPayEnergyCostForSkillNumber(skillNumber)
                && currentOverlay[xCoord][yCoord];
    }

    public void start() {
        refreshCreatureList();
        while (creaturePriority.size() < 5) {
            initializeCreaturePriority();
        }
        CreatureSpeedTurnTriplet startingPair = creaturePriority.peek();
        // System.out.println("*** NEXT TURN: " + startingPair);
        System.out.println('\n' + "------------------" + '\n' + "TURN #" + turnCounter++ + '\n' + "------------------");
        creaturePlayingTurn = startingPair.getCreature();
        displayCreatureSpeedPairsForTurnOrder();
    }

    private void initializeCreaturePriority() {
        for (Creature creature : creatureList) {
            addCreatureToCreaturePriorityOnce(creature);
        }
    }

    // Note: same creature can be added several times to the priority queue, each time having a higher speed value
    private void addCreatureToCreaturePriorityOnce(Creature creature) {
        creaturePriority.add(new CreatureSpeedTurnTriplet(creature));
        creature.incrementTurnsAssigned();
        creature.incrementTurnSpeedAfterEndOfTurn();
    }

    private void addCreatureToCreaturePriorityRecursivelyAtLeastOnceAccordingToSpeed(Creature creature) {
        do {
            addCreatureToCreaturePriorityOnce(creature);
        } while (creature.getCumulativeTurnSpeed() < getMaximumCumulativeSpeedInCreaturePriority());
    }

    private int getMaximumCumulativeSpeedInCreaturePriority() {
        int maximumCumulativeSpeed = 0;
        for (Creature creature : creatureList) {
            int cumulativeSpeed = creature.getCumulativeTurnSpeed();
            if (cumulativeSpeed > maximumCumulativeSpeed) {
                maximumCumulativeSpeed = cumulativeSpeed;
            }
        }
        return maximumCumulativeSpeed;
    }

    public void endTurn() {
        refreshCreatureList();
        creaturePriority.poll();
        //System.out.println("*** TURN PLAYED BY: " + creaturePriority.poll()); // Pop creature who just played from priority queue
        addCreatureToCreaturePriorityRecursivelyAtLeastOnceAccordingToSpeed(creaturePlayingTurn); // Push creature who just played, who is now 'slower'
        CreatureSpeedTurnTriplet nextPair = creaturePriority.peek();
        creaturePlayingTurn = nextPair.getCreature();
        //System.out.println("*** NEXT TURN: " + nextPair);
        System.out.println('\n' + "------------------" + '\n' + "TURN #" + turnCounter++ + '\n' + "------------------");
        displayCreatureSpeedPairsForTurnOrder();
    }

    private void removeDeadCreaturesFromTurnOrder() {
        List<Creature> deadCreatures = null;
        for (Creature creature : creatureList) {
            if (!creature.isAlive()) {
                if (deadCreatures == null) {
                    deadCreatures = new ArrayList<Creature>();
                }
                deadCreatures.add(creature);;
            }
        }
        // TODO: implement removal of dead creatures found in priority queue CreatureSpeed pairs
        /*
         if (deadCreatures != null) {
         for (Creature deadCreature : deadCreatures) {
         creatureList.remove(deadCreature);
         creaturePriority.remove(deadCreature);
         }
         }
         */
    }

    public boolean isZombieTurn() {
        return creaturePlayingTurn.isAlignedTo("bad");
    }

    public Creature[] getCreatureTurnOrder() {
        int allCreatureCount = creatureList.size();
        Creature[] fullCreatureTurnOrder = new Creature[allCreatureCount];
        creaturePriority.toArray(fullCreatureTurnOrder);
        Arrays.sort(fullCreatureTurnOrder);
        Creature[] fiveCreatureTurnOrder = new Creature[5];
        System.arraycopy(fullCreatureTurnOrder, 0, fiveCreatureTurnOrder, 0, Math.min(5, allCreatureCount));
        for (int i = allCreatureCount; i < 5; i++) {
            fiveCreatureTurnOrder[i] = null;
        }
        return fiveCreatureTurnOrder;
    }

    public void displayCreatureTurnOrder() {
        Creature[] creatureTurnOrder = getCreatureTurnOrder();
        System.out.print("TURN ORDER BANNER: ");
        for (Creature creature : creatureTurnOrder) {
            if (creature != null) {
                System.out.print(creature + ": " + creature.getTurnsAssigned() + " times speed of " + creature.getCumulativeTurnSpeed() + ", ");
            }
        }
        System.out.println();
    }

    public void displayCreatureSpeedPairsForTurnOrder() {
        CreatureSpeedTurnTriplet[] allCreatureSpeedTurnOrder = new CreatureSpeedTurnTriplet[creaturePriority.size()];
        creaturePriority.toArray(allCreatureSpeedTurnOrder);
        Arrays.sort(allCreatureSpeedTurnOrder);
        CreatureSpeedTurnTriplet[] fiveCreatureSpeedTurnOrder = new CreatureSpeedTurnTriplet[5];
        System.arraycopy(allCreatureSpeedTurnOrder, 0, fiveCreatureSpeedTurnOrder, 0, Math.min(5, creaturePriority.size()));
        for (int i = creaturePriority.size(); i < 5; i++) {
            fiveCreatureSpeedTurnOrder[i] = null;
        }
        System.out.print("TURN ORDER BANNER: ");
        for (CreatureSpeedTurnTriplet pair : fiveCreatureSpeedTurnOrder) {
            if (pair != null) {
                System.out.print(pair.toString() + ", ");
            }
        }
        System.out.println();
    }

    public Creature getCreaturePlayingTurn() {
        return creaturePlayingTurn;
    }

    public void randomlyMoveZombie() {
        if (isZombieTurn()) {
            System.out.println("Moving zombie: " + creaturePlayingTurn);
            moveCreatureRandomly(creaturePlayingTurn);
        }
    }

    public void moveCreatureRandomly(Creature creature) {
        if (creature.isAlignedTo("bad")) {
            creature.setEnergy(12); // Make sure enemy creature can move somewhat each turn
        }
        drawWithOverlayForCreatureMoves(creature);
        int validMoves = 0;
        // Counting valid moves (first pass)
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (currentOverlay[i][j]) {
                    validMoves++;
                }
            }
        }
        Random generator = new Random();
        int randomMove = generator.nextInt(validMoves);
        int movesCounter = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (currentOverlay[i][j] && movesCounter++ == randomMove) {
                    moveCreatureTo(creature, new Coordinates(i, j));
                }
            }
        }
    }
}
