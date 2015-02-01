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
    
    boolean isGood = true;
    boolean isImpaired = false;
    
    public Creature() {
        super();
    }
    
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
    
    public void setIsImpaired(boolean isImpaired) {
        this.isImpaired = isImpaired;
    }
    
    public void isImpaired(boolean isImpaired) {
        this.isImpaired = isImpaired;
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
    
    public void moveOnceInDirection(String direction) {
        if (direction.equals("up") && this.getXCoordinate() < 7) {
            
        }
    }
    
    public void displayPosition() {
        System.out.println("Position of creature <" + this + ">: ("
                + this.getXCoordinate()+ ", " + this.getYCoordinate() + ")");
    }
}
