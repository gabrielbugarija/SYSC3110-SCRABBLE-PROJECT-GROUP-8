import java.util.*;

/**
 * Controls the main gameplay loop for a word-board game.
 *
 * <p>The {@code Game} class is responsible for:
 * <ul>
 *   <li>Creating players</li>
 *   <li>Managing turns</li>
 *   <li>Interacting with the {@link Board}, {@link TileBag}, and players' {@link Move}s</li>
 *   <li>Determining when the game ends</li>
 * </ul>
 *
 * A {@code Game} instance is created with a {@link Board}, then {@link #runGame()} is called
 * to start interactive console play.
 */
public class Game {

    /** Shared scanner used for all console input. */
    private Scanner scanner = new Scanner(System.in);

    /** The game board where tiles are placed. */
    private Board board;

    /** The list of players currently in this game. */
    private ArrayList<Player> playersList = new ArrayList<>();

    /** The bag of remaining tiles to draw from. */
    private TileBag tileBag = new TileBag();

    /** Flag to indicate whether the game is over. */
    private boolean isGameOver;

    /**
     * Creates a new game with the given board.
     *
     * @param board the {@link Board} on which this game will be played
     */
    public Game(Board board) {
        this.board = board;
        this.isGameOver = false;
    }

    /**
     * Starts and runs the full game loop.
     *
     * <p>Flow:
     * <ol>
     *   <li>Ask how many players are playing (2-4)</li>
     *   <li>Create each player and give them tiles</li>
     *   <li>Print the initial board</li>
     *   <li>Loop through player turns in order until the game ends</li>
     * </ol>
     *
     * <p>End conditions (current version):</p>
     * <ul>
     *   <li>The tile bag is empty</li>
     *   <li>{@code isGameOver} is set externally (e.g. by a move in the future)</li>
     * </ul>
     */
    public void runGame() {

        // Ask the user how many people are playing and validate input
        int numberOfPlayers = getNumberOfPlayers(scanner);

        // TODO: not yet used, but you might use this later to detect no more spaces
        boolean isBoardFull = false;

        // Keep track of whose turn it is: index into playersList
        int currentPlayer = 0;

        // Create players (ask for their names, give them starting tiles, etc.)
        createPlayers(playersList, numberOfPlayers, scanner);

        // Show the starting board state before the first turn
        board.printBoard();



        // ---- Main game loop ----

        while (true) {

            // Break out if the game is marked as over
            if (isGameOver) {
                break;
            }

            // Let the current player take their turn
            takeTurn(playersList.get(currentPlayer), tileBag, board, scanner);

            // Advance to the next player (wrap back to 0 after the last one)
            if (currentPlayer < playersList.size() - 1) {
                currentPlayer++;
            } else {
                currentPlayer = 0;
            }

            // GAME OVER RULE (current version): if there are no tiles left in the bag
            if (tileBag.getNumberOfTilesLeft() == 0) {
                isGameOver = true;
            }
        }
    }

    /**
     * Prompts the user (via console) for how many players will be in the game.
     * Only allows 2–4 players.
     *
     * <p>This method will keep asking until the user gives a valid number.</p>
     *
     * @param scanner the {@link Scanner} to read user input from
     * @return the number of players (between 2 and 4, inclusive)
     */
    public int getNumberOfPlayers(Scanner scanner) {
        int numberOfPlayers = 0;

        while (true) {
            System.out.print("Enter number of players (2-4): ");
            numberOfPlayers = scanner.nextInt(); // read number of players
            scanner.nextLine(); // consume the leftover newline so future nextLine() calls work

            if (numberOfPlayers <= 4 && numberOfPlayers >= 2) {
                break;
            } else {
                System.out.println("The number of players should be between 2 and 4");
            }
        }

        return numberOfPlayers;
    }

    /**
     * Creates and registers all {@link Player} objects for this game.
     * Each player is asked for their name, then added to {@code playersList}.
     *
     * <p>Note: it also passes the {@link TileBag} to the {@link Player} constructor,
     * so players can draw their starting tiles there.</p>
     *
     * @param playersList     the list to add new players into
     * @param numberOfPlayers how many players to create
     * @param scanner         the scanner used to read player names
     */
    public void createPlayers(ArrayList<Player> playersList, int numberOfPlayers, Scanner scanner) {
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.print("Please enter player name: ");
            String name = scanner.nextLine();
            playersList.add(new Player(name, tileBag));
        }
    }

    /**
     * Handles a single player's turn.
     *
     * <p>This method:
     * <ul>
     *   <li>Announces whose turn it is</li>
     *   <li>Creates a {@link Move} for that player</li>
     *   <li>Delegates the actual move logic to {@link Move#makeMove(Player, TileBag, Board, Scanner)}</li>
     * </ul>
     *
     * <p>Right now, {@code Game} doesn't apply any game rules itself (like validity checking);
     * it assumes {@code Move} handles that. Later on, you might enforce scoring or challenge logic here.</p>
     *
     * @param player   the active player this turn
     * @param tileBag  the shared tile bag
     * @param board    the shared game board
     * @param scanner  scanner for user interaction during the move
     */
    public void takeTurn(Player player, TileBag tileBag, Board board, Scanner scanner) {
        System.out.print("Player: " + player.getName());
        Move move = new Move(player, tileBag, board, scanner);

        // Let the Move object drive the turn. You could eventually check here if the move
        // ended the game (e.g. player plays all tiles and bag is empty).
        move.makeMove(player, tileBag, board, scanner);
    }
}
