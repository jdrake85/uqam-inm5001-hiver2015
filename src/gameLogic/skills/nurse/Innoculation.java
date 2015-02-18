/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills.nurse;

import gameLogic.Creature;
import gameLogic.skills.RangedSkill;

/**
 *
 * @author User
 */
public class Innoculation extends RangedSkill{
    
    public Innoculation(int energyCost, int power) {
        super("Innoculation", energyCost, power, 4);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        return damageDealt;
    }

}