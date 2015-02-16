# SlidingBrickPuzzle

User Manual:

   When the code is executed, it comes up with a Sliding Brick Puzzle frame, on which you will see a matrix of bricks. The objective is to move the red brick onto the gray brick. The bricks can be moved only onto a free white space, and they can be moved only one brick - one position at a time. Every brick other than the pink walls and the gray goal brick can be moved one at a time. Only vertical and horizontal movements permitted.

At the bottom, you will see a Number of Moves and on the right you will see a JtextArea that lists the moves performed so far.
At the bottom, you will also see a count down timer that starts at 20 seconds. You have 20 seconds to finish the game, else you will get an error and the game board is reset. 
There is a reset button in the bottom, which when clicked will reset the game board, number of moves, list of moves and the 20 second timer  at any time. 

The timer is controlled using a background thread that is executed using the Swing Worker class. Every 20 seconds, the back ground thread resets the board if the game is not completed within that time. If you do complete the game within the time frame, you can reset the game board and the variables by clicking the 'RESET GAME' button at the bottom. 

I have used SwingWorker and SwingUtilities in the code. InelliJ was used to do the coding. The code is heavily commented to explain what is being done. 

-------------------------------------------------------------------------------------------------

Compile the files in the following order:

javac StateVO.java            
javac ProcessClass.java       
javac SlidingBrickPuzzle.java 
javac BrickGameFrame.java     

Please place the SBP-level0.txt file in the same directory as the code files. The initial game state is read from this text file. Code will error out without this file. You will see gamestate.txt and output.log files being generated while the code runs. They are essential for the functionality.
