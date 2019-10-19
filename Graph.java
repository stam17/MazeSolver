public class Graph {

    int numNodes;
    Node[] nodes;

    public void addEdge(int i, int j) {
        if (i < numNodes && j < numNodes) {
            nodes[i].neighbors.add(nodes[j]);
            nodes[j].neighbors.add(nodes[i]);
        }
    }

    public boolean edgeExists(int i, int j) {
        if (nodes[i].neighbors != null) {
            return nodes[i].neighbors.contains(nodes[j]);
        }
        return false;
    }

    public void printNeighbors() {
        for (Node n : nodes) {
            System.out.print(n.index + ": ");
            for (Node nb : n.neighbors) {
                System.out.print(nb.index + " ");
            }
            System.out.println();
        }
    }

    public Graph(int num) {
        numNodes = num;
        nodes = new Node[numNodes];
        for(int i = 0; i < numNodes; i++) {
            nodes[i] = new Node(i);
        }
        // int size = (int) Math.sqrt(numNodes);
        // System.out.println("numNodes: " + numNodes);
        // System.out.println("size: " + size);

    }

}