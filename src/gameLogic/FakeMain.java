/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gameLogic.skills.nurse.*;
import gameLogic.skills.hero.*;
import gameLogic.skills.soldier.*;
import gameLogic.pathfinding.Coordinates;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class FakeMain {

    public static void main(String[] args) {
        //displayGameInstructions();
        runBattle();
    }

    protected static void runBattle() { //private to protected
        GameBattle battle = new GameBattle();

        Creature hero = initializeHero(battle);
        initializeScenario(battle, hero);

        int turnCounter = 1;
        int TURN_LIMIT = 30;

        Scanner scan = new Scanner(System.in);
        int commandType;
        int commandX;
        int commandY;

        boolean keepPlaying = true;
/*
        while (battle.containsBadCreatures() && turnCounter <= TURN_LIMIT && keepPlaying) {
            System.out.println();
            System.out.println("------------------------------------");
            System.out.println("--- TURN #" + turnCounter++ + " out of " + TURN_LIMIT + "   ---");
            System.out.println("------------------------------------");
            System.out.println();

            battle.draw();
            battle.displayCombattants();
            displaySkills();

            
            System.out.print("STEP 1: SELECT COMMAND TYPE: 0 to move, or 1-12 for skills: ");
            try {
                commandType = scan.nextInt();
            } catch (Exception e) {
                System.out.println('\n' + "** Invalid choice; forcing move.");
                commandType = 0;
            }

            if (commandType == 0) {
                System.out.println("MOVEMENT:");
                battle.drawWithOverlayForCreatureMoves(hero);
            } else if (commandType >= 1 && commandType <= 12) {
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
                keepPlaying = performTurn(commandType, commandX, commandY, hero, battle);
            } catch (Exception e) {
                System.out.println('\n' + "** Invalid coordinates; ending turn.");
            }

           battle.refreshCreatureList();
        }

        scan.close();
        System.out.println("Victory");*/
    }

    private static void displayGameInstructions() {
        System.out.println("------------------------------------");
        System.out.println("--- TURN COMMANDS");
        System.out.println("------------------------------------");
        System.out.println("------------------------------------");
        System.out.println("--- STEP 1: CHOICE OF ACTIONS");
        System.out.println("------------------------------------");
        System.out.println("- To move, enter '0'");
        System.out.println("- To use a skill numbered 1 through 12, enter the corresponding number");
        System.out.println("------------------------------------");
        System.out.println("--- STEP 2: TARGET COORDINATES");
        System.out.println("- Type in X <enter>, then Y <enter> to set coordinates (X,Y);");
        System.out.println("- To use the turn to receive energy, set X to 8;");
        System.out.println("- To end the game, set X to -1;");
        System.out.println("------------------------------------");
    }

    protected static boolean performTurn(int commandType, int commandX, int commandY, Creature hero, GameBattle battle) {
        boolean keepPlaying = true;
        if (commandX == 8 || commandY == 8) {
            System.out.println("Energy boost +40!");
            hero.setEnergy(hero.getEnergy() + 40);
        } else if (commandX == -1 || commandY == -1) {
            keepPlaying = false;
        } else if (commandType == 0) {
            System.out.println("Moving to (" + commandX + ", " + commandY + ")...");
            battle.moveCreatureTo(hero, new Coordinates(commandX, commandY));
        } else if (commandType >= 1 && commandType <= 12) {
            System.out.println("Using skill " + hero.prepareSkill(commandType) + " at (" + commandX + ", " + commandY + ")...");
            battle.useCreatureSkillAt(hero, commandType, new Coordinates(commandX, commandY));
        } else {
            System.out.println("** Unrecognized commands; ending turn.");
        }
        return keepPlaying;
    }

    protected static void initializeScenario(GameBattle battle, Creature hero) {
        Creature zombie1 = new Creature("ZombieA1");
        Creature zombie2 = new Creature("ZombieA2");
        Creature zombie3 = new Creature("ZombieA3");
        
        battle.insertCreatureAt(zombie1, 1, 1);
        battle.insertCreatureAt(zombie2, 1, 2);

        
/*
        battle.insertCreatureAt(zombie1, 6, 7);
        battle.insertCreatureAt(zombie2, 6, 6);
        battle.insertCreatureAt(zombie3, 7, 6);

        for (int i = 0; i < 7; i++) {
            battle.insertCreatureAt(new Creature("ZombieB" + i), i, 1);
            battle.insertCreatureAt(new Creature("ZombieC" + i + 1), i + 1, 2);
            battle.insertCreatureAt(new Creature("ZombieD" + i + 1), i + 1, 3);
            battle.insertCreatureAt(new Creature("ZombieE" + i + 1), i + 1, 4);
            battle.insertCreatureAt(new Creature("ZombieF" + i), i, 5);
        }

        battle.removeCreatureAt(4, 3);
        battle.removeCreatureAt(0, 5);*/
    }

    protected static Creature initializeHero(GameBattle battle) {
        Creature hero = new Creature("Hero");
        hero.setAlignment("good");
        battle.insertCreatureAt(hero, 0, 0);
        assignAllSkillsTo(hero);
        return hero;
    }

    protected static void assignAllSkillsTo(Creature hero) {
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        hero.setSkillAsNumber(new SpinningPipe(3, 4), 3);
        hero.setSkillAsNumber(new Knockback(4, 4), 4);
        hero.setSkillAsNumber(new Heal(5, 4), 5);
        hero.setSkillAsNumber(new Innoculation(6, 4), 6);
        hero.setSkillAsNumber(new MustardGas(7, 4), 7);
        hero.setSkillAsNumber(new Push(8, 4), 8);
        hero.setSkillAsNumber(new AimedShot(9, 4), 9);
        hero.setSkillAsNumber(new ShootEmAll(10, 4), 10);
        hero.setSkillAsNumber(new Stab(11, 4), 11);
        hero.setSkillAsNumber(new CutThroat(12, 1), 12);

    }
    
    protected static void displaySkills() {
        System.out.println();/*
        System.out.println("SKILLS:");
        System.out.println("1 - Strike");
        System.out.println("2 - Home Run");
        System.out.println("3 - Spinning Pipe");
        System.out.println("4 - Knockback");
        System.out.println("5 - Heal");
        System.out.println("6 - Innoculation");
        System.out.println("7 - Mustard Gas");
        System.out.println("8 - Push");
        System.out.println("9 - Aimed Shot");
        System.out.println("10 - Shoot 'Em All");
        System.out.println("11 - Stab");
        System.out.println("12 - Cut Throat");*/
        System.out.println();
    }
}
