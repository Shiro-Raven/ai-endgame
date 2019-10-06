public class Node {
	private Node parentNode;
	private State state;
	private int pathCost;
	private String operator;
	
	private int depth;
	
	public Node(State state, Node parentNode, int pathCost, String operator){
		this.parentNode = parentNode;
		this.state = state;
		this.pathCost = pathCost;
		this.operator = operator;
		
		this.depth = parentNode == null? 0 : parentNode.depth + 1;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public State getState() {
		return state;
	}

	public int getPathCost() {
		return pathCost;
	}

	public String getOperator() {
		return operator;
	}

	public int getDepth() {
		return depth;
	}
}
