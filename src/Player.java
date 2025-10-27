/// @author Kemal Sogut - 101280677

import java.util.*;

public class Player {

    private String name;
    private int score;
    private ArrayList<Tile> rack;

    public Player(String name){
        this.name = name;
        this.score = 0;
        this.rack = new ArrayList<>();
    }

    public String getName(){

        return this.name;
    }

    public ArrayList<Tile> getRack(){
        return this.rack;
    }

    public void removeTiles(List<Tile> tiles) {
        if (tiles != null) {
            this.rack.removeAll(tiles);
        }
    }

        while(rack.size()<7) {
            rack.add(tileBag.drawTile());
        }
    }

    public void printRack(){
        for(int i=0;i<rack.size();i++){
            Tile tile = rack.get(i);
            System.out.println(tile.getLetter()+": "+tile.getPoints());
        }
    }

    public void updateScore(int scoreToAdd){
        score += scoreToAdd;
    }

    public int getScore(){
        return score;
    }

    @Override
    public String toString() {
        return "\nPlayer Name: "+ this.name + " Score: "+this.totalScore +" \n";
    }

}
