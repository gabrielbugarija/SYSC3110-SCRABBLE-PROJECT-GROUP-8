/// @author Kemal Sogut - 101280677

import java.util.ArrayList;

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

    public void drawTiles(TileBag tileBag) {

        while(rack.size()<7) {
            rack.add(tileBag.drawTile());
        }
    }

    public void printPlayerTiles(){
        for(int i=0;i<rack.size();i++){
            Tile tile = rack.get(i);
            System.out.println(tile.getLetter()+": "+tile.getPoints());
        }
    }

    public void updateScore(int scoreToAdd){
        score += scoreToAdd;
    }



    @Override
    public String toString() {
        return "\nPlayer Name: "+ this.name + " Score: "+this.score+" \n";
    }
}
