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
        System.out.println("*** Fake main running");
        System.out.println("------------------------------------");
        System.out.println("Initial positions:");
        runBattle();
    }

    private static void runBattle() {
        GameBattle battle = new GameBattle();

        Creature hero = new Creature();
        Creature zombie1 = new Creature();
        Creature zombie2 = new Creature();

        zombie1.setAlignment("bad");
        zombie2.setAlignment("bad");

        battle.insertCreatureAt(hero, 0, 0);
        battle.insertCreatureAt(zombie1, 0, 1);
        battle.insertCreatureAt(zombie2, 6, 6);

        int turnCounter = 1;
        int TURN_LIMIT = 18;

        System.out.print("HERO: ");
        battle.displayCreatureCoordinates(hero);
        System.out.print("ZOMBIE1: ");
        battle.displayCreatureCoordinates(zombie1);
        System.out.print("ZOMBIE2: ");
        battle.displayCreatureCoordinates(zombie2);
        System.out.println("------------------------------------");
        System.out.println();

        while (battle.containsBadCreatures() && turnCounter <= TURN_LIMIT) {
            if (turnCounter % 2 == 1) {
                System.out.println("Moving +y...");
                battle.moveCreatureInDirection(hero, "+y");
            } else {
                System.out.println("Moving +x...");
                battle.moveCreatureInDirection(hero, "+x");
            }

            System.out.print("HERO: ");
            battle.displayCreatureCoordinates(hero);
            System.out.print("ZOMBIE1: ");
            battle.displayCreatureCoordinates(zombie1);
            System.out.print("ZOMBIE2: ");
            battle.displayCreatureCoordinates(zombie2);

            System.out.println();
            System.out.println("Turn " + turnCounter++ + " completed");
            System.out.println("------------------------------------");
            System.out.println();
        }

        System.out.println("Victory");
    }
}
