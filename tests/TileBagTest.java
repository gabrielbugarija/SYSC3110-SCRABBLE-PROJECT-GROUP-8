import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileBagTest {
    @Test
    public void tileBagCreation(){
        TileBag tileBag = new TileBag();
        assertEquals(100, tileBag.getNumberOfTilesLeft());
    }

    @Test
    public void returnTiles(){
        TileBag tileBag = new TileBag();
        Tile testTile = new Tile('J', 2);
        tileBag.returnTile(testTile);
        assertEquals(101, tileBag.getNumberOfTilesLeft());

        Tile testTile2 = new Tile('M', 3);
        tileBag.returnTile(testTile2);
        assertEquals(102, tileBag.getNumberOfTilesLeft());
    }
}