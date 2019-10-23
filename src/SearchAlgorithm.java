import java.util.ArrayList;

public interface SearchAlgorithm {

    void makeQueue(Node initialState);

    void enqueue(ArrayList<Node> nodes);

    Node dequeue();
    
    boolean isQueueEmpty();
    
    void printQueue();

}
