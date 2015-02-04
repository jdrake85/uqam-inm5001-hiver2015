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

    static final int COST_OF_STEP = 2;
    int level = 8;
    String name = "noName";
    int health = 8;
    int maxHealth = 8;
    int energy = 30;
    int maxEnergy = 30;
    int speed = 8;
    int attack = 8;
    double defenseRating = 8;
    Skill[] skills;
    boolean isGood = true;
    boolean isImpaired = false;

    public Creature(String name) {
        this.name = name;
        skills = new Skill[4];
    }
    
    @Override
    public String toString() {
        return name;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean canPayEnergyCostForSkillNumber(int skillNumber) {
        int energyCost = getEneryCostForSkillNumber(skillNumber);
        return canPayEnergyCostOf(energyCost);
    }

    public int getEneryCostForSkillNumber(int skillNumber) {
        Skill chosenSkill = skills[skillNumber];
        return chosenSkill.getEnergyCost();
    }

    public void consumeEnergyForSkillNumber(int skillNumber) {
        consumeEnergy(getEneryCostForSkillNumber(skillNumber));
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

    private void consumeEnergy(int energyConsumed) {
        if (canPayEnergyCostOf(energyConsumed)) {
        energy -= energyConsumed;
    } else {
        System.out.println("Error: not enough energy to perform action");
        }
    }

    public boolean canPayEnergyCostOf(int energy) {
        return this.energy >= energy;
    }
    
    public int getEnergy() {
        return energy;
    }

    public int dealDamage() {
        return attack;
    }

    public void receiveDamage(int damage) {
        health -= (int) (damage / defenseRating);
    }

    public void setIsImpaired(boolean isImpaired) {
        this.isImpaired = isImpaired;
    }

    public boolean isImpaired() {
        return this.isImpaired;
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
}
