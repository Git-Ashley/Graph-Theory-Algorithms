/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphPackage;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.io.Serializable;
import graphalgorithms.GraphTools;
/**
 *
 * @author Student
 */
public class Graph implements Serializable {
    
    private boolean simple;
    private int vertexID;
 
    private ArrayList<Vertex> VS;
    private ArrayList<Edge> ES;

    public Graph(){
        VS = new ArrayList<Vertex>();
        ES = new ArrayList<Edge>();
        simple = true;
        vertexID = 1;
    }   
    
    public int numVertices(){
        return VS.size();
    }
    public int numEdges(){
        return ES.size();
    }

    public Vertex getVertex(int index){
        return VS.get(index);
    }
    public Edge getEdge(int index){
        return ES.get(index);
    }
    public int getMaxVertexId(){
        return vertexID-1;
    }

    public void addVertex(Vertex v){
        v.setID(vertexID);
        VS.add(v);
        vertexID++;
    }
    public int addEdge(Edge e){
        
        
        Vertex u = e.getV2();
        for(int i = 0; i < e.getV1().numAdjVertices(); i++){
            if (e.getV1().getAdjVertex(e.getV1().getIncEdge(i)) == u){
                return 2;
            }
        }
        
        
        if(e.getV1() == null || e.getV2() == null){
            System.out.println("Attempting to add edge to graph with a null vertex");
            System.exit(0);
            return -1;
        } else if(e.getV1() != e.getV2() || !simple){
            ES.add(e);
            e.getV1().addIncEdge(e);
            e.getV2().addIncEdge(e);
            return 0;
        } else {
            System.out.println("Cannot have a self loop in a simple graph");
            return 1;
        }
    }

    
    
}
