/// @author Kemal Sogut - 101280677

import java.util.*;

public class PlaceTiles {

    private ArrayList<Tile> tilesToPlace = new ArrayList<>();
    private boolean successful;
    private Integer direction; // 1 for vertical, 0 for horizontal, 2 if there is only 1 tile
    List<Integer> rowsList;
    List<Integer> columnsList;
    ArrayList<Tile> tilesToScore;

    public PlaceTiles(Player player, Board board, ArrayList<Tile> tilesToPlace, Set<Integer> rows, Set<Integer> columns){
        this.tilesToPlace = tilesToPlace;
        this.direction = checkDirection(rows, columns);
        this.successful=false;
        this.rowsList = new ArrayList<>(rows);
        this.columnsList = new ArrayList<>(columns);
        this.tilesToScore = tilesToPlace;
    }

    public void placeTiles(Board board){

        if (direction == 0){
            int row = rowsList.getFirst();
            for (int i=0; i<tilesToPlace.size();i++){
                board.setCell(row, columnsList.get(i), tilesToPlace.get(i));
                successful = true;
            }

        } else if (direction == 1) {
            int column = columnsList.getFirst();
            for (int i=0; i<tilesToPlace.size();i++){
                board.setCell(rowsList.get(i),column, tilesToPlace.get(i));
                successful = true;
            }

        } else if (direction == 2) {
            board.setCell(rowsList.getFirst(),columnsList.getFirst(), tilesToPlace.getFirst());
            successful = true;
        }
        else {
            System.out.print("Tile placing error!");
        }
    }

    private int checkDirection(Set<Integer> rows, Set<Integer> columns){
        int direction = -1;
        if (rows.size()==1 && columns.size()>1){
            direction = 0;
        } else if (rows.size()>1 && columns.size()==1) {
            direction = 1;

        } else if (rows.size()==1 && columns.size()==1) {
            direction = 2;
        }
        return direction;
    }



    private boolean checkValidaty(ArrayList<Tile> tilesToPlace, Board board) {

        Dictionary dictionary = new Dictionary();
        StringBuilder word = new StringBuilder();

        for (int i = 0; i < tilesToPlace.size(); i++) {
                word.append(tilesToPlace.get(i).getLetter());
        }

        if (direction == 1) {
            int upper = Collections.min(rowsList);
            int lower = Collections.max(rowsList);
            boolean foundNull = false;
            Cell cell;

            while(upper>0 && !foundNull){
                cell = board.getCell(upper-1,columnsList.getFirst());
                if(!cell.isEmpty()){
                    word.insert(0, cell.getLetter());
                    upper--;
                    tilesToScore.add(cell.getTile());
                }
                else{
                    foundNull = true;
                }
            }

            while(lower<14 && !foundNull){
                cell = board.getCell(lower+1,columnsList.getFirst());
                if(!cell.isEmpty()){
                    word.append(cell.getLetter());
                    lower++;
                    tilesToScore.add(cell.getTile());
                }
                else{
                    foundNull = true;
                }
            }

        } else if (direction == 0) {
            int left = Collections.min(columnsList);
            int right = Collections.max(columnsList);
            boolean foundNull = false;
            Cell cell;

            while(left>0 && !foundNull){
                cell = board.getCell(rowsList.getFirst(),left-1);
                if(!cell.isEmpty()){
                    word.insert(0, cell.getLetter());
                    left--;
                    tilesToScore.add(cell.getTile());
                }
                else{
                    foundNull = true;
                }
            }

            while(right<14 && !foundNull){
                cell = board.getCell(rowsList.getFirst(),right+1);
                if(!cell.isEmpty()){
                    word.append(cell.getLetter());
                    right++;
                    tilesToScore.add(cell.getTile());
                }
                else{
                    foundNull = true;
                }
            }

        } else if (direction == 2) {
            int row = rowsList.getFirst();
            int col = columnsList.getFirst();
            StringBuilder horizontalWord = new StringBuilder();
            StringBuilder VerticalWord = new StringBuilder();

            horizontalWord.append(word.toString());
            VerticalWord.append(word.toString());

            boolean foundNull = false;
            Cell cell;

            int i = row;
            while(i>0 && !foundNull){
                cell = board.getCell(i-1,col);
                if(!cell.isEmpty()){
                    horizontalWord.insert(0, cell.getLetter());
                    i--;
                    tilesToScore.add(cell.getTile());
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
                    tilesToScore.add(cell.getTile());
                }
                else{
                    foundNull = true;
                }
            }

            foundNull = false;
            i = col;

            while(i>0 && !foundNull){
                cell = board.getCell(row,i-1);
                if(!cell.isEmpty()){
                    word.insert(0, cell.getLetter());
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
                    word.append(cell.getLetter());
                    i++;
                    tilesToScore.add(cell.getTile());
                }
                else{
                    foundNull = true;
                }
            }
        }


        if (dictionary.isValidWord(word.toString())) {
                return true;
        } else {
                return false;
        }



    }


    public boolean isSuccessful(){
        return successful;
    }




}
