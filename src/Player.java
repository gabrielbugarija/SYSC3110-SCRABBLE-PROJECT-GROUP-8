import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Player {

    private String name;
    private int score;
    private ArrayList<Tile> rack;
    private TileBag tileBag = new TileBag();

    public Player(String name, TileBag tileBag){
        this.name = name;
        this.score = 0;
        this.rack = new ArrayList<>();
        this.tileBag = tileBag;
        initTiles(tileBag);
    }

    public String getName(){

        return this.name;
    }

    public ArrayList<Tile> getRack(){
        return this.rack;
    }

    private void initTiles(TileBag tileBag) {

        for(int i=0; i<7;i++){
            assert rack != null;
            rack.add(tileBag.drawTile());

        }

    }


    public void removeTiles(List<Tile> tiles) {
        if (tiles != null) {
            this.rack.removeAll(tiles);
        }

        while (rack.size() < 7) {
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
        return "\nPlayer Name: "+ this.name + " Score: "+this.score +" \n";
    }

    }
