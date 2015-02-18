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
public class HomeRun extends MeleeSkill{
    
    public HomeRun(int energyCost, int power) {
        super("Home Run", energyCost, power, false, true);
    }

    @Override
    public void performOn(Creature creature) {
        creature.receiveDamage(power);
        creature.becomeImpaired();
    }

}
