import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    public void checkPlayerCreatedProperly(){
        Player player = new Player("Random", new TileBag());
        assertEquals("Random", player.getName());
        assertEquals(7, player.getRackTiles().size());
    }

    @Test
    public void drawTiles(){
        TileBag tileBag = new TileBag();
        Player player = new Player("Random", tileBag);
        player.drawTiles(tileBag, 3);
        assertEquals(10, player.getRackTiles().size());
    }
}