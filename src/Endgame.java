import java.util.BitSet;

public class Endgame extends GenericSearchProblem {

	// Global values
	Point thanosPos;
	Point[] warriors;
	Point[] stoneLocations;

	// Grid boundaries
	int rows;
	int columns;

	// Constants
	private static final int thanosAttack = 5;
	private static final int warriorAttack = 1;
	private static final int warriorKill = 2;
	private static final int stoneCollect = 3;

	enum stateContents {
		ironMan("ironMan"), stones("stones"), warriors("warriors");

		private final String label;

		private stateContents(String label) {
			this.label = label;
		}
	}

	public Endgame(int n, int m, int ix, int iy, int tx, int ty, Point[] stones, Point[] warriors) {
		if (stones.length != 6) {
			System.err.println("Less than six stones!");
			return;
		}

		rows = m;
		columns = n;

		this.thanosPos = new Point(tx, ty);
		this.warriors = warriors;
		this.stoneLocations = stones;

		this.operators = new String[] { "up", "down", "left", "right", "collect", "kill" };

		this.initialState = new State();
		this.initialState.setValue(stateContents.ironMan.label, new Point(ix, iy));
		// A byte value of 00_111_111 where each zero represents one of the
		// stones.
		this.initialState.setValue(stateContents.stones.label, (byte) (1 << 6) - 1);
		// A bit representation of the alive warriors, 1 represents a warrior
		// that's not dead yet
		BitSet warriorsBitSet = new BitSet(warriors.length);
		warriorsBitSet.set(0, warriors.length);
		this.initialState.setValue(stateContents.warriors.label, warriorsBitSet);
	}

	@Override
	protected int getPathCost(Node parentNode, String appliedOperator, State resultingState) {
		int cost = 0;

		Point ironManLoc = (Point) resultingState.getValue(stateContents.ironMan.label);

		// adjacent warriors
		int counter = 0;
		for (int i = 0; i < warriors.length; i++) {
			if (Math.abs(ironManLoc.compareTo(warriors[i])) <= 1) {
				counter++;
				if (counter == 9)
					break;
			}
		}

		// Thanos
		boolean thanosPresent = false;
		if (Math.abs(thanosPos.compareTo(ironManLoc)) <= 1)
			thanosPresent = true;

		cost += thanosPresent ? thanosAttack : 0;

		switch (appliedOperator) {
		case "kill":
			cost = counter * warriorKill;
			break;
		case "collect":
			cost += stoneCollect;
		case "up":
		case "down":
		case "left":
		case "right":
			cost = counter * warriorAttack;
			break;

		default:
			break;
		}

		return parentNode.getPathCost() + cost;
	}

	@Override
	protected State applyOperator(State currentState, String operator) {
		// TODO: Legality of an operator

		State newState = new State();

		int changedField = 0;
		Point ironManLoc = (Point) currentState.getValue(stateContents.ironMan.label);

		switch (operator) {
		case "up":
			Point newLocUp = new Point(ironManLoc);
			newLocUp.x--;
			newState.setValue(stateContents.ironMan.label, newLocUp);
			changedField = 0;
			break;
		case "down":
			Point newLocDown = new Point(ironManLoc);
			newLocDown.x++;
			newState.setValue(stateContents.ironMan.label, newLocDown);
			changedField = 0;
			break;
		case "right":
			Point newLocRight = new Point(ironManLoc);
			newLocRight.y++;
			newState.setValue(stateContents.ironMan.label, newLocRight);
			changedField = 0;
			break;
		case "left":
			Point newLocLeft = new Point(ironManLoc);
			newLocLeft.y--;
			newState.setValue(stateContents.ironMan.label, newLocLeft);
			changedField = 0;
			break;
		case "collect":
			byte stonesLeft = (byte) currentState.getValue(stateContents.stones.label);
			for (int i = 0; i < stoneLocations.length; i++) {
				if (stoneLocations[0].compareTo(ironManLoc) == 0) {
					stonesLeft ^= (byte) (1 << i);
					break;
				}
			}
			newState.setValue(stateContents.stones.label, stonesLeft);
			changedField = 1;
			break;
		case "kill":
			BitSet newBitset = new BitSet(warriors.length);
			for (int i = 0; i < warriors.length; i++) {
				if (Math.abs(ironManLoc.compareTo(warriors[i])) <= 1) {
					newBitset.set(i);
					if (newBitset.cardinality() == 9)
						break;
				}
			}
			newBitset.xor((BitSet) currentState.getValue(stateContents.warriors.label));
			newState.setValue(stateContents.warriors.label, newBitset);
			changedField = 2;
			break;
		default:
			; // Throw some type of exception
		}

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
	protected boolean isGoalState(State currentState) {
		Point ironManLoc = (Point) currentState.getValue("ironMan");
		Byte stones = (Byte) currentState.getValue("stones");

		return (ironManLoc.compareTo(thanosPos) == 0) && (stones == 0);
	}

}
