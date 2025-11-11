import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;



/**
 * @author Kemal Sogut - 101280677
 *
 * This Number Controller class represents the Controller layer in teh MVC pattetn for the number the game
 *
 */

public class gameController implements ActionListener {

    private boolean swapMode = false;
    ArrayList<Integer> tilesToSwap = new ArrayList<>();
    gameModel model;
    public gameController(gameModel model){
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        System.out.println(command);

        // If Swap button pressed.
        if (command.equals("Swap")) {

            if (!swapMode) {
                System.out.println("Swap mode entered");
                // Enter swap mode
                swapMode = true;
                tilesToSwap.clear();

            } else {
                System.out.println(tilesToSwap);
                Player cp = model.getCurrentPlayer();
                System.out.println("Swap mode exited");
                // perform tiles swap.
                for (Integer i: tilesToSwap){
                    Tile tile = cp.getRack().get(i);
                    model.getTileBag().addTile(tile);
                    cp.removeTiles(i);
                }
                cp.drawTiles();
                model.advanceTurn();
            }
        }

        // If Swap button pressed.
        if (Objects.equals(command, "Pass")){
            model.advanceTurn();
        }

        if (command.startsWith("Tile: ")) {
            int idx = Integer.parseInt(command.substring(6).trim());
            if (swapMode){
                System.out.println("Tiles to Swap: " + idx);
                tilesToSwap.add(idx);
                System.out.println(tilesToSwap);
            }
            else {
                model.selectTileIndex(idx);
                System.out.println("Selected tile index: " + idx);
                return;
            }
            }


        if (command.matches("\\d+ \\d+")) {
            String[] parts = command.split(" ");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);

            if (model.placeSelectedTileAt(row, col)) {
                System.out.println("Placed tile on board.");
                model.updateViews();
            } else {
                System.out.println("Could not place tile.");
            }
            model.updateViews();

            return;
        }

        if (command.contains(" ")) {
            if(swapMode){
                return;
            }
            String[] rc = command.split(" ");
            int r = Integer.parseInt(rc[0]);
            int c = Integer.parseInt(rc[1]);
            if (model.placeSelectedTileAt(r, c)) {
            }

        }


    }
}
