public class Main {

    public static void main(String[] args) {
        Tile a = new Tile('A', 1);
        Tile blank = new Tile('\0', 1);

        Board board = new Board();
        TileBag tileBag = new TileBag();
        board.setCell(5,5,a);
        board.setCell(5,6,blank);
        board.printBoard();

        tileBag.checkDraw((tileBag.drawTile()).getLetter());
        System.out.println(a.isBlank());
        System.out.println(blank.isBlank());



    }
}