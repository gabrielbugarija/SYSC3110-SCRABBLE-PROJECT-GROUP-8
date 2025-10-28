import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    @Test
    public void testTileCreation1(){
        Tile tile = new Tile('A', 3);
        assertEquals('A', tile.getLetter());
        assertEquals(3, tile.getPoints());
        assertEquals("A", tile.toString());
    }

    @Test
    public void testTileCreation2(){
        Tile tile = new Tile('C', 7);
        assertEquals('C', tile.getLetter());
        assertEquals(7, tile.getPoints());
        assertEquals("C", tile.toString());
    }

    @Test
    public void testTileCreation3(){
        Tile tile = new Tile('E', 1);
        assertEquals('E', tile.getLetter());
        assertEquals(1, tile.getPoints());
        assertEquals("E", tile.toString());
    }
}