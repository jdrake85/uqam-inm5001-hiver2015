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
        for (Creature creature: creatureList) {
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
    
    public void insertCreatureAt(Creature creature, int xCoord, int yCoord) {
        gameboard.insertCreatureAt(creature, xCoord, yCoord);
        refreshCreatureList();
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
     
     public void useCreatureSkillAt(Creature creature, int skillNumber, Coordinates coords) {
         if(creatureCanUseSkillAt(creature, skillNumber, coords)) {
            creature.consumeEnergyForSkillNumber(skillNumber);
            
        } // Outputs for debugging purposes handled by Creature/Gameboard classes
     }

    private boolean creatureCanUseSkillAt(Creature creature, int skillNumber, Coordinates coords) {
        return false;
    }
}
