/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;
import GUI.InterlacementVertex;
import java.util.Stack;
import java.util.ArrayList;
import GraphPackage.*;
import graphalgorithms.GraphTools;
import java.awt.Color;
/**
 *
 * @author Student
 */
public class AuslanderParter extends Algorithm {
    
    Color cycleColour;
    
    public AuslanderParter(int speed){
        super(speed);
        cycleColour = new Color(0,150,0);
    }
    public int execute(){
        
        Graph G = GraphTools.getGraph();
        
        Stack<Vertex> vertexStack = new Stack();
        VertexSet visitedSet = new VertexSet(G);
        if(GraphTools.getGraph().numVertices() == 1){
            vertexStack.push(startVertex);
        } else {
            visitedSet.add(startVertex);
            GraphTools.drawVertex(startVertex,Color.red);
            
            GraphTools.drawMessage("Finding initial cycle");
            cycleDFS(startVertex, visitedSet, vertexStack);
            GraphTools.clearMessage();
        }
        
        if(!planar(G,vertexStack)){
            GraphTools.drawMessage("Interlacement graph is not bipartite; Graph is not planar.");
        } else GraphTools.drawMessage("Graph is planar.");
        
        return 0;
        
    }
    
    // RECURSIVE FUNCTION
    private boolean planar(Graph G, Stack<Vertex> cycleStack){
        
        //0. initiation
        colourCycle(cycleStack, cycleColour);
        sleep(vertexPause);
        VertexSet visitedSet = new VertexSet(G);
        VertexSet cycleSet = new VertexSet(G);
        ArrayList<SegmentSet> segmentList = new ArrayList();
        for(Vertex v : cycleStack){
            visitedSet.add(v);
            cycleSet.add(v);
        }
        
        //1. Finding the segments
        GraphTools.drawMessage("Finding Segments");
        findSegments(segmentList, G, visitedSet, cycleSet, cycleStack);
        GraphTools.clearMessage();
        
        //2. Creating the interlacement graph
        GraphTools.drawMessage("Checking compatibility of segments");
        Graph interlacementGraph = new Graph();
        createInterlacementGraph(interlacementGraph, segmentList, cycleStack);
        sleep(speed*4);
        
        //3. Testing if interlacement graph  is bipartite
        if(!isBipartite(interlacementGraph)){
            return false;
        }
        
        //4. Test if each segment requires recursion and if so, create new graph and cycle
        
        for(int i = 0; i<segmentList.size(); i++){
            SegmentSet segment = segmentList.get(i);
            //4.1: Draw the cycle with the segment (clear buffers + redraw)
            Graph H = new Graph();
            Stack<Vertex> newCycle = new Stack();
            
            //4.2: Check if recursion needed
            if(recursionNeeded(G, segment, cycleSet)){
                VertexSet mapping = new VertexSet(G);
                
                GraphTools.clearBuffers();
                GraphTools.drawGraph(G);
                GraphTools.drawMessage("Recursion needed on segment " + (i+1));
                
                //construct new graph to be passed on for the next recursion
                constructNewGraph(H, mapping, G, segment, cycleStack, cycleSet);
                
                //graphic aid: (sub-graph to be passed to next recursion flashes)
                for(int k = 0; k<4 ; k++){
                    GraphTools.drawGraph(H, Color.black);
                    sleep((int)(speed*0.5));
                    GraphTools.drawGraph(H, segment.getColour());
                    sleep((int)(speed*0.5));
                }
                
                //clear buffers & redraw only the current segment with cycle
                GraphTools.clearMessage();
                GraphTools.clearBuffers();
                GraphTools.drawGraph(H);
                colourCycle(cycleStack, cycleColour);
                sleep(4*speed);
                
                //get separating cycle in H to be passed to next recursion
                constructNewCycle(newCycle, H, G, segment, cycleStack, cycleSet);
                GraphTools.clearMessage();
                GraphTools.clearBuffers();
                GraphTools.drawGraph(H);
                sleep(3*speed);
                
                Stack<Vertex> inputCycle = new Stack();
                
                for(Vertex v : newCycle){
                    inputCycle.add(GtoH(v, mapping, H));
                }
                
                //highlight new cycle to make it clear this is the new cycle
                colourCycle(inputCycle, Color.cyan);
                
                sleep(4*speed);
                //Recursive call:
                if(!planar(H, inputCycle)){
                    return false;
                }
            }
        }
        
        return true;
    }
    
    
    //*************************** 0. FINDING THE INITIAL CYCLE *******************************
    private int cycleDFS(Vertex v, VertexSet visitedSet, Stack<Vertex> vertexStack){
        
        
        for(int i=0;i<v.numAdjVertices();i++){

            Vertex u=v.getAdjVertex(v.getIncEdge(i));

            if(visitedSet.contains(u)<=0){
                visitedSet.add(u);
                
                Edge e = v.getIncEdge(i);
                GraphTools.drawEdge(e,Color.red);
                v.getAdjVertex(e);
                sleep(edgePause);

                GraphTools.drawVertex(u,Color.RED);
                sleep(vertexPause);
                
                vertexStack.push(v);
                if(cycleDFS(u, visitedSet, vertexStack) == 1){
                    return 1;
                }
            }
            else if(visitedSet.contains(u) > 0 & u!=vertexStack.peek()){
                
                vertexStack.push(v);
                Edge e = v.getIncEdge(i);
                GraphTools.drawEdge(e,Color.orange);
                sleep(4*speed);
                
                GraphTools.drawMessage("Cycle Found!");
                cycleFound(v,u, vertexStack);
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
    private void cycleFound(Vertex z,Vertex TVertex, Stack<Vertex> inputVertexStack){
        
        Stack<Vertex> newStack = new Stack();
        
                
        while(inputVertexStack.peek()!=TVertex){
            newStack.push(inputVertexStack.pop());
        }                
        newStack.push(inputVertexStack.peek());
        
        inputVertexStack.clear();
        
        while(!newStack.isEmpty()){
            inputVertexStack.push(newStack.pop());
        }
        
        GraphTools.drawGraph(GraphTools.getGraph());
        
        colourCycle(inputVertexStack, new Color(0,150,0));
                
        sleep(speed*3);
        

                
        
    }
    
    
    //****************************** 1. FINDING THE SEGMENTS *********************************
    private void findSegments(ArrayList<SegmentSet> segmentList, Graph G, VertexSet visitedSet, VertexSet cycleSet, Stack<Vertex> cycleStack){
        
        //traverse vertex set of G first:
        for(int i = 0; i<G.numVertices(); i++){
            Vertex v = G.getVertex(i);
            if(visitedSet.contains(v)<=0){
                Color segColour = nextSegmentColour(segmentList.size());
                SegmentSet segmentSet = new SegmentSet(G,segColour);
                GraphTools.drawVertex(v, segmentSet.getColour());
                sleep(vertexPause);
                findSegment(v, visitedSet, cycleSet, segmentSet);
                segmentList.add(segmentSet);
            }
            
        }
        
        for(int i = 0; i<G.numEdges(); i++){
            Edge e = G.getEdge(i);
            if(isChord(e, cycleSet, cycleStack)){
                Color segColour = nextSegmentColour(segmentList.size());
                SegmentSet segment = new SegmentSet(G, segColour);
                segment.add(e.getV1());
                segment.add(e.getV2());
                segmentList.add(segment);
                GraphTools.drawEdge(e, segment.getColour());
                sleep(vertexPause);
            }
        }
        
        
    }
    private void findSegment(Vertex startVertex, VertexSet visitedSet, VertexSet cycleSet, SegmentSet segmentSet){
        
        Stack<Vertex> vertexStack = new Stack();
        Vertex currentVertex;
        
        vertexStack.push(startVertex);
        segmentSet.add(startVertex);
        visitedSet.add(startVertex);
        
        while(!vertexStack.isEmpty()){
            
            currentVertex = vertexStack.pop();
            
            for(int i = 0; i < currentVertex.numAdjVertices() ; i++){
                
                Edge e = currentVertex.getIncEdge(i);
                Vertex v = currentVertex.getAdjVertex(e);
                
                if(visitedSet.contains(v) <= 0){
                    GraphTools.drawEdge(e, segmentSet.getColour());
                    sleep(edgePause);
                    GraphTools.drawVertex(v, segmentSet.getColour());
                    sleep(vertexPause);
                    visitedSet.add(v);
                    segmentSet.add(v);
                    vertexStack.push(v);
                } else if (cycleSet.contains(v) > 0){
                    GraphTools.drawEdge(e, segmentSet.getColour());
                    sleep(edgePause);
                    segmentSet.add(v);
                } else if (segmentSet.contains(v) > 0){
                    GraphTools.drawEdge(e, segmentSet.getColour());
                    sleep(edgePause);
                }
            } //for loop
        } //while loop
    }
    private Color nextSegmentColour(int i){
        Color[] colourList = {Color.CYAN, Color.red, Color.orange, Color.green, new Color(145,44,238),
                              Color.MAGENTA, new Color(100,100,255), Color.YELLOW, Color.GRAY, Color.PINK};
        
        return colourList[i%colourList.length];
        }
    
    
    //***************************** 2. CREATING INTERLACEMENT GRAPH **************************
    private void createInterlacementGraph(Graph interlacementGraph, ArrayList<SegmentSet> segmentList, Stack<Vertex> C){
        int numSegments = segmentList.size();
        int radius = 50;
        int originx = 500;
        int originy = 100;
        
        for(int i = 0; i < numSegments; i++){
            SegmentSet segment = segmentList.get(i);
            int xcoord = (int) (originx + radius*Math.cos((i*2*Math.PI)/numSegments) );  //polar coordinates converted to cartesian
            int ycoord = (int) (originy - radius*Math.sin((i*2*Math.PI)/numSegments) );
            
            Vertex v = new Vertex(xcoord,ycoord);
            interlacementGraph.addVertex(v);
            GraphTools.drawInterlacementVertex(v, segment.getColour());
        }
        
        for(int i = 0; i < segmentList.size()-1; i++){
            
            SegmentSet segmenti = segmentList.get(i);
            
            for(int j = i + 1; j < segmentList.size(); j++){
                SegmentSet segmentj = segmentList.get(j);
                if(!compatible(segmenti, segmentj, C)){
                    Edge e = new Edge(interlacementGraph.getVertex(i), interlacementGraph.getVertex(j));
                    interlacementGraph.addEdge(e);
                    GraphTools.drawEdge(e);
                    sleep(edgePause);
                }
            }
        }
        
    }
    private boolean compatible(SegmentSet segmenti, SegmentSet segmentj, Stack<Vertex> C){
        
        SegmentFlags iFlag = SegmentFlags.NULL;
        SegmentFlags jFlag = SegmentFlags.NULL;
        
        for(int i = 0; i < C.size(); i++){
            
            Vertex v = C.get(i);
            
            //************* ASSIGNMENTS *************
            if(iFlag == SegmentFlags.NULL && jFlag == SegmentFlags.NULL){
                if(segmenti.contains(v) > 0 && segmentj.contains(v) <= 0){
                    iFlag = SegmentFlags.OUTER;
                } else if(segmenti.contains(v) <= 0 && segmentj.contains(v) > 0){
                    jFlag = SegmentFlags.OUTER;
                } else if(segmenti.contains(v) > 0 && segmentj.contains(v) > 0){
                    iFlag = SegmentFlags.SAME;
                    jFlag = SegmentFlags.SAME;
                }
            } else if (iFlag == SegmentFlags.OUTER && jFlag == SegmentFlags.NULL && segmentj.contains(v) > 0){
                jFlag = SegmentFlags.INNER;
            } else if (iFlag == SegmentFlags.NULL && jFlag == SegmentFlags.OUTER && segmenti.contains(v) > 0){
                iFlag = SegmentFlags.INNER;
            }
            //************ IF THEYRE 'SAME' **********
            else if (iFlag == SegmentFlags.SAME && jFlag == SegmentFlags.SAME){
                if(segmenti.contains(v) > 0 && segmentj.contains(v) <= 0){
                    iFlag = SegmentFlags.INNER;
                    jFlag = SegmentFlags.OUTER;
                } else if (segmenti.contains(v) <= 0 && segmentj.contains(v) > 0){
                    iFlag = SegmentFlags.OUTER;
                    jFlag = SegmentFlags.INNER;
                } else if (segmenti.contains(v) > 0 && segmentj.contains(v) > 0){
                    if(moreAttachments(segmenti, i, C) && moreAttachments(segmentj, i, C)){
                        return false;
                    } else return true;
                    
                }
            }
            //*********** REGULAR SCENARIO *************
            else if (iFlag == SegmentFlags.OUTER && jFlag == SegmentFlags.INNER && segmenti.contains(v) > 0){
                if(moreAttachments(segmentj, i, C)){
                    return false;
                } else return true;
            } else if (iFlag == SegmentFlags.INNER && jFlag == SegmentFlags.OUTER && segmentj.contains(v) > 0){
                if(moreAttachments(segmenti, i, C)){
                    return false;
                } else return true;
            }
            
        }
        
        return true;
        
    }
    private boolean moreAttachments(SegmentSet segment, int currentIndex, Stack<Vertex> C){
        
        for(int i = currentIndex + 1; i < C.size(); i++){
            Vertex v = C.get(i);
            if(segment.contains(v) > 0){
                return true;
            }
        }
        
        return false;
        
    }
    
    
    //************************ 3. CHECK IF INTERLACEMENT GRAPH IS BIPARTITE ******************
    private boolean isBipartite(Graph G){
        
        //first, re-colour all the interlacement vertices black.
        for(int i = 0; i<G.numVertices(); i++){
            Vertex v = G.getVertex(i);
            GraphTools.drawInterlacementVertex(v, Color.black);
        }
        
        sleep(3*speed);
        
        VertexSet greenSet = new VertexSet(G);
        VertexSet blueSet = new VertexSet(G);
        Color blue = new Color(50,50,255);
        Color green = new Color(0,200,0);
        
        for(int k = 0; k < G.numVertices(); k++){
            
            Vertex currentVertex = G.getVertex(k);
            if(blueSet.contains(currentVertex) > 0 || greenSet.contains(currentVertex) > 0) continue;
            
            Stack<Vertex> vertexStack = new Stack();
            vertexStack.push(currentVertex);
            greenSet.add(currentVertex);
            GraphTools.drawInterlacementVertex(currentVertex, green);
            sleep(vertexPause);

            while(!vertexStack.isEmpty()){

                currentVertex = vertexStack.pop();

                if(greenSet.contains(currentVertex) > 0){
                    for(int i = 0; i < currentVertex.numAdjVertices() ; i++){

                        Edge e = currentVertex.getIncEdge(i);
                        Vertex v = currentVertex.getAdjVertex(e);

                        if(greenSet.contains(v) > 0){
                            GraphTools.drawEdge(e, Color.red);
                            return false;
                        } else if (blueSet.contains(v) > 0){ // no action to be taken
                        } else {  // not visited yet, so add to opposite colour set
                            blueSet.add(v);
                            GraphTools.drawInterlacementVertex(v, blue);
                            sleep(vertexPause);
                            vertexStack.push(v);
                        }
                    } //for loop
                } else if (blueSet.contains(currentVertex) > 0){
                    for(int i = 0; i < currentVertex.numAdjVertices() ; i++){

                        Edge e = currentVertex.getIncEdge(i);
                        Vertex v = currentVertex.getAdjVertex(e);

                        if(blueSet.contains(v) > 0){
                            GraphTools.drawEdge(e, Color.red);
                            return false;
                        } else if (greenSet.contains(v) > 0){ // no action to be taken
                        } else {  // not visited yet, so add to opposite colour set
                            greenSet.add(v);
                            GraphTools.drawInterlacementVertex(v, green);
                            sleep(vertexPause);
                            vertexStack.push(v);
                        }
                    }
                } else System.out.println("ERROR: Current vertex has not been visited, in interlacement DFS - not possible.");
            } //while loop
        }
        
        GraphTools.drawMessage("Interlacement Graph is bipartite.");
        sleep(speed*3);
        GraphTools.clearMessage();
        return true;
    } // Uses DFS and checks if graph is 2-colourable
    
    
    //********************************** 4. RECURSION ****************************************
    private boolean recursionNeeded(final Graph G, final SegmentSet segment, final VertexSet cycleSet){
        
        int counter = 0;
        
        for(int i = 0; i<G.numVertices(); i++){
            Vertex v = G.getVertex(i);
            if(segment.contains(v) > 0){
                if(cycleSet.contains(v) <= 0 && v.numAdjVertices() > 2){
                    return true; //i.e. the segment cannot be a path or a chord
                                //so there exists a separating cycle. Recursion required.
                }
                counter++;
            }
        }
        
        //checks if segment is a chord
        if(counter < 2){
            System.out.println("ERROR: Segment cannot contain less than 2 vertices");
            System.exit(0);
            return false;
        } else if (counter == 2){
            //segment is a chord
            return false;
        } else {    //else counter > 2, segment must be a path.
            return false;
        }
    }
    private void constructNewGraph(Graph H, VertexSet mapping, final Graph G, final SegmentSet segment, final Stack<Vertex> cycleStack, final VertexSet cycleSet){
        
        copyVertexToGraph(cycleStack.get(0), mapping, H);
        
        for(int i = 0; i < cycleStack.size(); i++){ //Add current cycle to H.
            Vertex v = cycleStack.get(i);
            
            Vertex w = cycleStack.get((i+1)%cycleStack.size());
            
            Vertex v1 = GtoH(v, mapping, H);
            Vertex v2;
            
            if(!(i == cycleStack.size() - 1)){
                v2 = copyVertexToGraph(w, mapping, H);
            } else v2 = GtoH(w, mapping, H);
            
            Edge e = new Edge(v1, v2);
            H.addEdge(e);
            
        }
        
        /*Now add the segment to H. This is done by traversing the edge set of G
        * and solving if the edge is contained in the segment. 
        */
        for(int i = 0; i < G.numEdges(); i++){
            
            Edge e = G.getEdge(i);
            Vertex v1 = e.getV1();
            Vertex v2 = e.getV2();
            
            if(segment.contains(v1) <= 0 || segment.contains(v2) <= 0 ||        //if e is not contained in this segment
               cycleSet.contains(v1) > 0 && cycleSet.contains(v2) > 0)          //or if e is a cycle edge/chord
                continue;                                                       //then skip iteration
            
            //if v1 or v2 haven't already been added to H, do so now
            Vertex v1h = GtoH(v1, mapping, H);
            Vertex v2h = GtoH(v2, mapping, H);
            
            if(v1h == null){
                v1h = copyVertexToGraph(v1, mapping, H);
            }
            if(v2h == null){
                v2h = copyVertexToGraph(v2, mapping, H);
            }
            H.addEdge(new Edge(v1h, v2h));
            
        }
        
    }
    private int constructNewCycle(Stack<Vertex> newCycle, final Graph H, final Graph G, final SegmentSet segment, final Stack<Vertex> cycleStack, final VertexSet cycleSet){
        
        GraphTools.drawMessage("Finding new cycle");
        
        Vertex startV = null;
        
        //find a vertex to start construction of path gamma (see algorithm notes)
        for(int i = 0; i < cycleStack.size(); i++){
            Vertex v = cycleStack.get(i);
            if(cycleSet.contains(v) > 0 && segment.contains(v) > 0){
                startV = v;
                break;
            }
        }
        
        if(startV == null){
            GraphTools.drawMessage("ERROR: Path gamma could not be initialized");
            sleep(10000);
            System.exit(0);
        }
        
        Stack<Vertex> gammaStack = new Stack();
        VertexSet visitedSet = new VertexSet(G);
        visitedSet.add(startV);
        gammaStack.push(startV);
        GraphTools.drawVertex(startV,Color.green);
        gammaDFS(startV, startV, visitedSet, gammaStack, cycleSet, segment, cycleStack);
        GraphTools.drawGraph(H);
        colourStack(gammaStack, Color.green);
        colourCycle(cycleStack, cycleColour);
        
        
        Vertex v = gammaStack.get(0);
        Vertex w = gammaStack.peek();
        
        int i = cycleStack.indexOf(v);
        
        GraphTools.drawVertex(v,Color.red);
        GraphTools.drawEdge(new Edge(v,new Vertex(cycleStack.get((i+1)%cycleStack.size()))),Color.red);
        sleep(edgePause);
        GraphTools.drawVertex(new Vertex(cycleStack.get((i+1)%cycleStack.size())), Color.red);
        sleep(vertexPause);
                
        for(int j =(i+1)%cycleStack.size();; j = (j+1)%cycleStack.size()){
            Vertex u = cycleStack.get(j);
            Vertex z = cycleStack.get((j+1)%cycleStack.size());

            GraphTools.drawEdge(new Edge(u,z), Color.red);
            sleep(edgePause);


            if(u == w){ //Current path of traveral can be alpha (see algorithm notes)
                sleep(speed*2);
                newCycle.clear();
                GraphTools.clearBuffers();
                GraphTools.drawGraph(H);
                colourCycle(cycleStack, cycleColour);
                colourStack(gammaStack,Color.green);
                GraphTools.drawEdge(new Edge(u,z), Color.red);

                for(int k = (j+1)%cycleStack.size(); cycleStack.get(k) != v; k=(k+1)%cycleStack.size()){
                    GraphTools.drawVertex(cycleStack.get(k), Color.red);
                    sleep(vertexPause);
                    newCycle.push(cycleStack.get(k));
                    GraphTools.drawEdge(new Edge(cycleStack.get(k), cycleStack.get((k+1)%cycleStack.size())), Color.red);
                    sleep(edgePause);
                }

                for(int k = 0; k < gammaStack.size(); k++){
                    Vertex gammaVertex = gammaStack.get(k);
                    newCycle.push(gammaVertex);
                }
                return 0;
                
            }



            if(segment.contains(u) > 0){ //current path of traversal can be beta (see algorithm notes)

                GraphTools.drawVertex(u, Color.magenta);
                sleep(vertexPause);

                //Finish traversing beta until we reach w (the other 'end' of gamma)
                for(int k = j; cycleStack.get(k) != w; k++){
                    GraphTools.drawVertex(cycleStack.get(k), Color.red);
                    sleep(vertexPause);

                    newCycle.push(cycleStack.get(k));
                    GraphTools.drawEdge(new Edge(cycleStack.get(k), cycleStack.get((k+1)%cycleStack.size())), Color.red);
                    sleep(edgePause);

                }

                for(int k = gammaStack.size()-1; k >= 0; k--){
                    Vertex gammaVertex = gammaStack.get(k);
                    newCycle.push(gammaVertex);
                }
                return 0;

            }
            newCycle.push(u);
            GraphTools.drawVertex(z, Color.red);
            sleep(vertexPause);
        }
                
        
        
    } 
    private int gammaDFS(Vertex root, Vertex currentVertex, VertexSet visitedSet, Stack<Vertex> gammaStack, VertexSet cycleSet, SegmentSet segment, Stack<Vertex> cycleStack){
        
        for(int i=0;i<currentVertex.numAdjVertices();i++){
            
            Edge e = currentVertex.getIncEdge(i);
            Vertex u=currentVertex.getAdjVertex(e);
            
            if(cycleSet.contains(currentVertex) > 0 && cycleSet.contains(u) > 0)continue;
            if(gammaStack.size() > 1 && u == gammaStack.get(gammaStack.size() - 2)) continue;
            
            if(segment.contains(u) > 0 && visitedSet.contains(u)<=0){
                
                if(cycleSet.contains(u) <= 0){
                    visitedSet.add(u);
                    GraphTools.drawEdge(e, Color.green);
                    sleep(edgePause);
                    GraphTools.drawVertex(u, Color.green);
                    sleep(vertexPause);
                    visitedSet.add(u);
                    gammaStack.push(u);
                    if(gammaDFS(root, u, visitedSet, gammaStack, cycleSet, segment, cycleStack) == 1){
                        return 1;
                    }
                } else if (cycleSet.contains(u) > 0){
                    
                    if(adjacentAttachment(root, u, cycleStack, segment)){
                        gammaStack.push(u);
                        GraphTools.drawEdge(e, Color.ORANGE);
                        sleep(3*speed);
                        colourStack(gammaStack, Color.green);
                        return 1;
                    }
                }
                
                
                
                

            }

        }
        if(gammaStack.isEmpty()){
            System.out.println("error: attempted to pop an empty stack.");
        } else {
            gammaStack.pop();
        }
        
        return 0;  //no cycle found yet
        
    }
    private Vertex GtoH(Vertex v, VertexSet mapping, Graph H){
        if(mapping.contains(v) > 0){
            return H.getVertex(mapping.contains(v)-1);
        } else return null;
    }
    private Vertex copyVertexToGraph(final Vertex v, VertexSet mapping, Graph H){
        Vertex w = new Vertex(v);
        H.addVertex(w);
        if (mapping.map(v, w) < 0){
            System.exit(0);
        }
        return w;
        
    }
    private boolean adjacentAttachment(Vertex currentVertex, Vertex v, Stack<Vertex> cycleStack, VertexSet segment){
        
        Stack<Vertex> attachmentStack = new Stack();
        
        for(Vertex w : cycleStack){
            if(segment.contains(w) > 0){
                attachmentStack.push(w);
            }
        }
        int v1 = attachmentStack.indexOf(v);
        int v2 = attachmentStack.indexOf(currentVertex);
        
        if(v1 < 0){
            System.out.println("ERROR in adjacentAttachment");
        }
        if(v2 < 0){
            System.out.println("ERROR in adjacentAttachment");
        }

        if( (v1 == v2 + 1) || (v1 == v2 - 1)             ||
            (v1 == 0 && v2 == attachmentStack.size() -1) ||
            (v2 == 0 && v1 == attachmentStack.size() - 1) ){
            return true;
        } else return false;
    }

    
    //helper functions/tools
    private void colourCycle(Stack<Vertex> cycleStack, Color cycleCol){
                
        for(int i = 0; i<cycleStack.size() ; i++){
            Vertex v = cycleStack.get(i);
            Vertex w = cycleStack.get((i+1)%cycleStack.size());
            
            GraphTools.drawVertex(v,cycleCol);
            GraphTools.drawEdge(new Edge(v,w),cycleCol);
        }
    }
    private void colourStack(Stack<Vertex> stack, Color col){
        for(int i = 0; i<stack.size() - 1 ; i++){
                Vertex v = stack.get(i);
                Vertex w = stack.get(i+1);

                GraphTools.drawVertex(v,col);
                GraphTools.drawEdge(new Edge(v,w),col);
        }
        
        GraphTools.drawVertex(stack.peek(), col);
    }
    private boolean isChord(Edge e, VertexSet cycleSet, Stack<Vertex> C){
        
        if(cycleSet.contains(e.getV1()) > 0 && cycleSet.contains(e.getV2()) > 0){
            int v1 = C.indexOf(e.getV1());
            int v2 = C.indexOf(e.getV2());
            
            if( (v1 == v2 + 1) || (v1 == v2 - 1) || (v1 == 0 && v2 == C.size() -1) || (v2 == 0 && v1 == C.size() - 1) ){ //check if vertices are next to each other in the stack (or both at top/bottom of stack)
                return false;  //i.e. the edge is a part of the cycle.
            } else return true;//i.e. the edge contains two vertices in C, but which are not adjacent to each other.
            
        } else return false;


    }  //is edge e a chord of cycle C?
    enum SegmentFlags {
        NULL, INNER, OUTER, SAME
    }

}
