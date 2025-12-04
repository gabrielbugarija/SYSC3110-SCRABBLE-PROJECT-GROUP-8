package main.java;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads board configurations from JSON files.
 * Provides access to multiple board layouts for player selection.
 */
public class BoardLoader {
    private static final String BOARDS_FILE = "src/main/resources/boards.json";

    private List<BoardConfig> availableBoards = new ArrayList<>();

    public BoardLoader() {
        loadBoards();
    }

    private void loadBoards() {
        try {
            // Read entire JSON file as a String
            byte[] bytes = Files.readAllBytes(Paths.get(BOARDS_FILE));
            String jsonText = new String(bytes, StandardCharsets.UTF_8);

            JSONObject root = new JSONObject(jsonText);

            // Expect: { "boards": [ ... ] }
            JSONArray boardsArray = root.getJSONArray("boards");

            for (int i = 0; i < boardsArray.length(); i++) {
                JSONObject obj = boardsArray.getJSONObject(i);

                BoardConfig cfg = new BoardConfig();
                cfg.setId(obj.optString("id", "BOARD_" + i));
                cfg.setName(obj.optString("name", "Board " + (i + 1)));
                cfg.setRows(obj.optInt("rows", 15));
                cfg.setCols(obj.optInt("cols", 15));

                JSONArray premArr = obj.optJSONArray("premiumSquares");
                if (premArr != null) {
                    for (int j = 0; j < premArr.length(); j++) {
                        JSONObject po = premArr.getJSONObject(j);
                        BoardConfig.PremiumSquareConfig ps =
                                new BoardConfig.PremiumSquareConfig();

                        ps.row        = po.getInt("row");
                        ps.col        = po.getInt("col");
                        ps.multiplier = po.getInt("multiplier");
                        ps.type       = po.getString("type");

                        cfg.getPremiumSquares().add(ps);
                    }
                }

                availableBoards.add(cfg);
            }

            if (availableBoards.isEmpty()) {
                System.err.println("No boards found in JSON. Falling back to standard.");
                addFallbackStandardBoard();
            }

        } catch (Exception e) {
            System.err.println("Error reading boards.json: " + e.getMessage());
            addFallbackStandardBoard();
        }
    }

    private void addFallbackStandardBoard() {
        availableBoards = new ArrayList<>();
        BoardConfig standard = new BoardConfig();
        standard.setId("STANDARD");
        standard.setName("Standard Scrabble");
        standard.setRows(15);
        standard.setCols(15);
        availableBoards.add(standard);
    }

    public List<BoardConfig> getAvailableBoards() {
        return availableBoards;
    }

    public BoardConfig getById(String id) {
        if (availableBoards == null) return null;
        for (BoardConfig cfg : availableBoards) {
            if (cfg.getId() != null && cfg.getId().equalsIgnoreCase(id)) {
                return cfg;
            }
        }
        return null;
    }
}