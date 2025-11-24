import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AIPlayer extends Player {
    ArrayList<String> allCombinations;
    Dictionary dictionary;


    public AIPlayer(String name, TileBag tileBag) {
        super(name, tileBag);
        allCombinations = new ArrayList<>();
        dictionary = new Dictionary();
    }


    static class IndexedString {
        String word;
        List<Integer> indexes;

        IndexedString(String word, List<Integer> indexes) {
            this.word = word;
            this.indexes = indexes;
        }
    }


    public void AIMove(){

    }


    public IndexedString findBestWord() {

        char[] letters = new char[getRack().size()];
        for (int i = 0; i < getRack().size(); i++) {
            letters[i] = getRack().get(i).getLetter();
        }

        for (int length = 7; length >= 2; length--) {

            List<IndexedString> combos = new ArrayList<>();
            generateCombinations(letters, length, 0, new StringBuilder(),
                    new ArrayList<>(), combos);

            for (IndexedString combo : combos) {

                List<IndexedString> perms = new ArrayList<>();
                generatePermutations(combo, 0, perms);

                for (IndexedString perm : perms) {
                    if (dictionary.isValidWord(perm.word)) {
                        return perm;  // perm.word + perm.indexes
                    }
                }
            }
        }

        return null;
    }

    private void generateCombinations(char[] letters, int length, int start,
                                      StringBuilder current,
                                      List<Integer> currentIndexes,
                                      List<IndexedString> result) {

        if (current.length() == length) {
            result.add(new IndexedString(current.toString(),
                    new ArrayList<>(currentIndexes)));
            return;
        }

        for (int i = start; i < letters.length; i++) {
            current.append(letters[i]);
            currentIndexes.add(i);

            generateCombinations(letters, length, i + 1, current, currentIndexes, result);

            current.deleteCharAt(current.length() - 1);
            currentIndexes.remove(currentIndexes.size() - 1);
        }
    }


    private void generatePermutations(IndexedString combo,
                                      int index,
                                      List<IndexedString> result) {

        char[] arr = combo.word.toCharArray();
        List<Integer> idx = combo.indexes;

        generatePermutationRecursive(arr, idx, index, result);
    }

    private void generatePermutationRecursive(char[] arr, List<Integer> idx,
                                              int index,
                                              List<IndexedString> result) {

        if (index == arr.length) {
            result.add(new IndexedString(new String(arr), new ArrayList<>(idx)));
            return;
        }

        for (int i = index; i < arr.length; i++) {
            swap(arr, index, i);
            swap(idx, index, i);

            generatePermutationRecursive(arr, idx, index + 1, result);

            swap(arr, index, i);
            swap(idx, index, i);
        }
    }

    private void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private void swap(List<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }


    public boolean makeMove(gameModel model) {

        ArrayList<Tile> rack = model.getCurrentPlayer().getRack();

        IndexedString validWord = findBestWord();
        if (validWord == null) return false;

        System.out.println("AI Make Move: " + validWord.word);
        System.out.println("Indexes: " + validWord.indexes);

        Cell[][] board = model.getBoard();
        int length = validWord.word.length();


        // place horizontally at center

        if (!model.isFirstMoveDone()) {

            int row = 7;
            int col = 7;

            // place the word horizontally so it covers center
            col = col - (length / 2);
            if (col < 0) col = 0;
            if (col + length > 15) col = 15 - length;

            for (int i = 0; i < length; i++) {
                int rackIndex = validWord.indexes.get(i);
                model.setSelectedRackIndex(rackIndex);
                model.placeSelectedTileAt(row, col + i);
            }

            return true;
        }

        // try horizontal then vertical


        for (int r = 0; r < 15; r++) {
            for (int c = 0; c <= 15 - length; c++) {

                boolean canPlace = true;

                // check if all required cells are empty
                for (int i = 0; i < length; i++) {
                    if (!board[r][c + i].isEmpty()) {
                        canPlace = false;
                        break;
                    }
                }

                if (canPlace) {

                    for (int i = 0; i < length; i++) {
                        int rackIndex = validWord.indexes.get(i);
                        model.setSelectedRackIndex(rackIndex);
                        model.placeSelectedTileAt(r, c + i);
                    }
                    return true;
                }
            }
        }

        for (int r = 0; r <= 15 - length; r++) {
            for (int c = 0; c < 15; c++) {

                boolean canPlace = true;

                for (int i = 0; i < length; i++) {
                    if (!board[r + i][c].isEmpty()) {
                        canPlace = false;
                        break;
                    }
                }

                if (canPlace) {

                    for (int i = 0; i < length; i++) {
                        int rackIndex = validWord.indexes.get(i);
                        model.setSelectedRackIndex(rackIndex);
                        model.placeSelectedTileAt(r + i, c);
                    }
                    return true;
                }
            }
        }

        return false; // no valid place found
    }

}
