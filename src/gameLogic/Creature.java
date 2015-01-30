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
public class Creature extends Entity {
    int level = 8;
    
    int health = 8;
    int maxHealth = 8;
    
    int energy = 8;
    int maxEnergy = 8;
    
    int speed = 8;
    
    double attackRating = 8;
    double defenseRating = 8;
    
    Skill[] skills;
    
    boolean isImpaired = false;
    
    public Creature(int xCoord, int yCoord) {
        super(xCoord, yCoord);
        skills = new Skill[4];
    }
    
    public boolean isAlive() {
        return health > 0;
    }
    
    public boolean isAt(int xCoord, int yCoord) {
        return this.xCoord == xCoord && this.yCoord == yCoord;
    }
    
    public int getXCoordinate() {
        return xCoord;
    }
    
    public int getYCoordinate() {
        return yCoord;
    }
    
    public void useSkillAt(int skillNumber, int xCoord, int yCoord) {
        Skill chosenSkill = skills[skillNumber];
        
        if (chosenSkill.performableWithEnergy(energy) 
                && chosenSkill.performableAtFrom
                    (this.getXCoordinate(), this.getYCoordinate(), xCoord, yCoord)) {
            // TODO
           consumeEnergy(chosenSkill.getEnergyCost());
        } else {
            
        }
        
    }
    
    private void consumeEnergy(int energyConsumed) {
        assert(energyConsumed <= energy);
        energy -= energyConsumed;
    }
    
    public void receiveDamage(int damage) {
        health -= (int) (damage / defenseRating);
    }
    
    public void setImpaired(boolean isImpaired) {
        this.isImpaired = isImpaired;
    }
    
}
