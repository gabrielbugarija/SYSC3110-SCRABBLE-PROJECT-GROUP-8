/// @author Kemal Sogut - 101280677
/**
 * Represents a game board composed of a 15x15 grid of {@link Cell} objects.
 */
public class Board {

    /** The 15x15 grid of cells that make up the board. */
    private Cell[][] board;

    /** Flag indicating whether the board is currently empty (no tiles placed). */
    private boolean empty;

    /**
     * Constructs a new Board and initializes all cells as empty.
     */
    public Board() {
        this.board = new Cell[15][15];
        initBoard();
        this.empty = true;
    }

    /**
     * This method is called automatically by the constructor.
     */
    private void initBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell();  // create a new empty cell
            }
        }
    }

    /**
     * Places a {@link Tile} in the specified cell on the board.
     *
     * @param row the row index (0-based)
     * @param column the column index (0-based)
     * @param tile the {Tile} to place in the cell
     */
    public void setCell(int row, int column, Tile tile) {
        board[row][column].setTile(tile);
        empty = false; // once a tile is placed, the board is no longer empty
    }

    /**
     * Checks whether the board is currently empty (no tiles placed).
     *
     * @return {@code true} if no tiles are placed; {@code false} otherwise
     */
    public boolean isEmpty() {
        return this.empty;
    }

    /**
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return the cell object at the given coordinates
     */
    public Cell getCell(int row, int col) {
        return board[row][col];
    }

    /**
     * Clears the cell at the specified position, setting it to an empty state.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     */
    public void clearCell(int row, int col) {
        board[row][col].setEmpty();
    }

    /**
     * Prints a simple text-based representation of the board to the console.
     */
    public void printBoard() {
        for (Cell[] row : board) {
            System.out.println(); // move to next row
            for (Cell cell : row) {
                System.out.print(' ');
                System.out.print(cell);
            }
        }
    }
}
