package main.java;

import java.io.Serializable;

/**
 *
 * Represents a single cell on the game board.
 * @author Kemal Sogut - 101280677
 * @param multiplier the multiplier of tha cell.
 *
 */

public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;

    private int multiplier;
    private Tile tile;
    private boolean empty;
    private boolean isWordMultiplier;

    public Cell() {
        this.multiplier = 1;
        this.empty = true;
        this.isWordMultiplier = false;
    }


    public Cell(int multiplier) {
        this.multiplier = multiplier;
        this.isWordMultiplier = false;
    }

    public void setWordMultiplier(boolean wordMultiplier) {
        isWordMultiplier = wordMultiplier;
    }

    public boolean isWordMultiplier() {
        return isWordMultiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public char getLetter() {
        if (empty) {
            return '-';
        }

        // Handle blanks explicitly
        if (tile.isBlank()) {
            char assigned = tile.getAssignedLetter();
            if (assigned != '\0') {
                // use lowercase to visually distinguish blanks from normal tiles
                return Character.toLowerCase(assigned);
            }
            // unassigned blank on the board – show '*'
            return '*';
        }

        return tile.getLetter();
    }

    public void setTile(Tile tile) {

        this.tile = tile;
        setOccupied();
    }

    public Tile getTile() {
        return tile;
    }

    public int getTilePoints() {

        return tile.getPoints();
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty() {
        this.tile = null;
        this.empty = true;
    }

    public void setOccupied() {
        this.empty = false;
    }

    @Override
    public String toString() {
        if (empty) {
            return "-";
        }

        return tile.toString();
    }
}

