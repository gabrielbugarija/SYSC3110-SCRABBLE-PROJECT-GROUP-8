package main.java;

import java.util.ArrayList;
import java.util.Set;


public class AIPlayer extends Player {
    private final Dictionary dictionary;

    public AIPlayer(String name, TileBag tileBag) {
        super(name, tileBag);
        this.dictionary = new Dictionary();
    }

    /**
     * Main AI move:
     * 1) Try to extend the last player's word into a real dictionary word by adding ONE tile.
     * 2) If that fails, fall back to placing any tile on the first empty cell.
     */
    public boolean makeMove(gameModel model) {
        // Only move if it is actually this AI's turn
        if (model.getCurrentPlayer() != this) {
            return false;
        }

        ArrayList<int[]> lastMove = model.getTilesPlacedLastTurn();
        Cell[][] board = model.getBoard();

        if (lastMove != null && !lastMove.isEmpty()) {
            int r0 = lastMove.get(0)[0];
            int c0 = lastMove.get(0)[1];

            boolean sameRow = true;
            boolean sameCol = true;
            int minRow = r0, maxRow = r0, minCol = c0, maxCol = c0;

            for (int[] pos : lastMove) {
                int r = pos[0];
                int c = pos[1];

                if (r != r0) sameRow = false;
                if (c != c0) sameCol = false;

                if (r < minRow) minRow = r;
                if (r > maxRow) maxRow = r;
                if (c < minCol) minCol = c;
                if (c > maxCol) maxCol = c;
            }

            // Horizontal word (same row)
            if (sameRow) {
                String baseWord = buildHorizontalWord(board, r0, minCol, maxCol);
                baseWord = baseWord.toLowerCase();

                // Try extend to the RIGHT (word + X)
                if (maxCol + 1 < board[0].length && board[r0][maxCol + 1].isEmpty()) {
                    Character needed = findLetterToExtendRight(baseWord, getRack());
                    if (needed != null) {
                        if (placeSpecificLetterAt(model, r0, maxCol + 1, needed)) {
                            System.out.println("AI " + getName()
                                    + " extended word '" + baseWord + "' to the right using '" + needed + "'.");
                            return true;
                        }
                    }
                }

                // Try extend to the LEFT (X + word)
                if (minCol - 1 >= 0 && board[r0][minCol - 1].isEmpty()) {
                    Character needed = findLetterToExtendLeft(baseWord, getRack());
                    if (needed != null) {
                        if (placeSpecificLetterAt(model, r0, minCol - 1, needed)) {
                            System.out.println("AI " + getName()
                                    + " extended word '" + baseWord + "' to the left using '" + needed + "'.");
                            return true;
                        }
                    }
                }
            }

            // Vertical word (same column)
            if (sameCol) {
                String baseWord = buildVerticalWord(board, minRow, maxRow, c0);
                baseWord = baseWord.toLowerCase();

                // Try extend DOWN (word + X)
                if (maxRow + 1 < board.length && board[maxRow + 1][c0].isEmpty()) {
                    Character needed = findLetterToExtendRight(baseWord, getRack());
                    if (needed != null) {
                        if (placeSpecificLetterAt(model, maxRow + 1, c0, needed)) {
                            System.out.println("AI " + getName()
                                    + " extended word '" + baseWord + "' downward using '" + needed + "'.");
                            return true;
                        }
                    }
                }

                // Try extend UP (X + word)
                if (minRow - 1 >= 0 && board[minRow - 1][c0].isEmpty()) {
                    Character needed = findLetterToExtendLeft(baseWord, getRack());
                    if (needed != null) {
                        if (placeSpecificLetterAt(model, minRow - 1, c0, needed)) {
                            System.out.println("AI " + getName()
                                    + " extended word '" + baseWord + "' upward using '" + needed + "'.");
                            return true;
                        }
                    }
                }
            }
        }


        System.out.println("AI " + getName() + " could not form a real extension. Using fallback move.");

        ArrayList<Tile> rack = getRack();
        if (rack.isEmpty()) {
            return false;
        }

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c].isEmpty()) {
                    // choose highest-point tile
                    int bestIndex = 0;
                    int bestPts = -1;
                    for (int i = 0; i < rack.size(); i++) {
                        int pts = rack.get(i).getPoints();
                        if (pts > bestPts) {
                            bestPts = pts;
                            bestIndex = i;
                        }
                    }

                    model.setSelectedRackIndex(bestIndex);
                    boolean placed = model.placeSelectedTileAt(r, c);
                    if (placed) {
                        System.out.println("AI " + getName()
                                + " placed fallback tile at (" + r + "," + c + ").");
                        return true;
                    }
                }
            }
        }

        return false;
    }


    private String buildHorizontalWord(Cell[][] board, int row, int minCol, int maxCol) {
        StringBuilder sb = new StringBuilder();
        for (int c = minCol; c <= maxCol; c++) {
            sb.append(Character.toLowerCase(board[row][c].getLetter()));
        }
        return sb.toString();
    }

    private String buildVerticalWord(Cell[][] board, int minRow, int maxRow, int col) {
        StringBuilder sb = new StringBuilder();
        for (int r = minRow; r <= maxRow; r++) {
            sb.append(Character.toLowerCase(board[r][col].getLetter()));
        }
        return sb.toString();
    }


    /**
     * Find a letter L in rack such that baseWord + L is a valid dictionary word.
     */
    private Character findLetterToExtendRight(String baseWord, ArrayList<Tile> rack) {
        Set<String> words = dictionary.getValidWords();
        int baseLen = baseWord.length();

        for (String w : words) {
            if (w == null) continue;
            w = w.toLowerCase();
            if (w.length() != baseLen + 1) continue;

            if (w.startsWith(baseWord)) {
                char needed = w.charAt(w.length() - 1);
                if (canProvideLetterFromRack(needed, rack)) {
                    return needed;
                }
            }
        }
        return null;
    }

    /**
     * Find a letter L in rack such that L + baseWord is a valid dictionary word.
     */
    private Character findLetterToExtendLeft(String baseWord, ArrayList<Tile> rack) {
        Set<String> words = dictionary.getValidWords();
        int baseLen = baseWord.length();

        for (String w : words) {
            if (w == null) continue;
            w = w.toLowerCase();
            if (w.length() != baseLen + 1) continue;

            if (w.endsWith(baseWord)) {
                char needed = w.charAt(0);
                if (canProvideLetterFromRack(needed, rack)) {
                    return needed;
                }
            }
        }
        return null;
    }

    /**
     * Check if rack has either a tile with that letter OR a blank.
     */
    private boolean canProvideLetterFromRack(char letter, ArrayList<Tile> rack) {
        letter = Character.toUpperCase(letter);

        // exact letter tile
        for (Tile t : rack) {
            if (!t.isBlank() && Character.toUpperCase(t.getLetter()) == letter) {
                return true;
            }
        }
        // blank tile
        for (Tile t : rack) {
            if (t.isBlank()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Place a specific letter at (row,col) using either a matching tile or a blank.
     */
    private boolean placeSpecificLetterAt(gameModel model, int row, int col, char letter) {
        ArrayList<Tile> rack = getRack();
        letter = Character.toUpperCase(letter);

        // 1) try real letter tile
        int index = findTileIndexForChar(letter, rack);
        if (index == -1) {
            // 2) try blank tile
            index = findBlankTileIndex(rack);
            if (index != -1) {
                rack.get(index).setAssignedLetter(letter);
            }
        }

        if (index == -1) {
            return false; // no tile to produce this letter
        }

        model.setSelectedRackIndex(index);
        return model.placeSelectedTileAt(row, col);
    }

    /**
     * Find index of a non-blank tile matching this letter.
     */
    private int findTileIndexForChar(char letter, ArrayList<Tile> rack) {
        letter = Character.toUpperCase(letter);
        for (int i = 0; i < rack.size(); i++) {
            Tile t = rack.get(i);
            if (!t.isBlank() && Character.toUpperCase(t.getLetter()) == letter) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find index of a blank tile in rack, or -1 if none.
     */
    private int findBlankTileIndex(ArrayList<Tile> rack) {
        for (int i = 0; i < rack.size(); i++) {
            if (rack.get(i).isBlank()) {
                return i;
            }
        }
        return -1;
    }
}