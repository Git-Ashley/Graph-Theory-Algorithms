/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphalgorithms;

import java.awt.EventQueue;
import javax.swing.JFrame;
import GUI.GuiFrame;

/**
 *
 * @author Student
 */
public class GraphAlgorithms {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable()
            {
                public void run(){
                    GuiFrame gui = new GuiFrame();
                    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    gui.setVisible(true);
                }
            });
    }
}
