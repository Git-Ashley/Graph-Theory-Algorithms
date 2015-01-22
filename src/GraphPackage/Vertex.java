/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphPackage;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
/**
 *
 * @author Student
 */
public class Vertex {
    
    private int x;
    private int y;
    private int ID;
    private String label;
    
    private ArrayList<Edge> incidentEdges;
    
    public Vertex(int inputX,int inputY){
        x=inputX;
        y=inputY;
        
        incidentEdges = new ArrayList<Edge>();
        
        ID = -1;
    }
    
    public Vertex(Vertex copyVertex){
        x = copyVertex.getX();
        y = copyVertex.getY();
        
        incidentEdges = new ArrayList<Edge>();
        ID = -1;
    }
    
    public Edge getIncEdge(int i){
        return incidentEdges.get(i);
    }
    
    public Vertex getAdjVertex(Edge e){
                        
        if(this == e.getV1()){
            return e.getV2();
        }
        else if(this == e.getV2()){
            return e.getV1();
        } else {    /*ERROR*/
            System.out.println("Vertex contains incident edge at index which does not contain itself");
            System.exit(0);
            return null;
        }
    }
    
    public void addIncEdge(Edge e){
        incidentEdges.add(e);
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
    
    public int getID(){
        return ID;
    }
    
    public void setID(int x){
        ID = x;
    }
    
    public int numAdjVertices(){
        return incidentEdges.size();
    }
    
    static final public int VERTEX_RADIUS = 5;
    
}
