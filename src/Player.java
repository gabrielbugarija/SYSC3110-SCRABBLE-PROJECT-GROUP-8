import java.util.ArrayList;

/// @author Kemal Sogut - 101280677

public class Player {

    private String name;
    private int score;
    private ArrayList<Tile> tiles;

    public Player(String name){
        this.name = name;
        this.score = 0;
        initTiles();

    }

    private void initTiles() {

    }

}
