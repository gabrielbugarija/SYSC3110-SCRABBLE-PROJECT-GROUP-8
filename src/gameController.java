import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * @author Kemal Sogut - 101280677
 *
 * This Number Controller class represents the Controller layer in teh MVC pattetn for the number the game
 *
 */

public class gameController implements ActionListener {

    gameModel model;
    public gameController(gameModel model){
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println(e.getActionCommand());

        // If PLAY button pressed.
        if (Objects.equals(e.getActionCommand(), "Swap")){
            model.advanceTurn();
        }




    }
}
