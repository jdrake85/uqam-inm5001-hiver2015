/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

/**
 *
 * @author User
 */
public class Entity {
    int xCoord;
    int yCoord;
    
    public Entity() {
        // Default off-map coordinates
        xCoord = -1;
        yCoord = -1;
    }
    
    public Entity(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }
}
