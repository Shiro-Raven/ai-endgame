import java.util.BitSet;

public class Warriors implements Comparable<Warriors> {
	private BitSet bitMask;

	public Warriors(int noOfWarriors) {
		bitMask = new BitSet(noOfWarriors);
	}
	
	public Warriors(Warriors oldWarriors, int killedWarrior) {
		bitMask = (BitSet) oldWarriors.bitMask.clone();
		kill(killedWarrior);
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
