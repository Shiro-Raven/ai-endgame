import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar implements SearchAlgorithm {

	private int heuristic;
	PriorityQueue<Node> generatedNodes;
	private GenericSearchProblem problemInstance;

	public AStar(int heuristic, GenericSearchProblem problem) {
		if (heuristic == 1 || heuristic == 2) {
			this.heuristic = heuristic;
		} else {
			System.err.println("Invalid heuristic");
		}
		this.problemInstance = problem;
	}

	@Override
	public void makeQueue(Node initialState) {
		generatedNodes = new PriorityQueue<>((nodeA, nodeB) -> (problemInstance.evaluateHeuristic(nodeA, heuristic) + nodeA.getPathCost()) - (problemInstance.evaluateHeuristic(nodeB, heuristic) + nodeB.getPathCost()));
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

	@Override
	public void printQueue() {
		System.out.println(generatedNodes);
	}

}
