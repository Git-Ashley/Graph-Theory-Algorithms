/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphPackage;
import java.awt.Color;
import java.awt.Graphics;
/**
 *
 * @author Student
 */
public class Edge {
    
    private Vertex v1;
    private Vertex v2;
    
    public Edge(Vertex inputV1, Vertex inputV2){
        v1 = inputV1;
        v2 = inputV2;
    }    
    
    public Vertex getV1(){
        return v1;
    }
    
    public Vertex getV2(){
        return v2;
    }

}
