/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tp_pacman_kevin_hantzen_a2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;

/**
 *
 * @author Kevin
 */
public class Map implements Constant {
    private Case[][] gameGrid;
    
    public Map(int x, int y) 
    {
        gameGrid = new Case[x][y];
    }
    
    public Case[][] getGameGrid() {
        return this.gameGrid;
    }
    
    /*
    * reset distance
    */
    public void resetDistance() 
    {
        for (int i = 0; i < this.gameGrid.length; i++) {
            for (int j = 0; j < this.gameGrid.length; j++) {
                this.gameGrid[i][j].setDistance(99);
            }
        }
    }
    
    public void display(Graphics g, GridBagConstraints gbCoord) {

        for (int i = 0; i < this.gameGrid.length; i++) {
            for (int j = 0; j < this.gameGrid[0].length; j++) { 
                
                gbCoord.gridx      = j;
                gbCoord.gridy      = i;
                gbCoord.gridheight = 1;
                
                if (j == this.gameGrid[0].length - 1) {
                    gbCoord.gridwidth = GridBagConstraints.REMAINDER;
                } else {
                    gbCoord.gridwidth = 1;
                }      
                
                switch(this.gameGrid[i][j].getStatus()) {
                    case 0 :
                        g.setColor(Color.WHITE);
                        g.fillRect(this.gameGrid[i][j].getXPixel(), 
                                   this.gameGrid[i][j].getYPixel(), 
                                   this.gameGrid[i][j].getWidth(), 
                                   this.gameGrid[i][j].getHeight());
                    break;
                    case 1 :
                        g.setColor(Color.YELLOW);
                        g.fillOval(this.gameGrid[i][j].getXPixel() + this.gameGrid[i][j].getWidth()/3, 
                                   this.gameGrid[i][j].getYPixel() + this.gameGrid[i][j].getHeight()/3,
                                   this.gameGrid[i][j].getWidth()/3, 
                                   this.gameGrid[i][j].getHeight()/3);
                    break;
                }                
            }
        }      
    }
    

}
