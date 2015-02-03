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
    
    public void insertCreatureAt(Creature creature, int xCoord, int yCoord) {
        gameboard.insertCreatureAt(creature, xCoord, yCoord);
        refreshCreatureList();
    }
    
    public boolean creatureCanMoveInDirection(Creature creature, String direction) {
        assert(gameboard.containsCreature(creature));
        Coordinates coordinates = gameboard.getCreatureCoordinates(creature);
        int xCoord = coordinates.getXCoord();
        int yCoord = coordinates.getYCoord();
        return gameboard.validMoveAccordingToDirectionRelativeTo(xCoord, yCoord, direction);
    } 

    public void moveCreatureInDirection(Creature creature, String direction) {
        assert(creatureCanMoveInDirection(creature, direction));
        gameboard.moveCreatureInDirection(creature, direction);
        
    }
}
