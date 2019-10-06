import java.util.ArrayList;
import java.util.PriorityQueue;

public class UCS implements SearchAlgorithm {

	PriorityQueue<Node> generatedNodes;

	@Override
	public void makeQueue(Node initialState) {
		generatedNodes = new PriorityQueue<>((nodeA, nodeB) -> nodeA.getPathCost() - nodeB.getPathCost());
		generatedNodes.add(initialState);
	}

	@Override
	public void enqueue(ArrayList<Node> nodes) {
		generatedNodes.addAll(nodes);
	}

	@Override
	public Node dequeue() {
		return generatedNodes.poll();
	}

	@Override
	public boolean isQueueEmpty() {
		return generatedNodes.isEmpty();
	}

}
