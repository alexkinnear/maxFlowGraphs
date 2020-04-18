import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class Graph {
    int vertexCt;  // Number of vertices in the graph.
    int edgeCount;
    GraphNode[] G;  // Adjacency list for graph.
    Integer[][] capacity;

    String graphName;  //The file from which the graph was created.

    public Graph() {
        this.vertexCt = 0;
        this.edgeCount = 0;
        this.graphName = "";
    }

    public static void main(String[] args) {
        Graph graph1 = new Graph();
        graph1.makeGraph("demands1.txt");
//        System.out.println(graph1.toString());
        System.out.println(graph1.maxFlow(0, 5));


    }

    public int getVertexCt() {
        return vertexCt;
    }

    public boolean addEdge(int source, int destination, int cap) {
        //System.out.println("addEdge " + source + "->" + destination + "(" + cap + ")");
        if (source < 0 || source >= vertexCt) return false;
        if (destination < 0 || destination >= vertexCt) return false;

        //add edge
        G[source].addEdge(source, destination, cap);
        capacity[source][destination] = cap;

        edgeCount++;
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The Graph " + graphName + " \n");

        for (int i = 0; i < vertexCt; i++) {
            sb.append(G[i].toString());
        }
        return sb.toString();
    }

    public void makeGraph(String filename) {

        try {
            graphName = filename;
            Scanner reader = new Scanner(new File("data/"+filename));
            vertexCt = reader.nextInt();

            // initialize the arrays of length vertexCt
            G = new GraphNode[vertexCt];
            capacity = new Integer[vertexCt][vertexCt];




            // fill arrays
            for (int i = 0; i < vertexCt; i++) {
                G[i] = new GraphNode(i);
                for (int j = 0; j < vertexCt; j++) {
                    capacity[i][j] = 0;
                }
            }

            while (reader.hasNextInt()) {
                int v1 = reader.nextInt();
                int v2 = reader.nextInt();
                int cap = reader.nextInt();
                if (!addEdge(v1, v2, cap))
                    throw new Exception();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int maxFlow( int start, int end) {
        int flow = Integer.MAX_VALUE;
        GraphNode.EdgeInfo[] parent = new GraphNode.EdgeInfo[vertexCt];
        // breadth first search
        do {
            MyQueue<Integer> nodes = new MyQueue<>();
            nodes.enqueue(start);

            while (!nodes.isEmpty()) {
                int current = nodes.dequeue();
                for (GraphNode.EdgeInfo edge : G[current].succ) {
                    if (parent[edge.to] == null && edge.to != start && edge.capacity > 0) {
                        parent[edge.to] = edge;
                        nodes.enqueue(edge.to);
                    }
                }
            }


            int temp = capacity[parent[end].from][parent[end].to];
            for (int i = end; i != start; i = parent[i].from)
                temp = Math.min(temp, (capacity[parent[i].from][i]));

            for (int i = end; i != start; i = parent[i].from) {
                flow += temp;
            }
            end = parent[end].from;

        }
        while (parent[end] != null);

        return flow;
    }
}
