/**
 *
 *
 * @author Kemal Sogut - 101280677
 *
 * Represents a Letter Tile in the game.
 *
 */

public class Tile {

    private final char letter;
    private final int points;//The tile has points not a multiplier

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
        return points;
    }

    @Override
    public String toString() {
        return String.valueOf(letter);
    }
}
