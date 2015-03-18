/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills;

import gameLogic.pathfinding.Coordinates;

/**
 *
 * @author User
 */
public abstract class MeleeSkill extends Skill{
    
    public MeleeSkill(String name, int energyCost, int power) {
        super(name, energyCost, power);
    }

    public MeleeSkill(String name, int energyCost, int power, boolean hasKnockback, boolean hasImpair) {
        super(name, energyCost, power, hasKnockback, hasImpair);
    }
    
    public boolean usableRangeIncludesCoordinates(Coordinates coords) {
        return originatingCoords.areCardinalCoordinatesAdjacentTo(targetCoords);
    }
}
