import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

/**
 * Author 		: Renjith J Ephrem
 * Email  		: rje49@drexel.edu
 * Class Name   : BrickGameFrame.java
 *
 * This is the swing class for the sliding brick puzzle game.
 *
 */
public class BrickGameFrame extends JPanel {

    /*
        Initializing all the variables needed for the swing class.
     */
    private JFrame      levelFrame;
    private JPanel      levelPanel;
    private JPanel      buttonPanel;
    private JButton     resetButton;
    private JSplitPane  splitPane;
    private JTextArea   textArea;
    private JLabel      instructionsLabel;
    private GamePane    gamePane;

    private static final int CELL_SIZE  = 50;
    String fileName                     = "SBP-level0.txt";
    String gameStateFile                = "gamestate.txt";
    String textAreaText                 = " Moves so far : ";
    private int timeLeft                = 20;
    private boolean gameCompleted       = false;
    private boolean gameReset           = false;
    int movesCount                      = 0;
    private static ProcessClass process = new ProcessClass();
    private StateVO statevo;

    public BrickGameFrame() {
        initComponents();
    }

    /**
     *
     * initComponents - needed to initialize the variables and setting the layout and initial display text.
     *
     */
    private void initComponents() {

        levelFrame          = new JFrame("SLIDING BRICK GAME!");
        levelPanel          = new JPanel();
        buttonPanel         = new JPanel();
        resetButton         = new JButton();
        splitPane           = new JSplitPane();
        textArea            = new JTextArea();
        instructionsLabel   = new JLabel();

        /*
        start method implements a SwingWorker class to do the background thread.
         */
        start();

        levelFrame.setLocationRelativeTo(null);
        levelFrame.setSize(500, 500);

        String myString = " Move the RED brick to the GRAY brick to win. Bricks can be moved only one move at a time to an adjacent WHITE slot.  ";
        instructionsLabel.setText("<html>"+ myString +"</html>");
        instructionsLabel.setFont(new Font("SansSerif", Font.BOLD, CELL_SIZE/3));
        instructionsLabel.setSize(levelPanel.getWidth(),200);

        try {
            gamePane = new GamePane();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        levelFrame.setLayout(new BorderLayout());
        levelFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        levelFrame.setResizable(false);
        levelPanel.setBorder(BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        levelPanel.setLayout(new BorderLayout());
        levelPanel.add(instructionsLabel, BorderLayout.NORTH);

        buttonPanel.setLayout(new BorderLayout());
        resetButton.setText("RESET GAME");
        buttonPanel.add(resetButton, BorderLayout.WEST);
        buttonPanel.setVisible(true);

        splitPane.setResizeWeight(0.75);

        textArea.setText(textAreaText);
        textArea.setWrapStyleWord(true);

        splitPane.setLeftComponent(gamePane);
        splitPane.setRightComponent(textArea);

        levelPanel.add(splitPane);
        levelPanel.add(resetButton, BorderLayout.SOUTH);

        levelFrame.add(levelPanel, BorderLayout.CENTER);
        levelFrame.setVisible(true);

        /*
        Action listener the RESET button. It resets the movesCount and the timeLeft and starts a new background thread.
         */
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                //gameReset boolean is used to restart the timer and counter when the board is reset.
                gameReset   = true;
                movesCount  = 0;
                timeLeft    = 20;

                /*
                If the game has already been completed before clicking the reset button, then we start a new background thread.
                Else, we reset the board and continue on the thread that was ran before.
                 */
                if(gameCompleted) {
                    gameCompleted = false;
                    start();
                }

                try {
                    /*
                    Loads the game state again from the input text file to reset the board.
                     */
                    StateVO stateForReset = process.getInitialGameState(fileName, null);
                    process.writeGameState(stateForReset); //Writes the game state onto a temporary text file.

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Clearing the moves made in the previous attempt before repainting the panel.
                statevo.getMovesPerformedFromInitialState().clear();
                textAreaText     = " Moves so far : ";
                gamePane.repaint();
            }
        });
    }

