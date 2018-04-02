/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tp_pacman_kevin_hantzen_a2;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.ImageIcon;



/**
 *
 * @author Kevin
 */
public class GameModel {
    
    private PacMan   pacMan;
    private Minions  monster;
    private Map      map;
    private int      scoreMax = 0;
    private boolean  pause    = false;
    private boolean  gameOver = false;
    private boolean  win      = false;
    
    public GameModel() 
    {
        String filePathMap    = "map/map1/map.txt";
        String filePathSister = "map/map1/sister.txt";
        
        File oFileMap       = new File(filePathMap);
        File oFileSister    = new File(filePathSister);
        /*
        *  we read the map file
        */
        ArrayList<String> arMap       = new ArrayList<String>();
        ArrayList<String> arMapSister = new ArrayList<String>();
       
        try {
            Scanner scanner = new Scanner(oFileMap);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                arMap.add(line);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Scanner scannerSister = new Scanner(oFileSister);

            while (scannerSister.hasNextLine()) {
                String line = scannerSister.nextLine();
                arMapSister.add(line);
            }

            scannerSister.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }        

        int x = arMap.get(0).length();
        int y = arMap.size();
        int iPacman = 0;
        int jPacman = 0;
        this.map = new Map(y,x);
        int  iStatus = 1;
        
        ArrayList<Case> minionsPosition = new ArrayList<Case>();
        
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                String c = "" + arMap.get(j).charAt(i);
                iStatus  = Integer.parseInt(c);
                this.map.getGameGrid()[j][i] = new Case(i,j, iStatus, this.map);                
                if (iStatus == 1) {
                    this.scoreMax++;
                }                
                if (iStatus == 6) {
                    iPacman = i;
                    jPacman = j;
                }                
                if (iStatus == 7) {
                    minionsPosition.add(this.map.getGameGrid()[j][i]);
                }    
                //reset iStatus
                iStatus = 1;
            }
        }
            String[] aCoord;
            
            int c1X; int c1Y; String c1D;
            int c2X; int c2Y; String c2D;
            
            for (int k = 0; k < arMapSister.size(); k++) {
                aCoord = arMapSister.get(k).split(",");

                //Get sister coord
                c1Y = Integer.parseInt("" + aCoord[0]);
                c1X = Integer.parseInt("" + aCoord[1]);
                c1D = aCoord[2];

                c2Y = Integer.parseInt("" + aCoord[3]);
                c2X = Integer.parseInt("" + aCoord[4]);
                c2D = aCoord[5];

                //set Sister
                this.map.getGameGrid()[c1Y][c1X].setSister(this.map.getGameGrid()[c2Y][c2X]);
                this.map.getGameGrid()[c2Y][c2X].setSister(this.map.getGameGrid()[c1Y][c1X]);
                
                this.map.getGameGrid()[c1Y][c1X].setOut(c1D);
                this.map.getGameGrid()[c2Y][c2X].setOut(c2D);
            }                      
            this.pacMan = new PacMan(iPacman,jPacman,this.map);            
            for (int i = 0; i < minionsPosition.size(); i++) {
                if (i == 0) {
                    this.monster = new Minions(minionsPosition.get(i), this.pacMan, i);
                } else {
                    this.monster.setBrother(minionsPosition.get(i));       
                }
            }   
        }
    
    
    //Game operation
    public void operation() 
    {
        if (!this.gameOver && !this.win) {
            if (!this.pause) {
                System.out.println("et donc");
                this.pacMan.operation(this.map);
                this.checkLife();       
                System.out.println("test");
                this.monster.operation(this.map);              
                this.checkLife();
                
                if (this.pacMan.areYouDeadYet()) {
                    this.gameOver = true;
                } else if (this.pacMan.getScore() == this.scoreMax) {
                    this.win = true;
                }
            }
        }
    }
    
    public void checkLife() 
    {
        int Life = this.pacMan.getLife();
        Case pacMan = this.pacMan.getCurrentPosition();
        Case minion;
        Minions mob = this.monster;
        do  {
            minion = mob.getCurrentPosition();
            if (minion.getX() == pacMan.getX() && minion.getY() == pacMan.getY()) {
                this.pacMan.setLife(this.pacMan.getLife() - 1);
                this.pacMan.returnStart();
                this.monster.returnStart();
                
                if (this.pacMan.getLife() != 0) {
                    this.pause = true;
                }
            }
            mob = mob.getBrother();
        } while (mob != null);
    } 
    
    //Game display
    public void display(Graphics g, GridBagConstraints gbCoord)
    {
        this.map.display(g, gbCoord);
        this.monster.display(g, gbCoord);
        this.pacMan.display(g, gbCoord);
        
        
        int iScore    = this.pacMan.getScore();
        int iScoreMax = this.scoreMax;
        
        String pacGum = "Pac-Gum";
        String result = iScore + "/" + iScoreMax;
        
        g.setColor(Color.ORANGE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        FontMetrics font = g.getFontMetrics();
        int x = (g.getClipBounds().width - font.stringWidth(pacGum)) - 15;
        int y = (g.getClipBounds().height)-580;
        
        g.drawString(pacGum, x, y);
        x = (g.getClipBounds().width - font.stringWidth(pacGum)) - 10;
        y = (g.getClipBounds().height) + font.getMaxDescent() -530;
        
        g.drawString(result, x, y);
        
        String lifeDisplay = "Life";
        g.setColor(Color.RED);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        FontMetrics fontmet = g.getFontMetrics();
        x = (g.getClipBounds().width - fontmet.stringWidth(lifeDisplay)) - 115;
        y = (g.getClipBounds().height)-400;
        
        g.drawString(lifeDisplay, x, y);
        
        ImageIcon life = new ImageIcon("pacman/life/coeur.png");
        Image     lifeToDraw = life.getImage();
        
        for (int i = 0; i < this.pacMan.getLife(); i++) 
        {
            x = (g.getClipBounds().width  - 90 - i*50);
            y = (g.getClipBounds().height) -390;
            g.drawImage(lifeToDraw, x, y, null);
     
        } 
        
        
        if (this.gameOver) {
            String gameOver = "Game Over";
   
            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
            FontMetrics fm = g.getFontMetrics();
            x = (g.getClipBounds().width - fm.stringWidth(gameOver)) / 2;
            y = (g.getClipBounds().height / 2) + fm.getMaxDescent();
            g.fillRect(g.getClipBounds().width/3-20   , g.getClipBounds().height/3 + 20 , g.getClipBounds().width/3+40, g.getClipBounds().height/4 );
            g.setColor(Color.RED);
            g.drawString(gameOver, x, y);
        } else if (this.win) {
            String win = "Gagné";
            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
            FontMetrics fm = g.getFontMetrics();
            x = (g.getClipBounds().width - fm.stringWidth(win)) / 2;
            y = (g.getClipBounds().height / 2) + fm.getMaxDescent();
            g.fillRect(g.getClipBounds().width/3+35   , g.getClipBounds().height/3 + 20 , g.getClipBounds().width/4, g.getClipBounds().height/4 );            
            g.setColor(Color.GREEN);
            g.drawString(win, x, y);            
        } else if (this.pause) {
            String pause = "Pause";
            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
            FontMetrics fm = g.getFontMetrics();
            x = (g.getClipBounds().width - fm.stringWidth(pause)) / 2;
            y = (g.getClipBounds().height / 2) + fm.getMaxDescent();
            g.fillRect(g.getClipBounds().width/3+35   , g.getClipBounds().height/3 + 20 , g.getClipBounds().width/4, g.getClipBounds().height/4 );
            g.setColor(Color.CYAN);
            g.drawString(pause, x, y);              
        }
    }
    
    public void keyboardGestion(KeyEvent event) 
    {
        if (event.getKeyCode() == KeyEvent.VK_RIGHT) { // right arrow
            this.pacMan.setAskedWay(Direction.TO_THE_RIGHT);
        } else if (event.getKeyCode() == KeyEvent.VK_LEFT) { // left arrow
            this.pacMan.setAskedWay(Direction.TO_THE_LEFT);
        } else if (event.getKeyCode() == KeyEvent.VK_UP) { // top arrow
            this.pacMan.setAskedWay(Direction.TO_THE_TOP);
        } else if (event.getKeyCode() == KeyEvent.VK_DOWN) { // bot arrow
            this.pacMan.setAskedWay(Direction.TO_THE_BOT);
        } else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!this.gameOver && !this.win) {
                this.pause = !this.pause;
            } else {
                
            }
        }         
    }
}
