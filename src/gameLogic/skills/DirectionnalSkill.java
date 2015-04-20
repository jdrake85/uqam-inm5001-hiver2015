package gameLogic.skills;

import gameLogic.pathfinding.Coordinates;

public abstract class DirectionnalSkill extends Skill{
    
    protected int range;
    
    public DirectionnalSkill(String name, int energyCost, int power, int range) {
        super(name, energyCost, power);
        this.range = range;
    }
    
    public int getRange() {
        return range;
    }
    
    @Override
    public boolean usableRangeIncludesCoordinates(Coordinates coords) {
        return !originatingCoords.equals(coords)
                && !originatingCoords.isDiagonalTo(coords)
                && originatingCoords.hasXOrYComponentWithinNUnitsOf(range, coords);
    }
}

  
