/// @author Kemal Sogut - 101280677

public class Board {
    private Cell[][] board;

    public Board(){
        this.board = new Cell[15][15];
        initBoard();
    }

    private void initBoard(){
        for(int i=0; i < board.length; i++) {
            for (int j = 0; j< board[i].length; j++){
                board[i][j] = new Cell();
            }

        }

    }

    public void setCell(int row, int column, Tile tile){
        board[row][column].setTile(tile);

    }

    public Cell getCell(int row, int col){
        return board[row][col];
    }

    public void printBoard(){

        for (Cell[] row : board) {
            System.out.println();
            for (Cell cell : row) {
                System.out.print(' ');
                System.out.print(cell);
            }
        }

    }


}
