/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import java.util.List;

/**
 *
 * @author User
 */
public class GameBattle {

    //private Creature creatureHavingTurn; // TODO: Not used or implemented yet
    private List<Creature> creatureList; // Fast access to creatures on gameboard
    private OptimalPaths paths;
    private GameBoard gameboard;

    public GameBattle() {
        paths = new OptimalPaths(8,8);
        gameboard = new GameBoard();
    }
    
    public boolean[][] getValidMovesOverlayForCreature(Creature creature) {
        boolean[][] movesOverlay = new boolean[8][8];
        calculateOptimalPathsForCreature(creature);
        int stepCount = creature.maximumStepsAbleToWalk();
        return paths.getTilesReachableInAtMostNSteps(stepCount);
    }
    
    private void calculateOptimalPathsForCreature(Creature creature) {
        Coordinates initCoords = gameboard.getCreatureCoordinates(creature);
        boolean[][] occupiedTiles = gameboard.getOccupiedTiles();
        paths.calculateOptimalPathsStartingFromCoordinates(occupiedTiles , initCoords);
    }
    
    public void displayOptimalPaths() {
        paths.displayOptimalPaths();
    }

    public void refreshCreatureList() {
        creatureList = gameboard.getFullCreatureList();
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
        gameboard.draw();
    }

    public void drawWithOverlayForValidCreatureMoves(Creature creature) {
        boolean[][] overlay = getValidMovesOverlayForCreature(creature);
        gameboard.draw(overlay);
    }

    public void insertCreatureAt(Creature creature, int xCoord, int yCoord) {
        gameboard.insertCreatureAt(creature, xCoord, yCoord);
        refreshCreatureList();
    }
    
    // Quick implementation
    public void removeCreatureAt(int xCoord, int yCoord) { 
        Tile tile = gameboard.getTileAt(xCoord, yCoord);
        tile.removeOccupier(); 
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
            int stepsRequired = gameboard.tileCountBetweenCreatureAndCoordinates(creature, destCoords);
            creature.consumeEnergyForSteps(stepsRequired);
            gameboard.moveCreatureTo(creature, destCoords);
        } else {
            System.out.println("Error: creature cannot move to " + destCoords);
        }
    }

    // TODO: implement pathfinding
    private boolean creatureCanMoveTo(Creature creature, Coordinates destCoords) {
        int availableSteps = creature.maximumStepsAbleToWalk();
        //System.out.println("Creature can pay for " + stepsRequired + " steps: " + creature.canPayEnergyCostForSteps(stepsRequired));
        //System.out.println("Destination coordinates " + destCoords + " are valid: " + gameboard.validDestinationTileAt(destCoords));
        return paths.coordinatesReachableInAtMostDistanceOf(destCoords, availableSteps);
    }

    public void useCreatureSkillAt(Creature creature, int skillNumber, Coordinates coords) {
        if (creatureCanUseSkillAt(creature, skillNumber, coords)) {
            creature.consumeEnergyForSkillNumber(skillNumber);
            //TODO: implement
        } // Outputs for debugging purposes handled by Creature/Gameboard classes
    }

    private boolean creatureCanUseSkillAt(Creature creature, int skillNumber, Coordinates coords) {
        return false; // TODO: implement
    }
}
