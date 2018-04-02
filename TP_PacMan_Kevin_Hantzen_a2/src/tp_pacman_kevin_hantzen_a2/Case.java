/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tp_pacman_kevin_hantzen_a2;

import java.util.ArrayList;

/**
 *
 * @author Kevin
 */
public class Case implements Constant {
    private int         x;
    private int         y;
    private Case        sister      = null;
    private Direction   in          = null;
    private Direction   out         = null;
    private int         distance    = 99;
    private boolean     isStart     = false;
    private int         status;
    private Map         map;
    public static int   IS_WALL     = 0;
    public static int   IS_FOOD     = 1;
    public static int   IS_FRUIT    = 2;
    public static int   IS_PACMAN   = 3;
    public static int   IS_EMPTY    = 5;
    public static int   IS_DEPART   = 6;
    public static int   IS_MONSTER  = 7;

    
    public Case (int x, int y, int status, Map map)
    {
        this.x      = x;
        this.y      = y;
        this.status = status;
        this.map    = map;
    }
    
    public Case(int x, int y) 
    {
       this.x      = x;
       this.y      = y;
    }
    
    //horizontal
    public void setX(int x) 
    {
        this.x = x;
    }
    
    public int getX() 
    {
        return this.x;
    }
    
    public int getXPixel() 
    {
        return this.x * SQUARE_PIXEL_SIZE;
    }
    
    //vertical
    public void setY(int y) 
    {
        this.y = y;
    }
    
    public int getY() 
    {
        return this.y;
    }
    
    public int getYPixel() 
    {
        return this.y * SQUARE_PIXEL_SIZE;
    }
    
    //status
    public int getStatus() 
    {
        return this.status;
    }
    
    public void setStatus(int iStat) 
    {
        this.status = iStat;
    }
    
    //width
    public int getWidth() 
    {
        return SQUARE_PIXEL_SIZE;
    }
    
    //height
    public int getHeight() 
    {
        return SQUARE_PIXEL_SIZE;
    }
    
    public Map getMap() 
    {
        return this.map;
    }
    
    /*
    * Find case for Inky and Pinky
    */
    public static Case getGhostCase(PacMan target, Map map, Case currentPosition, int i) 
    {
        Direction wichWay = target.getDirection();
        Case newPosition  = target.getCurrentPosition();
        System.out.println("coucou");
                boolean testCase      = true;
                    switch (wichWay) {
                        case TO_THE_TOP:
                            while (testCase) {
                                try {System.out.println("1");
                                    if (map.getGameGrid()[target.getCurrentPosition().getY()-i][target.getCurrentPosition().getX()].getStatus() != 0) {
                                        newPosition = map.getGameGrid()[target.getCurrentPosition().getY()-i][target.getCurrentPosition().getX()];
                                        testCase = false;
                                    } else {
                                        i--;
                                    }
                                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                                    i--;
                                }
                            }
                        break;
                        case TO_THE_BOT:
                            while (testCase) {
                                try {System.out.println("2");
                                    if (map.getGameGrid()[target.getCurrentPosition().getY()+i][target.getCurrentPosition().getX()].getStatus() != 0) {
                                        newPosition = map.getGameGrid()[target.getCurrentPosition().getY()+i][target.getCurrentPosition().getX()];
                                        testCase = false;
                                    } else {
                                        i--;
                                    }
                                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                                    i--;
                                }
                            }
                        break;
                        case TO_THE_RIGHT:
                            while (testCase) {
                                try {System.out.println("3");
                                    if (map.getGameGrid()[target.getCurrentPosition().getY()][target.getCurrentPosition().getX()+i].getStatus() != 0) {
                                        newPosition = map.getGameGrid()[target.getCurrentPosition().getY()][target.getCurrentPosition().getX()+i];
                                        testCase = false;
                                    } else {
                                        i--;
                                    }
                                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                                    i--;
                                }
                            }                            
                        break;
                        case TO_THE_LEFT:
                            while (testCase) {
                                try {System.out.println("4");
                                    if (map.getGameGrid()[target.getCurrentPosition().getY()][target.getCurrentPosition().getX()-i].getStatus() != 0) {
                                        newPosition = map.getGameGrid()[target.getCurrentPosition().getY()][target.getCurrentPosition().getX()-i];
                                        testCase = false;
                                    } else {
                                        i--;
                                    }
                                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                                    i--;
                                }
                            }       
                        break;
                    }
                
                target.getCurrentPosition().setDistance(map);
                if (currentPosition.getDistance() < newPosition.getDistance()) {
                    newPosition = target.getCurrentPosition();
                }
                map.resetDistance();
                return newPosition;
    }

    
    /*
    * get available case near a case
    */
    public ArrayList<Case> getAvailableCase() 
    {

        ArrayList<Case> caseDispo = new ArrayList();
        int x        = this.x;
        int y        = this.y;
        Case[][] map = this.map.getGameGrid();

        for (int i = 0; i<4; i++) {
            try {
                switch (i) {
                    case 0://test top
                        if (map[y-1][x].status != this.IS_WALL) {
                            caseDispo.add(map[y-1][x]);
                        }
                    break;
                    case 1: //test bot
                        if (map[y+1][x].status != this.IS_WALL) {
                            caseDispo.add(map[y+1][x]);
                        }
                    break;
                    case 2: //test right
                        if (map[y][x+1].status != this.IS_WALL) {
                            caseDispo.add(map[y][x+1]);
                        }                       
                    break;
                    case 3: //test left
                        if (map[y][x-1].status != this.IS_WALL) {
                            caseDispo.add(map[y][x-1]);
                        }     
                    break;
                }
            } catch(java.lang.ArrayIndexOutOfBoundsException e) {
            }    
        }
        return caseDispo;
    }
    
