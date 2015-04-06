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
        setAnimationType("Skill1");
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        creature.becomeImpaired();
        return damageDealt;
    }

}
