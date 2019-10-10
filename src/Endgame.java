import java.util.BitSet;
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
    protected int getPathCost(Node parentNode, String appliedOperator, State resultingState) {
        int cost = 0;

        Point ironManLoc = (Point) resultingState.getValue(stateContents.ironMan.label);

        // adjacent warriors or thanos present
        int counter = 0;
        boolean thanosPresent = false;
        for (int i = 0; i < diffX.length; i++) {
            int newX = ironManLoc.x + diffX[i], newY = ironManLoc.y + diffY[i];
            Point adjPoint = new Point(newX, newY);
            if (isWarrior(adjPoint))
                counter++;

            // Thanos is adjacent to iron man
            if (thanosPos.compareTo(adjPoint) == 0) {
                thanosPresent = true;
            }
        }

        cost += thanosPresent ? thanosAttack : 0;

        switch (appliedOperator) {
            case "kill":
                cost = counter * warriorKill;
                break;
            case "collect":
                cost += stoneCollect;
            case "up":
            case "down":
            case "left":
            case "right":
                cost = counter * warriorAttack;
                break;

            default:
                break;
        }

        return parentNode.getPathCost() + cost;
    }

    @Override
    protected State applyOperator(State currentState, String operator) {
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
                newState.setValue(stateContents.ironMan.label, newLoc);
                
                // Validation: Can't move to Thanos cell if not all stones collected
                if(ironManLoc.compareTo(thanosPos) == 0 && (byte) currentState.getValue(stateContents.stones.label) != 0)
                	return null;
                
                // Validation: Can't move to cell with warrior
                if(warriorsIdx.get(ironManLoc) != null)
                	return null;
          
                changedField = 0;
                break;
            case "collect":
                Integer stoneIdx = stonesIdx.get(ironManLoc);
                byte stonesLeft = (byte) currentState.getValue(stateContents.stones.label);
                
                // Validation: Point doesn't contain a stone, or stone is already collected.
                if (stoneIdx == null || (stonesLeft & (1 << stoneIdx)) == 0)
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
                	Integer curr = warriorsIdx.get(new Point(diffX[i], diffY[i]));
                	
                	if(curr != null && newWarriors.isAlive(curr)){
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
    protected boolean isGoalState(State currentState) {
        Point ironManLoc = (Point) currentState.getValue("ironMan");
        Byte stones = (Byte) currentState.getValue("stones");

        return (ironManLoc.compareTo(thanosPos) == 0) && (stones == 0);
    }

    /*-----------------------------------------------Utility methods--------------------------------------------------*/

    private boolean isWarrior(Point p) {
        return warriorsIdx.containsKey(p);
    }

    private boolean isInsideGrid(Point p) {
        return 0 <= p.x && p.x < rows && 0 <= p.y && p.y < columns;
    }


}
