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
public abstract class RangedSkill extends Skill{
    
   protected int range;
    
   public RangedSkill(String name, int energyCost, int power, int range) {
        super(name, energyCost, power);
        this.range = range;
    }
   
   public int getRange() {
       return range;
   }
    
    public boolean usableRangeIncludesCoordinates(Coordinates coords) {
        return !originatingCoords.equals(coords) && 
                originatingCoords.sumOfXYComponentDistancesTo(coords) <= range;
    }
}