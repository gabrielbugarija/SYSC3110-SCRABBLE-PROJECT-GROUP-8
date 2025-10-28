import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {
    @Test
    public void makeEmptyCell(){
        Cell cell = new Cell();
        assertTrue(cell.isEmpty());
        assertEquals('-', cell.getLetter());
        assertEquals(1, cell.getMultiplier());
    }

    @Test
    public void makeManyValidTiles(){
        Cell cell = new Cell(2);
        Tile testTile = new Tile('B', 10);
        cell.setTile(testTile);
        assertFalse(cell.isEmpty());
        assertEquals('B', cell.getLetter());
        assertEquals(2, cell.getMultiplier());
        assertEquals(testTile, cell.getTile());
        assertEquals("B", cell.toString());

        Cell cell2 = new Cell(3);
        Tile testTile2 = new Tile('L', 5);
        cell2.setTile(testTile2);
        assertFalse(cell2.isEmpty());
        assertEquals('L', cell2.getLetter());
        assertEquals(3, cell2.getMultiplier());
        assertEquals(testTile2, cell2.getTile());
        assertEquals("L", cell2.toString());
    }
}