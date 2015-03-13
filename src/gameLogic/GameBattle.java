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
import java.util.List;
import java.util.Random;

/**
 *
 * @author User
 */
public class GameBattle {

    private Creature creatureHavingTurn; // TODO: Not used or implemented yet
    private List<Creature> creatureList; // Fast access to creatures on gameboard
    private OptimalPaths paths;
    private GameBoard gameboard;
    private boolean[][] currentOverlay = null;
    private Creature creaturesByTurn[] = null;

    public GameBattle() {
        paths = new OptimalPaths(8, 8);
        gameboard = new GameBoard();
        creaturesByTurn = initializeBlankCreaturesByTurn();
    }

    private Creature[] initializeBlankCreaturesByTurn() {
        Creature[] creatures = new Creature[5];
        for (Creature creature : creatures) {
            creature = null;
        }
        return creatures;
    }

    public void displayCombattants() {
        refreshCreatureList();
        System.out.println("\nCOMBATTANTS:");
        for (Creature creature : creatureList) {
            creature.displayStats();
        }
        System.out.println();
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
        System.out.println("Creature list: " + creatureList);
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

    public void setCreatureHavingTurn(Creature creature) {
        creatureHavingTurn = creature;
    }

    public void endTurn() {
        refreshCreatureList();
        Creature lastCreature = creatureHavingTurn;
        creaturesByTurn[0] = null;
        calculateCreatureTurnOrder();   
        creatureHavingTurn = creaturesByTurn[0];
        
        System.out.println("Ending turn for " + lastCreature + ", passing to " + creatureHavingTurn);
    }

    private void calculateCreatureTurnOrder() {
        int blankCreatureCounter = 0;
        int numberOfTurns = creaturesByTurn.length;
        for (int i = 0; i < numberOfTurns; i++) {
            System.out.println("Creature by turn: " + creaturesByTurn[i]);
            if (creaturesByTurn[i] == null) {
                creaturesByTurn[i] = creatureList.get(blankCreatureCounter++);
                System.out.println("Creature is now: " + creaturesByTurn[i]);
            } else if (!creaturesByTurn[i].isAlive()) {
                for (int j = i; j < numberOfTurns - 1; j++) { 
                    creaturesByTurn[j] = creaturesByTurn[j + 1];
                    System.out.println("Creature is now: " + creaturesByTurn[j]);
                }
            }
        }
    }
    
    public boolean isZombieTurn() {
        return creatureHavingTurn.isAlignedTo("bad");
    }
    
    public void randomlyMoveZombie() {
        if (isZombieTurn()) {
            moveCreatureRandomly(creatureHavingTurn);
        }
    }

    public Creature[] getCreatureTurnOrder() {
        return creaturesByTurn;
    }

    public void moveCreatureRandomly(Creature creature) {
        if (creature.isAlignedTo("bad")) {
            creature.setEnergy(12);
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
