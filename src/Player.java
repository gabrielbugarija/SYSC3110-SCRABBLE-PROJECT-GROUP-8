/// @author Kemal Sogut - 101280677

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int totalScore;
    private ArrayList<Tile> rack;


    public Player(String name){
        this.name = name;
        this.totalScore = 0;
        this.rack = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<Tile> getRackTiles(){
        return this.rack;
    }

    public int getScore(){
        return this.totalScore;
    }

    public void increaseScore(int points){
        this.totalScore += points;
    }

    public void addTiles(List<Tile> tiles) {
        if (tiles != null) {
            this.rack.addAll(tiles);
        }
    }

    public void removeTiles(List<Tile> tiles) {
        if (tiles != null) {
            this.rack.removeAll(tiles);
        }
    }

    public boolean ownsTile(String word, List<PlacedTile> placements){

    }

    public void fillRack(TileBag tileBag){
        int neededTiles = 7 - rack.size();
        if(neededTiles > 0){
            drawTiles(tileBag, neededTiles);
        }
    }

    private void drawTiles(TileBag tileBag, int neededTiles) {
        for (int i = 0; i < neededTiles && tileBag.getNumberOfTilesLeft() > 0; i++) {
            rack.add(tileBag.drawTile());
        }
    }

    public void printPlayerTiles(){
        for(int i = 0; i< rack.size(); i++){
            Tile tile = rack.get(i);
            System.out.println(tile.getLetter()+": "+tile.getPoints());
        }
    }

    @Override
    public String toString() {
        return "\nPlayer Name: "+ this.name + " Score: "+this.totalScore +" \n";
    }
}
