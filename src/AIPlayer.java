import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AIPlayer extends Player {
    ArrayList<String> allCombinations;
    Dictionary dictionary;


    public AIPlayer(String name, TileBag tileBag) {
        super(name, tileBag);
        allCombinations = new ArrayList<>();
        dictionary = new Dictionary();
    }


    public void AIMove(){

    }


    public String findValidWord(){
        String validWord;

        char[] letters = new char[getRack().size()];
        for (int i = 0; i < getRack().size(); i++) {
            letters[i] = getRack().get(i).getLetter();
        }


        for (int length = 7; length >= 2; length--) {
            Set<String> combinations = new HashSet<>();
            generateCombinations(letters, length, 0, new StringBuilder(), combinations);

            for (String combination : combinations) {
                Set<String> perms = new HashSet<>();
                generatePermutations(combination.toCharArray(), 0, perms);

                // Check each permutation against dictionary
                for (String perm : perms) {
                    if (dictionary.isValidWord(perm)) {
                        return perm;
                    }

                }
            }
        }

        return null;
    }

    private void generateCombinations(char[] letters, int length, int start,
                                      StringBuilder current, Set<String> result) {

        if (current.length() == length) {
            result.add(current.toString());
            return;
        }

        for (int i = start; i < letters.length; i++) {
            current.append(letters[i]);
            generateCombinations(letters, length, i + 1, current, result);
            current.deleteCharAt(current.length() - 1); // Backtrack
        }
    }


    private void generatePermutations(char[] chars, int index, Set<String> result) {
        if (index == chars.length - 1) {
            result.add(new String(chars));
            return;
        }

        for (int i = index; i < chars.length; i++) {
            swap(chars, index, i);
            generatePermutations(chars, index + 1, result);
            swap(chars, index, i); // Backtrack
        }
    }



    private void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }

    public boolean makeMove(gameModel model) {

        ArrayList<Tile> rack = model.getCurrentPlayer().getRack();
        for (int i = 0; i < rack.size(); i++) {
            System.out.println(rack.get(i));
        }
        System.out.println("AI Make Move");
        return false;
    }
}
