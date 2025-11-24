/// @author Kemal Sogut - 101280677

import java.util.ArrayList;
import java.util.Random;

public class TileBag {

    private ArrayList<Tile> tiles;
    private boolean testMode = true; // SET TO TRUE FOR TESTING

    public TileBag(){
        tiles = new ArrayList<>();
        initializeTiles();
    }

    private void initializeTiles() {
        if (testMode) {
            // TESTING MODE: Add lots of blank tiles first
            addTiles('\0', 0, 10); // 10 blank tiles for easy testing
        } else {
            // Normal mode: 2 blank tiles
            addTiles('\0', 0, 2);
        }

        addTiles('A', 1, 9);
        addTiles('B', 3, 2);
        addTiles('C', 3, 2);
        addTiles('D', 2, 4);
        addTiles('E', 1, 12);
        addTiles('F', 4, 2);
        addTiles('G', 2, 3);
        addTiles('H', 4, 2);
        addTiles('I', 1, 9);
        addTiles('J', 8, 1);
        addTiles('K', 5, 1);
        addTiles('L', 1, 4);
        addTiles('M', 3, 2);
        addTiles('N', 1, 6);
        addTiles('O', 1, 8);
        addTiles('P', 3, 2);
        addTiles('Q', 10, 1);
        addTiles('R', 1, 6);
        addTiles('S', 1, 4);
        addTiles('T', 1, 6);
        addTiles('U', 1, 4);
        addTiles('V', 4, 2);
        addTiles('W', 4, 2);
        addTiles('X', 8, 1);
        addTiles('Y', 4, 2);
        addTiles('Z', 10, 1);
    }

    private void addTiles(char letter, int multiplier, int count) {
        for (int i = 0; i < count; i++) {
            tiles.add(new Tile(letter, multiplier));
        }
    }

    public int getNumberOfTilesLeft() {
        return tiles.size();
    }

    public Tile drawTile(){
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(tiles.size());
        Tile tileToReturn = tiles.get(randomIndex);
        tiles.remove(tileToReturn);
        return tileToReturn;
    }

    public void returnTile(Tile tile){
        tiles.add(tile);
    }

    public int size(){
        return tiles.size();
    }
}