import java.util.ArrayList;
import java.util.Stack;

public class DLS implements SearchAlgorithm {

	private int limit;

	Stack<Node> generatedNodes;

	public DLS(int limit) {
		this.limit = limit;
	}

	@Override
	public void makeQueue(Node initialState) {
		generatedNodes = new Stack<>();
		generatedNodes.push(initialState);
	}

	@Override
	public void enqueue(ArrayList<Node> nodes) {
		for (Node node : nodes) {
			if (node.getDepth() <= limit)
				generatedNodes.push(node);
		}
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
