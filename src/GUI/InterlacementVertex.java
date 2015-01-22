/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GraphPackage.Vertex;
import java.awt.Color;

/**
 *
 * @author Student
 */
public class InterlacementVertex {
    private String label;
    private int x;
    private int y;
    private Color col;
    
    
    public InterlacementVertex(Vertex v, Color col){
        this.col = col;
        x = v.getX();
        y = v.getY();
        label = "" + v.getID();
    }
    
    public Color getColor(){
        return col;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public String getLabel(){
        return label;
    }
    
    public static final int INTERLACEMENT_SIZE = 20;
    
}
