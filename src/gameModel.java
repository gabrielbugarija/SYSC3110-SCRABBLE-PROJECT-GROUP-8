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

    private int numberOfPlayers;      // human players
    private int numberOfAIPlayers;    // AI players

    private TileBag tileBag = new TileBag();
    public int currentPlayer = 0;

    // Use Board class as single source of truth (includes premium squares)
    private Board board = new Board();

    // rack/tile selection
    private int selectedRackIndex = -1;

    // game state flags
    private boolean isFirstMoveDone;
    private Dictionary dictionary;

    // tiles placed during the current turn: [row, col]
    private ArrayList<int[]> tilesPlacedThisTurn = new ArrayList<>();

    private ArrayList<int[]> tilesPlacedLastTurn = new ArrayList<>();


    public gameModel() {
        isFirstMoveDone = false;
        views = new ArrayList<>();
        dictionary = new Dictionary();

        // Board constructor already sets up cells and premium squares
        board = new Board();
    }

    // ==== basic game state ====

    public void setFirstMoveDone() {
        this.isFirstMoveDone = true;
    }

    public boolean isFirstMoveDone() {
        return isFirstMoveDone;
    }

    public void setNumberOfPlayers(int i) {
        this.numberOfPlayers = i;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public void setNumberOfAIPlayers(int aiCount) {
        this.numberOfAIPlayers = aiCount;
    }

    public int getNumberOfAIPlayers() {
        return numberOfAIPlayers;
    }

    /** total humans + AI – used for turn rotation */
    public int getNumberOfTotalPlayers() {
        return numberOfPlayers + numberOfAIPlayers;
    }

    public Player getCurrentPlayer() {
        return playersList.get(currentPlayer);
    }

    // ==== players (human + AI) ====

    /** Create human players from names (called by frame). */
    public void setPlayersList(ArrayList<String> names) {
        for (int i = 0; i < numberOfPlayers; i++) {
            playersList.add(new Player(names.get(i), tileBag));
        }
    }

    /** Append AI players at the end of playersList. */
    public void setAIPlayersList() {
        int x = 1;
        for (int i = 0; i < numberOfAIPlayers; i++) {
            playersList.add(new AIPlayer("AI" + x, tileBag));
            x++;
        }
    }

    /**
     * If the current player is an AI, let it automatically take its turn.
     * This will also chain through multiple AIs in a row if there are several.
     */
    public void runAITurnsIfNeeded() {
        int total = getNumberOfTotalPlayers();
        if (total <= 0) return;

        // Loop in case there are multiple AIs back-to-back
        while (getCurrentPlayer() instanceof AIPlayer) {
            AIPlayer ai = (AIPlayer) getCurrentPlayer();

            // Let AI place tiles on the board
            boolean moved = ai.makeMove(this);

            // Refresh board/racks to show the AI move
            updateViews();

            if (moved) {
                // Score the AI's move using the same logic as human "Play"
                int points = calculateScoreForCurrentMove();
                if (points > 0) {
                    ai.addScore(points);
                }
            }

            // Move on to next player
            currentPlayer = (currentPlayer + 1) % total;
            tilesPlacedThisTurn.clear();
            updateViews();
        }
    }


    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    public void play(int x) {
        // not used in GUI version
    }

    public void endGame() {
        // hook for end-game logic if needed
    }

    public void updateStatus() {
        // left for console version; GUI uses updateViews()
    }

    // ==== board access ====

    /** Is the cell at (row, col) empty? */
    public boolean isCellEmpty(int row, int col) {
        return board.getCell(row, col).isEmpty();
    }

    /** Returns '-' if empty, otherwise the cell's letter (handles blanks via Cell.getLetter()). */
    public char getCellLetter(int row, int col) {
        Cell cell = board.getCell(row, col);
        return cell.isEmpty() ? '-' : cell.getLetter();
    }

    /**
     * Expose the underlying Cell[][] for the GUI board drawing.
     * Board already has all the premium squares initialized.
     */
    public Cell[][] getBoard() {
        return board.getBoard();
    }

    // ==== MVC views ====

    public void addGameView(gameView view) {
        views.add(view);
    }

    public void removeGameView(gameView view) {
        views.remove(view);
    }

    /** Notify all views that we advanced the turn / state changed. */
    public void updateViews() {
        if (views == null) return;
        for (gameView view : views) {
            view.handleAdvanceTurn();
        }
    }

    // ==== rack / tile selection and placement ====

    /** Select a tile from the current player's rack by index. */
    public boolean selectTileIndex(int index) {
        if (index < 0 || index >= getCurrentPlayer().getRack().size()) return false;
        selectedRackIndex = index;
        return true;
    }

    public void setSelectedRackIndex(int idx) {
        this.selectedRackIndex = idx;
    }

    public int getSelectedRackIndex() {
        return selectedRackIndex;
    }

    /** Returns the currently selected tile from the rack (or null if none). */
    public Tile getSelectedTile() {
        if (selectedRackIndex < 0) return null;
        if (selectedRackIndex >= getCurrentPlayer().getRack().size()) return null;
        return getCurrentPlayer().getRack().get(selectedRackIndex);
    }

    /**
     * Place the currently selected tile at (row, col) on the board.
     * Returns true if placement succeeded.
     */
    public boolean placeSelectedTileAt(int row, int col) {
        if (selectedRackIndex < 0) return false;
        if (row < 0 || row >= 15 || col < 0 || col >= 15) return false;

        Cell cell = board.getCell(row, col);
        if (!cell.isEmpty()) return false;

        Tile tile = getCurrentPlayer().getRack().get(selectedRackIndex);
        cell.setTile(tile);
        cell.setOccupied();

        // remember position for scoring this turn
        tilesPlacedThisTurn.add(new int[]{row, col});

        // remove from rack and refill from bag
        getCurrentPlayer().removeTiles(selectedRackIndex);

        while (getCurrentPlayer().getRack().size() < 7 && tileBag.size() > 0) {
            getCurrentPlayer().getRack().add(tileBag.drawTile());
        }

        selectedRackIndex = -1;
        return true;
    }

    // ==== turn / scoring ====

    /**
     * Advance to next player's turn and notify views.
     * Uses total (human + AI) players.
     */
    public void advanceTurn() {
        int total = getNumberOfTotalPlayers();
        if (total <= 0) return;

        // Move to the next player
        currentPlayer = (currentPlayer + 1) % total;
        tilesPlacedThisTurn.clear();
        updateViews();

        // If the next player(s) are AI, let them play automatically
        int safety = 0; // to avoid infinite loops if all players are AI
        while (getCurrentPlayer() instanceof AIPlayer && safety < total) {
            safety++;

            AIPlayer ai = (AIPlayer) getCurrentPlayer();
            System.out.println("AI turn for " + ai.getName());

            boolean moved = ai.makeMove(this);

            // Update board/racks so GUI shows the move
            updateViews();

            if (moved) {
                int points = calculateScoreForCurrentMove();
                if (points > 0) {
                    ai.addScore(points);
                    System.out.println("AI " + ai.getName() + " scored " + points + " points.");
                }
            }

            // Prepare for next player
            tilesPlacedThisTurn.clear();
            currentPlayer = (currentPlayer + 1) % total;
            updateViews();
        }
    }


    /**
     * Calculates the score for tiles placed in the current turn.
     *
     * Simplified scoring:
     * - Sums letter scores of tiles placed this turn.
     * - Multiplies each letter by the cell multiplier.
     * - Blanks are worth 0 via Tile.getPoints().
     * - Does NOT yet handle full Scrabble word-scoring or cross words.
     */
    public int calculateScoreForCurrentMove() {
        if (tilesPlacedThisTurn.isEmpty()) {
            return 0;
        }

        int totalScore = 0;

        // remember these positions as the "last move" before we clear them
        tilesPlacedLastTurn.clear();

        for (int[] pos : tilesPlacedThisTurn) {
            int row = pos[0];
            int col = pos[1];

            // store for AI
            tilesPlacedLastTurn.add(new int[]{row, col});

            Cell cell = board.getCell(row, col);
            Tile tile = cell.getTile();

            if (tile == null) continue;

            int letterScore = tile.getPoints(); // blanks already return 0
            int cellMultiplier = cell.getMultiplier();
            totalScore += letterScore * cellMultiplier;
        }

        // we used these positions to score this move
        tilesPlacedThisTurn.clear();
        return totalScore;
    }

    // ==== helpers for controller / view ====

    public TileBag getTileBag() {
        return this.tileBag;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public ArrayList<int[]> getTilesPlacedLastTurn() {
        return tilesPlacedLastTurn;
    }

}
