/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import GraphPackage.*;
import graphalgorithms.GraphTools;
import java.awt.Color;
import java.util.Stack;

/**
 *
 * @author Student
 */
public class CycleFind extends Algorithm {
    
    public CycleFind(int speed){
        super(speed);
    }
    
    public int execute(){
        
        Graph G = GraphTools.getGraph();
        
        Stack<Vertex> vertexStack = new Stack();
        VertexSet visitedSet = new VertexSet(G);
        visitedSet.add(startVertex);
        GraphTools.drawVertex(startVertex,Color.red);
        cycleDFS(startVertex, visitedSet, vertexStack);
        
        if(planar(G,vertexStack)){
            //message/label saying it ain't planar.
        }
        
        return 0;
        
    }
    
    private boolean planar(Graph G, Stack<Vertex> cycleStack){
        
        //...
        
        return false;
    }
    
    private int cycleDFS(Vertex v, VertexSet visitedSet, Stack<Vertex> vertexStack){
        
        
        for(int i=0;i<v.numAdjVertices();i++){

            Vertex u=v.getAdjVertex(v.getIncEdge(i));

            if(visitedSet.contains(u)<=0){
                visitedSet.add(u);
                
                Edge e = v.getIncEdge(i);
                GraphTools.drawEdge(e,Color.red);
                v.getAdjVertex(e);
                
                try{
                        Thread.sleep(speed);
                }catch(Exception ex){ex.printStackTrace();}

                GraphTools.drawVertex(u,Color.RED);
                try{
                        Thread.sleep(speed);
                }catch(Exception ex){ex.printStackTrace();}

               
                vertexStack.push(v);
                if(cycleDFS(u, visitedSet, vertexStack) == 1){
                    return 1;
                }
            }
            else if(visitedSet.contains(u) > 0 & u!=vertexStack.peek()){
                
                vertexStack.push(v);
                Edge e = v.getIncEdge(i);
                GraphTools.drawEdge(e,Color.orange);
                try{
                        Thread.sleep(speed*4);
                }catch(Exception ex){ex.printStackTrace();}

                cycleFound(v,u, vertexStack);
                System.out.println("Reached end of fn");
                return 1; //i.e. cycle found
            }  

        }
        if(vertexStack.isEmpty()){
            System.out.println("error: attempted to pop an empty stack.");
        } else {
            vertexStack.pop();
        }
        
        return 0;  //no cycle found yet
    }
    public void cycleFound(Vertex z,Vertex TVertex, Stack<Vertex> inputVertexStack){
        
        Stack<Vertex> newStack = new Stack();
        
                
        while(inputVertexStack.peek()!=TVertex){
            newStack.push(inputVertexStack.pop());
        }                
        newStack.push(inputVertexStack.peek());
        
        inputVertexStack.clear();
        
        while(!newStack.isEmpty()){
            inputVertexStack.push(newStack.pop());
        }
        
        
        for(int i = 0; i<inputVertexStack.size() ; i++){
            Vertex v = inputVertexStack.get(i);
            Vertex w = inputVertexStack.get((i+1)%inputVertexStack.size());
            
            GraphTools.drawVertex(v,Color.GREEN);
            GraphTools.drawEdge(new Edge(v,w),Color.GREEN);
        }
        
        try{
                Thread.sleep(2*speed);
        }catch(Exception ex){ex.printStackTrace();}
        

                
        
    }
    
}