    /**
     *
     * GamePane class is used to initialize and draw the game board onto the main Panel.
     *
     */
    public class GamePane extends JPanel implements MouseListener {

        public int ROWS;
        public int COLS;
        private JPanel bottomPanel                  = null;
        private JLabel counterLabel                 = null;
        private JLabel timerLabel                   = null;
        public ArrayList<Integer> indexArrayList    = new ArrayList<Integer>();
        int[][] stateArray                          = null;
        private static final int CELL_SIZE          = 50;


        public GamePane() {

            counterLabel    = new JLabel(); // counterLabel - for the number of moves.
            timerLabel      = new JLabel(); // timerLabel - for showing the timer count down.
            bottomPanel     = new JPanel();
            bottomPanel.setLayout(new BorderLayout());

            try {
                statevo = process.getInitialGameState(fileName, null);
                process.writeGameState(statevo);
            } catch (Exception e) {
                e.printStackTrace();
            }

            stateArray  = statevo.getCurrentState();
            this.ROWS   = statevo.getHeight();
            this.COLS   = statevo.getWidth();

            this.setLayout(new BorderLayout());
            this.setBackground(Color.black);

            /*
            Mouse listener is added to listen for mouse clicks.
             */
            this.addMouseListener(this);

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        /**
         * Over riding the paintComponents method to draw the game board.
         *
         * @param g
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            String labelTxt = "NUMBER OF MOVES : " + movesCount;
            String timeText = "TIME LEFT : " + timeLeft;

            /*
            After the initial execution, all the game states are loaded from gamestate.txt file.
             */
            try {
                statevo = process.getInitialGameState(gameStateFile, statevo);
            } catch (Exception e) {
                e.printStackTrace();
            }

            stateArray = statevo.getCurrentState();
            this.ROWS = statevo.getHeight();
            this.COLS = statevo.getWidth();

            counterLabel.setText(labelTxt);
            timerLabel.setText(timeText);
            textArea.setText(textAreaText);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            bottomPanel.add(counterLabel, BorderLayout.WEST);
            bottomPanel.add(timerLabel, BorderLayout.EAST);
            this.add(bottomPanel, BorderLayout.SOUTH);

            /**
             * Drawing the game board matrix.
             */
            for (int r=0; r<ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    int brickNumber = stateArray[r][c];
                    int x = (c+1) * CELL_SIZE;
                    int y = (r+1) * CELL_SIZE;
                    String text = "";
                    if (text != null) {
                        Color colorOfBrick = process.getBricksColor(brickNumber);
                        g.setColor(colorOfBrick);
                        g.fillRect(x + 2, y + 2, CELL_SIZE - 2, CELL_SIZE - 2);
                        g.setColor(Color.black);
                        g.drawString(text, x + 20, y + (3 * CELL_SIZE) / 4);
                    }
                }
            }


        }

