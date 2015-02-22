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
public abstract class EverywhereSkill extends Skill{
    String targetAlignment = "bad";
    
    public EverywhereSkill(String name, int energyCost, int power, String targetAlignment) {
        super(name, energyCost, power);
        this.targetAlignment = targetAlignment;
    }
    
    public boolean usableRangeIncludesCoordinates(Coordinates targetCoords) {
        return !originatingCoords.equals(targetCoords);
    }
    
    @Override
    public List<Coordinates> generateAffectedCoordinatesFrom(Coordinates coords) {
        ArrayList<Coordinates> coordsList = new ArrayList<Coordinates>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                coordsList.add(new Coordinates(i,j));
            }
        }
        return coordsList;
    }
}
