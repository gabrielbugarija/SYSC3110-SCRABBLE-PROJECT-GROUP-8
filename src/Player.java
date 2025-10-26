/// @author Kemal Sogut - 101280677

import java.util.ArrayList;

public class Player {

    private String name;
    private int score;
    private ArrayList<Tile> tiles;


    public Player(String name, TileBag tileBag){
        this.name = name;
        this.score = 0;
        tiles = new ArrayList<>(7);
        initTiles(tileBag);

    }

    private void initTiles(TileBag tileBag) {

        for(int i=0; i<7;i++){
            assert tiles != null;
                tiles.add(tileBag.drawTile());

        }

    }

    public void printPlayerTiles(){
        for(int i=0;i<tiles.size();i++){
            Tile tile = tiles.get(i);
            System.out.println(tile.getLetter()+": "+tile.getMultiplier());
        }
    }
}
