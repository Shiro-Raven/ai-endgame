import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS implements SearchAlgorithm {
	
	Queue<Node> frontier;

	@Override
	public void makeQueue(Node initialState) {
		frontier = new LinkedList<Node>();
		frontier.add(initialState);
	}

	@Override
	public void enqueue(ArrayList<Node> nodes) {
		frontier.addAll(nodes);
	}

	@Override
	public Node dequeue() {
		return frontier.remove();
	}

	@Override
	public boolean isQueueEmpty() {
		return frontier.isEmpty();
	}

}
