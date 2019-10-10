import java.util.BitSet;

public class Warriors implements Comparable<Warriors> {
	private BitSet bitMask;

	public Warriors(int noOfWarriors) {
		bitMask = new BitSet(noOfWarriors);
		bitMask.set(0, noOfWarriors);
	}

	public Warriors(Warriors oldWarriors) {
		bitMask = (BitSet) oldWarriors.bitMask.clone();
	}

	protected void add(int warriorIdx) {
		bitMask.set(warriorIdx);
	}

	protected void kill(int warriorIdx) {
		bitMask.clear(warriorIdx);
	}

	protected boolean isAlive(int warriorIdx) {
		return bitMask.get(warriorIdx);
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bitMask.length(); i++) {
			s.append(bitMask.get(i) == true ? 1 : 0);
		}

		return s.toString();
	}

	@Override
	public int compareTo(Warriors w) {
		int nextIdx = Math.max(bitMask.length(), w.bitMask.length()) - 1;
		while (nextIdx > 0) {
			int onBit1 = bitMask.previousSetBit(nextIdx);
			int onBit2 = w.bitMask.previousSetBit(nextIdx);
			if (onBit1 == onBit2) {
				nextIdx = onBit1 - 1;
				continue;
			}
			return onBit1 - onBit2;
		}
		return 0;
	}

}
