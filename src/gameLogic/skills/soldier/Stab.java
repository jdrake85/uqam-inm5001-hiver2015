/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills.soldier;

import gameLogic.Creature;
import gameLogic.skills.MeleeSkill;

/**
 *
 * @author User
 */
public class Stab extends MeleeSkill{
    
    public Stab(int energyCost, int power) {
        super("Stab", energyCost, power);
    }

    @Override
    public void performOn(Creature creature) {
        creature.receiveDamage(power);
    }

}
