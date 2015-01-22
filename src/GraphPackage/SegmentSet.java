/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphPackage;
import GraphPackage.*;
import java.awt.Color;
/**
 *
 * @author Student
 */
public class SegmentSet extends VertexSet {
    
    public SegmentSet(Graph G, Color col){
        super(G);
        colour = col;
    }
    
    Color colour;
    
    public Color getColour(){
        return colour;
    }
    
}
