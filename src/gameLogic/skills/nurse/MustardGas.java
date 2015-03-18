/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills.nurse;

import gameLogic.Creature;
import gameLogic.pathfinding.CoordPath;
import gameLogic.pathfinding.Coordinates;
import gameLogic.skills.DirectionnalSkill;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class MustardGas extends DirectionnalSkill {

    public MustardGas(int energyCost, int power) {
        super("Mustard Gas", energyCost, power, 4);
    }

    @Override
    public List<Coordinates> generateAffectedCoordinatesFrom(Coordinates targetCoords) {
        Coordinates firstCoords = originatingCoords.getAdjacentCoordinatesNearestTo(targetCoords);
        ArrayList<Coordinates> coordsList = new ArrayList<Coordinates>();
        CoordPath directionnalPath = CoordPath.generateStraightPathOfLengthNFromAdjacentCoordinates(originatingCoords, firstCoords, range + 1);
        Coordinates previousCoords = directionnalPath.popFirstCoordinates(); // Originating coordinates not affected by skill
        int orthogXStep = originatingCoords.getYCoord() - firstCoords.getYCoord();
        int orthogYStep = originatingCoords.getXCoord() - firstCoords.getXCoord();
        for (int i = 0; i < range; i++) { 
            Coordinates rootCoords = directionnalPath.popFirstCoordinates();
            int rootXCoord = rootCoords.getXCoord();
            int rootYCoord = rootCoords.getYCoord();
            for (int j = - i; j <= i; j++) {
                Coordinates orthogCoords = new Coordinates(rootXCoord + (j * orthogXStep), rootYCoord + (j * orthogYStep));
                coordsList.add(orthogCoords);
            }
            previousCoords = rootCoords;
        }
        return coordsList;
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        return damageDealt;
    }
}