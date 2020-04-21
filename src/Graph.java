import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


class Node {
    ArrayList<Edge> edges = new ArrayList<>();
}

class Edge {

    int start;
    int end;
    int flow;
    int capacity;


    public Edge(int start, int end, int flow, int capacity) {
        this.start = start;
        this.end = end;
        this.flow = flow;
        this.capacity = capacity;
    }

}



public class Graph {

    private void makeGraph(String filename) throws FileNotFoundException {
        System.out.println(filename);
        Scanner scan = new Scanner(new File("data/" + filename));

        int vertexCt = scan.nextInt();
        int source = 0;
        int sink = vertexCt - 1;
        

        Node[] graph = new Node[vertexCt];

        // initialize vertexCt
        for (int i = 0; i < vertexCt; i++)
            graph[i] = new Node();

        // read in edges from file
        while (scan.hasNextInt()) {
            int start = scan.nextInt();
            int end = scan.nextInt();
            int capacity = scan.nextInt();

            Edge a = new Edge(start , end , 0 , capacity);
            Edge b = new Edge(end , start , 0 , 0);


            graph[start].edges.add(a);
            graph[end].edges.add(b);
        }
        scan.close();

        EdmondsKarp(graph, vertexCt, source, sink);
    }
    


    private void EdmondsKarp(Node[] graph,int vertexCt, int source, int sink) {
        // sum of flow for all paths
        int totalFlow = 0;

        ArrayList<Edge> edgeList = new ArrayList<>();

        System.out.println("MAX FLOW:");
        while (true) {

            Edge[] parent = new Edge[vertexCt];

            ArrayList<Node> q = new ArrayList<>();
            q.add(graph[source]);

            // breadth first search
            while (!q.isEmpty()) {
                Node current = q.remove(0);

                for (Edge edge : current.edges)
                    if (parent[edge.end] == null && edge.end != source && edge.capacity > edge.flow) {
                        parent[edge.end] = edge;
                        q.add(graph[edge.end]);
                    }
            }

            
            if (parent[sink] == null)
                break;

            // flow for each path
            int flow = 1000;

            for (Edge edge = parent[sink]; edge != null; edge = parent[edge.start]) {
                flow = Math.min(flow, edge.capacity - edge.flow);
            }

            ArrayList<Integer> path = new ArrayList<>();
            // Update flow value for current path
            for (Edge edge = parent[sink]; edge != null; edge = parent[edge.start]) {
                path.add(edge.end);

                if (!edgeList.contains(edge)) {
                    edgeList.add(edge);
                }

                edge.flow += flow;

            }
            path.add(0);

            // print out the path and flow from each path
            System.out.print("Found flow " + flow + ": ");
            for (int i = path.size() - 1; i >= 0; i--) {
                System.out.print(path.get(i) + " ");
            }
            System.out.println();

            totalFlow += flow;
        }

        // print out total flow of graph
        System.out.println("Produced " + totalFlow);


        System.out.println();
        for (Edge edge : edgeList) {
            System.out.print("Edge (" + edge.start + ", " + edge.end + ") transports " + edge.flow + " cases" + "\n");
        }
        System.out.println();

        minCut(edgeList, vertexCt, source, sink);
    }
    
    private void minCut(ArrayList<Edge> edges, int vertexCt, int source, int sink) {
        int startFlow = 0;
        ArrayList<Edge> startEdges = new ArrayList<>();
        int endFlow = 0;
        ArrayList<Edge> endEdges = new ArrayList<>();
        System.out.println("MIN CUT:");
        for (Edge edge : edges) {
            if (edge.start == source) {
                startEdges.add(edge);
                startFlow += edge.flow;
            }
            else if (edge.end == sink) {
                endEdges.add(edge);
                endFlow += edge.flow;
            }
        }


        if (endFlow < startFlow) {
            for (Edge edge : endEdges)
                System.out.print("Edge (" + edge.start + "," + edge.end + ") transports " + edge.flow + " cases" + "\n");
        }
        else
            for (Edge edge : startEdges)
                System.out.print("Edge (" + edge.start + "," + edge.end + ") transports " + edge.flow + " cases" + "\n");
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = "demands5.txt";
        Graph graph1 = new Graph();
        graph1.makeGraph(filename);
        
    }
}

