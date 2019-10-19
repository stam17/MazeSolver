/*-----------------------------------------------------------------------------------------
ANSWER THE QUESTIONS FROM THE DOCUMENT HERE

(1) Which graph representation did you choose, and why?
I chose the adjacency list because it is more space efficient. If we were to use an adjacency
matrix, then nearly all the values stored for the neighbors of each cell would be 0. Instead,
the adjacency stores only the 1 or 4 neighbors each cell has.

(2) Which search algorithm did you choose, and why?
I chose depth-first search. In order to solve the maze, we only need to explore paths until
the end is found. However, breadth-first search requires exploring every possible path at each
depth before moving further; whereas, DFS only continues exploring each path fully until a path
to the end is found, which may be more time efficient.

  ----------------------------------------------------------------------------------------*/

import java.io.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;

public class MazeSolver {

    ArrayList<Node> solution;

    public void run(String filename) throws IOException {

        // read the input file to extract relevant information about the maze
        String[] readFile = parse(filename);
        int mazeSize = Integer.parseInt(readFile[0]);
        int numNodes = mazeSize*mazeSize;
        String mazeData = readFile[1];

        // construct a maze based on the information read from the file
        Graph mazeGraph = buildGraph(mazeData, numNodes);

        // SOLVE THE MAZE
        Node[] soln = solve(mazeGraph);

        // print cells in the solution
        System.out.print("Solution: ");
        for (int i = 0; i < soln.length; i++) {
            System.out.print(soln[i].index + " ");
        }
        System.out.println();

        // print out the final maze with the solution path
        printMaze(mazeGraph.nodes, mazeData, mazeSize);
    }
    public Node[] solve(Graph mazeGraph) {
        // create initial ArrayList containing the 1st node to pass into DFS
        ArrayList<Node> arr = new ArrayList<Node>();
        arr.add(mazeGraph.nodes[0]);

        DFS(mazeGraph, 0, arr);


        Node[] toReturn = new Node[solution.size()];
        int count = 0;

        for (Node n : solution) {
            n.inSolution = true;
            toReturn[count] = n;
            count++;
        }

        return toReturn;
    }
    public void DFS(Graph mazeGraph, int v, ArrayList<Node> path) {
        // Mark the current node (with index v) as visited
        mazeGraph.nodes[v].visited = true;
        // Base case: reaching the last node
        // Copy nodes in path into class variable solution
        if (v == mazeGraph.nodes.length - 1) {
            solution = path;
        }
        // Explore each of the neighbors of current node
        Iterator<Node> i = mazeGraph.nodes[v].neighbors.listIterator();
        while (i.hasNext()) {
            Node n = i.next();
            if (!n.visited) {
                ArrayList<Node> newList = new ArrayList<Node>();
                for (Node elt : path) {
                    newList.add(elt);
                }
                newList.add(n);
                DFS(mazeGraph, n.index, newList);
            }
        }
    }

    // prints out the maze in the format used for HW8
    // includes the final path from entrance to exit, if one has been recorded,
    // and which cells have been visited, if this has been recorded
    public void printMaze(Node[] mazeCells, String mazeData, int mazeSize) {

        int ind = 0;
        int inputCtr = 0;

        System.out.print("+");
        for(int i = 0; i < mazeSize; i++) {
            System.out.print("--+");
        }
        System.out.println();

        for(int i = 0; i < mazeSize; i++) {
            if(i == 0) System.out.print(" ");
            else System.out.print("|");

            for(int j = 0; j < mazeSize; j++) {
                System.out.print(mazeCells[ind] + "" + mazeCells[ind] +  mazeData.charAt(inputCtr));
                inputCtr++;
                ind++;
            }
            System.out.println();

            System.out.print("+");
            for(int j = 0; j < mazeSize; j++) {
                System.out.print(mazeData.charAt(inputCtr) + "" +  mazeData.charAt(inputCtr) + "+");
                inputCtr++;
            }
            System.out.println();
        }

    }

    // reads in a maze from an appropriately formatted file (this matches the format of the 
    // mazes you generated in HW8)
    // returns an array of Strings, where position 0 stores the size of the maze grid (i.e., the
    // length/width of the grid) and position 1 minimal information about which walls exist
    public String[] parse(String filename) throws IOException {
        FileReader fr = new FileReader(filename);

        // determine size of maze
        int size = 0;
        int nextChar = fr.read();
        while(nextChar >= 48 && nextChar <= 57) {
            size = 10*size + nextChar - 48;
            nextChar = fr.read();
        }

        String[] result = new String[2];
        result[0] = size + "";
        result[1] = "";


        // skip over up walls on first row
        for(int j = 0; j < size; j++) {
            fr.read();
            fr.read();
            fr.read();
        }
        fr.read();
        fr.read();

        for(int i = 0; i < size; i++) {
            // skip over left wall on each row
            fr.read();

            for(int j = 0; j < size; j++) {
                // skip over two spaces for the cell
                fr.read();
                fr.read();

                // read wall character
                nextChar = fr.read();
                result[1] = result[1] + (char)nextChar;

            }
            // clear newline character at the end of the row
            fr.read();

            // read down walls on next row of input file
            for(int j = 0; j < size; j++)  {
                // skip over corner
                fr.read();

                //skip over next space, then handle wall
                fr.read();
                nextChar = fr.read();
                result[1] = result[1] + (char)nextChar;
            }

            // clear last wall and newline character at the end of the row
            fr.read();
            fr.read();

        }

        return result;
    }

    public Graph buildGraph(String maze, int numNodes) {

        Graph mazeGraph = new Graph(numNodes);
        int size = (int)Math.sqrt(numNodes);

        int mazeInd = 0;
        for(int i = 0; i < size; i++) {
            // create edges for right walls in row i
            for(int j = 0; j < size; j++) {
                char nextChar = maze.charAt(mazeInd);
                mazeInd++;
                if(nextChar == ' ') {
                    // add an edge corresponding to a right wall, using the indexing convention
                    // for nodes
                    mazeGraph.addEdge(size*i + j, size*i + j + 1);
                }
            }

            // create edges for down walls below row i
            for(int j = 0; j < size; j++)  {
                char nextChar = maze.charAt(mazeInd);
                mazeInd++;
                if(nextChar == ' ') {
                    // add an edge corresponding to a down wall, using the indexing convention
                    // for nodes
                    mazeGraph.addEdge(size*i + j, size*(i+1) + j);
                }
            }
        }

        return mazeGraph;
    }

    public static void main(String [] args) {
        if(args.length < 1) {
            System.out.println("USAGE: java MazeSolver <filename>");
        }
        else{
            try{
                new MazeSolver().run(args[0]);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
