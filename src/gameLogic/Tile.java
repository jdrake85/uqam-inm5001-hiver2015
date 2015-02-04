/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;


public class Tile {
    private Creature occupier = null;
    
    public Tile() {
        occupier = null;
    }
    
    public Tile(Creature occupier) {
        this.occupier = occupier;
    }
    
    public boolean isOccupied() {
        return occupier != null;
    }
    
    public boolean isOccupiedBy(Creature creature) {
        return isOccupied() && occupier.equals(creature);
    }
    
    public Creature getOccupier() {
        return occupier;
    }
    
    public void addOccupier(Creature creature) {
        if (!isOccupied()) {
            occupier = creature;
        } else {
            System.out.println("Error: attempt to stack creatures on same tile");
        }
    }
    
    public void removeOccupier() {
        occupier = null;
    }
}
