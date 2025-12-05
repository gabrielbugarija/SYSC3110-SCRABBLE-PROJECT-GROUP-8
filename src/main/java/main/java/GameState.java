package main.java;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState {
    private static final long serialVersionUID = 1L;

    private ArrayList<Player> playerList;
    private int currentPlayer;
    private Board board;
    private TileBag tileBag;
    private boolean isFirstMoveDone;
    private int turnNumber;

    public GameState(ArrayList<Player> playerList, Board board, int currentPlayer, TileBag tileBag, boolean isFirstMoveDone, int turnNumber) {
        this.playerList = playerList;
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.tileBag = tileBag;
        this.isFirstMoveDone = isFirstMoveDone;
        this.turnNumber = turnNumber;
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
