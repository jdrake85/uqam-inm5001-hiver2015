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
public class Heal extends MeleeSkill{
    
    public Heal(int energyCost, int power) {
        super("Heal", energyCost, power * -1);
        setAnimationType("Skill1");
    }

    @Override
    public int performOn(Creature creature) {
        int healingDealt = power;
        creature.receiveDamage(healingDealt);
        return healingDealt;
    }
}
