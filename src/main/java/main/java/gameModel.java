package main.java;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.*;
import javax.swing.*;

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

    private int turnNumber = 0;

    private BoardConfig config;
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
        config = boardConfig;
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

    public Player getCurrentPlayer() { return playersList.get(currentPlayer);}

    public int getCurrentPlayerInt() {return currentPlayer;}

    public int getTurnNumber() {return turnNumber;}

    public BoardConfig getBoardConfig(){return config;}

    public ArrayList<int[]> getTilesPlacedThisTurn(){return tilesPlacedThisTurn;}

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

    public Board getBoardNoCell() {return board;}

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
            turnNumber++;
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

    public boolean saveGame(String filePath) {
        try {
            GameState gameState = new GameState(
                    playersList,
                    board,
                    currentPlayer,
                    tileBag,
                    isFirstMoveDone,
                    turnNumber,
                    config);

            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());

            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
                oos.writeObject(gameState);
            }

            System.out.println("Saved game state to " + filePath);
            return true;

        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Failed to save game: " + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            System.err.println("Error saving game: " + e.getMessage());
            return false;
        }
    }

    public boolean loadGame(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.canRead()) {
                throw new IOException("Cannot read file: " + filePath);
            }

            GameState gameState;
            try (ObjectInputStream ois = new ObjectInputStream(
                    Files.newInputStream(Paths.get(filePath)))) {
                gameState = (GameState) ois.readObject();
            }

            if (gameState == null) {
                throw new IOException("File is empty or corrupted");
            }

            this.playersList = gameState.getPlayerList();
            this.currentPlayer = gameState.getCurrentPlayer();
            this.board = gameState.getBoard();
            this.tileBag = gameState.getTileBag();
            this.isFirstMoveDone = gameState.isFirstMoveDone();
            this.turnNumber = gameState.getTurnNumber();

            for (Player player : playersList) {
                player.setTileBag(this.tileBag);
                if (player instanceof AIPlayer) {
                    ((AIPlayer) player).reinitializeAfterLoad();
                }
            }

            System.out.println("Game loaded successfully from: " + filePath);
            updateViews();
            return true;

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Save file not found: " + filePath,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IOException e) {
            System.err.println("IO Error loading game: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Error reading save file: " + e.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found during deserialization: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Save file format is incompatible.",
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error during load: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Failed to load game: " + e.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String getDefaultSaveDirectory() {
        return System.getProperty("user.home") + File.separator + "ScrabbleSaves";
    }

    public String getDefaultSaveFilePath() {
        return getDefaultSaveDirectory() + File.separator + "scrabble_save.dat";
    }

    public void changeModel(GameState state){
        this.playersList = state.getPlayerList();
        this.board = state.getBoard();
        this.currentPlayer = state.getCurrentPlayer();
        this.tileBag = state.getTileBag();
        this.isFirstMoveDone = state.isFirstMoveDone();
        this.turnNumber = state.getTurnNumber();
    }
}
