/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

/**
 *
 * @author User
 */
public class Tile {
    private final int xCoord;
    private final int yCoord;
    private Creature occupier = null;
    
    public Tile(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }
    
    public Tile(int xCoord, int yCoord, Creature occupier) {
        this(xCoord, yCoord);
        this.occupier = occupier;
    }
    
    public boolean isOccupied() {
        return occupier != null;
    }
    
    public boolean isOccupiedBy(Creature creature) {
        return occupier != null && occupier.equals(creature);
    }
    
    public Creature getOccupier() {
        return occupier;
    }
    
    public void addOccupier(Creature creature) {
        assert(!isOccupied());
        occupier = creature;
    }
    
    public void removeOccupier() {
        occupier = null;
    }
    
    public int getXCoord() {
        return xCoord;
    }
    
    public int getYCoord() {
        return yCoord;
    }
    
    public String getCondensedCoordinates() {
        return "(" + getXCoord() + ", " + getYCoord() + ")";
    }
}
