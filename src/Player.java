/// @author Kemal Sogut - 101280677

import java.util.ArrayList;

public class Player {

    private String name;
    private int score;
    private ArrayList<Tile> tiles;


    public Player(String name){
        this.name = name;
        this.score = 0;
        this.tiles = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<Tile> getTilesList(){
        return this.tiles;
    }

    public void drawTiles(TileBag tileBag) {

        while(tiles.size()<7) {


            tiles.add(tileBag.drawTile());
        }
    }

    public void printPlayerTiles(){
        for(int i=0;i<tiles.size();i++){
            Tile tile = tiles.get(i);
            System.out.println(tile.getLetter()+": "+tile.getPoints());
        }
    }



    @Override
    public String toString() {
        return "\nPlayer Name: "+ this.name + " Score: "+this.score+" \n";
    }
}
