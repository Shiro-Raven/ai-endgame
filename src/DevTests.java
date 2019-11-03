public class DevTests {

	/*
	 * 
	 * THIS CLASS IS PURELY FOR DEVELOPMENT TESTS AND IS NOT PART OF THE CORE CODE
	 * OF THE SEARCH AGENT
	 * 
	 */

	@SuppressWarnings("unused")
	public static void main(String[] args) {
//			SIZE | IRON MAN | THANOS | 			STONES 			| 		WARRIORS
		String test5 = "5,5;" + "0,0;" + "4,4;" + "0,1,0,2,0,3,0,4,1,4,2,4;" + "2,0,3,0,3,1,4,0,4,1";
		String test6 = "6,6;" + "0,0;" + "5,5;" + "0,1,0,2,0,3,0,4,0,5,1,5;" + "2,0,3,0,4,5,5,4,4,4";
		String test7 = "7,7;" + "0,0;" + "6,6;" + "0,1,0,2,0,3,0,4,0,5,0,6;" + "2,0,3,0,3,1,4,0,4,1";
		String test8 = "8,8;" + "0,0;" + "7,7;" + "0,1,0,2,0,3,0,4,0,6,0,7;" + "2,0,3,0,6,5,6,6,5,6";
		String test9 = "9,9;" + "0,0;" + "8,8;" + "0,1,0,2,0,3,0,4,0,7,0,8;" + "1,1,1,2,3,1,4,0,0,5";
		String test10 = "10,10;" + "0,0;" + "9,9;" + "0,5,0,2,0,3,1,4,1,5,2,4;" + "1,2,1,3,3,1,0,4,2,3";
		String test11 = "11,11;" + "0,0;" + "10,10;" + "0,1,0,2,0,3,0,4,1,4,2,4;" + "2,0,3,0,3,1,4,0,4,1";
		String test12 = "12,12;" + "0,0;" + "11,11;" + "0,1,0,2,0,3,0,4,1,4,2,4;" + "2,0,3,0,3,1,4,0,4,1";
		String test13 = "13,13;" + "0,0;" + "12,12;" + "0,1,0,2,0,3,0,4,1,4,2,4;" + "2,0,3,0,3,1,4,0,4,1";
		String test14 = "14,14;" + "0,0;" + "9,9;" + "8,6,9,4,7,1,4,4,4,7,2,3;" + "8,13,0,4,0,8,5,7,10,0";
		String test15 = "15,15;" + "0,0;" + "14,14;" + "7,0,9,14,14,8,5,8,8,9,8,4;" + "6,6,4,3,10,2,7,4,3,11";
		String testWrongInput = "5,5;" + "0,0;" + "4,4;" + "0,1,0,2,0,3,3,4,1,4,2,4;" + "1,1,3,0,3,1,4,0,4,1";
		String grid14 = "14,14;2,13;12,7;8,6,9,4,7,1,4,4,4,7,2,3;8,13,0,4,0,8,5,7,10,0";
		String grid = "7,7;3,3;1,1;0,1,1,0,1,2,2,1,4,4,6,0;0,0,0,2,1,3,2,0,2,2,3,1,3,4,4,3,5,0,0,6,5,6,6,6";
		String grid5 = "5,5;2,2;4,2;4,0,1,2,3,0,2,1,4,1,2,4;3,2,0,0,3,4,4,3,4,4";
		String grid10 = "10,10;5,1;0,4;3,1,6,8,1,2,9,2,1,5,0,8;7,8,7,6,3,3,6,0,3,8";

		String sol = Main.solve(test15, "AS1", false);

		System.out.println(sol);
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
		Warriors warriors = new Warriors(7);
		warriors.kill(6);
		warriors.kill(1);
		System.out.println(warriors);
		state.setValue("warriors", warriors);
		state.setValue("stones", 1 << 4);
		State otherState = new State();
		otherState.setValue("position", new Point(2, 2));
		Warriors otherWarriors = new Warriors(7);
		otherWarriors.kill(6);
		otherWarriors.kill(1);
		otherState.setValue("warriors", otherWarriors);
		otherState.setValue("stones", 1 << 4);
		System.out.println(state.compareTo(otherState));
	}

}
