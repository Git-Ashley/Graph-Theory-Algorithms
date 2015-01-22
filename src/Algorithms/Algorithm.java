/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;
import graphalgorithms.GraphTools;
import java.awt.Color;
import GraphPackage.*;
/**
 *
 * @author Student
 */
public abstract class Algorithm implements Runnable {
    
    protected int speed;
    protected int edgePause;
    protected int vertexPause;
    
    private boolean pause;
    
    protected Vertex startVertex;
    
    Algorithm(int speed){
        this.speed = speed;
        edgePause = speed;
        vertexPause = 2*speed;
        
        pause = false;
    }
    
    public void run(){
        execute();
        GraphTools.getFrame().getGraphPanel().finish();
    }
    
    abstract int execute();
    
    public void setStartVertex(Vertex v){
        startVertex = v;
    }    
    
    public Vertex getStartVertex(){
        return startVertex;
    }
    
    public void setSpeed(int i){
        speed = i;
        edgePause = speed;
        vertexPause = 2*speed;
    }
    
    protected void sleep(int i){
        
         if(pause){
            synchronized (this) {
                   try{
                       System.out.println("Thread paused");
                       this.wait();
                       System.out.println("Thread unpaused");
                   } catch(Exception ex){}
            }
         }
        
        try {
            Thread.sleep(i);
        } catch(Exception ex){}
    }
    
    public void setPause(boolean val){
        pause = val;
    }
    public boolean getPauseStatus(){
        return pause;
    }
    
    
}
