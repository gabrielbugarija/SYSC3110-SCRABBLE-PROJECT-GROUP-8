import java.util.*;

public class Game {

    Scanner scanner = new Scanner(System.in);

    private Board board;
    private  ArrayList<Player> playersList = new ArrayList<>();
    private TileBag tileBag = new TileBag();
    private boolean isGameOver;

    public Game (Board board){
        this.board = board;
    }

    public void runGame(){

        int numberOfPlayers = getNumberOfPlayers(scanner);
        boolean isBoardFull = false;
        int currentPlayer = 0;

        createPlayers(playersList, numberOfPlayers, scanner);
        board.printBoard();


        while (true){

            if(isBoardFull){
                break;
            }

            takeTurn(playersList.get(currentPlayer), tileBag, board, scanner);

            if(currentPlayer<playersList.size()-1){
                currentPlayer++;
            }
            else{
                isBoardFull=true;
                currentPlayer=0;
            }
        }
    }

    public int getNumberOfPlayers(Scanner scanner){
        int numberOfPlayers = 0;
        while(true) {
            System.out.print("Enter number of players (2-4): ");
            numberOfPlayers = scanner.nextInt(); //number of players playing the game.
            scanner.nextLine();  // Consuming "Enter" line
            if(numberOfPlayers<=4 && numberOfPlayers>=2){
                break;
            }
            else{
                System.out.println("The number of player should be between 2 and 4");
            }
        }
        return numberOfPlayers;

    }

    public void createPlayers(ArrayList<Player> playersList, int numberOfPlayers, Scanner scanner) {
        for(int i = 0; i<numberOfPlayers; i++){
            System.out.print("Please enter player name: ");
            String name = scanner.nextLine();
            playersList.add(new Player(name));
        }
    }


    public void takeTurn(Player player, TileBag tileBag, Board board, Scanner scanner) {

        Dictionary dictionary = new Dictionary();
        int action;

        player.drawTiles(tileBag);

        while (true) {
            player.printPlayerTiles();
            System.out.println("What do you want to do? (0: Place Tiles, 1: Swap Tiles, 2: Pass) ");
            action = scanner.nextInt();

            if (action <= 2 && action >= 0) {
                break;
            } else {
                System.out.println(("Invalid entry!"));
            }
        }

        if (action == 0){
            boolean isWordValid = true;
            Set<Integer> setRow = new LinkedHashSet<>();
            Set<Integer> setColumn = new LinkedHashSet<>();
            ArrayList<Tile> tilesToPlace = new ArrayList<>();

            while(isWordValid) {
                setRow.clear();
                setColumn.clear();
                tilesToPlace.clear();

                System.out.print("Enter how many tile(s) you want to place (1-7)");
                int numberOfTilesToPlace = scanner.nextInt();
                scanner.nextLine();  // Consuming "Enter" line

                for (int i = 0; i < numberOfTilesToPlace; i++) {
                    System.out.print("Please enter tile " + (i + 1) + " (0-6): ");
                    int tileIndexToPlace = scanner.nextInt();
                    scanner.nextLine();  // Consuming "Enter" line

                    tilesToPlace.add(player.getRack().get(tileIndexToPlace));

                    System.out.print("Please enter row position for tile " + (i + 1) + " (index 0-14): ");
                    setRow.add(scanner.nextInt());
                    scanner.nextLine(); // Consuming "Enter" line

                    System.out.print("Please enter column position for tile " + (i + 1) + " (index 0-14): ");
                    setColumn.add(scanner.nextInt());
                    scanner.nextLine(); // Consuming "Enter" line
                }
                PlaceTiles placeTiles = new PlaceTiles(player, board, tilesToPlace, setRow, setColumn); // should move into a class
                isWordValid = placeTiles.isSuccessful();
                if (!isWordValid){
                    System.out.print("The word is not valid! Please enter again.");
                }
            }

        }
        else if (action == 1) {
            System.out.print("Player swapped! ");
        }
        else if (action == 2) {
            System.out.print("Player passed! ");
        }
        board.printBoard();
    }

}
