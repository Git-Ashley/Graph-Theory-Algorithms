/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import GraphPackage.*;
import java.awt.Color;
/**
 *
 * @author Student
 */
public class DrawableVertex {
    
    private int x;
    private int y;
    private Color col;
    
    public DrawableVertex(Vertex v){
        x = v.getX();
        y = v.getY();
        col = Color.BLACK;
    }
    public DrawableVertex(Vertex v, Color inputCol){
        x = v.getX();
        y = v.getY();
        col = inputCol;
    }
    
    
    
    public int getX(){
        return x;
    }
        
    public int getY(){
        return y;
    }
    public Color getColor(){
        return col;
    }
}
