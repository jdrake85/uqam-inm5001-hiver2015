/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import gameLogic.skills.Skill;

/**
 *
 * @author User
 */
public class Creature {

    private static final int COST_OF_STEP = 2;
    private int level = 8;
    private String name = "noName";
    private int health = 8;
    private int maxHealth = 8;
    private int energy =6;
    private int maxEnergy = 30;
    private int speed = 8;
    private int power = 8;
    private double defenseRating = 1;
    private Skill[] skills;
    private boolean isGood = false;
    private boolean isImpaired = false;
    
    public Creature(String name) {
        this.name = name;
        skills = new Skill[12]; // TODO: eventually set to 4
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public void displayStats() {
        String output = name + ": HEALTH: " + health
                + " / ENERGY: " + energy;
        if (isGood) { 
                output = "#####################################\n" + output;
                output += "\n#####################################";
        }
        System.out.println(output);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean canPayEnergyCostForSkillNumber(int skillNumber) {
        int energyCost = getEneryCostForSkillNumber(skillNumber);
        return canPayEnergyCostOf(energyCost);
    }

    public int getEneryCostForSkillNumber(int skillNumber) {
        Skill chosenSkill = skills[skillNumber - 1];
        return chosenSkill.getEnergyCost();
    }

    public void consumeEnergyForSkillNumber(int skillNumber) {
        consumeEnergy(getEneryCostForSkillNumber(skillNumber));
    }
    
    private void consumeEnergy(int energyConsumed) {
        if (canPayEnergyCostOf(energyConsumed)) {
        energy -= energyConsumed;
    } else {    // Debugging?
        System.out.println("Error: not enough energy to perform action");
        }
    }

    public boolean canPayEnergyCostForSteps(int numberOfSteps) {
        int energyCost = getEnergyCostForSteps(numberOfSteps);
        return canPayEnergyCostOf(energyCost);
    }

    public int getEnergyCostForSteps(int numberOfSteps) {
        return numberOfSteps * COST_OF_STEP;
    }

    public void consumeEnergyForSteps(int numberOfSteps) {
        consumeEnergy(getEnergyCostForSteps(numberOfSteps));
    }

    public boolean canPayEnergyCostOf(int energy) {
        return this.energy >= energy;
    }
    
    public int getEnergy() {
        return energy;
    }

    public int getPower() {
        return power;
    }
    
    public int maximumStepsAbleToWalk() {
        return (int) (energy / COST_OF_STEP);
    }

    public void receiveDamage(int damage) {
        health -= (int) (damage / defenseRating);
        if (health < 0) {
            health = 0;
        } else if (health > maxHealth){
            health = maxHealth;
        }
    }

    public void setIsImpaired(boolean isImpaired) {
        this.isImpaired = isImpaired;
    }

    public void isImpaired(boolean isImpaired) {
        this.isImpaired = isImpaired;
    }
    
    public void becomeImpaired() {
        energy = maxEnergy / 2;
    }

    public void setAlignment(String alignment) {
        if (alignment.equals("bad")) {
            isGood = false;
        } else {
            isGood = true;
        }
    }

    public boolean isAlignedTo(String alignment) {
        boolean matchingAlignment;
        if (alignment.equals("all")) {
            matchingAlignment = true;
        } else {
            matchingAlignment = alignment.equals("good") && isGood
                    || alignment.equals("bad") && !isGood;
        }
        return matchingAlignment;
    }
    
    public void setSkillAsNumber(Skill skill, int skillNumber) {
        if (1 <= skillNumber && skillNumber <= 12) {
            skills[skillNumber - 1] = skill;
        } else {
            System.err.println("Error: skill added with an out-of-bounds number");
        }
    }
    
    public Skill prepareSkill(int skillNumber) { 
        return skills[skillNumber - 1];
    }

    public void setEnergy(int energy) {
       this.energy = energy;
    }
}
