import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Kemal Sogut - 101280677
 *
 * This gameFrame class represents the FRAME layer in teh MVC pattetn for the number the game.
 * Implemets the JFrame and components. As well as handles action event.
 *
 */

public class gameFrame extends JFrame implements gameView {

    private static final int BOARD_SIZE = 15;   // playable cells
    private static final int CELL_SIZE  = 40;   // pixel size of each board cell

    // NOTE: this local board is no longer used for logic, model's board is the truth
    private Cell[][] board = new Cell[BOARD_SIZE][BOARD_SIZE];

    gameModel model;

    JPanel  mainPanel;
    JPanel  scorePanel;
    JLabel  scoreLabel;
    JPanel  boardPanel;
    JPanel  rightPanel;
    JLabel  tilesLabel;
    JPanel  bottomPanel;
    JPanel  buttonPanel;
    JPanel  tilesPanel;
    JLabel  tilesLabelBottom;

    private JButton[]   tileButtons;
    // +1 for row/column headers
    private JButton[][] boardButtons = new JButton[BOARD_SIZE + 1][BOARD_SIZE + 1];

    public gameFrame() {
        model = new gameModel();
        model.setNumberOfPlayers(getNumberOfPlayers());
        ArrayList<String> names = new ArrayList<>();

        for (int i = 0; i < model.getNumberOfPlayers(); i++) {
            String input = JOptionPane.showInputDialog("Enter name for player " + (i + 1) + " :");
            names.add(input);
        }
        model.setPlayersList(names);

        initBoard();

        setTitle("Scrabble Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        model.addGameView(this);
        gameController gc = new gameController(model);

        // ===== MAIN PANEL =====
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ===== TOP SCORE PANEL =====
        scorePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        scorePanel.setBackground(new Color(64, 64, 64));

        scoreLabel = new JLabel(buildScoreText());
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scorePanel.add(scoreLabel);

        // ===== BOARD PANEL =====
        // +1 row/col for headers
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE + 1, BOARD_SIZE + 1));
        boardPanel.setBackground(Color.BLACK);

        Cell[][] modelBoard = model.getBoard();

        for (int row = 0; row <= BOARD_SIZE; row++) {
            for (int col = 0; col <= BOARD_SIZE; col++) {

                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                cell.setLayout(new BorderLayout());

                // Header cells
                if (row == 0 && col == 0) {
                    cell.setBackground(new Color(64, 64, 64)); // top-left corner
                } else if (row == 0) {
                    // column headers
                    cell.setBackground(new Color(96, 96, 96));
                    JLabel label = new JLabel(String.valueOf(col), SwingConstants.CENTER);
                    label.setForeground(Color.WHITE);
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                    cell.add(label, BorderLayout.CENTER);
                } else if (col == 0) {
                    // row headers
                    cell.setBackground(new Color(96, 96, 96));
                    JLabel label = new JLabel(String.valueOf(row), SwingConstants.CENTER);
                    label.setForeground(Color.WHITE);
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                    cell.add(label, BorderLayout.CENTER);
                } else {   // real board cell
                    cell.setLayout(new BorderLayout());
                    Cell boardCell = model.getBoard()[row - 1][col - 1];

                    if (boardCell.getMultiplier() > 1) {
                        cell.setBackground(getCellColor(boardCell));
                        JLabel label = new JLabel(getCellText(boardCell), SwingConstants.CENTER);
                        label.setFont(new Font("Arial", Font.BOLD, 12));
                        cell.add(label, BorderLayout.CENTER);
                    } else {
                        JLabel label = new JLabel(String.valueOf(boardCell.getLetter()), SwingConstants.CENTER);
                        label.setFont(new Font("Arial", Font.BOLD, 12));
                        cell.add(label, BorderLayout.CENTER);
                    }

                    cell.addActionListener(gc);
                    cell.setActionCommand((row - 1) + " " + (col - 1));
                }


                boardButtons[row][col] = cell;
                boardPanel.add(cell);
            }
        }

