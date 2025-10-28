import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    public void testGetNumOfPlayers(){
        Board board = new Board();
        Game game = new Game(new Board());
        String playerNum = "3";
        InputStream in = new ByteArrayInputStream(playerNum.getBytes());
        System.setIn(in);
        game.getNumberOfPlayers();

    }
}