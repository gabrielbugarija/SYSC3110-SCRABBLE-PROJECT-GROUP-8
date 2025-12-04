package main.java;

import java.util.ArrayList;
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
    private int numberOfAIPlayers;

    private TileBag tileBag = new TileBag();
    public int currentPlayer = 0;

    private Board board;

    private int selectedRackIndex = -1;

    private boolean isFirstMoveDone;
    private Dictionary dictionary;

    private ArrayList<int[]> tilesPlacedThisTurn = new ArrayList<>();
    private ArrayList<int[]> tilesPlacedLastTurn = new ArrayList<>();

    /**
     * Default constructor - creates model with standard board.
     */
    public gameModel() {
        this(null);
    }

    /**
     * Constructor with board configuration.
     * @param boardConfig the board configuration to use, or null for standard
     */
    public gameModel(BoardConfig boardConfig) {
        isFirstMoveDone = false;
        views = new ArrayList<>();
        dictionary = new Dictionary();
        board = new Board(boardConfig);
    }

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

    public int getNumberOfTotalPlayers() {
        return numberOfPlayers + numberOfAIPlayers;
    }

    public Player getCurrentPlayer() {
        return playersList.get(currentPlayer);
    }

    public void setPlayersList(ArrayList<String> names) {
        for (int i = 0; i < numberOfPlayers; i++) {
            playersList.add(new Player(names.get(i), tileBag));
        }
    }

    public void setAIPlayersList() {
        int x = 1;
        for (int i = 0; i < numberOfAIPlayers; i++) {
            playersList.add(new AIPlayer("AI" + x, tileBag));
            x++;
        }
    }

    public void runAITurnsIfNeeded() {
        int total = getNumberOfTotalPlayers();
        if (total <= 0) return;

        while (getCurrentPlayer() instanceof AIPlayer) {
            AIPlayer ai = (AIPlayer) getCurrentPlayer();

            boolean moved = ai.makeMove(this);

            updateViews();

            if (moved) {
                int points = calculateScoreForCurrentMove();
                if (points > 0) {
                    ai.addScore(points);
                }
            }

            currentPlayer = (currentPlayer + 1) % total;
            tilesPlacedThisTurn.clear();
            updateViews();
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
        return board.getCell(row, col).isEmpty();
    }

    public char getCellLetter(int row, int col) {
        Cell cell = board.getCell(row, col);
        return cell.isEmpty() ? '-' : cell.getLetter();
    }

    public Cell[][] getBoard() {
        return board.getBoard();
    }

    public void addGameView(gameView view) {
        views.add(view);
    }

    public void removeGameView(gameView view) {
        views.remove(view);
    }

    public void updateViews() {
        if (views == null) return;
        for (gameView view : views) {
            view.handleAdvanceTurn();
        }
    }

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

    public Tile getSelectedTile() {
        if (selectedRackIndex < 0) return null;
        if (selectedRackIndex >= getCurrentPlayer().getRack().size()) return null;
        return getCurrentPlayer().getRack().get(selectedRackIndex);
    }

    public boolean placeSelectedTileAt(int row, int col) {
        if (selectedRackIndex < 0) return false;
        if (row < 0 || row >= 15 || col < 0 || col >= 15) return false;

        Cell cell = board.getCell(row, col);
        if (!cell.isEmpty()) return false;

        Tile tile = getCurrentPlayer().getRack().get(selectedRackIndex);
        cell.setTile(tile);
        cell.setOccupied();

        tilesPlacedThisTurn.add(new int[]{row, col});

        getCurrentPlayer().removeTiles(selectedRackIndex);

        while (getCurrentPlayer().getRack().size() < 7 && tileBag.size() > 0) {
            getCurrentPlayer().getRack().add(tileBag.drawTile());
        }

        selectedRackIndex = -1;
        return true;
    }

    public void advanceTurn() {
        int total = getNumberOfTotalPlayers();
        if (total <= 0) return;

        currentPlayer = (currentPlayer + 1) % total;
        tilesPlacedThisTurn.clear();
        updateViews();

        int safety = 0;
        while (getCurrentPlayer() instanceof AIPlayer && safety < total) {
            safety++;

            AIPlayer ai = (AIPlayer) getCurrentPlayer();
            System.out.println("AI turn for " + ai.getName());

            boolean moved = ai.makeMove(this);

            updateViews();

            if (moved) {
                int points = calculateScoreForCurrentMove();
                if (points > 0) {
                    ai.addScore(points);
                    System.out.println("AI " + ai.getName() + " scored " + points + " points.");
                }
            }

            tilesPlacedThisTurn.clear();
            currentPlayer = (currentPlayer + 1) % total;
            updateViews();
        }
    }

    public int calculateScoreForCurrentMove() {
        if (tilesPlacedThisTurn.isEmpty()) {
            return 0;
        }

        int totalScore = 0;

        tilesPlacedLastTurn.clear();

        for (int[] pos : tilesPlacedThisTurn) {
            int row = pos[0];
            int col = pos[1];

            tilesPlacedLastTurn.add(new int[]{row, col});

            Cell cell = board.getCell(row, col);
            Tile tile = cell.getTile();

            if (tile == null) continue;

            int letterScore = tile.getPoints();
            int cellMultiplier = cell.getMultiplier();
            totalScore += letterScore * cellMultiplier;
        }

        tilesPlacedThisTurn.clear();
        return totalScore;
    }

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