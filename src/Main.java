import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in); // Scanner integration
        int numberOfPlayers = getNumberOfPlayers(scanner); // get number of players from the user
        ArrayList<Player> playersList = new ArrayList<>();

        Board board = new Board();
        TileBag tileBag = new TileBag();
        createPlayers(playersList, numberOfPlayers, scanner);

        board.printBoard();
        boolean isBoardFull = false;
        int currentPlayer = 0;
        while (true){

            if(isBoardFull){
                break;
            }

            takeTurn(playersList.get(currentPlayer), tileBag);

            if(currentPlayer<playersList.size()-1){
                currentPlayer++;
            }
            else{
                isBoardFull=true;
                currentPlayer=0;
            }


        }


        // --------------------START OF ROUGH TESTS.

        for(int i =0; i<numberOfPlayers;i++){
            System.out.println(playersList.get(i));
        }

        // ---------------------END OF ROUGH TESTS.

    }

    public static void takeTurn(Player player, TileBag tileBag) {
        System.out.print("\n"+player.getName()+" played...");


    }

    public static void createPlayers(ArrayList<Player> playersList, int numberOfPlayers, Scanner scanner) {
        for(int i = 0; i<numberOfPlayers; i++){
            System.out.print("Please enter player name: ");
            String name = scanner.nextLine();
            playersList.add(new Player(name));
        }
    }




    public static int getNumberOfPlayers(Scanner scanner){
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



}