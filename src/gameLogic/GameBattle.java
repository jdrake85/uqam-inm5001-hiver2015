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

    private Creature creatureHavingTurn; // Not used or implemented yet
    private List<Creature> creatureList; // Fast access to creatures on gameboard
    private GameBoard gameboard;

    public GameBattle() {
        gameboard = new GameBoard();
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
        boolean[][] overlay = getValidCreatureMoves(creature);
        gameboard.draw(overlay);
    }

    public void insertCreatureAt(Creature creature, int xCoord, int yCoord) {
        gameboard.insertCreatureAt(creature, xCoord, yCoord);
        refreshCreatureList();
    }

    public boolean[][] getValidCreatureMoves(Creature creature) {
        int xDim = gameboard.getDimensionAlongXAxis();
        int yDim = gameboard.getDimensionAlongYAxis();
        boolean validMoves[][] = new boolean[xDim][yDim];
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                Coordinates destCoords = new Coordinates(i, j);
                if (creatureCanMoveTo(creature, destCoords)) {
                    validMoves[i][j] = true;
                } else {
                    validMoves[i][j] = false;
                }
            }
        }
        return validMoves;
    }

    public void moveCreatureInDirection(Creature creature, String direction) {
        if (creatureCanMoveInDirection(creature, direction)) {
            creature.consumeEnergyForSteps(1);
            gameboard.moveCreatureInDirection(creature, direction);
        } // Outputs for debugging purposes handled by Creature/Gameboard classes
    }

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
            System.out.println("** creature can move to " + destCoords);
            int stepsRequired = gameboard.tileCountBetweenCreatureAndCoordinates(creature, destCoords);
            creature.consumeEnergyForSteps(stepsRequired);
            gameboard.moveCreatureTo(creature, destCoords);
        } else {
            System.out.println("Error: creature cannot move to " + destCoords);
        }
    }

    // TODO: implement pathfinding or having the path being blocked
    private boolean creatureCanMoveTo(Creature creature, Coordinates destCoords) {
        int stepsRequired = gameboard.tileCountBetweenCreatureAndCoordinates(creature, destCoords);
        //System.out.println("Creature can pay for " + stepsRequired + " steps: " + creature.canPayEnergyCostForSteps(stepsRequired));
        //System.out.println("Destination coordinates " + destCoords + " are valid: " + gameboard.validDestinationTileAt(destCoords));
        return creature.canPayEnergyCostForSteps(stepsRequired) && gameboard.validDestinationTileAt(destCoords);
    }

    public void useCreatureSkillAt(Creature creature, int skillNumber, Coordinates coords) {
        if (creatureCanUseSkillAt(creature, skillNumber, coords)) {
            creature.consumeEnergyForSkillNumber(skillNumber);

        } // Outputs for debugging purposes handled by Creature/Gameboard classes
    }

    private boolean creatureCanUseSkillAt(Creature creature, int skillNumber, Coordinates coords) {
        return false;
    }
}
