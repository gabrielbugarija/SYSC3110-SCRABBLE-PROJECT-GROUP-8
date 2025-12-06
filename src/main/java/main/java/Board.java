package main.java; /// @author Kemal Sogut - 101280677

import java.io.Serializable;

/**
 * Represents a game board composed of a 15x15 grid of {@link Cell} objects.
 */
public class Board implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The 15x15 grid of cells that make up the board. */
    private Cell[][] board;

    /** Flag indicating whether the board is currently empty (no tiles placed). */
    private boolean empty;

    /**
     * Default constructor – uses the standard board layout.
     */
    public Board() {
        this(null); // delegate to the other constructor
    }

    /**
     * Constructs a new Board and initializes all cells as empty.
     * If boardConfig is null, uses the standard Scrabble layout.
     * If boardConfig is provided, uses its premium squares instead.
     */
    public Board(BoardConfig boardConfig) {
        // We keep a fixed 15x15 for all boards in this project
        this.board = new Cell[15][15];
        initBoard();

        if (boardConfig == null) {
            // Standard layout (what you already had)
            initPremiumSquares();
        } else {
            // Custom layout from JSON
            applyPremiumLayout(boardConfig);
        }

        this.empty = true;
    }

    /**
     * Applies premium squares from a BoardConfig (loaded from JSON).
     */
    private void applyPremiumLayout(BoardConfig config) {
        if (config == null || config.getPremiumSquares() == null) {
            return;
        }

        for (BoardConfig.PremiumSquareConfig ps : config.getPremiumSquares()) {
            // Safety: clamp to board range and assume 15x15 boards
            if (ps.row < 0 || ps.row >= 15 || ps.col < 0 || ps.col >= 15) {
                continue;
            }
            boolean isWord = ps.type != null && ps.type.equalsIgnoreCase("WORD");
            setPremiumSquare(ps.row, ps.col, ps.multiplier, isWord);
        }
    }



    /**
     * Initializes all premium squares for the standard Scrabble board.
     */
    private void initPremiumSquares() {
        //Triple Word Scores
        setPremiumSquare(0, 0, 3, true);
        setPremiumSquare(0, 7, 3, true);
        setPremiumSquare(0, 14, 3, true);
        setPremiumSquare(7, 0, 3, true);
        setPremiumSquare(7, 14, 3, true);
        setPremiumSquare(14, 0, 3, true);
        setPremiumSquare(14, 7, 3, true);
        setPremiumSquare(14, 14, 3, true);

        //Double Word Scores
        setPremiumSquare(1, 1, 2, true);
        setPremiumSquare(1, 13, 2, true);
        setPremiumSquare(2, 2, 2, true);
        setPremiumSquare(2, 12, 2, true);
        setPremiumSquare(3, 3, 2, true);
        setPremiumSquare(3, 11, 2, true);
        setPremiumSquare(4, 4, 2, true);
        setPremiumSquare(4, 10, 2, true);
        setPremiumSquare(10, 4, 2, true);
        setPremiumSquare(10, 10, 2, true);
        setPremiumSquare(11, 3, 2, true);
        setPremiumSquare(11, 11, 2, true);
        setPremiumSquare(12, 2, 2, true);
        setPremiumSquare(12, 12, 2, true);
        setPremiumSquare(13, 1, 2, true);
        setPremiumSquare(13, 13, 2, true);

        //Triple Letter Score
        setPremiumSquare(1, 5, 3, false);
        setPremiumSquare(1, 9, 3, false);
        setPremiumSquare(5, 1, 3, false);
        setPremiumSquare(5, 5, 3, false);
        setPremiumSquare(5, 9, 3, false);
        setPremiumSquare(5, 13, 3, false);
        setPremiumSquare(9, 1, 3, false);
        setPremiumSquare(9, 5, 3, false);
        setPremiumSquare(9, 9, 3, false);
        setPremiumSquare(9, 13, 3, false);
        setPremiumSquare(13, 5, 3, false);
        setPremiumSquare(13, 9, 3, false);

        //Double Letter Score
        setPremiumSquare(0, 3, 2, false);
        setPremiumSquare(0, 11, 2, false);
        setPremiumSquare(2, 6, 2, false);
        setPremiumSquare(2, 8, 2, false);
        setPremiumSquare(3, 0, 2, false);
        setPremiumSquare(3, 7, 2, false);
        setPremiumSquare(3, 14, 2, false);
        setPremiumSquare(6, 2, 2, false);
        setPremiumSquare(6, 6, 2, false);
        setPremiumSquare(6, 8, 2, false);
        setPremiumSquare(6, 12, 2, false);
        setPremiumSquare(7, 3, 2, false);
        setPremiumSquare(7, 11, 2, false);
        setPremiumSquare(8, 2, 2, false);
        setPremiumSquare(8, 6, 2, false);
        setPremiumSquare(8, 8, 2, false);
        setPremiumSquare(8, 12, 2, false);
        setPremiumSquare(11, 0, 2, false);
        setPremiumSquare(11, 7, 2, false);
        setPremiumSquare(11, 14, 2, false);
        setPremiumSquare(12, 6, 2, false);
        setPremiumSquare(12, 8, 2, false);
        setPremiumSquare(14, 3, 2, false);
        setPremiumSquare(14, 11, 2, false);
    }

    /**
     * Sets Premium square.
     * @param row is row of selected square
     * @param col is col of selected square
     * @param multiplier is multipler of the square
     * @param isWordMultiplier bool true if cell is wordMultiplier
     */
    private void setPremiumSquare(int row, int col, int multiplier, boolean isWordMultiplier) {
        board[row][col].setMultiplier(multiplier);
        board[row][col].setWordMultiplier(isWordMultiplier);
    }

    private void initBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setCell(int row, int column, Tile tile) {
        board[row][column].setTile(tile);
        empty = false;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public Cell getCell(int row, int col) {
        return board[row][col];
    }

    public void clearCell(int row, int col) {
        board[row][col].setEmpty();
    }

    public void printBoard() {
        for (Cell[] row : board) {
            System.out.println();
            for (Cell cell : row) {
                System.out.print(' ');
                System.out.print(cell);
            }
        }
    }

    public boolean isCellEmpty(int r, int c) {
        return board[r][c].isEmpty();
    }

    public boolean placeIfEmpty(int r, int c, Tile t) {
        if (!isCellEmpty(r, c)) return false;
        board[r][c].setTile(t);
        empty = false;
        return true;
    }

    /** Returns true if any 4-neighbour of (r,c) contains a tile. */
    public boolean hasAdjacentTile(int r, int c) {
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        for (int k = 0; k < 4; k++) {
            int nr = r + dr[k], nc = c + dc[k];
            if (0 <= nr && nr < 15 && 0 <= nc && nc < 15 && !board[nr][nc].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Board deepCopy(BoardConfig config){
        Board newBoard = new Board(config);
        for (int i=0; i<15; i++){
            for (int j=0; j<15; j++){
                if (!this.board[i][j].isEmpty()) {
                    newBoard.board[i][j].setTile(this.board[i][j].getTile());
                    newBoard.board[i][j].setMultiplier(this.board[i][j].getMultiplier());
                    newBoard.board[i][j].setWordMultiplier(this.board[i][j].isWordMultiplier());
                }
            }
        }
        return newBoard;
    }
}
