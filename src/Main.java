import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Scanner integration
        int numberOfPlayers = getNumberPlayers(scanner);

        Board board = new Board();
        Tile a = new Tile('A', 1);
        Tile blank = new Tile('\0', 1);

        TileBag tileBag = new TileBag();
        Player player = new Player("Kemal", tileBag);

        player.printPlayerTiles();

        Player player2 = new Player("Ahmet", tileBag);

        player2.printPlayerTiles();

        System.out.println(tileBag.getNumberOfTilesLeft());
        board.printBoard();

        tileBag.checkDraw((tileBag.drawTile()).getLetter());
        System.out.println(a.isBlank());
        System.out.println(blank.isBlank());



    }

    public static int getNumberPlayers(Scanner scanner){
        int numberOfPlayers = 0;
        while(true) {
            System.out.print("Enter number of players (2-4): ");
            numberOfPlayers = scanner.nextInt(); //number of players playing the game.
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