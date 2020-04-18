import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class EdmondsKarp {

    public static void main(String[] args) throws FileNotFoundException {
        String filename = "demands1.txt";
        System.out.println(filename);
        Scanner scan = new Scanner(new File("data/" + filename));

        int nodes = scan.nextInt();
        int edges = 0;
        int source = 0;
        int sink = nodes - 1;

        Node[] graph = new Node[nodes];

        // initialize nodes
        for (int i = 0; i < nodes; i++)
            graph[i] = new Node();

        // read in edges from file
        while (scan.hasNextInt()) {
            edges++;
            int start = scan.nextInt();
            int end = scan.nextInt();
            int capacity = scan.nextInt();

            Edge a = new Edge(start , end , 0 , capacity);
            Edge b = new Edge(end , start , 0 , 0);


            graph[start].edges.add(a);
            graph[end].edges.add(b);
        }
        scan.close();

        // sum of flow for all paths
        int totalFlow = 0;

        while (true) {

            Edge[] parent = new Edge[nodes];

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

            // If no path were found (min cut)
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

    }
}


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
