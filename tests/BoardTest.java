import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void newBoardisEmpty() {
        Board board = new Board();
        assertTrue(board.isEmpty());
    }

    @Test
    public void boardWithTileNotEmpty() {
        Board board = new Board();
        board.setCell(2, 4, new Tile('j', 4));
        assertFalse(board.isEmpty());
    }

    @Test
    public void cellsArePlacedInRightSpot() {
        Board board = new Board();
        //Normal Tile
        Tile testTile1 = new Tile('I', 5);
        board.setCell(2, 4, testTile1);
        assertEquals(testTile1, board.getCell(2, 4).getTile());
        assertEquals('I', board.getCell(2, 4).getLetter());
        assertEquals(5, board.getCell(2, 4).getTilePoints());
        //First Position
        Tile testTile2 = new Tile('H', 2);
        board.setCell(0, 0, testTile2);
        assertEquals(testTile2, board.getCell(0, 0).getTile());
        assertEquals('H', board.getCell(0, 0).getLetter());
        assertEquals(2, board.getCell(0, 0).getTilePoints());
        //Last Position
        Tile testTile3 = new Tile('Z', 3);
        board.setCell(14, 14, testTile3);
        assertEquals(testTile3, board.getCell(14, 14).getTile());
        assertEquals('Z', board.getCell(14, 14).getLetter());
        assertEquals(3, board.getCell(14, 14).getTilePoints());
    }

    @Test
    public void placeCellOutsideBoard(){
        Board board = new Board();
        Tile testTile1 = new Tile('I', 5);
        ArrayIndexOutOfBoundsException e = assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.setCell(15, 15, testTile1));
    }
}