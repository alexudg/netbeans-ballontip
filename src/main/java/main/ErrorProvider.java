package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;

/**
 *
 * @author Alejandro Ramirez Macias
 */
public class ErrorProvider {
    static JWindow win;
    static Timer tmrHide;
    static Timer tmrOpacity;
    static int time;  

    public static void setError(Component component, String message) {
        final int PADDING_HOR = 30;
        final int PADDING_VER = 10;
        
        if (win == null) {
            tmrHide = new Timer(2000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    System.out.println("TimerHide");
                    tmrHide.stop();
                    time = 1000; // 1 second
                    tmrOpacity.start();
                }
            });

            // each 100 ms
            tmrOpacity = new Timer(100, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    System.out.println("TimerOpacity");
                    time -= 100;
                    win.setOpacity((float) (time * 0.001)); // 0.9,0.8,...,0.1
                    if (time == 100) {
                        tmrOpacity.stop();
                        win.dispose();
                    }
                }
            });
        } else {
            tmrHide.stop();
            tmrOpacity.stop();
            win.dispose();            
        }

        win = new JWindow();

        // make the background transparent 
        win.setBackground(new Color(0, 0, 0, 0));

        // create panel
        JPanel p = new JPanel() {            
                       
            @Override
            public void paintComponent(Graphics g) {
                int w = g.getFontMetrics().stringWidth(message);
                int h = g.getFontMetrics().getHeight();
                
                // fill rect
                g.setColor(Color.PINK);
                g.fillRect(4, 4, w + PADDING_HOR, h + PADDING_VER);                

                // line rect
                g.setColor(Color.RED);
                g.drawRect(4, 4, w + PADDING_HOR, h + PADDING_VER);
                
                // draw 3 lines of shadow of the toast 
                int t = 240; // tranparent
                for (int i = 0; i < 4; i++) {
                    t -= 60; // 180, 120, 60
                    g.setColor(new Color(255, 0, 0, t));
                    g.drawRect(
                            3 - i, // x
                            3 - i, // y
                            w + PADDING_HOR + (i + 1) * 2, // w 
                            h + PADDING_VER + (i + 1) * 2  // h
                    );
                }  
                
                // set the color of text
                g.setColor(Color.BLACK);
                g.drawString(message, 20, 22);                             
            }
        };        
        int w = p.getFontMetrics(p.getFont()).stringWidth(message);
        int h = p.getFontMetrics(p.getFont()).getHeight();
        win.add(p);
        win.setSize(w + PADDING_HOR + 8, h + PADDING_VER + 8);
        win.setLocation(component.getLocation());        
        //win.setOpacity(1);
        win.setVisible(true);
        tmrHide.start();
    }
}
