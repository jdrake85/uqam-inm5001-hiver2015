/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills;

import gameLogic.Creature;

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
