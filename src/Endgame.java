import java.util.Map.Entry;
import java.util.TreeMap;

public class Endgame extends GenericSearchProblem {

	/*---------------------------------------Global Values----------------------------------------*/
	Point thanosPos;
	Point[] warriors;
	Point[] stoneLocations;
	TreeMap<Point, Integer> warriorsIdx;
	TreeMap<Point, Integer> stonesIdx;

	/*-------------------------------------Grid Boundaries----------------------------------------*/
	int rows;
	int columns;

	/*----------------------------------------Constants-------------------------------------------*/
	private static final int thanosAttack = 5;
	private static final int warriorAttack = 1;
	private static final int warriorKill = 2;
	private static final int stoneCollect = 3;

	// Adjacent cells offsets
	private static int[] diffX = { 0, 0, 1, -1 };
	private static int[] diffY = { -1, 1, 0, 0 };

	// Enum used to ensure consistency when handling the state read/write
	// operations
	enum stateContents {
		ironMan("ironMan"), stones("stones"), warriors("warriors");

		private final String label;

		private stateContents(String label) {
			this.label = label;
		}
	}

	/*-----------------------------------------Class----------------------------------------------*/
	public Endgame(int n, int m, int ix, int iy, int tx, int ty, Point[] stones, Point[] warriors,
			TreeMap<Point, Integer> warriorsIdx, TreeMap<Point, Integer> stonesIdx) {
		rows = m;
		columns = n;

		this.thanosPos = new Point(tx, ty);
		this.warriors = warriors;
		this.stoneLocations = stones;
		this.warriorsIdx = warriorsIdx;
		this.stonesIdx = stonesIdx;

		// Input Checks
		if (!validateInput(ix, iy))
			return;

		// List of all allowed operators for this search problem
		this.operators = new String[] { "up", "down", "left", "right", "collect", "kill" };

		/*
		 * State consists of: 1. ironMan location. 2. Stones picked up or not.
		 * 3. Warriors defeated or not.
		 */
		this.initialState = new State();
		this.initialState.setValue(stateContents.ironMan.label, new Point(ix, iy));

		// A byte value of 00_111_111 where each one represents one of the
		// remaining stones in the world.
		this.initialState.setValue(stateContents.stones.label, (byte) ((1 << 6) - 1));

		// A bit representation of the alive warriors, 1 represents a warrior
		// that's not dead yet
		Warriors warriorsBitSet = new Warriors(warriors.length);
		this.initialState.setValue(stateContents.warriors.label, warriorsBitSet);

		// reset the number of expanded nodes
		this.expandedNodesCounter = 0;

	}

	private boolean validateInput(int ix, int iy) {
		if (stonesIdx.size() != 6) {
			System.err.println("Less than six stones OR Duplicate stone input!");
			return false;
		}

		if (warriorsIdx.size() != warriors.length) {
			System.err.println("Duplicate warrior input!");
			return false;
		}

		for (Entry<Point, Integer> x : warriorsIdx.entrySet()) {
			if (x.getKey().compareTo(thanosPos) == 0) {
				System.err.println("Warrior and Thanos in same place!");
				return false;
			}

			if (x.getKey().compareTo(new Point(ix, iy)) == 0) {
				System.err.println("Warrior and Iron Man in same place!");
				return false;
			}

			for (Entry<Point, Integer> y : stonesIdx.entrySet()) {
				if (x.getKey().compareTo(y.getKey()) == 0) {
					System.err.println("Warrior and Stone in same place!");
					return false;
				} else if (y.getKey().compareTo(thanosPos) == 0) {
					System.err.println("Stone and Thanos in same place!");
					return false;
				} else if (y.getKey().compareTo(new Point(ix, iy)) == 0) {
					System.err.println("Stone and Iron Man in same place!");
					return false;
				}
			}
		}

		if (thanosPos.compareTo(new Point(ix, iy)) == 0) {
			System.err.println("Thanos and Iron Man in same place!");
			return false;
		}

		return true;
	}

