import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Kemal Sogut - 101280677
 *
 * This Model class represents the MODEL layer in the MVC pattern for the number the game.
 * It is a representation of the object of the game.
 *
 */

public class gameModel {

    private ArrayList<Player> playersList = new ArrayList<>();
    public List<gameView> views;
    private int numberOfPlayers;
    private TileBag tileBag = new TileBag();
    public int currentPlayer = 0;
    private Cell[][] board = new Cell[15][15];
    private int selectedRackIndex = -1;
    private boolean isFirstMoveDone;
    private Dictionary dictionary;
    private int numberOfAIPlayers;

    public gameModel(){
        isFirstMoveDone = false;
        views = new ArrayList<>();
        dictionary = new Dictionary();
        // Initialize board in constructor
        for (int r = 0; r < 15; r++) {
            for (int c = 0; c < 15; c++) {
                board[r][c] = new Cell();
            }
        }
    }

    public void setFirstMoveDone(){
        this.isFirstMoveDone = true;
    }

    public boolean isFirstMoveDone(){
        return isFirstMoveDone;
    }

    public Player getCurrentPlayer(){
        return playersList.get(currentPlayer);
    }

    public void setNumberOfPlayers(int i){
        this.numberOfPlayers = i;
    }

    public int getNumberOfPlayers(){
        return this.numberOfPlayers;
    }

    public int getNumberOfTotalPlayers(){
        return this.numberOfPlayers+this.numberOfAIPlayers;
    }

    public void setPlayersList(ArrayList<String> names){
        for (int i = 0; i < numberOfPlayers; i++) {
            playersList.add(new Player(names.get(i), tileBag));
        }
    }

    public void setAIPlayersList(){
        int x=1;
        for (int i = 0; i < numberOfAIPlayers; i++) {
            playersList.add(new AIPlayer("AI"+x, tileBag));
        }
    }

    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    public void play(int x){

    }

    public void endGame(){

    }

    public void updateStatus(){

    }

    public boolean isCellEmpty(int row, int col) {
        return board[row][col].isEmpty();
    }

    public void addGameView(gameView view){
        views.add(view);
    }

    public char getCellLetter(int row, int col) {
        return board[row][col].isEmpty() ? '-' : board[row][col].getLetter();
    }

    public boolean selectTileIndex(int index) {
        if (index < 0 || index >= getCurrentPlayer().getRack().size()) return false;
        selectedRackIndex = index;
        return true;
    }

    public Tile getSelectedTile() {
        if (selectedRackIndex < 0) return null;
        if (selectedRackIndex >= getCurrentPlayer().getRack().size()) return null;
        return getCurrentPlayer().getRack().get(selectedRackIndex);
    }

    public boolean placeSelectedTileAt(int row, int col) {
        if (selectedRackIndex < 0) return false;
        if (row < 0 || row >= 15 || col < 0 || col >= 15) return false;
        Cell cell = board[row][col];
        if (!cell.isEmpty()) return false;

        Tile tile = getCurrentPlayer().getRack().get(selectedRackIndex);
        cell.setTile(tile);
        cell.setOccupied();

        getCurrentPlayer().removeTiles(selectedRackIndex);
        updateCells();


        selectedRackIndex = -1;
        return true;
    }


    public void removeGameView(gameView view){
        views.remove(view);
    }

    public void advanceTurn(){
        // next player index.
        while (getCurrentPlayer().getRack().size() < 7 && tileBag.size() > 0) {
            getCurrentPlayer().getRack().add(tileBag.drawTile());
        }
        currentPlayer = (currentPlayer+1)%getNumberOfTotalPlayers();
        updateViews();
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setSelectedRackIndex(int idx) {
        this.selectedRackIndex = idx;
    }

    public int getSelectedRackIndex() {
        return selectedRackIndex;
    }

    public void updateViews() {
        for (gameView view : views) {
            view.handleAdvanceTurn();
        }
    }
    public void updateCells() {
        for (gameView view : views) {
            view.handleUpdateCells();
        }
    }


    public TileBag getTileBag(){
        return this.tileBag;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }



    public void setNumberOfAIPlayers(int AIPlayerNumber) {
        numberOfAIPlayers = AIPlayerNumber;
    }

    public int getNumberOfAIPlayers() {
       return numberOfAIPlayers;
    }
}