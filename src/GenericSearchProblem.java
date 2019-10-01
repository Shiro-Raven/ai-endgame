import java.util.ArrayList;

public abstract class GenericSearchProblem {

	// the operators representing the allowed agent actions
	protected String[] operators;

	// the initial state of the search problem
	protected State initialState;

	// TODO: path cost function

	// TODO: state space

	/*
	 * applyOperator takes an operator and the current state as inputs and returns
	 * the new state that results from applying the operator to the current state.
	 * Subclasses of the GenericSearchProblem should implement this method for the
	 * method expand to work.
	 */
	protected abstract State applyOperator(State currentState, String operator);

	/*
	 * isGoalState returns true if the current state is a goal state
	 */
	protected abstract Boolean isGoalState(State currentState);

	/*
	 * TODO: finish the implementation
	 */
	protected ArrayList<Node> expand(Node currentNode) {

		ArrayList<Node> resultingNodes = new ArrayList<>();

		for (String operator : operators) {
			// resultingNodes.add(new Node(applyOperator(currentState, operator)));
		}

		return resultingNodes;
	}

	// TODO: implement performGeneralSearch
	protected Node performGeneralSearch() {
		return new Node();
	}

}
