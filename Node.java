import java.util.LinkedList;

public class Node {

    int index;
    LinkedList<Node> neighbors;

    boolean visited = false;
    boolean inSolution = false;

    static final String PATH = "X";
    static final String VISIT = ".";
    static final String NOT_VISIT = " ";

    public String toString() {
        if(visited) {
            if(inSolution) return PATH;
            else return VISIT;
        }
        else return NOT_VISIT;
    }

    public Node(int i) {
        index = i;
        neighbors = new LinkedList<Node>();
    }
}