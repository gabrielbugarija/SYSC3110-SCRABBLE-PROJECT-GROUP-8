/// @author Kemal Sogut - 101280677

public class Cell  {
    private int multiplier;
    private Tile tile;
    private boolean empty;

    public Cell(){
        this.multiplier = 1;
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

    public int getTileMultiplier(){

        return tile.getPoints();
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty() {
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


