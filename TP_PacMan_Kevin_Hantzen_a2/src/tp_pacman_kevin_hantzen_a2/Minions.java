/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tp_pacman_kevin_hantzen_a2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Kevin
 */
public class Minions {
    private Case            currentPosition;
    private Case            starter;
    private PacMan          target;
    private int             number;
    private boolean         isStunt   = false;
    private static boolean  isAffraid = false;
    private static int      limit     = 3;
    private Minions         brother   = null;
    
    
    public Minions(Case starter, PacMan target, int number) 
    {
        this.currentPosition = starter;
        this.starter         = starter;
        this.target          = target;
        this.number          = number;
    }
    /*
        operation on minions
    */
    public void operation(Map map) 
    {
        System.out.println("allo?");
        Case blinky = this.getMinionsPosition(0);
        this.getNextPosition(map, blinky);
        this.checkPacman();
        
    }
    
    /*
        display minions
    */
    public void display(Graphics g, GridBagConstraints gbCoord) 
    {
        switch (this.number) {
            case 0:
                g.setColor(Color.RED);
                break;
            case 1:
                g.setColor(Color.PINK);
                break;
            case 2:
                g.setColor(Color.CYAN);
                break;
            case 3:
                g.setColor(Color.ORANGE);
        }

        g.fillRect( this.currentPosition.getXPixel(),
                    this.currentPosition.getYPixel(),
                    this.currentPosition.getWidth(),
                    this.currentPosition.getHeight());
        
        if (this.brother != null) {
            this.brother.display(g, gbCoord);
        }
    }
    
    /*
    * return start
    */
    public void returnStart() 
    {
        this.currentPosition = this.starter;
        
        if (this.brother != null) {
            this.brother.returnStart();
        }
    }
    
    private void checkPacman() 
    { 
        if (this.currentPosition == this.target.getCurrentPosition()) {
            this.target.setLife(this.target.getLife() - 1);
        }
        
        if (this.brother != null) {
            this.brother.checkPacman();
        }
    }
    
    public Case getCurrentPosition() 
    {
        return this.currentPosition;
    }
    
    public Case getMinionsPosition(int i) 
    {
        if (this.number != i && this.brother != null) {
            return this.brother.getMinionsPosition(i);
        } else {
            return this.getCurrentPosition();
        }
    }
    /*
    * get next position
    */
    private void getNextPosition(Map map, Case blinky) 
    {
        ArrayList<Case> availableCase;
        availableCase       = this.currentPosition.getAvailableCase();
        Case    newPosition = this.target.getCurrentPosition();
        System.out.println("test");
        //Blinky the chaser follow pacman everywhere: default configuration
        switch (this.number) { 
         case 1: //Pinky the son of a bitch, target 4 case in front of pacman
                newPosition = Case.getGhostCase(this.target,  map, this.currentPosition, 4); 
            break;
         case 2: //Inky the fickle, follow blinky symetric by two case in front of pacman
                Case center = Case.getGhostCase(this.target, map, this.currentPosition, 2);
                
                int blinkyX = blinky.getX();
                int blinkyY = blinky.getY();
                
                int centerX = center.getX();
                int centerY = center.getY();
                
                int inkyX = 2 * centerX - blinkyX;
                int inkyY = 2 * centerY - blinkyY;
                
                try {
                        if (map.getGameGrid()[inkyY][inkyX].getStatus() != blinky.IS_WALL) {
                            newPosition = map.getGameGrid()[inkyY][inkyX];
                        } else {
                            if (!map.getGameGrid()[inkyY][inkyX].getAvailableCase().isEmpty()) {
                                newPosition = map.getGameGrid()[inkyY][inkyX].getAvailableCase().get(0);
                            } else {
                                newPosition = this.target.getCurrentPosition();
                            }
                        }
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    newPosition = this.target.getCurrentPosition();
                }         
            break;
            case 3: //Clyde the idiot
                int clydeX = this.currentPosition.getX();
                int clydeY = this.currentPosition.getY();
                
                int pacX   = this.target.getCurrentPosition().getX();
                int pacY   = this.target.getCurrentPosition().getY();
                
                int squareDistance = (pacX - clydeX)^2 + (pacY - clydeY)^2;
                
                //Si en dessous de 8 case, suivre pacman, sinon random
                if (squareDistance <= 64) {
                    newPosition = this.target.getCurrentPosition();
                } else {
                        availableCase  = this.currentPosition.getAvailableCase();
                        Random r = new Random();
                        int i = r.nextInt(availableCase.size());        
                        this.currentPosition = availableCase.get(i);
                }       
            break;
        }
        
        newPosition.setIsStart(true);
        newPosition.setDistance(map);
        map.getGameGrid()[newPosition.getY()][newPosition.getX()].setDistance(0);
        //check case

     /*   for (int i = 0; i < map.getGameGrid().length ; i++) {
                for (int j=0; j<map.getGameGrid()[0].length; j++) {
                    if (map.getGameGrid()[i][j].getStatus() == 0) {
                        System.out.print(" X|");
                    } else if (map.getGameGrid()[i][j].getDistance() < 10) {
                        System.out.print(" " + map.getGameGrid()[i][j].getDistance() + "|");
                    } else {
                        System.out.print("" + map.getGameGrid()[i][j].getDistance() + "|");
                    }
                } System.out.println(""); 
            }
       */ 
        int iMin = 0;                
        for (int i = 1; i<availableCase.size(); i++) {
            if (availableCase.get(iMin).getDistance() > availableCase.get(i).getDistance()) {
                iMin = i;
            }
        }
        this.currentPosition = availableCase.get(iMin); 
        this.target.getCurrentPosition().setIsStart(true);
        map.resetDistance();
         
        if (this.brother != null) {
            this.brother.getNextPosition(map, blinky);
        }
    }


    /*
    * getBrother
    */
    public Minions getBrother() 
    {
        return this.brother;
    }
    /*
        add minions in game
    */
    public void setBrother(Case starter) 
    {
        if (this.brother == null) {
            if (this.number < this.limit) {
                this.brother = new Minions(starter, this.target, this.number + 1); 
            }
        } else {
            this.brother.setBrother(starter);
        }
    }
    
    /*
    * change minion state.
    */
    public void setStunt() 
    {
        this.isStunt = !this.isStunt;
    }
    
    /*
    * this.isAffraid
    */
    public void setIsAffraid() 
    {
        this.isAffraid = !this.isAffraid;
    }
}
