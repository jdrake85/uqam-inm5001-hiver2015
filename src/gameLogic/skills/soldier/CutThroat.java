/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills.soldier;

import gameLogic.Creature;
import gameLogic.skills.MeleeSkill;
import java.util.Random;

/**
 *
 * @author User
 */
public class CutThroat extends MeleeSkill{
    
    public CutThroat(int energyCost, int power) {
        super("Cut Throat", energyCost, power, false, true);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        Random generator = new Random();
        int numberRange = 10;
        int numberLimit = (int) (numberRange * 0.4);
        if (generator.nextInt(numberRange) > numberLimit) {
            creature.becomeImpaired();
        }
        return damageDealt;
    }

}
