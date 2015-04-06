/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameGUI;

import java.util.ArrayList;

/**
 *
 * @author This PC
 */
public class GUIData {

    public static ArrayList<String> buttons;
    public static ArrayList<String> facesHolders;
    public static ArrayList<String> turnsHolders;
    public static ArrayList<String> skillsHolders;
    public static ArrayList<String> moveAndEnd;
    public static ArrayList<String> hpAndEnergy;
    public static ArrayList<String> skillsList;
    public static ArrayList<String> picsPaths;
    public static String imagesPath = "Interface/Images/";
    public static String noneAsChar = imagesPath + "None.png";
    public static String lockedAsSkill = imagesPath + "Locked.png";
    public static String greyedAsMoveAndEnd = imagesPath + "GreyedMnE.png";
    public static String heroMovePng = imagesPath + "heroMove.png";
    public static String heroEndTurnPng = imagesPath + "heroEndTurn.png";
    public static String heroHpPng = imagesPath + "health.png";
    public static String heroEnergyPng = imagesPath + "energy.png";
    public static String greyedAsHpAndEnergy = imagesPath + "GreyedHnE.png";

    public static ArrayList<String> fillButtons() {
        
        buttons = new ArrayList<String>();

        buttons.add("hero1Skill1");
        buttons.add("hero1Skill2");
        buttons.add("hero1Skill3");
        buttons.add("hero1Skill4");
        buttons.add("hero2Skill1");
        buttons.add("hero2Skill2");
        buttons.add("hero2Skill3");
        buttons.add("hero2Skill4");
        buttons.add("hero3Skill1");
        buttons.add("hero3Skill2");
        buttons.add("hero3Skill3");
        buttons.add("hero3Skill4");
        buttons.add("hero1move");
        buttons.add("hero1endTurn");
        buttons.add("hero2move");
        buttons.add("hero2endTurn");
        buttons.add("hero3move");
        buttons.add("hero3endTurn");
        return buttons;
    }

    public static ArrayList<String> fillFacesHolders() {

        facesHolders = new ArrayList<String>();

        facesHolders.add("hero1pic");
        facesHolders.add("hero2pic");
        facesHolders.add("hero3pic");
        facesHolders.add("turn1");
        facesHolders.add("turn2");
        facesHolders.add("turn3");
        facesHolders.add("turn4");
        facesHolders.add("turn5");
        return facesHolders;
    }

    public static ArrayList<String> fillTurnsHolders() {

        turnsHolders = new ArrayList<String>();

        turnsHolders.add("turn1");
        turnsHolders.add("turn2");
        turnsHolders.add("turn3");
        turnsHolders.add("turn4");
        turnsHolders.add("turn5");
        return turnsHolders;
    }

    public static ArrayList<String> fillSkillsHolders() {

        skillsHolders = new ArrayList<String>();

        skillsHolders.add("hero1sk1");
        skillsHolders.add("hero1sk2");
        skillsHolders.add("hero1sk3");
        skillsHolders.add("hero1sk4");
        skillsHolders.add("hero2sk1");
        skillsHolders.add("hero2sk2");
        skillsHolders.add("hero2sk3");
        skillsHolders.add("hero2sk4");
        skillsHolders.add("hero3sk1");
        skillsHolders.add("hero3sk2");
        skillsHolders.add("hero3sk3");
        skillsHolders.add("hero3sk4");
        return skillsHolders;
    }

    public static ArrayList<String> fillMoveAndEnd() {

        moveAndEnd = new ArrayList<String>();

        moveAndEnd.add("hero1mv");
        moveAndEnd.add("hero1et");
        moveAndEnd.add("hero2mv");
        moveAndEnd.add("hero2et");
        moveAndEnd.add("hero3mv");
        moveAndEnd.add("hero3et");
        return moveAndEnd;
    }

    public static ArrayList<String> fillHpAndEnergy() {

        hpAndEnergy = new ArrayList<String>();

        hpAndEnergy.add("hero1health");
        hpAndEnergy.add("hero1enr");
        hpAndEnergy.add("hero2health");
        hpAndEnergy.add("hero2enr");
        hpAndEnergy.add("hero3health");
        hpAndEnergy.add("hero3enr");
        return hpAndEnergy;
    }

    public static ArrayList<String> fillSkills() {

        skillsList = new ArrayList<String>();

        skillsList.add(imagesPath + "Strike.png");
        skillsList.add(imagesPath + "Home Run.png");
        skillsList.add(imagesPath + "Spinning Pipe.png");
        skillsList.add(imagesPath + "Knockback.png");
        skillsList.add(imagesPath + "Heal.png");
        skillsList.add(imagesPath + "Innoculation.png");
        skillsList.add(imagesPath + "Mustard Gas.png");
        skillsList.add(imagesPath + "Push.png");
        skillsList.add(imagesPath + "Stab.png");
        skillsList.add(imagesPath + "Aimed Shot.png");
        skillsList.add(imagesPath + "Shoot Em All.png");
        skillsList.add(imagesPath + "Cut Throat.png");
        return skillsList;
    }

    public static ArrayList<String> fillPicsPaths() {

        picsPaths = new ArrayList<String>();

        picsPaths.add(imagesPath + "Hero.png");
        picsPaths.add(imagesPath + "Nurse.png");
        picsPaths.add(imagesPath + "Soldier.png");
        return picsPaths;
    }
}