	@Override
	/*
	 * Calculates path cost and returns it.
	 */
	protected int getPathCost(Node parentNode, String appliedOperator, State resultingState) {
		int cost = 0;

		Point ironManLoc = (Point) resultingState.getValue(stateContents.ironMan.label);

		// adjacent warriors or Thanos present
		int warriorsCounterPreviousState = 0;
		int warriorsCounterResultingState = 0;
		boolean thanosPresent = false;

		// A loop for checking adjacent locations for the presence of warriors
		// or Thanos
		for (int i = 0; i < diffX.length; i++) {
			int newX = ironManLoc.x + diffX[i], newY = ironManLoc.y + diffY[i];
			Point adjPoint = new Point(newX, newY);

			// kill requires a check on the previous state; other operators on
			// the resulting one
			if (appliedOperator == "kill") {
				if (isWarrior(parentNode.getState(), adjPoint)) {
					warriorsCounterPreviousState++;
				}
			} else {
				if (isWarrior(resultingState, adjPoint)) {
					warriorsCounterResultingState++;
				}
			}

			// Thanos is adjacent to iron man
			if (thanosPos.compareTo(adjPoint) == 0) {
				thanosPresent = true;
			}
		}

		// Thanos is in the same location as Iron Man
		if (thanosPos.compareTo(ironManLoc) == 0)
			thanosPresent = true;

		// Adding the damage to the cost
		cost += thanosPresent ? thanosAttack : 0;

		switch (appliedOperator) {
		case "kill":
			cost += warriorsCounterPreviousState * warriorKill;
			break;
		case "collect":
			cost += stoneCollect;
		case "up":
		case "down":
		case "left":
		case "right":
			cost += warriorsCounterResultingState * warriorAttack;
			break;

		default:
			break;
		}

		return parentNode.getPathCost() + cost;
	}

	@Override
	protected State applyOperator(Node currentNode, String operator) {

		State currentState = currentNode.getState();

		// return no results if the currentState has damage >= 100
		int damageInCurrentState = currentNode.getPathCost();
		if (damageInCurrentState >= 100) {
			return null;
		}

		State newState = new State();

		// Pointer to indicate which of the fields in the state got changed;
		// this is needed to optimise the space complexity
		int changedField = -1;
		
		Point ironManLoc = (Point) currentState.getValue(stateContents.ironMan.label);

		switch (operator) {
		case "up":
		case "down":
		case "right":
		case "left":
			Point newLoc = new Point(ironManLoc);
			newLoc.move(operator);

			// Validation: Point is inside the grid.
			if (!isInsideGrid(newLoc))
				return null;

			// Validation: Can't move to Thanos cell if not all stones collected
			if ((newLoc.compareTo(thanosPos) == 0) && ((byte) currentState.getValue(stateContents.stones.label) != 0))
				return null;

			// Validation: Can't move to cell with ALIVE warrior
			if (isWarrior(currentState, newLoc))
				return null;

			newState.setValue(stateContents.ironMan.label, newLoc);

			changedField = 0;
			break;
		case "collect":

			Integer stoneIdx = stonesIdx.get(ironManLoc);
			byte stonesLeft = (byte) currentState.getValue(stateContents.stones.label);

			// Validation: Point doesn't contain a stone, or stone is already
			// collected.
			if ((stoneIdx == null) || ((stonesLeft & (1 << stoneIdx)) == 0))
				return null;

			// XOR the respective stone bit to zero, to designate it as collected
			stonesLeft ^= (byte) (1 << stoneIdx);
			newState.setValue(stateContents.stones.label, stonesLeft);
			changedField = 1;
			break;
		case "kill":
			Warriors oldWarriors = (Warriors) currentState.getValue(stateContents.warriors.label);
			Warriors newWarriors = new Warriors(oldWarriors);

			boolean warriorsDead = false;
			// Loop over all adjacent cells and see if they have living warriors in them
			for (int i = 0; i < diffX.length; i++) {
				Integer curr = warriorsIdx.get(new Point(ironManLoc.x + diffX[i], ironManLoc.y + diffY[i]));

				if ((curr != null) && newWarriors.isAlive(curr)) {
					newWarriors.kill(curr);
					warriorsDead = true;
				}
			}

			// If no warriors are killed, then the move is not allowed
			if (!warriorsDead)
				return null;

			newState.setValue(stateContents.warriors.label, newWarriors);
			changedField = 2;
			break;
		default:
			return null;
		}

		if (changedField == -1)
			System.err.println("Something is wrong here");

		// Copy the unchanged fields
		for (int i = 0; i < stateContents.values().length; i++) {
			if (i == changedField)
				continue;

			String fieldLabel = stateContents.values()[i].label;

			newState.setValue(fieldLabel, currentState.getValue(fieldLabel));
		}

		return newState;
	}

