import java.util.ArrayList;
import java.util.PriorityQueue;

public class Greedy implements SearchAlgorithm {

	private int heuristic;
	PriorityQueue<Node> generatedNodes;
	GenericSearchProblem problemInstance;

	public Greedy(int heuristic, GenericSearchProblem problem) {
		if (heuristic == 1 || heuristic == 2) {
			this.heuristic = heuristic;
		} else {
			System.err.println("Invalid heuristic");
		}
		this.problemInstance = problem;
	}

	@Override
	public void makeQueue(Node initialState) {
		generatedNodes = new PriorityQueue<>((nodeA, nodeB) -> problemInstance.evaluateHeuristic(nodeA, heuristic) - problemInstance.evaluateHeuristic(nodeB, heuristic));
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
