import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

//Gabriel Bugarija 101262776
public class Dictionary {


    private final Set<String> validWords = new HashSet<>();


    // Constructor that loads words from file
    public Dictionary() {
        loadWordsFromFile("src/wordsList");
    }

    // Method to return true if provided word is valid or not given the validWords set
    public boolean isValidWord(String word) {
        return validWords.contains(word.toLowerCase());
    }

    //  Getter method for all valid words
    public Set<String> getValidWords() {
        return validWords;
    }

    // Method to load words from the text file
    private void loadWordsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Trim whitespace and convert to lowercase for consistency
                String word = line.trim().toLowerCase();
                if (!word.isEmpty()) {
                    validWords.add(word);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading dictionary file: " + e.getMessage());
            // Optionally, you could initialize with the hardcoded words as fallback
        }
    }


}
