/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills;

/**
 *
 * @author User
 */
public abstract class Skill {

    String name;
    boolean hasKnockback = false;
    boolean hasImpair = false;
    int energyCost;
    int power;

    public Skill(String name, int energyCost, int power) {
        this.name = name;
        this.energyCost = energyCost;
        this.power = power;
    }

    public Skill(String name, boolean hasKnockback, boolean hasImpair, int energyCost, int power) {
        this(name, energyCost, power);
        this.hasKnockback = hasKnockback;
        this.hasImpair = hasImpair;
    }

    @Override
    public String toString() {
        return name;
    }

    // The child skill must describe the action performed
    public abstract void performAtFrom(int targetXCoord, int targetYCoord, int sourceXCoord, int sourceYCoord);

    // The child skill must determine if its action can be performed
    public abstract boolean performableAtFrom(int targetXCoord, int targetYCoord, int sourceXCoord, int sourceYCoord);

    public boolean performableWithEnergy(int energy) {
        return energy >= energyCost;
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
        return power;
    }

    private String getName() {
        return name;
    }
}
