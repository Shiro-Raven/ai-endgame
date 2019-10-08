import java.util.*;

public class Main {

	public static String solve(String grid, String strategy, boolean visualize) {
		Endgame problemInstance = parser(grid);
		
		// TODO: call resetVisitedNodes
		return null;
	}

	static Endgame parser(String grid) {
		int n, m, ix, iy, tx, ty;
		Point[] warriors;
		Point[] stones;
		String[] variables = grid.split(";");
		StringTokenizer st = new StringTokenizer(variables[0] + "," + variables[1] + "," + variables[2], ",");
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		ix = Integer.parseInt(st.nextToken());
		iy = Integer.parseInt(st.nextToken());
		tx = Integer.parseInt(st.nextToken());
		ty = Integer.parseInt(st.nextToken());
		stones = stonesAndWarriors(variables[3],
				new TreeMap<>((a, b) -> (a.x != b.x ? a.x - b.x : a.y - b.y)));
		warriors = stonesAndWarriors(variables[4],
				new TreeMap<>((a, b) -> (a.x != b.x ? a.x - b.x : a.y - b.y)));
		
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
	 * Iterative Deepening Search: Starts at a limit of 0 and continues looping till
	 * infinity. It performs DLS and returns the result if the failure is not
	 * detected.
	 */
	protected static Node performIDS(GenericSearchProblem problem) {

		int limit = 0;

		while (true) {
			
			DLS depthLimitedSearch = new DLS(limit);
			
			Node result = performGeneralSearch(problem, depthLimitedSearch);

			if (result != null)
				return result;

			limit++;
		}
	}

}
