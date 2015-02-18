/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills;

import gameLogic.Creature;
import gameLogic.pathfinding.Coordinates;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author User
 */
public class SpinningPipe extends MeleeSkill {
    public SpinningPipe(int energyCost, int power) {
        super("Spinning Pipe", energyCost, power);
    }

    @Override
    public void performOn(Creature creature) {
        creature.receiveDamage(power);
    }

    @Override
    public List<Coordinates> generateAffectedCoordinatesFrom(Coordinates targetCoords) {
        ArrayList<Coordinates> coordsList = new ArrayList<Coordinates>();
        coordsList.addAll(Arrays.asList(originatingCoords.getFourSurroundingCardinalCoordinates()));
        return coordsList;
    }
}