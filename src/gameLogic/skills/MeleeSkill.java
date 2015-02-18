/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills;

import gameLogic.pathfinding.Coordinates;
import java.util.ArrayList;
import java.util.List;

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
    
    public boolean usableRangeIncludesCoordinates(Coordinates targetCoords) {
        return originatingCoords.areCardinalCoordinatesAdjacentTo(targetCoords);
    }
    
    public List<Coordinates> generateAffectedCoordinatesFrom(Coordinates targetCoords) {
        ArrayList<Coordinates> coordsList = new ArrayList<Coordinates>();
        coordsList.add(targetCoords);
        return coordsList;
    }
}
