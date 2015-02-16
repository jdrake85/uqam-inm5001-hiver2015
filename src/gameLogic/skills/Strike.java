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
    
    public Strike(String name, int energyCost, int power) {
        super(name, energyCost, power);
    }

    @Override
    public void performWithEnergyPointsAt(int energyPoints, Coordinates targetCoords) {
        // TODO
    }


    
}