        /**
         *
         * Listening for the mouse clicks.
         *
         * @param e
         */
        public void mousePressed(MouseEvent e) {
            //--- map x,y coordinates into a row and col.
            int col = (e.getX())/CELL_SIZE - 1;
            int row = (e.getY())/CELL_SIZE - 1;

            int fromBrickNumberX, fromBrickNumberY, toBrickNumberX, toBrickNumberY;

            //Adding the indexes of the mouse clicks to the arrayList.
            indexArrayList.add(row);
            indexArrayList.add(col);

            //When there are two clicks, the size of the arrayList becomes 4. Then the processing is done.
            if(indexArrayList.size()==4) {

                fromBrickNumberX    = indexArrayList.get(0);
                fromBrickNumberY    = indexArrayList.get(1);
                toBrickNumberX      = indexArrayList.get(2);
                toBrickNumberY      = indexArrayList.get(3);

                try {

                    /*
                    Checking if the game has been completed.
                     */
                    if(!process.checkStateForCompletion(statevo)) {
                        statevo.getMovesPerformedFromInitialState().add("GAME COMPLETED!!");
                        gameCompleted = true;
                        JOptionPane.showMessageDialog(null,"GAME COMPLETED!!","",JOptionPane.INFORMATION_MESSAGE);
                    }
                    //Applying the move if not completed.
                    else {
                        if (!process.applyMove(statevo, fromBrickNumberX, fromBrickNumberY, toBrickNumberX, toBrickNumberY)) {
                            JOptionPane.showMessageDialog(null,"INVALID MOVE","",JOptionPane.ERROR_MESSAGE);
                        } else {
                            movesCount++; //Incrementing the number of moves.
                        }
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                //Clearing the arrayList to wait for the next two clicks.
                indexArrayList.clear();

                try {
                    //If game state complete, a pop up dialog box is given.
                    if(!process.checkStateForCompletion(statevo)) {
                        gameCompleted = true;
                        statevo.getMovesPerformedFromInitialState().add("GAME COMPLETED!!");
                        JOptionPane.showMessageDialog(null,"GAME COMPLETED!!","",JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                /*
                Adding the moves performed so far to show in the text area.
                 */
                textAreaText = " Moves so far : ";
                for (int i = 0; i < statevo.getMovesPerformedFromInitialState().size(); i++) {
                    textAreaText += "\n";
                    textAreaText += statevo.getMovesPerformedFromInitialState().get(i);

                }
                this.repaint();
            }


        }//end mousePressed

        /*
        Rest of the mouse events are ignored.
         */
        public void mouseClicked (MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered (MouseEvent e) {}
        public void mouseExited  (MouseEvent e) {}



    }

    /**
     *
     * start method implements the SwingWorker class to do the functions in the background.
     *
     */
    private void start(){

        final SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {

            /**
             * doInBackground() runs till the timer expires and then breaks out of the loop after 20 seconds.
             *
             * @return
             * @throws Exception
             */
            @Override
            protected Boolean doInBackground() throws Exception {

                Date dtStart = new java.util.Date();
                Date baseDate = new Date();

                while(true)
                {
                    /*
                    If the game reset button has been pressed, then new time has to be retrieved.
                     */
                    if(gameReset) {
                        dtStart = new java.util.Date();
                        baseDate = new Date();
                        gameReset = false;
                    }

                    Date dtCurrent = new Date();
                    /*
                    Each second, the timeLeft is decremented and the panel redrawn.
                     */
                    if(dtCurrent.getTime() - dtStart.getTime() > 1000)
                    {
                        dtStart = new java.util.Date();
                        --timeLeft;

                        if(!gameCompleted)
                            gamePane.repaint();
                    }

                    /*
                    When timer reaches 20 seconds, the background thread is broken out of, and the control goes to the over ridden done() method.
                     */
                    if(dtCurrent.getTime()-baseDate.getTime() > 20000) {

                        baseDate = new Date();
                        break;
                    }
                }
                return true;
            }

            /**
             * done() method called when the background processing is done. If the game is not completed yet, then the error message is shown.
             */
            @Override
            protected void done() {
                super.done();

                try {

                    /*
                    If the game is not completed yet, then the error message is shown.
                     */
                    if(!gameCompleted) {

                        textAreaText = "You could not finish in the allocated time!! \nTry Again!";
                        movesCount = 0;
                        timeLeft = 20;

                        StateVO stateForReset = process.getInitialGameState(fileName, null);
                        process.writeGameState(stateForReset);

                        statevo.getMovesPerformedFromInitialState().clear();
                        gamePane.repaint();

                        JOptionPane.showMessageDialog(null,"You are out of time!! Try Again!","",JOptionPane.ERROR_MESSAGE);

                        //Restarting the thread since game has not been completed yet.
                        start();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}
