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
        this.tilesToScore = new ArrayList<>();

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






    public boolean isSuccessful(){
        return successful;
    }
}
