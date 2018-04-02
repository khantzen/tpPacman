/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tp_pacman_kevin_hantzen_a2;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import javax.swing.ImageIcon;
import tp_pacman_kevin_hantzen_a2.Case;
import tp_pacman_kevin_hantzen_a2.Direction;
import static tp_pacman_kevin_hantzen_a2.Direction.TO_THE_BOT;
import static tp_pacman_kevin_hantzen_a2.Direction.TO_THE_LEFT;
import static tp_pacman_kevin_hantzen_a2.Direction.TO_THE_RIGHT;
import static tp_pacman_kevin_hantzen_a2.Direction.TO_THE_TOP;
import tp_pacman_kevin_hantzen_a2.Map;

/**
 *
 * @author Kevin
 */
public class PacMan {
    
    private Case      currentPosition;
    private Case      starter;
    private boolean   openMouth = true;
    private Direction direction;
    private Direction askedWay;
    private int       score     = 0;
    private int       life      = 3;
   
    
    public PacMan(int x, int y, Map map) 
    {
        this.currentPosition = map.getGameGrid()[y][x];
        this.starter         = map.getGameGrid()[y][x];
        this.direction       = direction.TO_THE_RIGHT;
    }
    
    /*
    * PacMan's operation
    */
    public void operation(Map map) 
    {
        turn(map);
        if (canAdvance(this.direction, map)) {
            if (noMobHere()) {
                this.life--;
            }
            advance(map);
        }
    }
   
    
    /*
    * Display PacMan
    */
    public void display (Graphics g, GridBagConstraints gbCoord) 
    {   
        String path = "pacman/";
        
        switch (this.direction) {
            case TO_THE_RIGHT:
                path = path + "right/";
                break;
            case TO_THE_LEFT:
                path = path + "left/";
                break;
            case TO_THE_TOP:
                path = path + "top/";
                break;
            default:
                path = path + "bot/";
                break;
        }
        
        if (this.openMouth) {
             path = path + "2.png";
             
        } else {
             path = path + "1.png";
        }
        
        this.openMouth = !this.openMouth;
        
        ImageIcon pacMan = new ImageIcon(path);
        Image     pacManToDraw = pacMan.getImage();
        g.drawImage(pacManToDraw,
                    currentPosition.getXPixel(),
                    currentPosition.getYPixel(),
                    currentPosition.getWidth(),
                    currentPosition.getHeight(),
                    null
        );
    }
    /*
    * Get direction
    */
    public Direction getDirection() 
    {
        return this.direction;
    }
    /*
    * return start
    */
    public void returnStart() 
    {
        this.currentPosition = this.starter;
    }
    /*
    * Where should I go now...
    */
    public void setAskedWay(Direction ask) 
    {
            this.askedWay = ask;
    }
    /*
    * Get Next Case
    */
    private Case getNextCase(Direction way)
    {
        Case curPos = this.currentPosition;
        
        switch (way) {
            case TO_THE_TOP:
                return new Case(curPos.getX(), curPos.getY() - 1);
            case TO_THE_RIGHT:
                return new Case(curPos.getX() + 1, curPos.getY());
            case TO_THE_BOT:
                return new Case(curPos.getX(), curPos.getY() +1);
            case TO_THE_LEFT:
                return new Case(curPos.getX() - 1, curPos.getY());
        }
        return curPos;
    }
    
    public int getScore() 
    {
        return this.score;
    }
    
    public Case getCurrentPosition() 
    {
        return this.currentPosition;
    }
 
    public int getLife() 
    {
        return this.life;
    }
    
    public void setLife(int life)
    {
        this.life = life;
    }
    /*
    * HE IS ALIVE !!!!!
    */
    private void advance(Map map) 
    {
        if (map.getGameGrid()[this.currentPosition.getY()][this.currentPosition.getX()].getSister() != null
            && this.direction == map.getGameGrid()[this.currentPosition.getY()][this.currentPosition.getX()].getIn()
        ) {
                this.currentPosition = map.getGameGrid()[this.currentPosition.getY()][this.currentPosition.getX()].getSister();
                this.askedWay        = map.getGameGrid()[this.currentPosition.getY()][this.currentPosition.getX()].getOut();
                this.direction       = map.getGameGrid()[this.currentPosition.getY()][this.currentPosition.getX()].getOut();

        } else {  
                this.currentPosition = getNextCase(this.direction);
        }

        if (map.getGameGrid()[this.currentPosition.getY()][this.currentPosition.getX()].getStatus() 
            == this.currentPosition.IS_FOOD
        ) {
                map.getGameGrid()[this.currentPosition.getY()][this.currentPosition.getX()].setStatus(this.currentPosition.IS_EMPTY);
                this.score++;
        }
    }
    
    
    /*
    Don't go anywhere my friend
    */
    private boolean canAdvance(Direction way, Map gameMap) 
    {
        Case nextCase = getNextCase(way);
       
        int iStatus = nextCase.IS_WALL;
        try    {
            iStatus = gameMap.getGameGrid()[nextCase.getY()][nextCase.getX()].getStatus();
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            if (gameMap.getGameGrid()[this.currentPosition.getY()][this.currentPosition.getX()].getSister() != null) {
                return gameMap.getGameGrid()[this.currentPosition.getY()][this.currentPosition.getX()].getSister() != null;
            }
        }
        return iStatus != nextCase.IS_WALL;
    }
    /*
    You are not immortal
    */
    private boolean noMobHere()
    {
        return getNextCase(this.direction).isMob();
    }
    
    /*
    * Can I Turn ?
    */
    private void turn(Map map) 
    {
        if (this.askedWay != null) { //Who the fuck has touch my keyboard ?!
                Case nextCase = getNextCase(this.askedWay);
                if(canAdvance(this.askedWay, map)) {
                    
                this.direction = this.askedWay;
            } 
        }
        this.askedWay = null;
    }
    
    /*
    Are you dead yet ?
    */
    public boolean areYouDeadYet() 
    {
        return this.life == 0;
    }
}
