import java.util.BitSet;

public class Test {

	public static void main(String[] args) {
		// testStateComparable();
		testStateCloning();
	}

	private static void testStateCloning() {
		State oldState = new State();

		oldState.setValue("ironMan", new Point(5, 4));
		oldState.setValue("stones", (byte) 192);
		BitSet warriorsBitSet = new BitSet(9);
		warriorsBitSet.set(0, 9 - 1);
		oldState.setValue("warriorsAlive", warriorsBitSet);

		State newState1 = null;

		newState1 = oldState.deepCopy();
		Point ironMan = (Point) newState1.getValue("ironMan");
		ironMan.x = 9;
		newState1.setValue("ironMan", ironMan);

		State newState2 = oldState;

		System.out.println("Done Cloning");
		System.out.println(oldState == newState1);
		System.out.println(oldState == newState2);
		System.out.println(((Point) oldState.getValue("ironMan")).x);
		System.out.println(((Point) newState1.getValue("ironMan")).x);

	}

	static void testStateComparable() {
		State state = new State();
		state.setValue("position", new Point(3, 2));
		Warriors warriors = new Warriors(10);
		warriors.add(1);
		warriors.add(6);
		state.setValue("warriors", warriors);
		state.setValue("stones", 1 << 4);
		State otherState = new State();
		otherState.setValue("position", new Point(2, 2));
		Warriors otherWarriors = new Warriors(9);
		otherWarriors.add(2);
		otherWarriors.add(5);
		otherState.setValue("warriors", otherWarriors);
		otherState.setValue("stones", 1 << 5);
		System.out.println(state.compareTo(otherState));
	}

}
