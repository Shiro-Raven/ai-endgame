import java.util.TreeMap;

public class Endgame extends GenericSearchProblem {

    /*-------------------------------------------------Global Values--------------------------------------------------*/
    Point thanosPos;
    Point[] warriors;
    Point[] stoneLocations;
    TreeMap<Point, Integer> warriorsIdx;
    TreeMap<Point, Integer> stonesIdx;

    /*-----------------------------------------------Grid Boundaries--------------------------------------------------*/
    int rows;
    int columns;

    /*--------------------------------------------------Constants-----------------------------------------------------*/
    private static final int thanosAttack = 5;
    private static final int warriorAttack = 1;
    private static final int warriorKill = 2;
    private static final int stoneCollect = 3;
    // Adjacent cells
    private static int[] diffX = { 0, 0, 1, -1};
    private static int[] diffY = {-1, 1, 0,  0};

    enum stateContents {
        ironMan("ironMan"), stones("stones"), warriors("warriors");

        private final String label;

        private stateContents(String label) {
            this.label = label;
        }
    }

    /*---------------------------------------------------Class--------------------------------------------------------*/

    public Endgame(int n, int m, int ix, int iy, int tx, int ty, Point[] stones, Point[] warriors,
                   TreeMap<Point, Integer> warriorsIdx, TreeMap<Point, Integer> stonesIdx) {
        if (stones.length != 6) {
            System.err.println("Less than six stones!");
            return;
        }

        rows = m;
        columns = n;

        this.thanosPos = new Point(tx, ty);
        this.warriors = warriors;
        this.stoneLocations = stones;
        this.warriorsIdx = warriorsIdx;
        this.stonesIdx = stonesIdx;

        this.operators = new String[]{"up", "down", "left", "right", "collect", "kill"};

        /*
         * State consists of:
         * 1. ironMan location.
         * 2. Stones picked up or not.
         * 3. Warriors defeated or not.
         * 4. Damage received so far.
         */
        this.initialState = new State();
        this.initialState.setValue(stateContents.ironMan.label, new Point(ix, iy));
        
        // A byte value of 00_111_111 where each zero represents one of the
        // stones.
        this.initialState.setValue(stateContents.stones.label, (byte) (1 << 6) - 1);
        
        // A bit representation of the alive warriors, 1 represents a warrior
        // that's not dead yet
        Warriors warriorsBitSet = new Warriors(warriors.length);
        this.initialState.setValue(stateContents.warriors.label, warriorsBitSet);
        
    }

    @Override
    /*
     * Calculates path cost and returns it.
     * Assigns the path cost to the damage variable of the state.
     */
    protected int getPathCost(Node parentNode, String appliedOperator, State resultingState) {
        int cost = 0;

        Point ironManLoc = (Point) resultingState.getValue(stateContents.ironMan.label);

        // adjacent warriors or Thanos present
        int warriorsCounterPreviousState = 0;
        int warriorsCounterResultingState = 0;
        boolean thanosPresent = false;
        
        for (int i = 0; i < diffX.length; i++) {
            int newX = ironManLoc.x + diffX[i], newY = ironManLoc.y + diffY[i];
            Point adjPoint = new Point(newX, newY);
            
            // kill requires a check on the previous state; other operators on the resulting one
            if (appliedOperator == "kill") {
            	if(isWarrior(parentNode.getState(), adjPoint)) {
            		warriorsCounterPreviousState++;
            	}
            } else {
            	if(isWarrior(resultingState, adjPoint)) {
            		warriorsCounterResultingState++;
            	}
            }

            // Thanos is adjacent to iron man
            if (thanosPos.compareTo(adjPoint) == 0) {
                thanosPresent = true;
            }
        }
        
		// Thanos is in the same location as Iron Man
		if (thanosPos.compareTo(ironManLoc) == 0)
			thanosPresent = true;

        cost += thanosPresent ? thanosAttack : 0;

        switch (appliedOperator) {
            case "kill":
                cost = warriorsCounterPreviousState * warriorKill;
                break;
            case "collect":
                cost += stoneCollect;
            case "up":
            case "down":
            case "left":
            case "right":
                cost = warriorsCounterResultingState * warriorAttack;
                break;

            default:
                break;
        }
        
        return parentNode.getPathCost() + cost;
    }

