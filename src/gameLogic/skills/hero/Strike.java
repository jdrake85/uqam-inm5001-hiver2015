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
public class Strike extends MeleeSkill{
    
    public Strike(int energyCost, int power) {
        super("Strike", energyCost, power);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        return damageDealt;
    }

}
