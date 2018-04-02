/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tp_pacman_kevin_hantzen_a2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Kevin
 */
public class Window extends JFrame implements Constant  {
        
        private GameModel model;
        
        public Window() {
            //Title
            super("This is not PacMan ! It's a Sandwich !");
            //Create game model
            this.model = new GameModel();
            //End of prog when close
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            //no resizable
            setResizable(false);
            //background colo
            
            //Create content for game
            final JPanel content = new JPanel() {
                  @Override
                  protected void paintComponent(Graphics g) {
                        setBackground(Color.BLACK);
                        setLayout(new GridBagLayout());
                        
                        GridBagConstraints gbCoord = new GridBagConstraints();
                        
                        super.paintComponent(g);
                        // Display from game model
                        Window.this.model.display(g, gbCoord);
                  }
            };
            
            //Set his dimension 
            content.setPreferredSize(new Dimension(
                                        COLUMN_NUMBER_MAX * SQUARE_PIXEL_SIZE + 200,
                                        LINE_NUMBER_MAX   * SQUARE_PIXEL_SIZE 
                                        )
                                    );
            
            
            // Get keyboard input
            content.addKeyListener(new KeyAdapter() {
                  @Override
                  public void keyPressed(KeyEvent e) {
                        Window.this.model.keyboardGestion(e);
                  }
            });
            
            setFocusable(false);
            content.setFocusable(true);
            //Add it to the window
            setContentPane(content);
            
            //Create infinite Thread
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {//Infinite loop
                        Window.this.model.operation();
                        //redraw content
                        content.repaint();
                        //get some time
                        try {
                              Thread.sleep(230);
                        } catch (InterruptedException e) {
                              //
                        }                        
                    }
                }
            });           
             thread.start();
        }
}
