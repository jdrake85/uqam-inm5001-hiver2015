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
    private Creature creatureHavingTurn;
    private List<Creature> creatureList;
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

    void moveCreatureInDirection(Creature creature, String direction) {
        assert(gameboard.containsCreature(creature));
        int xCoord = gameboard.getCreatureXCoordinate(creature);
        int yCoord = gameboard.getCreatureYCoordinate(creature);
        if (gameboard.validMoveDirectionAccordingToPosition(xCoord, yCoord, direction)) {
            gameboard.moveCreatureInDirection(creature, direction);
        } else {
            System.out.println("Error: invalid move direction '" + direction 
                    + "' for creature <" + creature + ">");
        }
    }
    
}
