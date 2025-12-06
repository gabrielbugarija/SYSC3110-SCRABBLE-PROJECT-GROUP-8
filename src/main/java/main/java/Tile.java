package main.java;

import java.io.Serializable;

/**
 *
 *
 * @author Kemal Sogut - 101280677
 *
 * Represents a Letter Tile in the game.
 *
 */

public class Tile implements Serializable {
    private static final long serialVersionUID = 1L;

    private final char letter;
    private final int points;//The tile has points not a multiplier
    private char assignedLetter; // For blank tiles


    public Tile(char letter, int points) {
        this.letter = Character.toUpperCase(letter);
        this.points = points;
    }

    public boolean isBlank(){
        return letter == '\0';
    }

    public char getLetter(){
        return letter;
    }

    public int getPoints() {
        // Blank tiles are always worth 0 points
        if (isBlank()) {
            return 0;
        }
        return points;
    }

    public void setAssignedLetter(char letter) {
        if (isBlank()) {
            this.assignedLetter = Character.toUpperCase(letter);
        }
    }

    public char getAssignedLetter() {
        return assignedLetter;
    }

    public Tile deepCopy(){
        Tile newTile = new Tile(letter, points);
        return newTile;
    }

    @Override
    public String toString() {
        if (isBlank()) {
            if (assignedLetter != '\0') {
                return String.valueOf(assignedLetter).toLowerCase(); // Show lowercase for blank
            }
            return "*"; // Unassigned blank
        }
        return String.valueOf(letter);
    }
}
