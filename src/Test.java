public class Test {

	public static void main(String[] args) {
		// testStateComparable();
		// testStateCloning();
		// testPointCompareTo();
		// testWarriorsConst();
	}

	static void testWarriorsConst() {
		Warriors normal = new Warriors(5);
		System.out.println(normal.toString());

		normal.kill(2);
		System.out.println(normal.toString());

		Warriors changed = new Warriors(normal);
		System.out.println(changed.toString());

		changed.kill(0);

		System.out.println("--------------");
		System.out.println(normal);
		System.out.println(changed);
	}

	static void testPointCompareTo() {
		Point testCenter = new Point(0, 0);

		System.out.println(testCenter.compareTo(new Point(-1, -1)));
		System.out.println(testCenter.compareTo(new Point(-1, 1)));
		System.out.println(testCenter.compareTo(new Point(-1, 0)));

		System.out.println(testCenter.compareTo(new Point(1, -1)));
		System.out.println(testCenter.compareTo(new Point(1, 0)));
		System.out.println(testCenter.compareTo(new Point(1, 1)));

		System.out.println(testCenter.compareTo(new Point(0, -1)));
		System.out.println(testCenter.compareTo(new Point(0, 0)));
		System.out.println(testCenter.compareTo(new Point(0, 1)));
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
