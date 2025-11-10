import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * @author Kemal Sogut - 101280677
 *
 * This Model class represents the MODEL layer in teh MVC pattetn for the number the game.
 * It is a representation of the object of the game.
 *
 */


public class gameModel {

    private ArrayList<Player> playersList = new ArrayList<>();
    public List<gameView> views;
    private int numberOfPlayers;
    private TileBag tileBag = new TileBag();
    public int currentPlayer = 0;

    public gameModel(){
        views = new ArrayList<>();
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

    public void setPlayersList(ArrayList<String> names){
        for (int i = 0; i < numberOfPlayers; i++) {
            playersList.add(new Player(names.get(i), tileBag));
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

    public void addGameView(gameView view){
        views.add(view);
    }

    public void removeGameView(gameView view){
        views.remove(view);
    }
    
    public void advanceTurn(){
        // next player index.
        currentPlayer = (currentPlayer+1)%numberOfPlayers;

        // Iterate through views to update.
        for (gameView view : views){
            view.handleAdvanceTurn();
        }


    }

}
