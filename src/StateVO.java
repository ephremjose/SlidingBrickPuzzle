
import java.util.ArrayList;

/**
 * Author 		: Renjith J Ephrem
 * Email  		: rje49@drexel.edu
 * Subject		: CS510 - Assignment 1.
 * Instructor 	: Dr. Christopher Geib
 * Class Name   : StateVO.java
 *
 * This is the main Virtual Object (VO) file that has the place holders, setters and getters for storing cumulative information about the current game state.
 *
 * It holds the following information:
 *
 *      -- currentState     : A 2D integer matrix that holds the game state information.
 *      -- height           : The number of rows of the matrix.
 *      -- width            : The number of columns of the matrix.
 *      -- isComplete       : The boolean flag that holds the information, whether the game is in solved state or not.
 *      -- bricksHashMap    : A hash map that holds the complete information about the positions of each brick, and the brick type.
 *      -- movesHashMap     : A hash map that holds the cumulative information related to all the moves available for the bricks.
 *      -- totalMovesMap    : This hash map holds the list of all possible moves, but just the names of the moves.
 *
 */

public class StateVO implements Cloneable {

    public StateVO(int height, int width) {
        this.height = height;
        this.width = width;
    }


    public StateVO(StateVO state)
    {

        this.height = state.height;
        this.width = state.width;

        this.isComplete = state.isComplete;

        int[][] arr = state.getCurrentState();

        try {

            this.currentState = new int[height][width];
            for (int i = 0; i < state.height; i++) {
                for (int j = 0; j < state.width; j++) {
                    this.currentState[i][j] = arr[i][j];
                }
            }

            for (int i = 0; i < state.getMovesPerformedFromInitialState().size(); i++) {
                this.movesPerformedFromInitialState.add(state.getMovesPerformedFromInitialState().get(i));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public int height;
    public int width;

    int[][] currentState = new int[height][width];

    boolean isComplete = false;


    public ArrayList<String> getMovesPerformedFromInitialState() {
        return movesPerformedFromInitialState;
    }

    ArrayList<String> movesPerformedFromInitialState = new ArrayList<String>();

    public int[][] getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int[][] currentState) {
        this.currentState = currentState;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }



}
