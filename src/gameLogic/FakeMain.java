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
        displayGameInstructions();
        System.out.println("Initial positions:");
        runBattle();
    }

    private static void runBattle() {
        GameBattle battle = new GameBattle();

        Creature hero = new Creature("Hero");
        Creature zombie1 = new Creature("Zombie1");
        Creature zombie2 = new Creature("Zombie2");
        Creature zombie3 = new Creature("Zombie3");

        zombie1.setAlignment("bad");
        zombie2.setAlignment("bad");
        zombie3.setAlignment("bad");

        battle.insertCreatureAt(hero, 0, 0);
        battle.insertCreatureAt(zombie1, 6, 7);
        battle.insertCreatureAt(zombie2, 6, 6);
        battle.insertCreatureAt(zombie3, 7, 6);

        int turnCounter = 1;
        int TURN_LIMIT = 30;
        //displayAllCreatureCoordinates(battle, hero, zombie1, zombie2);



        Scanner scan = new Scanner(System.in);
        int commandX;
        int commandY;

        System.out.println();
        battle.drawWithOverlayForValidCreatureMoves(hero);
        System.out.println();

        boolean keepPlaying = true;
        
        //Coordinates targetCoord = new Coordinates(3,3);

        while (battle.containsBadCreatures() && turnCounter <= TURN_LIMIT) {
            System.out.println();
            System.out.println("------------------------------------");
            System.out.println("--- TURN #" + turnCounter++ + " out of " + TURN_LIMIT + "   ---");
            System.out.println("------------------------------------");

            System.out.println("Hero's energy: " + hero.getEnergy());
            System.out.println();

            System.out.println("** MOVE TO (X,Y): ");
            try {
                System.out.print("X = ");
                commandX = scan.nextInt();
                System.out.print("Y = ");
                commandY = scan.nextInt();
                System.out.println("\nMoving to (" + commandX + ", " + commandY + ')');
                keepPlaying = playTurn(commandX, commandY, hero, battle);
            } catch (Exception e) {
                System.out.println("Error: turn ending due to invalid input");
            }

            System.out.println();
            //displayAllCreatureCoordinates(battle, hero, zombie1, zombie2);
            System.out.println();

            battle.drawWithOverlayForValidCreatureMoves(hero);
            
            int stepsAvailable = hero.getEnergy() / 2;
            /*
            System.out.println("TEST: clear path of at most " + stepsAvailable + " steps between hero and " + targetCoord
                     + ": " + battle.clearPathOfAtMostNStepsBetweenCreatureAndCoordinates(stepsAvailable, hero, targetCoord));*/
            
            battle.calculateOptimalPathsForCreature(hero);
            battle.displayOptimalPaths();

            if (!keepPlaying) {
                break;
            }
        }

        scan.close();
        System.out.println("Victory");
    }

    private static void displayAllCreatureCoordinates(GameBattle battle, Creature hero, Creature zombie1, Creature zombie2) {
        battle.displayCreatureCoordinates(hero);
        battle.displayCreatureCoordinates(zombie1);
        battle.displayCreatureCoordinates(zombie2);
    }

    private static void displayGameInstructions() {
        System.out.println("------------------------------------");
        System.out.println("--- COMMANDS");
        System.out.println("------------------------------------");
        System.out.println("- Type in X <enter>, then Y <enter> to move to coordinates (X,Y);");
        System.out.println("- To use a turn to receive energy, set X or Y to 8;");
        //System.out.println("- To use a turn to increase the turn limit, set X or Y to 9;");
        System.out.println("- To end the game, set X or Y to -1;");
        System.out.println("------------------------------------");
    }

    private static boolean playTurn(int commandX, int commandY, Creature hero, GameBattle battle) {
        boolean keepPlaying = true;
        if (commandX == 8 || commandY == 8) {
            System.out.println("Energy boost +20!");
            hero.setEnergy(hero.getEnergy() + 20);
        } else if (commandX == -1 || commandY == -1) {
            keepPlaying = false;
        } else {
            Coordinates coords = new Coordinates(commandX, commandY);
            battle.moveCreatureTo(hero, coords);
        }
        return keepPlaying;
    }
}
