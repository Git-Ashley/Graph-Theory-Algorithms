/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphalgorithms;
import GUI.GuiFrame;
import GraphPackage.*;
import java.awt.Color;


public class GraphTools {
    
    static private GuiFrame guiFrame;
    static private Graph currentGraph;
    
    
    public static int drawVertex(Vertex v, Color col){
        //add vertex to panels vertex list, then repaint.
        if(guiFrame.getGraphPanel() == null){
            System.out.println("Cannot draw on this type of panel");
            System.exit(0);
        }
        guiFrame.getGraphPanel().addVertex(v,col);
        guiFrame.getGraphPanel().repaint();
        return 0;
    }
    public static int drawVertex(Vertex v){
        if(guiFrame.getGraphPanel() == null){
            System.out.println("Cannot draw on this type of panel");
            System.exit(0);
        }        
        guiFrame.getGraphPanel().addVertex(v);
        guiFrame.getGraphPanel().repaint();
        return 0;
    }
    public static int drawInterlacementVertex(Vertex v, Color col){
        if(guiFrame.getGraphPanel() == null){
            System.out.println("Cannot draw on this type of panel");
            System.exit(0);
        }        
        guiFrame.getGraphPanel().addInterlacementVertex(v, col);
        guiFrame.getGraphPanel().repaint();
        return 0;
    }
    public static int drawEdge(Edge e, Color col){
        if(guiFrame.getGraphPanel() == null){
            System.out.println("Cannot draw on this type of panel");
            System.exit(0);
        }        
        guiFrame.getGraphPanel().addEdge(e,col);
        guiFrame.getGraphPanel().repaint();
        return 0;
    }
    public static int drawEdge(Edge e){
        if(guiFrame.getGraphPanel() == null){
            System.out.println("Cannot draw on this type of panel");
            System.exit(0);
        }        
        guiFrame.getGraphPanel().addEdge(e);
        guiFrame.getGraphPanel().repaint();
        return 0;
    }
    public static int drawGraph(Graph G, Color col){
        for (int i = 0; i<G.numVertices(); i++){
            Vertex v = G.getVertex(i);
            GraphTools.drawVertex(v,col);
        }
        
        for(int i = 0; i<G.numEdges();i++){
            Edge e = G.getEdge(i);
            GraphTools.drawEdge(e,col);
        }
        
        guiFrame.getGraphPanel().repaint();
        
        return 0;
    }
    public static int drawGraph(Graph G){
        
        return drawGraph(G, Color.BLACK);
    }
    public static int clearBuffers(){
        if(guiFrame.getGraphPanel() == null){
            System.out.println("Cannot draw on this type of panel");
            System.exit(0);
        }        
        guiFrame.getGraphPanel().clearBuffers();
        return 0;
    }
    public static int clearGraphics(){
        if(guiFrame.getGraphPanel() == null){
            System.out.println("Cannot draw on this type of panel");
            System.exit(0);
        }   
        GraphTools.clearBuffers();
        GraphTools.getFrame().getGraphPanel().repaint();
        return 0;
    }
    
    public static int drawMessage(String S){
        
        guiFrame.getGraphPanel().setString(S);
        guiFrame.getGraphPanel().repaint();
        
        return 0;
    }
    public static int clearMessage(){
        guiFrame.getGraphPanel().clearString();
        guiFrame.getGraphPanel().repaint();
        
        return 0;
    }

    public static void setGraph(Graph G){
        currentGraph = G;
    }
    public static void setFrame(GuiFrame frame){
        guiFrame = frame;
    }
    
    public static GuiFrame getFrame(){
        return guiFrame;
    }
    public static Graph getGraph(){
        return currentGraph;
    }
    
}
