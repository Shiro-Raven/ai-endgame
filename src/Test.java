public class Test {

	public static void main(String[] args) {
		testStateComparable();
	}
	
	static void testStateComparable() {
		State state = new State();
		state.setValue("position", new Point(3, 2));
		Warriors warriors = new Warriors(10);
		warriors.add(1);
		warriors.add(6);
		state.setValue("warriors", warriors);
		state.setValue("stones", 1<<4);
		State otherState = new State();
		otherState.setValue("position",  new Point(2, 2));
		Warriors otherWarriors = new Warriors(9);
		otherWarriors.add(2);
		otherWarriors.add(5);
		otherState.setValue("warriors", otherWarriors);
		otherState.setValue("stones", 1<<5);
		System.out.println(state.compareTo(otherState));
	}
	

}
