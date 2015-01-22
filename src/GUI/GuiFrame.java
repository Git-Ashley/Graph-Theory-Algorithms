/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import graphalgorithms.GraphTools;
import GraphPackage.*;
import java.awt.Color;
/**
 *
 * @author Student
 */
public class GuiFrame extends JFrame {
    
    GraphPanel graphPanel;
    
    public GuiFrame(){
        
        add(new StartPanel());
        pack();
        GraphTools.setFrame(this);
        
        GraphTools.setGraph(new Graph());
        
    }
    
    public GraphPanel getGraphPanel(){
        return graphPanel;
    }
    
    public void setPanel(JPanel fromPanel, JPanel toPanel){
        
        if(toPanel.toString() != "GraphPanel"){
            graphPanel = null;
        } else {
            graphPanel = (GraphPanel) toPanel;
        }
        
        this.remove(fromPanel);
        this.add(toPanel);
        this.revalidate();
        this.repaint();        
    }
    
}
