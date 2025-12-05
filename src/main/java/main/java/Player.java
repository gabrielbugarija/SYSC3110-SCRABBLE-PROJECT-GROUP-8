package main.java;

import java.util.ArrayList;

public class Player implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    //player class
    private String name;
    private int score;
    private ArrayList<Tile> rack;
    private transient TileBag tileBag = new TileBag();

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

    public ArrayList<Tile> getRackTiles(){
        return this.rack;
    }

    private void initTiles(TileBag tileBag) {

        for(int i=0; i<7;i++){
            assert rack != null;
            rack.add(tileBag.drawTile());

        }
    }



    public void drawTiles(){

        while (rack.size()<7){
            rack.add(tileBag.drawTile());
        }

    }

    public void removeTiles(char c) {

        for(int i=0;i<rack.size();i++){
            if(rack.get(i).getLetter()==c){
                rack.remove(i);
            }
        }

    }

    public void removeTiles(int i) {
        rack.remove(i);
    }

    public void printRack(){
    for(int i=0;i<rack.size();i++){
        Tile tile = rack.get(i);
        System.out.println(tile.getLetter()+": "+tile.getPoints());
    }
    }

    private void drawTiles(TileBag tileBag, int neededTiles) {
        for (int i = 0; i < neededTiles && tileBag.getNumberOfTilesLeft() > 0; i++) {
            rack.add(tileBag.drawTile());
        }
    }

    public int getScore(){
        return score;
        }

    public void addScore(int score){
        this.score += score;
    }

    public void setTileBag(TileBag tileBag){
        this.tileBag = tileBag;
    }

    @Override
    public String toString() {
        return "\nPlayer Name: "+ this.name + " Score: "+this.score +" \n";
    }

    public ArrayList<Tile> getRack() {

        return this.rack;
    }
}
