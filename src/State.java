import java.util.HashMap;
import java.util.Map.Entry;

public class State implements Comparable<State> {
	private HashMap<String, Object> contents;

	public State() {
		contents = new HashMap<String, Object>();
	}

	public Object getValue(String key) {
		return contents.get(key);
	}

	public void setValue(String key, Object value) {
		contents.put(key, value);
	}

	@Override
	public boolean equals(Object obj) {
		State otherState = (State) obj;
		
		if (otherState.contents.size() != this.contents.size()) {
			System.err.println("States should have the same attributes");
			return false;
		}
		
		for (Entry<String, Object> entry : this.contents.entrySet()) {
			Object attribute = entry.getValue();
			Object otherAttribute = otherState.contents.get(entry.getKey());
			
			if(!attribute.equals(otherAttribute))
				return false;
		}
		
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int compareTo(State state) {
		HashMap<String, Object> otherContents = state.contents;

		if (otherContents.size() != contents.size()) {
			System.err.println("States should have the same attributes");
			return 0;
		}

		for (Entry<String, Object> entry : contents.entrySet()) {
			try {
				Comparable attribute = (Comparable) entry.getValue();
				Comparable otherAttribute = (Comparable) otherContents.get(entry.getKey());

				if (attribute.compareTo(otherAttribute) == 0) {
					continue;
				}

				return attribute.compareTo(otherAttribute);
			} catch (Exception _e) {
				System.err.println("State attributes should implement Comparable");
			}

		}

		return 0;
	}

	public String toString() {
		return contents.toString();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

}
