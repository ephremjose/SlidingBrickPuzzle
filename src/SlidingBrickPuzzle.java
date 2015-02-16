
/**
 * Created by REPHREM on 2/8/2015.
 */
import javax.swing.*;

///////////////////////////////////////////// class SlidePuzzle
class SlidingBrickPuzzle {
    //============================================= method main
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    new BrickGameFrame();
                }
            });

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}//endclass SlidingBrickPuzzle
