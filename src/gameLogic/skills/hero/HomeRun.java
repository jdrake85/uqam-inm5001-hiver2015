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
public class HomeRun extends MeleeSkill{
    
    public HomeRun(int energyCost, int power) {
        super("Home Run", energyCost, power, false, true);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        creature.becomeImpaired();
        return damageDealt;
    }

}
