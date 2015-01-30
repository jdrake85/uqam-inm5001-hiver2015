/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.skills;

/**
 *
 * @author User
 */
public class Strike extends Skill{

    @Override
    public void performAtFrom(int targetXCoord, int targetYCoord, int sourceXCoord, int sourceYCoord) {
        assert(performableAtFrom(targetXCoord, targetYCoord, sourceXCoord, sourceYCoord));
        
        
    }

    @Override
    public boolean performableAtFrom(int targetXCoord, int targetYCoord, int sourceXCoord, int sourceYCoord) {
        return targetXCoord - sourceXCoord == 1 && targetYCoord - sourceYCoord == 1;
    }
    
}
