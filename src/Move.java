import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Move {

    private Player player;

    public Move(Player player, TileBag tileBag, Board board, Scanner scanner){

        player.drawTiles(tileBag);
        player.printRack();
        Dictionary dictionary = new Dictionary();
        int action;
        int direction = -1;
        int moveScore;
        ArrayList<Integer> setRow = new ArrayList<>();
        ArrayList<Integer> setColumn = new ArrayList<>();
        ArrayList<Tile> tilesToPlace = new ArrayList<>();


        while (true) {
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

            while(isWordValid) {

                while (direction == -1) {
                    setRow.clear();
                    setColumn.clear();
                    tilesToPlace.clear();

                    System.out.print("Enter how many tile(s) you want to place (1-7)");
                    int numberOfTilesToPlace = scanner.nextInt();
                    scanner.nextLine();  // Consuming "Enter" line

                    for (int i = 0; i < numberOfTilesToPlace; i++) {
                        System.out.print("Please enter tile " + (i + 1) + " (index 0-6): ");
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
                    direction = checkDirection(setRow, setColumn);
                }
                isWordValid = checkValidatyOfWord(direction, tilesToPlace, setRow, setColumn, board);


            }

        }
        else if (action == 1) {
            System.out.print("Player swapped! ");
        }
        else if (action == 2) {
            System.out.print("Player passed! ");
        }
    }


    private boolean checkValidatyOfWord(int direction, ArrayList<Tile> tilesToPlace, ArrayList<Integer> setRow, ArrayList<Integer> setCol, Board board) {
        Dictionary dictionary = new Dictionary();
        StringBuilder word = new StringBuilder();
        int scoreToAdd = 0 ;

        for (int i = 0; i < tilesToPlace.size(); i++) {
            word.append(tilesToPlace.get(i).getLetter());
            scoreToAdd += tilesToPlace.get(i).getPoints() * board.getCell(setRow.get(i), setCol.get(i)).getMultiplier();
        }

        if (direction == 1) {
            int upper = Collections.min(setRow);
            int lower = Collections.max(setRow);
            boolean foundNull = false;
            Cell cell;

            while(upper>0 && !foundNull){
                cell = board.getCell(upper-1,setCol.getFirst());
                if(!cell.isEmpty()){
                    word.insert(0, cell.getLetter());
                    scoreToAdd += cell.getTilePoints() * cell.getMultiplier();
                    upper--;

                }
                else{
                    foundNull = true;
                }
            }

            while(lower<14 && !foundNull){
                cell = board.getCell(lower+1,setCol.getFirst());
                if(!cell.isEmpty()){
                    word.append(cell.getLetter());
                    scoreToAdd += cell.getTilePoints() * cell.getMultiplier();
                    lower++;

                }
                else{
                    foundNull = true;
                }
            }

        } else if (direction == 0) {
            int left = Collections.min(setCol);
            int right = Collections.max(setCol);
            boolean foundNull = false;
            Cell cell;

            while(left>0 && !foundNull){
                cell = board.getCell(setRow.getFirst(),left-1);
                if(!cell.isEmpty()){
                    word.insert(0, cell.getLetter());
                    scoreToAdd += cell.getTilePoints() * cell.getMultiplier();
                    left--;

                }
                else{
                    foundNull = true;
                }
            }

            while(right<14 && !foundNull){
                cell = board.getCell(setRow.getFirst(),right+1);
                if(!cell.isEmpty()){
                    word.append(cell.getLetter());
                    right++;
                    scoreToAdd += cell.getTilePoints() * cell.getMultiplier();
                }
                else{
                    foundNull = true;
                }
            }

        }
        else if (direction == 2) {
            int row = setRow.getFirst();
            int col = setCol.getFirst();
            StringBuilder horizontalWord = new StringBuilder();
            StringBuilder verticalWord = new StringBuilder();

            horizontalWord.append(word.toString());
            verticalWord.append(word.toString());

            boolean foundNull = false;
            Cell cell;

            int i = row;
            while(i>0 && !foundNull){
                cell = board.getCell(i-1,col);
                if(!cell.isEmpty()){
                    verticalWord.insert(0, cell.getLetter());

                    i--;
                }
                else{
                    foundNull = true;
                }

            }
            i = row;

            while(i<14 && !foundNull){
                cell = board.getCell(i+1,col);
                if(!cell.isEmpty()){
                    horizontalWord.append(cell.getLetter());
                    i++;

                }
                else{
                    foundNull = true;
                }
            }

            foundNull = false;
            i = col;
            ArrayList<Tile> temp = tilesToScore;   // act as a save point for tilesToScore

            if(!dictionary.isValidWord(horizontalWord.toString())){
                tilesToScore.clear();
            }


            while(i>0 && !foundNull){
                cell = board.getCell(row,i-1);
                if(!cell.isEmpty()){
                    verticalWord.insert(0, cell.getLetter());
                    i--;
                    tilesToScore.add(cell.getTile());
                }
                else{
                    foundNull = true;
                }
            }

            i = col;

            while(i<14 && !foundNull){
                cell = board.getCell(rowsList.getFirst(),i+1);
                if(!cell.isEmpty()){
                    verticalWord.append(cell.getLetter());
                    i++;
                    tilesToScore.add(cell.getTile());
                }
                else{
                    foundNull = true;
                }
            }


            if (!dictionary.isValidWord(verticalWord.toString())){
                tilesToScore = temp;
            }
        }

        // dictionary.isValidWord(word.toString())
        if(true) {
            tilesToScore.addAll(tilesToPlace);
            return true;
        } else {
            tilesToScore.clear();
            return false;
        }


    }

    public int checkDirection(ArrayList<Integer> setRow, ArrayList<Integer> setColumn){

        if(areAllElementsSame(setRow) && !areAllElementsSame(setColumn)){
            return 0;
        } else if (areAllElementsSame(setColumn) && !areAllElementsSame(setRow)) {
            return 1;

        } else if (areAllElementsSame(setRow) && areAllElementsSame(setColumn)){
            return 2;
        }
        else{
            System.out.print("Invalid direction: ");
            return -1;
        }

    }

    public static <T> boolean areAllElementsSame(List<T> list) {

        if (list == null || list.isEmpty() || list.size() == 1) {
            return true;
        }

        // Get the first element to compare against
        T firstElement = list.getFirst();

        // The allMatch() method returns true if every element in the stream
        // matches the provided condition.
        return list.stream().allMatch(element -> element.equals(firstElement));
    }
    

}
