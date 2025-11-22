import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Kemal Sogut - 101280677
 *
 * This Number Controller class represents the Controller layer in the MVC pattern for the number the game
 *
 */

public class gameController implements ActionListener {

    private boolean swapMode = false;
    ArrayList<Integer> tilesToSwap = new ArrayList<>();
    ArrayList<PlacedTile> tilesPlacedThisTurn = new ArrayList<>();
    gameModel model;

    // Helper class to track placed tiles
    private class PlacedTile {
        int row;
        int col;
        char letter;

        PlacedTile(int row, int col, char letter) {
            this.row = row;
            this.col = col;
            this.letter = letter;
        }
    }

    public gameController(gameModel model){
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        System.out.println(command);

        if (command.equals("Play")){
            if (tilesPlacedThisTurn.isEmpty()) {
                System.out.println("No tiles placed this turn!");
                // Optionally show error message to user
                return;
            }

            // Validate the placement
            if (validatePlacement()) {
                System.out.println("Valid word(s) formed!");
                // Finalize the turn
                Player currentPlayer = model.getCurrentPlayer();
                currentPlayer.drawTiles(); // Draw new tiles to fill rack
                tilesPlacedThisTurn.clear(); // Clear for next turn
                model.advanceTurn();
                model.updateViews();
            } else {
                System.out.println("Invalid word placement!");
                // Optionally: revert tiles back to rack
                revertPlacement();
            }
        }


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
                swapMode = false;
                cp.drawTiles();
                model.advanceTurn();
            }
        }

        // If Pass button pressed.
        if (Objects.equals(command, "Pass")){
            tilesPlacedThisTurn.clear(); // Clear any placed tiles
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
                // Track the placed tile
                Cell[][] board = model.getBoard();
                Tile placedTile = board[row][col].getTile();
                tilesPlacedThisTurn.add(new PlacedTile(row, col, placedTile.getLetter()));
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
                // Track the placed tile
                Cell[][] board = model.getBoard();
                Tile placedTile = board[r][c].getTile();
                tilesPlacedThisTurn.add(new PlacedTile(r, c, placedTile.getLetter()));
            }
        }


    }

    /**
     * Validates all words formed by the tiles placed this turn
     */
    private boolean validatePlacement() {
        if (tilesPlacedThisTurn.isEmpty()) {
            return false;
        }

        if(!model.isFirstMoveDone()){
            boolean foundCenter = false;
            for (int i = 0; i < tilesPlacedThisTurn.size(); i++) {
                if(tilesPlacedThisTurn.get(i).col == 7 && tilesPlacedThisTurn.get(i).row == 7){
                    foundCenter = true;
                    break;
                }
            }
            if(!foundCenter){
                System.out.println("First move must include the center cell (7, 7)!");
                return false;
            }
        }



        // Check if all tiles are in one line (row or column)
        if (!aretilesInLine()) {
            System.out.println("Tiles must be placed in a single row or column!");
            return false;
        }

        // Determine orientation
        boolean isHorizontal = aretilesHorizontal();

        // Get the main word formed
        String mainWord = getMainWord(isHorizontal);
        if (mainWord.length() < 2) {
            System.out.println("Word must be at least 2 letters!");
            return false;
        }

        System.out.println("Main word formed: " + mainWord);

        // Validate main word (you'll need to check against dictionary)
        if (!isValidWord(mainWord)) {
            System.out.println("'" + mainWord + "' is not a valid word!");
            return false;
        }

        // Check for perpendicular words formed
        ArrayList<String> perpendicularWords = getPerpendicularWords(isHorizontal);
        for (String word : perpendicularWords) {
            System.out.println("Perpendicular word formed: " + word);
            if (!isValidWord(word)) {
                System.out.println("'" + word + "' is not a valid word!");
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if all placed tiles are in a single row or column
     */
    private boolean aretilesInLine() {
        if (tilesPlacedThisTurn.size() == 1) {
            return true; // Single tile is always valid
        }

        boolean sameRow = true;
        boolean sameCol = true;

        int firstRow = tilesPlacedThisTurn.get(0).row;
        int firstCol = tilesPlacedThisTurn.get(0).col;

        for (PlacedTile pt : tilesPlacedThisTurn) {
            if (pt.row != firstRow) sameRow = false;
            if (pt.col != firstCol) sameCol = false;
        }

        return sameRow || sameCol;
    }

    /**
     * Determines if tiles are placed horizontally
     */
    private boolean aretilesHorizontal() {
        if (tilesPlacedThisTurn.size() == 1) {
            // Check if there are adjacent tiles
            int row = tilesPlacedThisTurn.get(0).row;
            int col = tilesPlacedThisTurn.get(0).col;
            Cell[][] board = model.getBoard();

            boolean hasHorizontalNeighbor =
                    (col > 0 && !board[row][col - 1].isEmpty()) ||
                            (col < 14 && !board[row][col + 1].isEmpty());

            boolean hasVerticalNeighbor =
                    (row > 0 && !board[row - 1][col].isEmpty()) ||
                            (row < 14 && !board[row + 1][col].isEmpty());

            return hasHorizontalNeighbor; // Default to horizontal if both or neither
        }

        int firstRow = tilesPlacedThisTurn.get(0).row;
        int secondRow = tilesPlacedThisTurn.get(1).row;

        return firstRow == secondRow;
    }

    /**
     * Extracts the main word formed by placed tiles
     */
    private String getMainWord(boolean isHorizontal) {
        Cell[][] board = model.getBoard();

        if (isHorizontal) {
            // Find leftmost and rightmost positions
            int row = tilesPlacedThisTurn.get(0).row;
            int minCol = tilesPlacedThisTurn.get(0).col;
            int maxCol = minCol;

            for (PlacedTile pt : tilesPlacedThisTurn) {
                minCol = Math.min(minCol, pt.col);
                maxCol = Math.max(maxCol, pt.col);
            }

            // Extend to the left
            while (minCol > 0 && !board[row][minCol - 1].isEmpty()) {
                minCol--;
            }

            // Extend to the right
            while (maxCol < 14 && !board[row][maxCol + 1].isEmpty()) {
                maxCol++;
            }

            // Build the word
            StringBuilder word = new StringBuilder();
            for (int col = minCol; col <= maxCol; col++) {
                if (!board[row][col].isEmpty()) {
                    word.append(board[row][col].getTile().getLetter());
                }
            }

            return word.toString();

        } else {
            // Vertical word
            int col = tilesPlacedThisTurn.get(0).col;
            int minRow = tilesPlacedThisTurn.get(0).row;
            int maxRow = minRow;

            for (PlacedTile pt : tilesPlacedThisTurn) {
                minRow = Math.min(minRow, pt.row);
                maxRow = Math.max(maxRow, pt.row);
            }

            // Extend upward
            while (minRow > 0 && !board[minRow - 1][col].isEmpty()) {
                minRow--;
            }

            // Extend downward
            while (maxRow < 14 && !board[maxRow + 1][col].isEmpty()) {
                maxRow++;
            }

            // Build the word
            StringBuilder word = new StringBuilder();
            for (int row = minRow; row <= maxRow; row++) {
                if (!board[row][col].isEmpty()) {
                    word.append(board[row][col].getTile().getLetter());
                }
            }

            return word.toString();
        }
    }

    /**
     * Gets all perpendicular words formed by the placement
     */
    private ArrayList<String> getPerpendicularWords(boolean mainIsHorizontal) {
        ArrayList<String> words = new ArrayList<>();


        for (PlacedTile pt : tilesPlacedThisTurn) {
            String perpendicularWord;

            if (mainIsHorizontal) {
                // Check vertical word at this position
                perpendicularWord = getVerticalWordAt(pt.row, pt.col);
            } else {
                // Check horizontal word at this position
                perpendicularWord = getHorizontalWordAt(pt.row, pt.col);
            }

            if (perpendicularWord.length() > 1) {
                words.add(perpendicularWord);
            }
        }

        return words;
    }

    private String getVerticalWordAt(int row, int col) {
        Cell[][] board = model.getBoard();
        int minRow = row;
        int maxRow = row;

        // Extend upward
        while (minRow > 0 && !board[minRow - 1][col].isEmpty()) {
            minRow--;
        }

        // Extend downward
        while (maxRow < 14 && !board[maxRow + 1][col].isEmpty()) {
            maxRow++;
        }

        // Build word
        StringBuilder word = new StringBuilder();
        for (int r = minRow; r <= maxRow; r++) {
            if (!board[r][col].isEmpty()) {
                word.append(board[r][col].getTile().getLetter());
            }
        }

        return word.toString();

    }

    private String getHorizontalWordAt(int row, int col) {
        Cell[][] board = model.getBoard();
        int minCol = col;
        int maxCol = col;

        // Extend left
        while (minCol > 0 && !board[row][minCol - 1].isEmpty()) {
            minCol--;
        }

        // Extend right
        while (maxCol < 14 && !board[row][maxCol + 1].isEmpty()) {
            maxCol++;
        }

        // Build word
        StringBuilder word = new StringBuilder();
        for (int c = minCol; c <= maxCol; c++) {
            if (!board[row][c].isEmpty()) {
                word.append(board[row][c].getTile().getLetter());
            }
        }

        return word.toString();
    }

    /**
     * Validates a word against dictionary
     * You'll need to implement this with your dictionary
     */
    private boolean isValidWord(String word) {
        // TODO: Implement dictionary check
        // For now, return true for testing
        // You should check against a dictionary file or data structure
        //return model.getDictionary().isValidWord(word.toUpperCase());
        return true;
    }

    /**
     * Reverts the placement if validation fails
     */
    private void revertPlacement() {

        Player currentPlayer = model.getCurrentPlayer();
        Cell[][] board = model.getBoard();

        for (PlacedTile pt : tilesPlacedThisTurn) {
            // Remove tile from board and return to player's rack
            Tile tile = board[pt.row][pt.col].getTile();
            board[pt.row][pt.col].setEmpty();
            currentPlayer.getRack().add(tile); // Add tile back to player's rack
        }

        tilesPlacedThisTurn.clear();
        model.updateViews();

    }
}