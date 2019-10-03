import java.util.ArrayList;

public abstract class GenericSearchProblem {

	// the operators representing the allowed agent actions
	protected String[] operators;

	// the initial state of the search problem
	protected State initialState;

	/*
	 * getPathCost is the path cost function. Parameters: 1. parentNode: The parent
	 * node. 2. appliedOperator: The operator/action that was used to reach the
	 * current node. 3. resultingState: The state that resulted from applying the
	 * operator to the parent node's state. Assumption: no other parameters are
	 * needed for cost calculations; if there are, they would be provided by the
	 * subclass.
	 */
	protected abstract int getPathCost(Node parentNode, String appliedOperator, State resultingState);

	/*
	 * Transition Function: applyOperator takes an operator and the current state as
	 * inputs and returns the new state that results from applying the operator to
	 * the current state. Subclasses of the GenericSearchProblem should implement
	 * this method for the method expand to work.
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

			State resultingState = applyOperator(currentNode.state, operator);

			int resultingPathCost = getPathCost(currentNode, operator, resultingState);

			resultingNodes
					.add(new Node(resultingState, currentNode, operator, currentNode.depth + 1, resultingPathCost));
		}

		return resultingNodes;
	}

}
