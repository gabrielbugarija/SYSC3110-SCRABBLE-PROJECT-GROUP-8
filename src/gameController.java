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

        if (e.getActionCommand().startsWith("Tile: ")) {
            int idx = Integer.parseInt(e.getActionCommand().substring(6).trim());
            model.selectTileIndex(idx);
            System.out.println("Selected tile index: " + idx);
            return;
        }

        if (e.getActionCommand().matches("\\d+ \\d+")) {
            String[] parts = e.getActionCommand().split(" ");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);

            if (model.placeSelectedTileAt(row, col)) {
                System.out.println("Placed tile on board.");
            } else {
                System.out.println("Could not place tile.");
            }


            model.updateViews();

            return;
        }

        if (e.getActionCommand().contains(" ")) {
            String[] rc = e.getActionCommand().split(" ");
            int r = Integer.parseInt(rc[0]);
            int c = Integer.parseInt(rc[1]);
            if (model.placeSelectedTileAt(r, c)) {
            }

        }





    }
}
