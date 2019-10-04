import java.util.HashMap;

public class State implements Comparable<State> {
	private HashMap<String, Object> contents;
	
	public State(){
		contents = new HashMap<String, Object>();
	}
	
	public Object getValue(String key){
		return contents.get(key);
	}
	
	public void setValue(String key, Object value){
		contents.put(key, value);
	}
	
	public int compareTo(State arg0) {
		// TODO Auto-generated method stub
		return 0;
	}	
}
