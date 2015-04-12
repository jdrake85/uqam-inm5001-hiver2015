/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills.soldier;

import gameLogic.Creature;
import gameLogic.skills.EverywhereSkill;
import java.util.Random;

/**
 *
 * @author User
 */
public class ShootEmAll extends EverywhereSkill {

    public ShootEmAll(int energyCost, int power) {
        super("Shoot Em All", energyCost, power, "bad");
        setAnimationType("Skill1");
        String description =
                "A desperate attempt to shoot all enemies!  The further they are, the more likely it is to miss the shot.";
        setDescription(description);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = 0;
        if (creature.isAlignedTo("bad")) {
            Random generator = new Random();
            int range = 10;
            if (generator.nextInt(range) > range / 2) {
                damageDealt = power;
                creature.receiveDamage(damageDealt);
            }
        }
        return damageDealt;
    }
}
