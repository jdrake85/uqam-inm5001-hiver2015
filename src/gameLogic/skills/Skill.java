/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills;

import gameLogic.Coordinates;

/**
 *
 * @author User
 */
public abstract class Skill {

    private String name;
    private boolean hasKnockback = false;
    private boolean hasImpair = false;
    private int energyCost;
    private int power;
    protected Coordinates originatingCoords = null;

    public Skill(String name, int energyCost, int power) {
        this.name = name;
        this.energyCost = energyCost;
        this.power = power;
    }

    public Skill(String name, int energyCost, int power, boolean hasKnockback, boolean hasImpair) {
        this(name, energyCost, power);
        this.hasKnockback = hasKnockback;
        this.hasImpair = hasImpair;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public abstract void performWithEnergyPointsAt(int energyPoints, Coordinates targetCoords);
    
    public boolean performableWithEnergyPointsAt(int energyPoints, Coordinates targetCoords) {
        return (energyPoints >= energyCost) && usableAt(targetCoords);
    }
    
    public abstract boolean usableAt(Coordinates targetCoords);

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
        return power;
    }

    public String getName() {
        return name;
    }
    
    public void setOriginatingFrom(Coordinates coordinates) {
        originatingCoords = coordinates;
    }
    
    public Coordinates getOriginatingFrom() {
        return originatingCoords;
    }
}