    /*
    * set Is Start
    */
    public void setIsStart(boolean b) 
    {
        this.isStart = b;
    }
    
    /*
    * getDistance
    */
    public int getDistance()
    {
        return this.distance;
    }
    
    public void setDistance(int i) 
    {
        this.distance = 0;
    }
    
    /*
    * setDistance between two Case
    */
    public void setDistance(Map map) 
    {
        Case[][] gridGame = map.getGameGrid();
        try {
            //top
            if ( (gridGame[this.y - 1][this.x].distance > this.distance + 1 
                  || gridGame[this.y - 1][this.x].distance == 0) 
                && gridGame[this.y - 1][this.x].status != this.IS_WALL
                && !gridGame[this.y - 1][this.x].isStart
            ) {
                gridGame[this.y - 1][this.x].distance = this.distance + 1;
                gridGame[this.y - 1][this.x].setDistance(map);
            }
            
            //bot
            if ( (gridGame[this.y + 1][this.x].distance > this.distance + 1 
                  || gridGame[this.y + 1][this.x].distance == 0) 
                && gridGame[this.y + 1][this.x].status != this.IS_WALL
                && !gridGame[this.y - 1][this.x].isStart
            ) {
                gridGame[this.y + 1][this.x].distance = this.distance + 1;
                gridGame[this.y + 1][this.x].setDistance(map);
            }
            
            //right
            if ( (gridGame[this.y][this.x + 1].distance > this.distance + 1 
                  || gridGame[this.y][this.x + 1].distance == 0) 
                && gridGame[this.y][this.x + 1].status != this.IS_WALL
                && !gridGame[this.y - 1][this.x].isStart
            ) {
                gridGame[this.y][this.x + 1].distance = this.distance + 1;
                gridGame[this.y][this.x + 1].setDistance(map);
            }
            
            //left
            if ( (gridGame[this.y][this.x - 1].distance > this.distance + 1 
                  || gridGame[this.y][this.x - 1].distance == 0) 
                && gridGame[this.y][this.x - 1].status != this.IS_WALL
                && !gridGame[this.y - 1][this.x].isStart
            ) {
                gridGame[this.y][this.x - 1].distance = this.distance + 1;
                gridGame[this.y][this.x - 1].setDistance(map);
            }
            
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            
        }
    }

    /*
        Sister
    */
    public Case getSister() 
    {
        return this.sister;
    }
    
    public void setSister(Case sister) 
    {
        this.sister = sister;
    }
    
    public void setOut(String d) 
    {
        switch (d) {
            case "R":
                this.out = Direction.TO_THE_RIGHT;
                this.in  = Direction.TO_THE_LEFT;
            break;
            case "L":
                this.out = Direction.TO_THE_LEFT;
                this.in  = Direction.TO_THE_RIGHT;
            break;
            case "T":
                this.out = Direction.TO_THE_TOP;
                this.in  = Direction.TO_THE_BOT;
            break;
            default:
                this.out = Direction.TO_THE_BOT;
                this.in  = Direction.TO_THE_TOP;
            break;
        }
    }
    
    public Direction getOut() 
    {
        return this.out;
    }
    
    public Direction getIn() 
    {
        return this.in;
    }
    
    //checkCase
    public boolean isValid() 
    {  
        Case[][] currentMap = this.map.getGameGrid();
        
        int iStatus = currentMap[this.getX()][this.getY()].getStatus();
     
        return iStatus != this.IS_WALL;
    }
    
    public boolean isMob() 
    {
        return this.status == this.IS_MONSTER;
    }
}   
