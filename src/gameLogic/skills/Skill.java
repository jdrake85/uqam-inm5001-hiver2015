/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills;

import gameLogic.Creature;
import gameLogic.pathfinding.Coordinates;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public abstract class Skill {

    private String name;
    protected boolean hasKnockback = false;
    protected boolean hasImpair = false;
    protected int energyCost;
    protected int power;
    protected Coordinates originatingCoords = null;
    protected Coordinates targetCoords = null;

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

    public abstract int performOn(Creature creature);

    public boolean performableWithEnergyPointsAt(int energyPoints, Coordinates targetCoords) {
        return (energyPoints >= energyCost) && usableRangeIncludesCoordinates(targetCoords);
    }

    public abstract boolean usableRangeIncludesCoordinates(Coordinates targetCoords);

    public int getEnergyCost() {
        return energyCost;
    }

    public boolean hasKnockback() {
        return hasKnockback;
    }

    public int getDamage() {
        return power;
    }

    public void setOriginatingFrom(Coordinates coordinates) {
        originatingCoords = coordinates;
    }

    public Coordinates getOriginatingFrom() {
        return originatingCoords;
    }

    public void setTargetCoordinates(Coordinates coords) {
        targetCoords = coords;
    }

    public Coordinates getTargetCoordinates() {
        return targetCoords;
    }

    public List<Coordinates> generateAffectedCoordinatesFrom(Coordinates targetCoords) {
        ArrayList<Coordinates> coordsList = new ArrayList<Coordinates>();
        coordsList.add(targetCoords);
        return coordsList;
    }
    
}
