package main.java;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Player> playerList;
    private int currentPlayer;
    private Board board;
    private TileBag tileBag;
    private boolean isFirstMoveDone;
    private int turnNumber;
    private BoardConfig config;

    public GameState(ArrayList<Player> playerList, Board board, int currentPlayer, TileBag tileBag, boolean isFirstMoveDone, int turnNumber, BoardConfig config) {
        this.playerList = new ArrayList<>();
        for (Player p: playerList){
            this.playerList.add(p.deepCopy());
        }
        this.board = board.deepCopy(config);
        this.currentPlayer = currentPlayer;
        this.tileBag = tileBag.deepCopy();
        this.isFirstMoveDone = isFirstMoveDone;
        this.turnNumber = turnNumber;
        this.config = config;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public void removeCell(ArrayList<int[]> r)
    {
        for (int[] t : r){
            board.clearCell(t[0], t[1]);
        }
    }
    public TileBag getTileBag() {
        return tileBag;
    }

    public boolean isFirstMoveDone() {
        return isFirstMoveDone;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
}