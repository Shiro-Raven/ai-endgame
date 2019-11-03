import java.util.*;

public class Main {

	public static String solve(String grid, String strategy, boolean visualize) {
		Endgame problemInstance = parser(grid);

		SearchAlgorithm algorithm = null;
		int heuristic;
		
		switch (strategy) {
		case "BF":
			algorithm = new BFS();
			break;
		case "DF":
			algorithm = new DFS();
			break;
		case "ID":
			break;
		case "UC":
			algorithm = new UCS();
			break;
		case "GR1":
		case "GR2":
			heuristic = Character.getNumericValue(strategy.charAt(strategy.length() - 1));
			algorithm = new Greedy(heuristic, problemInstance);
			break;
		case "AS1":
		case "AS2":
			heuristic = Character.getNumericValue(strategy.charAt(strategy.length() - 1));
			algorithm = new AStar(heuristic, problemInstance);
			break;
		default:
			System.err.println("Undefined Algorithm!");
			return null;
		}

		problemInstance.resetVisitedStates(); 
		
		Node finalNode;
		if (!strategy.equals("ID")) {
			finalNode = performGeneralSearch(problemInstance, algorithm);
		} else {
			finalNode = performIDS(problemInstance);
		}		

		if(visualize && finalNode != null)
			System.out.println(visualizeSolution(finalNode, problemInstance));
		
		// construct the solution
		return constructSolution(finalNode, problemInstance);
		
	}
	
	private static String constructSolution(Node finalNode, Endgame problemInstance) {

		if (finalNode == null)
			return "There is no solution.";

		StringBuilder build = new StringBuilder();
		Stack<Node> st = Endgame.getFullSolution(finalNode);
		
		// add the actions to the plan
		while (!st.isEmpty()) {
			String currentOperator = st.pop().getOperator();
			
			if (currentOperator != null) {
				build.append(currentOperator);
				build.append(',');
			}
		}
		// add snap to the plan
		build.append("snap;");
		
		// add the cost of the plan
		build.append((finalNode.getPathCost() + ";"));

		// add the number of expanded nodes
		build.append(problemInstance.expandedNodesCounter);

		return build.toString();
	}
	
	public static String visualizeSolution(Node goalNode, Endgame problemInstance) {
		Stack<Node> st = Endgame.getFullSolution(goalNode);
		StringBuilder visualization = new StringBuilder();
		while(!st.isEmpty()) {
			visualization.append(problemInstance.visualizeState(st.pop())+"\n");
		}
		return visualization.toString();
		
	}

	static Endgame parser(String grid) {
		int n, m, ix, iy, tx, ty;
		TreeMap<Point, Integer> warriorsIdx;
		TreeMap<Point, Integer> stonesIdx;
		String[] variables = grid.split(";");
		StringTokenizer st = new StringTokenizer(variables[0] + "," + variables[1] + "," + variables[2], ",");
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		ix = Integer.parseInt(st.nextToken());
		iy = Integer.parseInt(st.nextToken());
		tx = Integer.parseInt(st.nextToken());
		ty = Integer.parseInt(st.nextToken());

		stonesAndWarriors(variables[3],
				stonesIdx = new TreeMap<>((a, b) -> (a.x != b.x ? a.x - b.x : a.y - b.y)));
		int noOfWarriors = stonesAndWarriors(variables[4],
				warriorsIdx = new TreeMap<>((a, b) -> (a.x != b.x ? a.x - b.x : a.y - b.y)));

		return new Endgame(n, m, ix, iy, tx, ty, warriorsIdx, stonesIdx, noOfWarriors);
	}

	static int stonesAndWarriors(String representation, TreeMap<Point, Integer> ptsToIdx) {
		StringTokenizer st = new StringTokenizer(representation, ",");
		int i = 0;
		while(st.hasMoreTokens()) {
			int x = Integer.parseInt(st.nextToken()), y = Integer.parseInt(st.nextToken());
			ptsToIdx.put(new Point(x, y), i++);
		}
		return i;
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

			if (problem.isGoalState(currentNode))
				return currentNode;

			algorithm.enqueue(problem.expand(currentNode));
		}

	}

	/*
	 * Iterative Deepening Search: Starts at a limit of 0 and continues looping
	 * till infinity. It performs DLS and returns the result if the failure is
	 * not detected.
	 */
	protected static Node performIDS(GenericSearchProblem problem) {

		int limit = 0;

		while (true) {

			DLS depthLimitedSearch = new DLS(limit);

			Node result = performGeneralSearch(problem, depthLimitedSearch);

			if (result != null)
				return result;

			problem.resetVisitedStates();
			
			limit++;
		}
	}

}
