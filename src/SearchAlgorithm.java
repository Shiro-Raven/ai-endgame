public interface SearchAlgorithm {

    void makeQueue(Node initialState);

    void enqueue(ArrayList<Node>);

    Node dequeue();

}
