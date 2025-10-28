import java.util.*;

public class Game {

    Scanner scanner = new Scanner(System.in);

    private Board board;
    private  ArrayList<Player> playersList = new ArrayList<>();
    private TileBag tileBag = new TileBag();
    private boolean isGameOver;

    public Game (Board board){
        this.board = board;
        this.isGameOver=false;
    }

    public void runGame(){

        int numberOfPlayers = getNumberOfPlayers(scanner);
        boolean isBoardFull = false;
        int currentPlayer = 0;

        createPlayers(playersList, numberOfPlayers, scanner);
        board.printBoard();


        while(true){

            if(isGameOver){
                break;
            }
            takeTurn(playersList.get(currentPlayer), tileBag, board, scanner);

            if(currentPlayer<playersList.size()-1){
                currentPlayer++;
            }
            else{
                currentPlayer=0;
            }
            if(tileBag.getNumberOfTilesLeft()==0){
                isGameOver = true;
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
            playersList.add(new Player(name, tileBag));
        }
    }
    public void takeTurn(Player player, TileBag tileBag, Board board, Scanner scanner) {
        System.out.print("Player: "+player.getName());
        Move move = new Move(player, tileBag, board, scanner);

        move.makeMove(player,tileBag,board,scanner);

    }
}
