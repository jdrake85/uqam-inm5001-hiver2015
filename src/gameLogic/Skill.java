/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

/**
 *
 * @author User
 */
public class Skill {
    int level = 8;
    boolean hasKnockback = false;
    boolean hasImpair = false;
    String name;
    int energyCost = 0;
    int damage = 1;
    
    public Skill() {
        // Dummy skill
        this("attack", 1, 5, 1);
    }
    
    public Skill(String name, int level, int energyCost, int damage) {
        this.name = name;
        this.level = level;
        this.energyCost = energyCost;
        this.damage = damage;
    }
    
    @Override
    public String toString() { 
        return name;
    }
    
    public boolean performableWithEnergy(int energy) {
        return energy >= energyCost;
    }
    
    public boolean performableAt(int xCoord, int yCoord) {
        return true; // TODO
    }
    
    public boolean getHasKnockback() {
        return hasKnockback; 
    }
    
    public void setHasKnockback(boolean hasKnockback) {
        this.hasKnockback = hasKnockback; 
    }
    
    public boolean getHasImpair() {
        return hasImpair; 
    }
    
    public void setHasImpair(boolean hasImpair) {
        this.hasImpair = hasImpair; 
    }
    
    public int getEnergyCost() {
        return energyCost;
    }
    
    public int getDamage() {
        return damage;
    }
}