        // ===== RIGHT INFO PANEL =====
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(150, 0));

        tilesLabel = new JLabel(
                "Tiles In Bag: " + model.getTileBag().getNumberOfTilesLeft(),
                SwingConstants.CENTER
        );
        tilesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        tilesLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        rightPanel.add(tilesLabel, BorderLayout.NORTH);

        // ===== BOTTOM PANEL =====
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);

        // --- Buttons (Play / Swap / Pass) ---
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);

        JButton playButton = new JButton("Play");
        playButton.setBackground(Color.GREEN);
        playButton.setForeground(Color.BLACK);
        playButton.setFocusPainted(false);
        playButton.addActionListener(gc);

        JButton swapButton = new JButton("Swap");
        swapButton.setBackground(Color.YELLOW);
        swapButton.setForeground(Color.BLACK);
        swapButton.setFocusPainted(false);
        swapButton.addActionListener(gc);

        JButton passButton = new JButton("Pass");
        passButton.setBackground(Color.RED);
        passButton.setForeground(Color.WHITE);
        passButton.setFocusPainted(false);
        passButton.addActionListener(gc);

        buttonPanel.add(playButton);
        buttonPanel.add(swapButton);
        buttonPanel.add(passButton);

        // --- Player tiles (rack) ---
        tilesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tilesPanel.setBackground(Color.WHITE);

        tilesLabelBottom = new JLabel(model.getCurrentPlayer().getName() + " 's Tiles:");
        tilesLabelBottom.setFont(new Font("Arial", Font.PLAIN, 12));
        tilesPanel.add(tilesLabelBottom);

        tileButtons = new JButton[7];
        for (int i = 0; i < 7; i++) {
            tileButtons[i] = new JButton();
            tileButtons[i].setPreferredSize(new Dimension(50, 40));
            tileButtons[i].setBackground(new Color(220, 220, 200));
            tileButtons[i].setFont(new Font("Arial", Font.BOLD, 10));

            Tile tile = model.getCurrentPlayer().getRack().get(i);
            tileButtons[i].setText(tile.toString());  // shows '*' or assigned letter

            tileButtons[i].setFocusPainted(false);
            tileButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            tileButtons[i].setActionCommand("Tile: " + i);
            tileButtons[i].addActionListener(gc);

            tilesPanel.add(tileButtons[i]);
        }

        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(tilesPanel, BorderLayout.CENTER);

        // ===== ADD PANELS TO FRAME =====
        mainPanel.add(scorePanel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** local dummy board init (model’s board is the real one). */
    private void initBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    /** Redraws the board from the model after a move. */
    public void refreshBoard() {
        Cell[][] b = model.getBoard();

        for (int r = 1; r <= BOARD_SIZE; r++) {
            for (int c = 1; c <= BOARD_SIZE; c++) {
                JButton btn = boardButtons[r][c];
                if (btn.getComponentCount() > 0 && btn.getComponent(0) instanceof JLabel) {
                    JLabel lbl = (JLabel) btn.getComponent(0);
                    Cell cell = b[r - 1][c - 1];

                    if (!cell.isEmpty() && cell.getTile() != null) {
                        // There is a tile here – show tile letter (handles blanks)
                        lbl.setText(getTileDisplayText(cell));
                    } else if (cell.getMultiplier() > 1) {
                        lbl.setText(getCellText(cell));
                    } else {
                        lbl.setText("");
                    }
                }
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
        updateScoreDisplay();
    }

    public int getNumberOfPlayers() {
        int numberOfPlayers;
        while (true) {
            numberOfPlayers = Integer.parseInt(
                    JOptionPane.showInputDialog("Enter number of Players:")
            );
            if (numberOfPlayers <= 4 && numberOfPlayers >= 2) {
                break;
            } else {
                System.out.println("The number of player should be between 2 and 4");
            }
        }
        return numberOfPlayers;
    }

    private String buildScoreText() {
        StringBuilder scoreText = new StringBuilder();
        for (int i = 0; i < model.getNumberOfPlayers(); i++) {
            Player player = model.getPlayersList().get(i);
            scoreText.append(player.getName())
                    .append("'s Score: ")
                    .append(player.getScore());
            if (i < model.getPlayersList().size() - 1) {
                scoreText.append("   ");
            }
        }
        return scoreText.toString();
    }

    public void updateScoreDisplay() {
        scoreLabel.setText(buildScoreText());
        scorePanel.revalidate();
        scorePanel.repaint();
    }

    // Handles advancing turn to the next player. Updates labels and buttons.
    @Override
    public void handleAdvanceTurn() {
        Player cp = model.getCurrentPlayer();

        tilesLabelBottom.setText(cp.getName() + "'s Turn");
        tilesLabel.setText("Tiles In Bag: " + model.getTileBag().getNumberOfTilesLeft());

        // Update tile rack on GUI
        for (int i = 0; i < 7; i++) {
            tileButtons[i].setText(String.valueOf(cp.getRack().get(i)));
        }

        refreshBoard();
    }

    private Color getCellColor(Cell cell) {
        if (cell.getMultiplier() > 1) {
            if (cell.isWordMultiplier()) {
                // word bonus: red-ish
                return cell.getMultiplier() == 3
                        ? new Color(255, 150, 150)
                        : new Color(255, 200, 200);
            } else {
                // letter bonus: blue-ish
                return cell.getMultiplier() == 3
                        ? new Color(150, 150, 255)
                        : new Color(200, 200, 255);
            }
        }
        return Color.WHITE;
    }

    private String getCellText(Cell cell) {
        if (cell.getMultiplier() > 1) {
            if (cell.isWordMultiplier()) {
                return cell.getMultiplier() == 3 ? "TW" : "DW";
            } else {
                return cell.getMultiplier() == 3 ? "TL" : "DL";
            }
        }
        return "";
    }

    /**
     * Text to show for a tile on the board, including blanks.
     */
    private String getTileDisplayText(Cell cell) {
        Tile t = cell.getTile();
        if (t == null) {
            return "";
        }
        if (t.isBlank()) {
            char assigned = t.getAssignedLetter();
            if (assigned != '\0') {
                // show lower-case letter for assigned blank, to match rack display
                return String.valueOf(Character.toLowerCase(assigned));
            }
            return "*"; // unassigned blank (should be rare, but safe)
        }
        return String.valueOf(t.getLetter());
    }
}