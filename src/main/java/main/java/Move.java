package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Move {
    private Player player;

    public Move(Player player, TileBag tileBag, Board board, Scanner scanner) {

        this.player = player;

        Dictionary dictionary = new Dictionary();


    }

    public void makeMove(Player player, TileBag tileBag, Board board, Scanner scanner){
        player.printRack();
        int action;
        int moveScore = 0;
        int direction = -1;
        ArrayList<Integer> setRow = new ArrayList<>();
        ArrayList<Integer> setColumn = new ArrayList<>();
        ArrayList<Tile> tilesToPlace = new ArrayList<>();


        while (true) {
            System.out.println("What do you want to do? (0: Place Tiles, 1: Swap Tiles, 2: Pass) ");
            action = scanner.nextInt();
            scanner.nextLine();// consume enter line.

            if (action <= 2 && action >= 0) {
                break;
            } else {
                System.out.println(("Invalid entry!"));
            }
        }

        if (action == 0){
            boolean isWordValid = false;

            while(!isWordValid) {
                direction = -1;

                while (direction == -1) {
                    setRow.clear();
                    setColumn.clear();
                    tilesToPlace.clear();

                    int numberOfTilesToPlace;

                    while (true) {
                        System.out.print("Enter how many tile(s) you want to place (1-7)");
                        numberOfTilesToPlace = scanner.nextInt();
                        scanner.nextLine();  // Consuming "Enter" line

                        if (numberOfTilesToPlace >= 1 && numberOfTilesToPlace <= 7){
                            break;
                        }
                        System.out.print("Invalid entry. Please try again\n");
                    }
                    for (int i = 0; i < numberOfTilesToPlace; i++) {
                        int tileIndexToPlace;
                        while (true) {
                            System.out.print("Please enter tile " + (i + 1) + " (index 0-6): ");
                            tileIndexToPlace = scanner.nextInt();
                            scanner.nextLine();  // Consuming "Enter" line

                            if (tileIndexToPlace >= 0 && tileIndexToPlace <= 6){
                                break;
                            }
                            System.out.print("Invalid entry. Please try again\n");
                        }
                        tilesToPlace.add(player.getRack().get(tileIndexToPlace));
                        int row;
                        int col;
                        while (true) {
                            System.out.print("Please enter row position for tile " + (i + 1) + " (index 0-14): ");
                            row = scanner.nextInt();
                            scanner.nextLine();// Consuming "Enter" line
                            if (row>=0 && row<15){
                                break;
                            }
                            System.out.print("Invalid entry. Please try again\n");
                        }

                        setRow.add(row);


                        while (true) {
                            System.out.print("Please enter column position for tile " + (i + 1) + " (index 0-14): ");
                            col = scanner.nextInt();
                            scanner.nextLine(); // Consuming "Enter" line

                            if (col>=0 && col<15){
                                break;
                            }
                            System.out.print("Invalid entry. Please try again\n");

                        }
                        setColumn.add(col);

                    }
                    direction = checkDirection(setRow, setColumn);
                }
                moveScore = checkValidityOfWordAndReturnScore(direction, tilesToPlace, setRow, setColumn, board);
                if (moveScore == 0){
                    isWordValid = false;
                    System.out.print("Invalid word. Please try again\n");
                    direction = -1;
                }
                else{
                    isWordValid = true;
                    placeTiles(board, direction, tilesToPlace, setRow, setColumn);

                    player.addScore(moveScore);
                    System.out.println("Move successful! Score: " + moveScore);

                    board.printBoard();
                }

            }

        }
        else if (action == 1) {
            int indexToSwap;
            while (true){
                System.out.print("\nPlease enter the index of the tile you want to swap: (index 0-6)");
                indexToSwap = scanner.nextInt();
                scanner.nextInt();// consume enter line;

                if (indexToSwap>=0 && indexToSwap<15){
                    break;
                }
                System.out.print("Invalid entry. Please try again\n");

            }

            player.removeTiles((char) indexToSwap);
            player.drawTiles();
        }
        else if (action == 2) {
            System.out.print("Player passed! ");
        }
    }

    public void placeTiles(Board board, int direction, ArrayList<Tile> tilesToPlace, ArrayList<Integer> rowsList, ArrayList<Integer> columnsList){
        boolean successful;
        if (direction == 0){
            int row = rowsList.get(0);
            for (int i=0; i<tilesToPlace.size();i++){
                board.setCell(row, columnsList.get(i), tilesToPlace.get(i));
                successful = true;
            }

        } else if (direction == 1) {
            int column = columnsList.get(0);
            for (int i=0; i<tilesToPlace.size();i++){
                board.setCell(rowsList.get(i),column, tilesToPlace.get(i));
                successful = true;
            }

        } else if (direction == 2) {
            board.setCell(rowsList.get(0),columnsList.get(0), tilesToPlace.get(0));
            successful = true;
        }
        else {
            System.out.print("Tile placing error!");
        }


    }

    private void sortPlacementByPosition(ArrayList<Integer> rows, ArrayList<Integer> cols, ArrayList<Tile> tiles, int direction) {
        ArrayList<Integer> order = new ArrayList<>();
        for (int i = 0; i < tiles.size(); i++) {
            order.add(i);
        }

        if (direction == 0) {
            // horizontal -> sort by column
            order.sort((a, b) -> Integer.compare(cols.get(a), cols.get(b)));
        } else {
            // vertical -> sort by row
            order.sort((a, b) -> Integer.compare(rows.get(a), rows.get(b)));
        }

        ArrayList<Integer> newRows = new ArrayList<>();
        ArrayList<Integer> newCols = new ArrayList<>();
        ArrayList<Tile> newTiles = new ArrayList<>();

        for (int index : order) {
            newRows.add(rows.get(index));
            newCols.add(cols.get(index));
            newTiles.add(tiles.get(index));
        }

        rows.clear();
        cols.clear();
        tiles.clear();
        rows.addAll(newRows);
        cols.addAll(newCols);
        tiles.addAll(newTiles);
    }



    private int checkValidityOfWordAndReturnScore(int direction, ArrayList<Tile> tilesToPlace, ArrayList<Integer> setRow, ArrayList<Integer> setCol, Board board) {
        Dictionary dictionary = new Dictionary();
        StringBuilder word = new StringBuilder();
        sortPlacementByPosition(setRow, setCol, tilesToPlace, direction);

        int scoreToAdd = 0 ;
        int wordMultiplier = 1;

        for (int i = 0; i < tilesToPlace.size(); i++) {
            word.append(tilesToPlace.get(i).getLetter());
            int row = setRow.get(i);
            int col = setCol.get(i);
            Cell cell = board.getCell(row, col);
            int cellMultiplier = cell.getMultiplier();

            if (cell.isWordMultiplier()){
                wordMultiplier *= cellMultiplier;
                scoreToAdd += tilesToPlace.get(i).getPoints();
            } else {
                scoreToAdd += tilesToPlace.get(i).getPoints() * cellMultiplier;
            }
        }

        scoreToAdd *= wordMultiplier;

        if (direction == 1) {
            int upper = Collections.min(setRow);
            int lower = Collections.max(setRow);
            boolean foundNull = false;
            Cell cell;

            while(upper>0 && !foundNull){
                cell = board.getCell(upper-1,setCol.get(0));
                if(!cell.isEmpty()){
                    word.insert(0, cell.getLetter());
                    scoreToAdd += cell.getTilePoints();
                    upper--;
                }
                else{
                    foundNull = true;
                }
            }

            while(lower<14 && !foundNull){
                cell = board.getCell(lower+1,setCol.get(0));
                if(!cell.isEmpty()){
                    word.append(cell.getLetter());
                    scoreToAdd += cell.getTilePoints();
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
                cell = board.getCell(setRow.get(0),left-1);
                if(!cell.isEmpty()){
                    word.insert(0, cell.getLetter());
                    scoreToAdd += cell.getTilePoints();
                    left--;

                }
                else{
                    foundNull = true;
                }
            }

            while(right<14 && !foundNull){
                cell = board.getCell(setRow.get(0),right+1);
                if(!cell.isEmpty()){
                    word.append(cell.getLetter());
                    right++;
                    scoreToAdd += cell.getTilePoints();
                }
                else{
                    foundNull = true;
                }
            }

        }
        else if (direction == 2) {
//            int row = setRow.get(0);
//            int col = setCol.get(0);
//            StringBuilder horizontalWord = new StringBuilder();
//            StringBuilder verticalWord = new StringBuilder();
//
//            horizontalWord.append(word.toString());
//            verticalWord.append(word.toString());
//
//            boolean foundNull = false;
//            Cell cell;
//
//            int i = row;
//            while(i>0 && !foundNull){
//                cell = board.getCell(i-1,col);
//                if(!cell.isEmpty()){
//                    verticalWord.insert(0, cell.getLetter());
//                    i--;
//                }
//                else{
//                    foundNull = true;
//                }
//            }
//            i = row;
//
//            while(i<14 && !foundNull){
//                cell = board.getCell(i+1,col);
//                if(!cell.isEmpty()){
//                    horizontalWord.append(cell.getLetter());
//                    i++;
//
//                }
//                else{
//                    foundNull = true;
//                }
//            }
//
//            foundNull = false;
//            i = col;
//            ArrayList<Tile> temp = tilesToScore;   // act as a save point for tilesToScore
//
//            if(!dictionary.isValidWord(horizontalWord.toString())){
//                tilesToScore.clear();
//            }
//            while(i>0 && !foundNull){
//                cell = board.getCell(row,i-1);
//                if(!cell.isEmpty()){
//                    verticalWord.insert(0, cell.getLetter());
//                    i--;
//                    tilesToScore.add(cell.getTile());
//                }
//                else{
//                    foundNull = true;
//                }
//            }
//
//            i = col;
//
//            while(i<14 && !foundNull){
//                cell = board.getCell(rowsList.get(0),i+1);
//                if(!cell.isEmpty()){
//                    verticalWord.append(cell.getLetter());
//                    i++;
//                    tilesToScore.add(cell.getTile());
//                }
//                else{
//                    foundNull = true;
//                }
//            }
//
//
//            if (!dictionary.isValidWord(verticalWord.toString())){
//                tilesToScore = temp;
//            }

            scoreToAdd = 10; //random value for testing;
        }

        if(dictionary.isValidWord(word.toString())) {
            System.out.print("Word: "+ word.toString());
            System.out.print("Score: "+scoreToAdd);
            return scoreToAdd;
        } else {
            return 0;
        }

    }

    public int checkDirection(ArrayList<Integer> setRow, ArrayList<Integer> setColumn){

        if (setRow.isEmpty() || setColumn.isEmpty()) {
            System.out.print("Invalid direction: ");
            return -1;
        }

        boolean sameRow = allSame(setRow);
        boolean sameColumn = allSame(setColumn);

        if (sameRow && !sameColumn) {
            return 0;
        } else if (sameColumn && !sameRow) {
            return 1;
        } else if (sameRow && sameColumn) {
            if (setRow.size() == 1) {
                return 2;
            }
            System.out.print("Invalid direction: ");
            return -1;
        }

        System.out.print("Invalid direction: ");
        return -1;
    }

    private static boolean allSame(List<Integer> values) {
        if (values.isEmpty()) {
            return true;
        }

        int first = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) != first) {
                return false;
            }
        }
        return true;
    }


}