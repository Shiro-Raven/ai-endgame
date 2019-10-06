import java.util.BitSet;
import java.util.stream.LongStream;

public class Endgame extends GenericSearchProblem {

	// Global values
	Point thanosPos;
	Point[] warriors;

	public Endgame(int n, int m, int ix, int iy, int tx, int ty, Point[] stones, Point[] warriors) {
		if (stones.length != 6) {
			System.err.println("Less than six stones!");
			return;
		}

		this.thanosPos = new Point(tx, ty);
		this.warriors = warriors;

		this.operators = new String[] { "up", "down", "left", "right", "collect", "kill" };

		this.initialState = new State();
		this.initialState.setValue("ironMan", new Point(ix, iy));
		// A byte value of 11_000_000 where each zero represents one of the
		// stones.
		// The two most significant bits are set to one to easily check when all
		// stones are collected.
		this.initialState.setValue("stones", (byte) 192);
		// A bit representation of the alive warriors, 1 represents a warrior
		// that's not dead yet
		BitSet warriorsBitSet = new BitSet(warriors.length);
		warriorsBitSet.set(0, warriors.length - 1);
		this.initialState.setValue("warriorsAlive", warriorsBitSet);

	}

	@Override
	protected int getPathCost(Node parentNode, String appliedOperator, State resultingState) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected State applyOperator(State currentState, String operator) {
		State newState = currentState.deepCopy();

		switch (operator) {
		case "up":
			;
			break;
		case "down":
			;
			break;
		case "right":
			;
			break;
		case "left":
			;
			break;
		case "collect":
			;
			break;
		case "kill":
			;
			break;
		default:
			; // Throw some type of exception
		}

		return null;
	}

	@Override
	protected boolean isGoalState(State currentState) {
		Point ironManLoc = (Point) currentState.getValue("ironMan");
		Byte stones = (Byte) currentState.getValue("stones");

		return (ironManLoc.compareTo(thanosPos) == 0) && (stones == 0);
	}

}
