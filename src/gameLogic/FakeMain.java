/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import java.util.Scanner;

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

        Creature hero = new Creature("Hero");
        Creature zombie1 = new Creature("Zombie1");
        Creature zombie2 = new Creature("Zombie2");

        zombie1.setAlignment("bad");
        zombie2.setAlignment("bad");

        battle.insertCreatureAt(hero, 0, 0);
        battle.insertCreatureAt(zombie1, 0, 1);
        battle.insertCreatureAt(zombie2, 6, 6);

        int turnCounter = 1;
        int TURN_LIMIT = 20;

        System.out.println("------------------------------------");
        System.out.println("--- COMMANDS");
        System.out.println("------------------------------------");
        System.out.println("-- MOVE:              8           --");
        System.out.println("--                  4   6         --");
        System.out.println("--                    2           --");
        System.out.println("-- GET ENERGY:     5              --");
        System.out.println("-- EXIT:           0              --");
        System.out.println("-- GET MORE TURNS: 9              --");
        System.out.println("------------------------------------");
        System.out.println();

        Scanner scan = new Scanner(System.in);
        int command;

        System.out.println();
        battle.drawWithOverlayForValidCreatureMoves(hero);
        System.out.println();

        while (battle.containsBadCreatures() && turnCounter <= TURN_LIMIT) {

            System.out.println();
            System.out.println("------------------------------------");
            System.out.println("--- TURN #" + turnCounter++ + " out of " + TURN_LIMIT + "   ---");
            System.out.println("------------------------------------");
            
            
            System.out.println("Hero's energy: " + hero.getEnergy());
            System.out.println();
            
            System.out.print("** YOUR MOVE: ");
            command = scan.nextInt();
            System.out.println();

            if (command == 8) {
                battle.moveCreatureInDirection(hero, "+y");
            } else if (command == 2) {
                battle.moveCreatureInDirection(hero, "-y");
            } else if (command == 4) {
                battle.moveCreatureInDirection(hero, "-x");
            } else if (command == 6) {
                battle.moveCreatureInDirection(hero, "+x");
            } else if (command == 5) {
                System.out.println("Energy boost +20!");
                hero.setEnergy(hero.getEnergy()+20);
            } else if (command == 0) {
                break;
             } else if (command == 9) {
                TURN_LIMIT += 5;  
            } else {
                System.out.println("** Error: unrecognized command, ending turn");
            }
            
            System.out.println();
            battle.displayCreatureCoordinates(hero);
            battle.displayCreatureCoordinates(zombie1);
            battle.displayCreatureCoordinates(zombie2);
            System.out.println();
            
            battle.drawWithOverlayForValidCreatureMoves(hero);
        }

        System.out.println("Victory");
    }
}
