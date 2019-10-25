import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
			break;
		}

		problemInstance.resetVisitedStates(); 
		
		Node finalNode;
		if (!strategy.equals("ID")) {
			finalNode = performGeneralSearch(problemInstance, algorithm);
		} else {
			finalNode = performIDS(problemInstance);
		}		

		if(visualize)
			System.out.println(visualizeSolution(finalNode, problemInstance));
		
		// construct the solution
		return constructSolution(finalNode, problemInstance);
		
	}
	
	private static String constructSolution(Node finalNode, Endgame problemInstance) {

		if (finalNode == null)
			return "There is no solution.";

		String resultingOutput = "";
		Stack<Node> st = Endgame.getFullSolution(finalNode);

		// add the actions to the plan
		while (!st.isEmpty()) {
			String currentOperator = st.pop().getOperator();
			if (currentOperator != null)
				resultingOutput += (currentOperator + ",");
		}

		// add snap to the plan
		resultingOutput += "snap;";

		// add the cost of the plan
		resultingOutput += (finalNode.getPathCost() + ";");

		// add the number of expanded nodes
		resultingOutput += problemInstance.expandedNodesCounter;

		return resultingOutput;
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
		Point[] warriors;
		Point[] stones;
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

		stones = stonesAndWarriors(variables[3],
				stonesIdx = new TreeMap<>((a, b) -> (a.x != b.x ? a.x - b.x : a.y - b.y)));
		warriors = stonesAndWarriors(variables[4],
				warriorsIdx = new TreeMap<>((a, b) -> (a.x != b.x ? a.x - b.x : a.y - b.y)));

		return new Endgame(n, m, ix, iy, tx, ty, stones, warriors, warriorsIdx, stonesIdx);
	}

	static Point[] stonesAndWarriors(String representation, TreeMap<Point, Integer> ptsToIdx) {
		StringTokenizer st = new StringTokenizer(representation, ",");
		Point[] pts = new Point[st.countTokens() / 2];
		
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
			
			//System.out.println("Examining: " + currentNode);
			//((Endgame) problem).visualizeState(currentNode);

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

	@SuppressWarnings("unused")
	public static void main(String[] args) throws FileNotFoundException {
		// 				SIZE | IRON MAN | THANOS | 			STONES 			| 		WARRIORS
		String test5 = "5,5;" + "0,0;" + "4,4;" + "0,1,0,2,0,3,0,4,1,4,2,4;" + "2,0,3,0,3,1,4,0,4,1";
		String test6 = "6,6;" + "0,0;" + "5,5;" + "0,1,0,2,0,3,0,4,0,5,1,5;" + "2,0,3,0,4,5,5,4,4,4";
		String test7 = "7,7;" + "0,0;" + "6,6;" + "0,1,0,2,0,3,0,4,0,5,0,6;" + "2,0,3,0,3,1,4,0,4,1";
		String test8 = "8,8;" + "0,0;" + "7,7;" + "0,1,0,2,0,3,0,4,0,6,0,7;" + "2,0,3,0,6,5,6,6,5,6";
		String test9 = "9,9;" + "0,0;" + "8,8;" + "0,1,0,2,0,3,0,4,0,7,0,8;" + "1,1,1,2,3,1,4,0,0,5";
		String test10 = "10,10;" + "0,0;" + "9,9;" + "0,5,0,2,0,3,1,4,1,5,2,4;" + "1,2,1,3,3,1,0,4,2,3";
		String test11 = "11,11;" + "0,0;" + "10,10;" + "0,1,0,2,0,3,0,4,1,4,2,4;" + "2,0,3,0,3,1,4,0,4,1";
		String test12 = "12,12;" + "0,0;" + "11,11;" + "0,1,0,2,0,3,0,4,1,4,2,4;" + "2,0,3,0,3,1,4,0,4,1";
		String test13 = "13,13;" + "0,0;" + "12,12;" + "0,1,0,2,0,3,0,4,1,4,2,4;" + "2,0,3,0,3,1,4,0,4,1";
		String test14 = "14,14;" + "0,0;" + "13,13;" + "8,6,9,4,7,1,4,4,4,7,2,3;" + "8,13,0,4,0,8,5,7,10,0";
		String test15 = "15,15;" + "0,0;" + "14,14;" + "7,0,9,14,14,8,5,8,8,9,8,4;" + "6,6,4,3,10,2,7,4,3,11";
		
		String testWrongInput = "5,5;" + "0,0;" + "4,4;" + "0,1,0,2,0,3,3,4,1,4,2,4;" + "1,1,3,0,3,1,4,0,4,1";

		String sol = solve(testWrongInput, "BF", false);
		
		System.out.println(sol);
		
		File solution = new File("solution.txt");
		PrintWriter out = new PrintWriter(solution);
		out.println(sol);
		out.close();
	}

}
