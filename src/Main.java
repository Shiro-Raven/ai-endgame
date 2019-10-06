import java.util.*;

public class Main {

	static int n, m, ix, iy, tx, ty;
	static Point[] warriors;
	static Point[] stones;
	static TreeMap<Point, Integer> warriorIdx;
	static TreeMap<Point, Integer> stoneIdx;

	public static String solve(String grid, String strategy, boolean visualize) {
		Endgame problemInstance = parser(grid);
		
		// TODO: call resetVisitedNodes
		return null;
	}

	static Endgame parser(String grid) {
		String[] variables = grid.split(";");
		StringTokenizer st = new StringTokenizer(variables[0] + "," + variables[1] + "," + variables[2], ",");
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		ix = Integer.parseInt(st.nextToken());
		iy = Integer.parseInt(st.nextToken());
		tx = Integer.parseInt(st.nextToken());
		ty = Integer.parseInt(st.nextToken());
		stones = stonesAndWarriors(variables[3],
				stoneIdx = new TreeMap<>((a, b) -> (a.x != b.x ? a.x - b.x : a.y - b.y)));
		warriors = stonesAndWarriors(variables[4],
				warriorIdx = new TreeMap<>((a, b) -> (a.x != b.x ? a.x - b.x : a.y - b.y)));
		
		return new Endgame(n,m,ix,iy,tx,ty,stones,warriors);
	}

	static Point[] stonesAndWarriors(String representation, TreeMap<Point, Integer> ptsToIdx) {
		StringTokenizer st = new StringTokenizer(representation, ",");
		Point[] pts = new Point[st.countTokens()];
		for (int i = 0; i < pts.length; i++) {
			int x = Integer.parseInt(st.nextToken()), y = Integer.parseInt(st.nextToken());
			pts[i] = new Point(x, y);
			ptsToIdx.put(new Point(x, y), i);
		}
		return pts;
	}

	/*
	 * performGeneralSearch: The general search procedure.
	 */
	public static Node performGeneralSearch(GenericSearchProblem problem, SearchAlgorithm algorithm) {

		algorithm.makeQueue(new Node(problem.initialState, null, 0, null));

		while (true) {

			if (algorithm.isQueueEmpty())
				return null;

			Node currentNode = algorithm.dequeue();

			if (problem.isGoalState(currentNode.getState()))
				return currentNode;

			algorithm.enqueue(problem.expand(currentNode));
		}

	}

	/*
	 * Depth-limited search: returns a node The node's operator is the cutoffFlag if
	 * the cutoff was reached. The node is null if failure occurred. Otherwise the
	 * node is the goal node. The cutoffFlag static string is used to mark that a
	 * cutoff occurred.
	 */
	static final String cutoffFlag = "###$$$@@@";

	protected static Node performDLS(GenericSearchProblem problem, long limit) {
		return recursiveDLS(new Node(problem.initialState, null, 0, null), problem, limit);
	}

	private static Node recursiveDLS(Node node, GenericSearchProblem problem, long limit) {

		if (problem.isGoalState(node.getState()))
			return node;
		else {
			if (limit == 0)
				return new Node(null, null, 0, cutoffFlag);
			else {
				boolean cutoffOccurred = false;

				for (Node childNode : problem.expand(node)) {
					Node result = recursiveDLS(childNode, problem, limit - 1);
					if (result.getOperator() == cutoffFlag)
						cutoffOccurred = true; // cut-off detected
					else {
						if (result != null)
							return result; // goal reached
					}
				}

				if (cutoffOccurred)
					return new Node(null, null, 0, cutoffFlag); // cut-off
				else
					return null; // failure
			}
		}

	}

	/*
	 * Iterative Deepening Search: Starts at a limit of 0 and continues looping till
	 * infinity. It performs DLS and returns the result if the cutoffFlag is not
	 * detected.
	 */
	protected static Node performIDS(GenericSearchProblem problem) {

		long limit = 0;

		while (true) {

			Node result = performDLS(problem, limit);

			if (result.getOperator() != cutoffFlag)
				return result;

			limit++;
		}
	}

}
