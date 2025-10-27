import java.util.HashSet;
import java.util.Set;

//Gabriel Bugarija 101262776
public class Dictionary {

private final Set<String> validWords =  new HashSet<>(Set.of("ability", "absorb", "abstract", "academy", "access",
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
        "safe", "salary", "sample", "scene", "school",
        "table", "tackle", "talent", "talk", "target",
        "unable", "uncle", "under", "uniform", "unique",
        "vacant", "valid", "value", "variety", "vehicle",
        "wage", "wait", "walk", "wall", "water",
        "x-ray", "xenon", "xerox", "xylem", "xylophone",
        "yacht", "yard", "year", "yellow", "young",
        "zebra", "zenith", "zero", "zone", "zodiac"));

// Constructor for file pathways(Later milestones)
    public Dictionary() {

    }

// Method to return true if provided word is valid or not given the validWords set
public boolean isValidWord(String word) {
    return validWords.contains(word);
}

//  Getter method for all valid words
public Set<String> getValidWords() {
    return validWords;
}



}
