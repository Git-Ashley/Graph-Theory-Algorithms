/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import GraphPackage.*;

/**
 *
 * @author Student
 */
public class DrawableEdge {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Color col;
    
    public DrawableEdge(Edge e){
        x1 = e.getV1().getX();
        y1 = e.getV1().getY();
        x2 = e.getV2().getX();
        y2 = e.getV2().getY();
        col = Color.BLACK;
    }
    
    public DrawableEdge(Edge e, Color inputCol){
        x1 = e.getV1().getX();
        y1 = e.getV1().getY();
        x2 = e.getV2().getX();
        y2 = e.getV2().getY();
        col = inputCol;
    }
    
    public int getX1(){
        return x1;
    }
    
    public int getY1(){
        return y1;
    }
        
    public int getX2(){
        return x2;
    }
        
    public int getY2(){
        return y2;
    }
    
    public Color getColor(){
        return col;
    }
}
