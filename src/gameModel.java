import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Kemal Sogut - 101280677
 *
 * This Model class represents the MODEL layer in the MVC pattern for the number the game.
 * It is a representation of the object of the game.
 *
 */

public class gameModel {

    private ArrayList<Player> playersList = new ArrayList<>();
    public List<gameView> views;
    private int numberOfPlayers;
    private TileBag tileBag = new TileBag();
    public int currentPlayer = 0;
    private Cell[][] board = new Cell[15][15];
    private int selectedRackIndex = -1;
    private ArrayList<Integer> currentMoveRows = new ArrayList<>();
    private ArrayList<Integer> currentMoveCols = new ArrayList<>();
    private ArrayList<Tile> currentMoveTiles = new ArrayList<>();


    public gameModel() {
        views = new ArrayList<>();
        // Initialize board in constructor
        for (int r = 0; r < 15; r++) {
            for (int c = 0; c < 15; c++) {
                board[r][c] = new Cell();
            }
        }
    }

    public Player getCurrentPlayer() {
        return playersList.get(currentPlayer);
    }

    public void setNumberOfPlayers(int i) {
        this.numberOfPlayers = i;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public void setPlayersList(ArrayList<String> names) {
        for (int i = 0; i < numberOfPlayers; i++) {
            playersList.add(new Player(names.get(i), tileBag));
        }
    }

    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    public void play(int x) {

    }

    public void endGame() {

    }

    public void updateStatus() {

    }

    public boolean isCellEmpty(int row, int col) {
        return board[row][col].isEmpty();
    }

    public void addGameView(gameView view) {
        views.add(view);
    }

    public char getCellLetter(int row, int col) {
        return board[row][col].isEmpty() ? '-' : board[row][col].getLetter();
    }

    public boolean selectTileIndex(int index) {
        if (index < 0 || index >= getCurrentPlayer().getRack().size()) return false;
        selectedRackIndex = index;
        return true;
    }

    public Tile getSelectedTile() {
        if (selectedRackIndex < 0) return null;
        if (selectedRackIndex >= getCurrentPlayer().getRack().size()) return null;
        return getCurrentPlayer().getRack().get(selectedRackIndex);
    }

    public boolean placeSelectedTileAt(int row, int col) {
        if (selectedRackIndex < 0) return false;
        if (row < 0 || row >= 15 || col < 0 || col >= 15) return false;
        Cell cell = board[row][col];
        if (!cell.isEmpty()) return false;

        Tile tile = getCurrentPlayer().getRack().get(selectedRackIndex);
        cell.setTile(tile);
        cell.setOccupied();

        currentMoveRows.add(row);
        currentMoveCols.add(col);
        currentMoveTiles.add(tile);

        getCurrentPlayer().removeTiles(selectedRackIndex);

        while (getCurrentPlayer().getRack().size() < 7 && tileBag.size() > 0) {
            getCurrentPlayer().getRack().add(tileBag.drawTile());
        }

        selectedRackIndex = -1;
        return true;
    }

    public void removeGameView(gameView view) {
        views.remove(view);
    }

    public void advanceTurn() {
        // next player index.
        currentPlayer = (currentPlayer + 1) % numberOfPlayers;
        clearCurrentMove();
        updateViews();
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setSelectedRackIndex(int idx) {
        this.selectedRackIndex = idx;
    }

    public int getSelectedRackIndex() {
        return selectedRackIndex;
    }

    public void updateViews() {
        for (gameView view : views) {
            view.handleAdvanceTurn();
        }
    }

    public void clearCurrentMove() {
        currentMoveRows.clear();
        currentMoveCols.clear();
        currentMoveTiles.clear();
    }

    public int calculateScoreForCurrentMove() {
        // No tiles placed this turn
        if (currentMoveTiles.isEmpty()) {
            return 0;
        }

        // Determine direction: 0 = horizontal (same row), 1 = vertical (same col)
        boolean sameRow = true;
        boolean sameCol = true;

        int firstRow = currentMoveRows.get(0);
        int firstCol = currentMoveCols.get(0);

        for (int i = 1; i < currentMoveRows.size(); i++) {
            if (currentMoveRows.get(i) != firstRow) {
                sameRow = false;
            }
            if (currentMoveCols.get(i) != firstCol) {
                sameCol = false;
            }
        }

        int direction;
        if (sameRow && !sameCol) {
            direction = 0; // horizontal word
        } else if (!sameRow && sameCol) {
            direction = 1; // vertical word
        } else if (currentMoveTiles.size() == 1) {
            // Single tile: allow it, treat as horizontal
            direction = 0;
        } else {
            // Tiles not in a straight line -> invalid
            return 0;
        }

        StringBuilder word = new StringBuilder();
        int score = 0;
        Dictionary dictionary = new Dictionary();

        if (direction == 0) {
            // Horizontal: fixed row, varying column
            int row = firstRow;

            // Start from the leftmost continuous tile of the whole word
            int startCol = Collections.min(currentMoveCols);
            while (startCol > 0 && !board[row][startCol - 1].isEmpty()) {
                startCol--;
            }

            int col = startCol;
            while (col < 15 && !board[row][col].isEmpty()) {
                Cell cell = board[row][col];
                word.append(cell.getLetter());
                score += cell.getTilePoints() * cell.getMultiplier();
                col++;
            }
        } else {
            // Vertical: fixed column, varying row
            int col = firstCol;

            int startRow = Collections.min(currentMoveRows);
            while (startRow > 0 && !board[startRow - 1][col].isEmpty()) {
                startRow--;
            }

            int row = startRow;
            while (row < 15 && !board[row][col].isEmpty()) {
                Cell cell = board[row][col];
                word.append(cell.getLetter());
                score += cell.getTilePoints() * cell.getMultiplier();
                row++;
            }
        }

        String finalWord = word.toString().toLowerCase();

        // Use your Dictionary to check if the word is valid
        if (!dictionary.isValidWord(finalWord)) {
            return 0;
        }

        return score;
    }

}


