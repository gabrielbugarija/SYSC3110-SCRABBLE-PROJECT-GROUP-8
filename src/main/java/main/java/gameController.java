package main.java;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;



/**
 * @author Kemal Sogut - 101280677
 *
 * This Number Controller class represents the Controller layer in teh MVC pattetn for the number the game
 *
 */

public class gameController implements ActionListener {

    private boolean swapMode = false;
    ArrayList<Integer> tilesToSwap = new ArrayList<>();
    gameModel model;
    ArrayList<GameState> undoList = new ArrayList<>();
    ArrayList<GameState> redoList = new ArrayList<>();

    public gameController(gameModel model){
        this.model = model;
        undoList.add(new GameState(model.getPlayersList(), model.getBoardNoCell(), model.getCurrentPlayerInt(), model.getTileBag(), model.isFirstMoveDone(), model.getTurnNumber(), model.getBoardConfig()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        System.out.println(command);

        if (command.equals("Play")) {
            GameState currState = new GameState(model.getPlayersList(), model.getBoardNoCell(), model.getCurrentPlayerInt(), model.getTileBag(), model.isFirstMoveDone(), model.getTurnNumber(), model.getBoardConfig());
            currState.removeCell(model.getTilesPlacedThisTurn());
            undoList.add(currState);
            redoList.clear();
            int points = model.calculateScoreForCurrentMove();

            if (points == 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid move or no valid word formed.",
                        "Invalid Move",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            model.getCurrentPlayer().addScore(points);
            System.out.println("Move scored: " + points + " points");

            // Move to next player (could be an AI)
            model.advanceTurn();

            // Let AI(s) play automatically if it's their turn
            model.runAITurnsIfNeeded();
            return;
        }




        // If Swap button pressed.
        if (command.equals("Swap")) {

            if (!swapMode) {
                System.out.println("Swap mode entered");
                // Enter swap mode
                swapMode = true;
                tilesToSwap.clear();

            } else if (swapMode){
                System.out.println(tilesToSwap);
                Player cp = model.getCurrentPlayer();
                System.out.println("Swap mode exited");
                // perform tiles swap.
                for (Integer i: tilesToSwap){
                    cp.removeTiles(i);
                }
                cp.drawTiles();

                model.advanceTurn();
                model.runAITurnsIfNeeded();
            }


        }

        // If Pass button pressed.
        if (Objects.equals(command, "Pass")) {
            model.advanceTurn();
            model.runAITurnsIfNeeded();
        }


        if (command.startsWith("Tile: ")) {
            int idx = Integer.parseInt(command.substring(6).trim());
            if (swapMode){
                System.out.println("Tiles to Swap: " + idx);
                tilesToSwap.add(idx);
                System.out.println(tilesToSwap);
            }
            else {
                model.selectTileIndex(idx);

                // Check if selected tile is blank and prompt for letter
                Tile selectedTile = model.getSelectedTile();
                if (selectedTile != null && selectedTile.isBlank() && selectedTile.getAssignedLetter() == '\0') {
                    String letter = JOptionPane.showInputDialog(
                            null,
                            "This is a blank tile! Enter a letter (A-Z):",
                            "Assign Blank Tile",
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (letter != null && letter.length() == 1 && Character.isLetter(letter.charAt(0))) {
                        selectedTile.setAssignedLetter(letter.charAt(0));
                        System.out.println("Blank tile assigned as: " + letter.toUpperCase());
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid letter. Please try again.");
                        return;
                    }
                }

                System.out.println("Selected tile index: " + idx);
                return;
            }
        }

        if (command.matches("\\d+ \\d+")) {
            String[] parts = command.split(" ");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);

            if (model.placeSelectedTileAt(row, col)) {
                System.out.println("Placed tile on board.");
            } else {
                System.out.println("Could not place tile.");
            }
            model.updateViews();

            return;
        }

        if (command.contains(" ")) {
            if(swapMode){
                return;
            }
            String[] rc = command.split(" ");
            int r = Integer.parseInt(rc[0]);
            int c = Integer.parseInt(rc[1]);
            if (model.placeSelectedTileAt(r, c)) {
            }
        }

        if (command.equals("Undo")){
            if (undoList.isEmpty()){
                JOptionPane.showMessageDialog(null, "Cannot Undo anymore");
            }
            redoList.add(new GameState(model.getPlayersList(), model.getBoardNoCell(), model.getCurrentPlayerInt()+1, model.getTileBag(), model.isFirstMoveDone(), model.getTurnNumber(), model.getBoardConfig()));
            model.changeModel(undoList.remove(undoList.size()-1));
            model.updateViews();
            return;
        }

        if (command.equals("Redo")){
            if (redoList.isEmpty() || model.getTurnNumber() == redoList.size()+1){
                JOptionPane.showMessageDialog(null, "Cannot Redo");
            }
            undoList.add(new GameState(model.getPlayersList(), model.getBoardNoCell(), model.getCurrentPlayerInt(), model.getTileBag(), model.isFirstMoveDone(), model.getTurnNumber(), model.getBoardConfig()));
            model.changeModel((redoList.remove(redoList.size()-1)));
            model.advanceTurn();
            return;
        }
    }
}