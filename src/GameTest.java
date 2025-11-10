import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

class GameTest {

    private Game game;
    private Board board;
    private TileBag tileBag;

    @BeforeEach
    void setUp() {
        board = new Board();
        tileBag = new TileBag();
        game = new Game(board);
    }

    @AfterEach
    void tearDown() {
        game = null;
        board = null;
    }

    @Test
    @DisplayName("Test Game Setup")
    void testGameSetup() {
        assertNotNull(game, "Game should not be null");
        assertNotNull(board,  "Board should not be null");
        assertTrue(board.isEmpty(), "Board should be empty");
    }

    @Test
    @DisplayName("Test getNumberOfPlayers using valid input")
    void testGetNumberOfPlayersValid() {
        String input = "2\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        int numberOfPlayers = game.getNumberOfPlayers(scanner);
        assertEquals(2, numberOfPlayers, "Should return 2");
    }

    @Test
    @DisplayName("Test getNumberOfPlayers using invalid input then valid")
    void testGetNumberOfPlayersInvalid() {
        String input = "5\n4\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        int numberOfPlayers = game.getNumberOfPlayers(scanner);
        assertEquals(4, numberOfPlayers, "Should return 2 after invalid input");
    }

    @Test
    @DisplayName("Test createPlayers creates right amount of players")
    void testCreatePlayers() {
        ArrayList<Player> players = new ArrayList<>();
        int numOfPlayers = 3;

        String input = "Mark\nGabe\nBob\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        game.createPlayers(players, numOfPlayers, scanner);

        assertEquals(numOfPlayers, players.size(), "Should return " + numOfPlayers + " players");
        assertEquals("Mark", players.get(0).getName(), "Should return Mark");
        assertEquals("Gabe", players.get(1).getName(), "Should return Gabe");
        assertEquals("Bob", players.get(2).getName(), "Should return Bob");
    }

    @Test
    @DisplayName("Test createPlayers with empty names")
    void testCreatePlayersEmpty() {
        ArrayList<Player> players = new ArrayList<>();
        int numOfPlayers = 3;
        String input = "\n\n\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        game.createPlayers(players, numOfPlayers, scanner);

        assertEquals(numOfPlayers, players.size(), "Should return " + numOfPlayers + " players");
        assertNotNull(players.get(0).getName(), "Players should not be null");
        assertNotNull(players.get(1).getName(), "Players should not be null");
        assertNotNull(players.get(2).getName(), "Players should not be null");
    }

    @Test
    @DisplayName("Test takeTurn")
    void testTakeTurn() {

        Player testPlayer = new Player("TestPlayer", tileBag);

        String input = "2\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        assertDoesNotThrow(() -> game.takeTurn(testPlayer, tileBag, board, scanner), "takeTurn should not throw exception");
    }

    @Test
    @DisplayName("Test Player Creation with tile bag")
    void testPlayerCreation() {
        ArrayList<Player> players = new ArrayList<>();
        int numOfPlayers = 2;

        String input = "Player1\nPlayer2\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(inputStream);

        game.createPlayers(players, numOfPlayers, scanner);

        assertEquals(2, players.size(), "Should create 2, players");
        assertEquals(7, players.get(0).getRack().size(), "Rack should be initialized with 7 tiles");
    }
}