/**
 *
 * Represents a single cell on the game board.
 * @author Kemal Sogut - 101280677
 * @param multiplier the multiplier of tha cell.
 *
 */

public class Cell  {
    private int multiplier;
    private Tile tile;
    private boolean empty;

    public Cell(){
        this.multiplier = 1;
        this.empty=true;
    }


    public Cell(int multiplier){
        this.multiplier = multiplier;
    }

    public int getMultiplier() {

        return multiplier;
    }

    public char getLetter() {
        if (empty){
            return '-';
        }
        else {
            return  tile.getLetter();
        }

    }

    public void setTile(Tile tile) {

        this.tile = tile;
        setOccupied();
    }




    public Tile getTile() {
        return tile;
    }


    public int getTilePoints(){

        return tile.getPoints();
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty() {
        this.tile=null;
        this.empty = true;
    }

    public void setOccupied() {
        this.empty = false;
    }


    @Override
    public String toString() {
        if(empty){
            return "-";
        }
        return String.valueOf(tile.getLetter());
    }
}


