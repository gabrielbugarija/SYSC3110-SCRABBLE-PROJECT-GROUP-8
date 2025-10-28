/// @author Kemal Sogut - 101280677
import java.util.ArrayList;
import java.util.Random;

/**
 * <p>The {@code TileBag} stores all available tiles at the start of the game and allows
 * drawing random tiles or returning them to the bag. Each tile has a letter and a score value.</p>
 */
public class TileBag {

    /** The collection of all available tiles in the bag. */
    private ArrayList<Tile> tiles;

    /** Flag to indicate if the bag is empty. */
    private boolean empty;

    /**
     * Constructs a new {@code TileBag} and fills it with the standard set of tiles.
     */
    public TileBag() {
        tiles = new ArrayList<>();
        initializeTiles();
        empty = false;
    }

    /**
     * Populates the bag with a standard set of tiles.
     * <p>Each letter has a defined point value (multiplier) and frequency (count).</p>
     */
    private void initializeTiles() {
        // The blank tiles are represented by '\0'
        addTiles('\0', 1, 2);
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

    /**
     * Checks whether the bag is empty.
     *
     * @return {@code true} if there are no tiles left, {@code false} otherwise.
     *
     * <p><b>Note:</b> The original implementation always returned {@code false}.
     * This corrected version checks the size of the bag properly.</p>
     */
    public boolean isEmpty() {
        empty = tiles.isEmpty();
        return empty;
    }

    /**
     * Adds multiple copies of a specific letter tile to the bag.
     *
     * @param letter the character printed on the tile
     * @param multiplier the score value of the tile
     * @param count how many copies of the tile to add
     */
    private void addTiles(char letter, int multiplier, int count) {
        for (int i = 0; i < count; i++) {
            tiles.add(new Tile(letter, multiplier));
        }
    }

    /**
     * Returns the total number of tiles currently remaining in the bag.
     *
     * @return number of tiles left
     */
    public int getNumberOfTilesLeft() {
        return tiles.size();
    }

    /**
     * Randomly draws a tile from the bag and removes it.
     *
     * @return a randomly selected {@link Tile} from the bag
     * @throws IllegalStateException if the bag is empty
     */
    public Tile drawTile() {
        if (tiles.isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty tile bag!");
        }

        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(tiles.size());
        Tile tileToReturn = tiles.get(randomIndex);

        tiles.remove(tileToReturn);  // remove the drawn tile from the bag
        return tileToReturn;
    }

    /**
     * Returns a tile back to the bag.
     *
     * @param tile the {@link Tile} to be returned to the bag
     */
    public void returnTile(Tile tile) {
        tiles.add(tile);
    }

    /**
     * Returns the number of tiles currently in the bag.
     *
     * @return current number of tiles in the bag
     */
    public int size() {
        return tiles.size();
    }


}