    @Override
    protected State applyOperator(Node currentNode, String operator) {
    	
    	State currentState = currentNode.getState();
    	
		// return no results if the currentState has damage >= 100
		int damageInCurrentState = currentNode.getPathCost();

		if (damageInCurrentState >= 100) {
			return null;
		}
    	
        State newState = new State();

        int changedField = 0;
        Point ironManLoc = (Point) currentState.getValue(stateContents.ironMan.label);

        switch (operator) {
            case "up":
            case "down":
            case "right":
            case "left":
                Point newLoc = new Point(ironManLoc);
                newLoc.move(operator);
                
                // Validation: Point is outside the grid.
                if (!isInsideGrid(newLoc))
                    return null;
                
                // Validation: Can't move to Thanos cell if not all stones collected
                if( (newLoc.compareTo(thanosPos) == 0) && ((byte) currentState.getValue(stateContents.stones.label) != 0))
                	return null;
                
                // Validation: Can't move to cell with ALIVE warrior
                if(isWarrior(currentState, newLoc))
                	return null;
                
                newState.setValue(stateContents.ironMan.label, newLoc);
          
                changedField = 0;                
                break;
            case "collect":
            	
                Integer stoneIdx = stonesIdx.get(ironManLoc);
                byte stonesLeft = (byte) currentState.getValue(stateContents.stones.label);
                
                // Validation: Point doesn't contain a stone, or stone is already collected.
                if ( (stoneIdx == null) || ((stonesLeft & (1 << stoneIdx)) == 0))
                    return null;

                stonesLeft ^= (byte) (1 << stoneIdx);
                newState.setValue(stateContents.stones.label, stonesLeft);
                changedField = 1;
                break;
            case "kill":
                Warriors oldWarriors = (Warriors) currentState.getValue(stateContents.warriors.label);
                Warriors newWarriors = new Warriors(oldWarriors);
                
                boolean warriorsDead = false;
                for(int i = 0; i < diffX.length; i++){
                	Integer curr = warriorsIdx.get(new Point(ironManLoc.x + diffX[i], ironManLoc.y + diffY[i]));
                	
                	if( (curr != null) && newWarriors.isAlive(curr)){
                		newWarriors.kill(curr);
                		warriorsDead = true;
                	}
                }
                
                // If no warriors are killed, then the move is not allowed
                if(!warriorsDead)
                	return null;
                
                newState.setValue(stateContents.warriors.label, newWarriors);
                changedField = 2;
                break;
            default:
                return null;
        }

        // Copy the unchanged fields
        for (int i = 0; i < stateContents.values().length; i++) {
            if (i == changedField)
                continue;

            String fieldLabel = stateContents.values()[i].label;

            newState.setValue(fieldLabel, currentState.getValue(fieldLabel));
        }

        return newState;
    }


    @Override
    protected boolean isGoalState(Node currentNode) {
    	
    	State currentState = currentNode.getState();
    	
        Point ironManLoc = (Point) currentState.getValue(stateContents.ironMan.label);
        Byte stones = (Byte) currentState.getValue(stateContents.stones.label);
        int damage = (Integer) currentNode.getPathCost();
        
        return (ironManLoc.compareTo(thanosPos) == 0) && (stones == 0) && (damage < 100);
    }

    /*-----------------------------------------------Utility methods--------------------------------------------------*/

	private boolean isWarrior(State stateToExamine, Point locationToExamine) {
		// retrieve warrior index
		Integer warriorIndex = warriorsIdx.get(locationToExamine);

		// location does not correspond to a warrior
		if (warriorIndex == null)
			return false;

		// check if the warrior is alive
		Warriors warriorsInState = (Warriors) stateToExamine.getValue(stateContents.warriors.label);

		if (warriorsInState.isAlive(warriorIndex)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	@SuppressWarnings("unused")
	private boolean isStone(State stateToExamine, Point locationToExamine) {
		// retrieve stone index
		Integer stoneIndex = stonesIdx.get(locationToExamine);

		// location does not correspond to a stone
		if (stoneIndex == null)
			return false;

		// check if the stone has not been already picked up
		byte stonesLeft = (byte) stateToExamine.getValue(stateContents.stones.label);

		if ((stonesLeft & (1 << stoneIndex)) == 0)
			return false;
		else
			return true;
	}

    private boolean isInsideGrid(Point p) {
        return 0 <= p.x && p.x < rows && 0 <= p.y && p.y < columns;
    }


}
