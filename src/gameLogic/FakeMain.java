/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

/**
 *
 * @author User
 */
public class FakeMain {
    
    public static void main(String[] args) {
        System.out.println("Fake main running");
        runBattle();
    }
    
    private static void runBattle() {
        GameBattle battle = new GameBattle();
        
        Creature hero = new Creature();
        Creature zombie = new Creature();
        
        battle.insertCreatureAt(hero, 0, 0);
        battle.insertCreatureAt(zombie, 1, 0);
        
        int turnCounter = 0;
        int TURN_LIMIT = 5;
        
        while (battle.containsBadCreatures() && turnCounter++ < TURN_LIMIT) {
            battle.displayCreatureCoordinates(hero);
            battle.moveCreatureInDirection(hero, "+x");
            battle.displayCreatureCoordinates(zombie);
        }
        
        System.out.println("Victory");
    }
}
