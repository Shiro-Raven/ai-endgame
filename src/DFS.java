import java.util.ArrayList;
import java.util.Stack;

public class DFS implements SearchAlgorithm {
	
	Stack<Node> generatedNodes;

	@Override
	public void makeQueue(Node initialState) {
		generatedNodes = new Stack<>();
		generatedNodes.add(initialState);
	}

	@Override
	public void enqueue(ArrayList<Node> nodes) {
		generatedNodes.addAll(nodes);
	}

	@Override
	public Node dequeue() {
		return generatedNodes.pop();
	}

	@Override
	public boolean isQueueEmpty() {
		return generatedNodes.isEmpty();
	}

}
