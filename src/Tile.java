/// @author Kemal Sogut - 101280677

public class Tile {

    private int multiplier;
    private char letter;

    public Tile(char letter, int multiplier){
        this.multiplier = multiplier;
        this.letter = letter;
    }

    public boolean isBlank(){
        return letter == '\0';
    }

    public char getLetter(){
        return letter;
    }

    public int getMultiplier() {
        return multiplier;
    }

    @Override
    public String toString() {
        return String.valueOf(letter);
    }
}
