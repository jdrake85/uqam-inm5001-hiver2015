/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills.hero;

import gameLogic.Creature;
import gameLogic.pathfinding.Coordinates;
import gameLogic.skills.MeleeSkill;
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
        setAnimationType("Skill1");
        String description =
                "A four-way swinging attack.";
        setDescription(description);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        return damageDealt;
    }

    @Override
    public List<Coordinates> generateAffectedCoordinatesFrom(Coordinates targetCoords) {
        ArrayList<Coordinates> coordsList = new ArrayList<Coordinates>();
        coordsList.addAll(Arrays.asList(originatingCoords.getFourSurroundingCardinalCoordinates()));
        return coordsList;
    }
}