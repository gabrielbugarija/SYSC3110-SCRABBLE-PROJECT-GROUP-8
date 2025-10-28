import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {
    @Test
    public void validateTrueWord1(){
        Dictionary dictionary = new Dictionary();
        assertTrue(dictionary.isValidWord("habit"));
    }

    @Test
    public void validateTrueWord2(){
        Dictionary dictionary = new Dictionary();
        assertTrue(dictionary.isValidWord("economy"));
    }

    @Test
    public void validateFalseWord1(){
        Dictionary dictionary = new Dictionary();
        assertFalse(dictionary.isValidWord("beninging"));
    }

    @Test
    public void validateFalseWord2(){
        Dictionary dictionary = new Dictionary();
        assertFalse(dictionary.isValidWord("drucks"));
    }

    @Test
    public void canGetWordList(){
        Dictionary dictionary = new Dictionary();
        Set<String> validWordsTest =  new HashSet<>(Set.of("ability", "absorb", "abstract", "academy", "access",
                "balance", "balloon", "barrier", "basic", "battery",
                "calculate", "camera", "campus", "capacity", "capital",
                "damage", "danger", "deadline", "decade", "decision",
                "eager", "early", "earth", "economy", "edition",
                "fabric", "factor", "fail", "family", "fashion",
                "galaxy", "gain", "gallery", "garden", "gather",
                "habit", "half", "handle", "happy", "health",
                "idea", "identify", "ignore", "image", "impact",
                "jacket", "jail", "jam", "jealous", "jewel",
                "keen", "keep", "kernel", "kid", "kingdom",
                "label", "labor", "lady", "lake", "language",
                "machine", "magazine", "magic", "maintain", "market",
                "name", "nation", "native", "natural", "network",
                "object", "observe", "obtain", "ocean", "office",
                "package", "page", "pain", "paint", "parent",
                "quality", "quantity", "quarter", "queen", "query",
                "race", "radio", "rain", "raise", "range",
                "safe", "salary", "sample", "scene", "school","set",
                "table", "tackle", "talent", "talk", "target",
                "unable", "uncle", "under", "uniform", "unique",
                "vacant", "valid", "value", "variety", "vehicle",
                "wage", "wait", "walk", "wall", "water",
                "x-ray", "xenon", "xerox", "xylem", "xylophone",
                "yacht", "yard", "year", "yellow", "young",
                "zebra", "zenith", "zero", "zone", "zodiac"));
        assertEquals(dictionary.getValidWords() , validWordsTest);
    }
}