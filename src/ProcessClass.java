
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Author 		: Renjith J Ephrem
 * Email  		: rje49@drexel.edu
 * Class Name   : ProcessClass.java
 *
 * This class holds the entire code logic to perform various operations on the game state.
 *
 */

public class ProcessClass {


    /**
     *
     * This method is used to output all the logs to the output.log file, thereby keeping the console output clean.
     *
     * @param value
     */
    public void log(String value)  throws Exception
    {

        FileOutputStream fileOutputStream = null;
        File logFile = new File("output.log");
        try{

            FileWriter fileWriter = new FileWriter(logFile.getName(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(value);
            bufferedWriter.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     *
     * writeGameState writes the game state ( 2D Matrix ) onto the text file gamestate.txt.
     *
     * @param state
     * @throws Exception
     */
    public void writeGameState(StateVO state)  throws Exception
    {
        File logFile = new File("gamestate.txt");
        int[][] stateArray = state.getCurrentState();
        int height = state.getHeight();
        int width = state.getWidth();

        try{

            Writer w = new OutputStreamWriter(new FileOutputStream(logFile.getName()));
            //FileWriter fileWriter = new FileWriter(logFile.getName());
            PrintWriter pw = new PrintWriter(w);
            pw.write("");
            pw.flush();
            pw.close();
            //fileWriter = new FileWriter(logFile.getName(), true);
            w = new OutputStreamWriter(new FileOutputStream(logFile.getName()));
            BufferedWriter bufferedWriter = new BufferedWriter(w);
            bufferedWriter.write(width + "," + height + ",");
            bufferedWriter.write("\n");
            for (int i = 0; i < state.getHeight(); i++) {
                for (int j = 0; j < state.getWidth(); j++) {
                    bufferedWriter.write(stateArray[i][j] + ",");
                }

                if(i != state.getHeight() - 1)
                bufferedWriter.write("\n");
            }

            bufferedWriter.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * getBricksColor() returns unique color depending on the brick number.
     *
     * @param brickNumber
     * @return
     */
    public Color getBricksColor (int brickNumber)
    {

        if( brickNumber == 0 )
            return Color.WHITE;

        if( brickNumber == -1 )
            return Color.GRAY;

        if( brickNumber == 1 )
            return Color.PINK;


        if( brickNumber == 2 )
            return Color.RED;


        if( brickNumber == 3 )
            return Color.BLUE;

        if( brickNumber == 4 )
            return Color.GREEN;

        if( brickNumber == 5 )
            return Color.MAGENTA;

        if( brickNumber == 6 )
            return Color.YELLOW;

        if( brickNumber == 7 )
            return Color.CYAN;

        return Color.WHITE;

    }


    /**
     *
     * This method reads the input text file and gets the initial state of the game.
     *
     *      -- int[][] stateArray   holds the game state matrix.
     *      -- StateVO state        holds the information regarding the state.
     *
     * @param fileName
     * @return
     */
    public StateVO getInitialGameState( String fileName, StateVO statevo ) throws Exception
    {

        //log("\n" + "In ProcessClass.getInitialGameState() ");

        int width               = 0;
        int height              = 0;
        int lineIndex           = 0;
        int[][] stateArray      = null;
        String lineFromFile     = "";
        Scanner scanner         = null;
        String[][] startArray   = null;
        String[] dimensionsArr  = new String [3];
        String[] tempArr        = null;
        StateVO state           = null;

        try{

            scanner = new Scanner( new FileInputStream("./" + fileName));

            while( scanner.hasNextLine())
            {
                lineFromFile = scanner.nextLine();
                if(lineIndex == 0) {
                    dimensionsArr = lineFromFile.split(",");

                    width = Integer.parseInt(dimensionsArr[0]);
                    height = Integer.parseInt(dimensionsArr[1]);

                    //log("Height of Array : " + height);
                    //log("Width  of Array : " + width );

                    startArray  = new String[height][width];
                    stateArray  = new int[height][width];

                    if(statevo == null)
                    state = new StateVO(height,width);

                    else
                        state = new StateVO(statevo);
                }

                else
                {
                    tempArr = lineFromFile.split(",");
                    startArray[lineIndex-1] = tempArr;
                }
                lineIndex++;
            }

            log("\n" +"Initial Game State : \n" );

            for (int i = 0; i < height ; i++) {
                for (int j = 0; j < width; j++) {

                    stateArray[i][j] = Integer.parseInt(startArray[i][j]);

                    log(stateArray[i][j] + " ");
                }
                log("\n");
            }

            state.setCurrentState(stateArray);
            state.setHeight(height);
            state.setWidth(width);
        }

        catch (Exception e)
        {

            e.printStackTrace();
            throw e;

        }
        finally {
            if (scanner != null )
            scanner.close();
        }

        return state;
    }

    /**
     *
     * Checks if the given state is in completed position or not. This method is not called from anywhere in the code as of now.
     * AnalyzeState() method also checks for game state completion.
     *
     * @param state1
     * @return
     * @throws Exception
     */
    public boolean checkStateForCompletion( StateVO state1) throws Exception
    {


        boolean incompleteFlag      = false;
        int currentBrick            = 0;
        int[][] stateArray1         = state1.getCurrentState();

        try{
            for (int i = 0; i < state1.getHeight() ; i++) {
                for (int j = 0; j < state1.getWidth(); j++) {

                    currentBrick = stateArray1[i][j];
                    if (currentBrick == -1)
                        incompleteFlag = true;
                }
            }

            if (incompleteFlag != true){
                state1.setComplete(true);

            }

        } catch ( Exception e )
        {
            e.printStackTrace();
            throw e;
        }

        return incompleteFlag;
    }


    /**
     * applyMove() performs the move operation and adds the moves performed to the movesPerformedFromInitialState arrayList.
     *
     * brickNumber: 0  - Free brick.
     * brickNumber: -1 - Goal brick.
     * brickNumber: 1  - Wall brick.
     * brickNumber: 2  - Main brick to be moved to the Goal.
     * All other brick numbers are barriers.
     *
     * @param state
     * @param fromBrickNumberX
     * @param fromBrickNumberY
     * @param toBrickNumberX
     * @param toBrickNumberY
     * @return
     * @throws Exception
     */
    public boolean applyMove( StateVO state, int fromBrickNumberX,int fromBrickNumberY , int toBrickNumberX, int toBrickNumberY) throws Exception
    {

        int[][] stateArray                                  = state.getCurrentState();
        ArrayList<String> movesPerformedFromInitialState    = state.getMovesPerformedFromInitialState();
        String direction;
        int fromBrickNumber                                 = stateArray[fromBrickNumberX][fromBrickNumberY];
        int toBrickNumber                                   = stateArray[toBrickNumberX][toBrickNumberY];

        try {

            /*
            if the fromBrickNumber is either the free brick, wall brick or goal brick then return error false.
             */
            if(fromBrickNumber == 0 || fromBrickNumber == -1 || fromBrickNumber == 1)
                return false;
            /*
            If the tobrickNumber is anything other than the free brick or goal brick, return error false.
             */
            if( toBrickNumber!=0 && toBrickNumber != -1)
                return false;
            /*
            Preventing any other brick other than the main brick to be put on the goal.
             */
            if(fromBrickNumber!=2 && toBrickNumber == -1)
                return false;

            //Performing the operation if its a legal move.
            else
            {
                if((Math.abs(fromBrickNumberX-toBrickNumberX)==1 && fromBrickNumberY == toBrickNumberY) ||
                        (Math.abs(fromBrickNumberY-toBrickNumberY)==1 && fromBrickNumberX == toBrickNumberX))
                {
                    stateArray[fromBrickNumberX][fromBrickNumberY] = 0;
                    stateArray[toBrickNumberX][toBrickNumberY] = fromBrickNumber;
                }
                else
                    return false;

            }

            /*
            Finding the move direction.
             */
            if((fromBrickNumberY == toBrickNumberY) ) {

                if( fromBrickNumberX>toBrickNumberX )
                    direction = "UP";
                else
                    direction = "DOWN";
            }
            else
            {
                if (fromBrickNumberY > toBrickNumberY)
                    direction = "LEFT";
                else
                    direction = "RIGHT";
            }

            //Adding the move to an arrayList.
            movesPerformedFromInitialState.add(getColorOfBrick(fromBrickNumber) + " - " + direction);
            /*
            Writing the updated game state.
             */
            writeGameState(state);

        } catch (Exception ex)
        {

            ex.printStackTrace();
            throw ex;
        }

        return true;
    }



    /**
     * getColorOfBrick() returns unique color (string value) depending on the brick number.
     *
     * @param brickNumber
     * @return
     */

    public String getColorOfBrick( int brickNumber )
    {
        if( brickNumber == 0 )
            return "WHITE";

        if( brickNumber == -1 )
            return "GRAY";

        if( brickNumber == 1 )
            return "PINK";

        if( brickNumber == 2 )
            return "RED";

        if( brickNumber == 3 )
            return "BLUE";

        if( brickNumber == 4 )
            return "GREEN";

        if( brickNumber == 5 )
            return "MAGENTA";

        if( brickNumber == 6 )
            return "YELLOW";

        if( brickNumber == 7 )
            return "CYAN";

        return "WHITE";

    }






}