/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphPackage;

/**
 * VertexSet represents a vertex set, where each vertex belongs to the same graph.
 * It's implementation resembles a simple hash set.
 * @author Student
 */
public class VertexSet {
    
    int[] vertexArray;
    
    public VertexSet(Graph G){
        vertexArray = new int[G.getMaxVertexId()];
    }
    
    public int add(Vertex v){
        
        if(v.getID() > vertexArray.length ){
            System.out.println("Attempting to access vertex in VertexSet with ID > vertexSet size");
            return -1;
        }
        
        int index = v.getID() - 1;
        
        if(vertexArray[index] > 0){
            return 1;
        } else vertexArray[index] = 1;
        
        return 0;
    }
    public int map(Vertex fromVertex, Vertex toVertex){
        if(fromVertex.getID() > vertexArray.length ){
            System.out.println("Attempting to access vertex in VertexSet with ID > vertexSet size");            
            return -1;
        }
        
        if(this.contains(fromVertex) > 0){
            vertexArray[fromVertex.getID() - 1] = toVertex.getID();
            return 1;
        } else {
            vertexArray[fromVertex.getID() - 1] = toVertex.getID();
            return 0;
        }
    }
    
    public int contains(Vertex v){
        if (v.getID() > vertexArray.length){
            System.out.println("ERROR: set membership being checked for a vertex with ID greater than set size");
            return -1;
        }

        return vertexArray[v.getID()-1];
    }
    

    
    
}
