package main.java;


import java.util.ArrayList;
import java.util.List;


public class BoardConfig {

    // Represents one premium square in the JSON
    public static class PremiumSquareConfig {
        public int row;
        public int col;
        public int multiplier;
        public String type;   // "WORD" or "LETTER"
    }

    private String id;
    private String name;
    private int rows;
    private int cols;
    private List<PremiumSquareConfig> premiumSquares = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public List<PremiumSquareConfig> getPremiumSquares() {
        return premiumSquares;
    }

    public void setPremiumSquares(List<PremiumSquareConfig> premiumSquares) {
        this.premiumSquares = premiumSquares;
    }

    // Convenience: add a single premium square
    public void addPremiumSquare(int row, int col, int multiplier, String type) {
        PremiumSquareConfig ps = new PremiumSquareConfig();
        ps.row = row;
        ps.col = col;
        ps.multiplier = multiplier;
        ps.type = type;
        premiumSquares.add(ps);
    }
}