	@Override
	protected boolean isGoalState(Node currentNode) {

		State currentState = currentNode.getState();

		Point ironManLoc = (Point) currentState.getValue(stateContents.ironMan.label);
		Byte stones = (Byte) currentState.getValue(stateContents.stones.label);
		int damage = currentNode.getPathCost();

		return (ironManLoc.compareTo(thanosPos) == 0) && (stones == 0) && (damage < 100);
	}
	
	/*------------------------------Heuristics---------------------------------------*/
	@Override
	protected int evaluateHeuristic(Node currentNode, int heuristicNum) {
		int value;
		switch (heuristicNum) {
		case 1:
			value = getOneMoveWarriorDamage(currentNode);
		case 2:
			value = getRemainingStonesDamage(currentNode);
		default:
			value = -1;
		}

		return value;
	}
	
	private int getOneMoveWarriorDamage(Node currentNode) {
		// satisfy the centering property
		if (isGoalState(currentNode))
			return 0;
		else {
			// calculate the cost of one future step with respect to warrior damage
			State currentState = currentNode.getState();

			Point ironManLoc = (Point) currentState.getValue(stateContents.ironMan.label);

			int[] futureMoveCosts = { 0, 0, 0, 0 };

			// calculate future move costs
			for (int i = 0; i < diffX.length; i++) {
				// there are at max 4 possible locations where ironMan can move
				// these locations are stored in newCentralCell
				int newCentralX = ironManLoc.x + diffX[i], newCentralY = ironManLoc.y + diffY[i];
				Point newCentralCell = new Point(newCentralX, newCentralY);

				// check the 4 cells adjacent to the central cell for warriors
				for (int j = 0; j < diffX.length; j++) {
					int adjPointX = newCentralCell.x + diffX[j], adjPointY = newCentralCell.y + diffY[j];
					Point adjPoint = new Point(adjPointX, adjPointY);

					if (isWarrior(currentState, adjPoint))
						futureMoveCosts[i]++;
				}
			}

			// find the minimum future move cost and return it
			int minimumMoveCost = futureMoveCosts[0];
			for (int i = 1; i < futureMoveCosts.length; i++) {
				if (futureMoveCosts[i] < minimumMoveCost)
					minimumMoveCost = futureMoveCosts[i];
			}

			return minimumMoveCost;
		}
	}

	private int getRemainingStonesDamage(Node currentNode) {

		State currentState = currentNode.getState();

		byte stonesLeft = (byte) currentState.getValue(stateContents.stones.label);

		// count the number of stones left and multiply it by damage
		return 3 * Integer.bitCount(stonesLeft);
	}

	/*-------------------------------Utility methods----------------------------------*/

