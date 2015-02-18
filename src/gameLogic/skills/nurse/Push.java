/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills.nurse;

import gameLogic.Creature;
import gameLogic.skills.MeleeSkill;

/**
 *
 * @author User
 */
public class Push extends MeleeSkill{
    
    public Push(int energyCost, int power) {
        super("Push", energyCost, power, true, false);
    }

    @Override
    public void performOn(Creature creature) {
        creature.receiveDamage(power);
    }

}
