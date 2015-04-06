/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills.soldier;

import gameLogic.Creature;
import gameLogic.skills.RangedSkill;
import java.util.Random;

/**
 *
 * @author User
 */
public class AimedShot extends RangedSkill{
    
    public AimedShot(int energyCost, int power) {
        super("Aimed Shot", energyCost, power, 6);
        setAnimationType("Skill1");
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = 0;
        Random generator = new Random();
        int stepCount = originatingCoords.sumOfXYComponentDistancesTo(targetCoords);
        int nextNumber = generator.nextInt(range) + 2;
        if (nextNumber >= stepCount) {
            damageDealt = power;
            creature.receiveDamage(damageDealt);
        }
        return damageDealt;
    }
}
