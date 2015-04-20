package gameLogic.skills;

import gameLogic.pathfinding.Coordinates;

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