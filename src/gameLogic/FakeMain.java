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
        displayGameInstructions();
        runBattle();
    }

    private static void runBattle() {
        GameBattle battle = new GameBattle();

        Creature hero = new Creature("Hero");
        hero.setAlignment("good");

        initializeScenario(battle, hero);

        int turnCounter = 1;
        int TURN_LIMIT = 30;

        Scanner scan = new Scanner(System.in);
        int commandType;
        int commandX;
        int commandY;

        boolean keepPlaying = true;

        while (battle.containsBadCreatures() && turnCounter <= TURN_LIMIT && keepPlaying) {
            System.out.println();
            System.out.println("------------------------------------");
            System.out.println("--- TURN #" + turnCounter++ + " out of " + TURN_LIMIT + "   ---");
            System.out.println("------------------------------------");
            System.out.println();

            battle.draw();
            battle.displayCombattants();
            

            System.out.print("STEP 1: SELECT COMMAND TYPE: 0 to move, or 1-4 for skills: ");
            try {
                commandType = scan.nextInt();
            } catch (Exception e) {
                System.out.println('\n' + "** Invalid choice; forcing move.");
                commandType = 0;
            }
            
            if (commandType == 0) { 
                System.out.println("MOVEMENT:");
                battle.drawWithOverlayForValidCreatureMoves(hero);
            } else if (commandType >= 1 && commandType <= 4) {
                System.out.println("USING SKILL " + commandType + ": " + hero.prepareSkill(commandType));
                battle.drawWithOverlayForCreatureSkill(hero, commandType);
            }

            System.out.println("STEP 2: SELECT VALID COORDINATES: ");
            try {
                System.out.print("     X = ");
                commandX = scan.nextInt();
                System.out.print("     Y = ");
                commandY = scan.nextInt();
                System.out.println();
                System.out.println(hero);
                keepPlaying = performTurn(commandType, commandX, commandY, hero, battle);
            } catch (Exception e) {
                System.out.println('\n' + "** Invalid coordinates; ending turn.");
            }
        }

        scan.close();
        System.out.println("Victory");
    }

    private static void displayGameInstructions() {
        System.out.println("------------------------------------");
        System.out.println("--- TURN COMMANDS");
        System.out.println("------------------------------------");
        System.out.println("------------------------------------");
        System.out.println("--- STEP 1: CHOICE OF ACTIONS");
        System.out.println("------------------------------------");
        System.out.println("- To move, enter '0'");
        System.out.println("- To use a skill numbered 1 through 4, enter '1', '2', '3' or '4'");
        System.out.println("------------------------------------");
        System.out.println("--- STEP 2: TARGET COORDINATES");
        System.out.println("- Type in X <enter>, then Y <enter> to set coordinates (X,Y);");
        System.out.println("- To use the turn to receive energy, set X to 8;");
        System.out.println("- To end the game, set X to -1;");
        System.out.println("------------------------------------");
    }

    private static boolean performTurn(int commandType, int commandX, int commandY, Creature hero, GameBattle battle) {
        boolean keepPlaying = true;
        if (commandX == 8 || commandY == 8) {
            System.out.println("Energy boost +20!");
            hero.setEnergy(hero.getEnergy() + 20);
        } else if (commandX == -1 || commandY == -1) {
            keepPlaying = false;
        } else if (commandType == 0) {
            System.out.println("Moving to (" + commandX + ", " + commandY + ")...");
            battle.moveCreatureTo(hero, new Coordinates(commandX, commandY));
        } else if (commandType >= 1 && commandType <= 4) {
            System.out.println("Using skill " + hero.prepareSkill(commandType) + " at (" + commandX + ", " + commandY + ")...");
            battle.useCreatureSkillAt(hero, commandType, new Coordinates(commandX, commandY));
        } else {
            System.out.println("** Unrecognized commands; ending turn.");
        }
        return keepPlaying;
    }

    private static void initializeScenario(GameBattle battle, Creature hero) {
        Creature zombie1 = new Creature("ZombieA1");
        Creature zombie2 = new Creature("ZombieA2");
        Creature zombie3 = new Creature("ZombieA3");

        battle.insertCreatureAt(hero, 0, 0);
        battle.insertCreatureAt(zombie1, 6, 7);
        battle.insertCreatureAt(zombie2, 6, 6);
        battle.insertCreatureAt(zombie3, 7, 6);

        for (int i = 0; i < 7; i++) {
            battle.insertCreatureAt(new Creature("ZombieB" + i), i, 1);
            battle.insertCreatureAt(new Creature("ZombieC" + i + 1), i + 1, 3);
            battle.insertCreatureAt(new Creature("ZombieD" + i), i, 5);
        }

        battle.removeCreatureAt(4, 3);
        battle.removeCreatureAt(0, 5);
    }
}
