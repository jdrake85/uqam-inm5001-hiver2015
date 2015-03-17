/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

/**
 *
 * @author User
 */
public class CreatureSpeedTurnTriplet implements Comparable {
    Creature creature;
    int speed;
    int turnCount;
    
    
    public CreatureSpeedTurnTriplet(Creature creature) {
        this.creature = creature;
        speed = creature.getCumulativeTurnSpeed();
        turnCount = creature.getTurnsAssigned();
    }
     
    public Creature getCreature() {
        return creature;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public int getTurnCount() {
        return turnCount;
    }

    public int compareTo(Object o) {
        int comparison = 0;
        if (o == null) {
            throw new NullPointerException();
        } else {
            CreatureSpeedTurnTriplet otherTriplet = (CreatureSpeedTurnTriplet) o;
            Creature otherCreature = otherTriplet.getCreature();
            int otherSpeed = otherTriplet.getSpeed();
            int otherTurnCount = otherTriplet.getTurnCount();
            if (speed < otherSpeed
                    || (speed  == otherSpeed && turnCount < otherTurnCount)) {
                comparison = -1;
            } else if (speed  > otherSpeed
                    || (speed  == otherSpeed && turnCount > otherTurnCount)) {
                comparison = 1;
            } else {
                String name = creature.toString();
                comparison = name.compareTo(otherCreature.toString());
            }
            // TODO: remove this, used for debugging
            String compareSign = " = ";
            if (comparison > 0) {
                compareSign = " > ";
            } else if (comparison < 0) { 
                compareSign = " < ";
            }
            //System.out.println("                            " + this + compareSign + otherTriplet);
        }
        return comparison;
    }
    
    // Note: non standard equals, because its implementation will be used only to quickly remove triplits from a PriorityQueue
    @Override
    public boolean equals(Object o) {
        boolean equality = false;
        if (o == null) {
            throw new NullPointerException();
        } else {
            CreatureSpeedTurnTriplet otherTriplet = (CreatureSpeedTurnTriplet) o;
            Creature otherCreature = otherTriplet.getCreature();
            equality = creature.equals(otherCreature);
        }
        return equality;
    }
    
    @Override
    public String toString() {
        return creature.toString() + "(" + speed + ")";
    }
    
    
}