	private boolean isWarrior(State stateToExamine, Point locationToExamine) {
		// retrieve warrior index
		Integer warriorIndex = warriorsIdx.get(locationToExamine);

		// location does not correspond to a warrior
		if (warriorIndex == null)
			return false;

		// check if the warrior is alive
		Warriors warriorsInState = (Warriors) stateToExamine.getValue(stateContents.warriors.label);

		if (warriorsInState.isAlive(warriorIndex)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unused")
	private boolean isStone(State stateToExamine, Point locationToExamine) {
		// retrieve stone index
		Integer stoneIndex = stonesIdx.get(locationToExamine);

		// location does not correspond to a stone
		if (stoneIndex == null)
			return false;

		// check if the stone has not been already picked up
		byte stonesLeft = (byte) stateToExamine.getValue(stateContents.stones.label);

		if ((stonesLeft & (1 << stoneIndex)) == 0)
			return false;
		else
			return true;
	}

	private boolean isInsideGrid(Point p) {
		return 0 <= p.x && p.x < rows && 0 <= p.y && p.y < columns;
	}

	private int numOfAdjacentAliveWarriors(State state) {
		Point ironManLoc = (Point) state.getValue(stateContents.ironMan.label);

		int counter = 0;
		for (int i = 0; i < diffX.length; i++) {
			int newX = ironManLoc.x + diffX[i], newY = ironManLoc.y + diffY[i];
			Point adjPoint = new Point(newX, newY);

			if (isWarrior(state, adjPoint)) {
				counter++;
			}
		}

		return counter * warriorAttack;
	}

	private int adjacentThanosDamage(State state) {
		Point ironManLoc = (Point) state.getValue(stateContents.ironMan.label);

		boolean isThere = false;

		for (int i = 0; i < diffX.length; i++) {
			int newX = ironManLoc.x + diffX[i], newY = ironManLoc.y + diffY[i];
			Point adjPoint = new Point(newX, newY);

			if (adjPoint.compareTo(thanosPos) == 0) {
				isThere = true;
				break;
			}
		}

		return isThere ? thanosAttack : 0;
	}

	/*------------------------------Visualization---------------------------------------*/

	/*
	 * Method to visualize a node
	 */
	final static String emptyCell = "-";

	protected String visualizeState(Node currentNode) {
		String[][] grid = new String[rows][columns];

		// fill all with empty label
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				grid[i][j] = emptyCell;

		// add Thanos
		appendToGrid(grid, thanosPos.x, thanosPos.y, "T");

		State currentState = currentNode.getState();

		// add iron man
		Point ironManLoc = (Point) currentState.getValue(stateContents.ironMan.label);

		appendToGrid(grid, ironManLoc.x, ironManLoc.y, "I");

		// add the stones
		byte stonesLeft = (byte) currentState.getValue(stateContents.stones.label);

		for (Entry<Point, Integer> entry : stonesIdx.entrySet()) {
			Point location = entry.getKey();
			Integer stoneIdx = entry.getValue();

			if (!((stonesLeft & (1 << stoneIdx)) == 0))
				appendToGrid(grid, location.x, location.y, "S");
		}

		// add the warriors
		Warriors warriorsLeft = (Warriors) currentState.getValue(stateContents.warriors.label);

		for (Entry<Point, Integer> entry : warriorsIdx.entrySet()) {
			Point location = entry.getKey();
			Integer warriorIdx = entry.getValue();

			if (warriorsLeft.isAlive(warriorIdx))
				appendToGrid(grid, location.x, location.y, "W");
		}

		// print the resulting visualization
		StringBuilder visualizationResult = new StringBuilder();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				visualizationResult.append("|\t" + grid[i][j] + "\t");
			}
			visualizationResult.append("|\n");
		}
		return "Step #" + currentNode.getDepth() + ": damage = " + currentNode.getPathCost() + ", step performed is: "
				+ currentNode.getOperator() + "\n" + visualizationResult;
	}

	private void appendToGrid(String[][] grid, int x, int y, String value) {
		if (grid[x][y] == emptyCell)
			grid[x][y] = value;
		else
			grid[x][y] += (", " + value);
	}

}
