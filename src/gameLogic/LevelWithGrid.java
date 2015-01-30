/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

/**
 *
 * @author User
 */
public class LevelWithGrid {
    int level = 1;
    Creature[] creatures;
    boolean[][] tilesOccupied;
    
    public LevelWithGrid() {
        initializeGrid();
    }

    private void initializeGrid() {
        initializeEmptyGrid();
        populateGridWithCreatures();
    }
    
    private void initializeEmptyGrid() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tilesOccupied[i][j] = false;
            }
        }
    }

    private void populateGridWithCreatures() {
        for (Creature aCreature: creatures) {
            int xCoord = aCreature.getXCoordinate();
            int yCoord = aCreature.getYCoordinate();
            
            assert(!tilesOccupied[xCoord][yCoord]);
            
            tilesOccupied[xCoord][yCoord] = true;
        }
    }
    
    public boolean hasOccupiedTile(int xCoord, int yCoord) {
        return tilesOccupied[xCoord][yCoord];
    }
}
