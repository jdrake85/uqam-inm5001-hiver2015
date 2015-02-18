/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills.hero;

import gameLogic.Creature;
import gameLogic.skills.MeleeSkill;

/**
 *
 * @author User
 */
public class Knockback extends MeleeSkill{
    
    public Knockback(int energyCost, int power) {
        super("Knockback", energyCost, power, true, true);
    }

    @Override
    public void performOn(Creature creature) {
        creature.receiveDamage(power);
        creature.becomeImpaired();
    }

}
