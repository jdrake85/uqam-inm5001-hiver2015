/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills;

import gameLogic.Coordinates;

/**
 *
 * @author User
 */
public class Strike extends MeleeSkill{
    
    public Strike(int energyCost, int power) {
        super("Strike", energyCost, power);
    }

    @Override
    public void performWithEnergyPointsAt(int energyPoints, Coordinates targetCoords) {
        System.out.println("PERFORMING STRIKE AT " + targetCoords);
    }


    
}
