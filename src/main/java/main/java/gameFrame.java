package main.java;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import java.awt.event.*;
import java.io.File;

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
    private JButton[][] boardButtons = new JButton[BOARD_SIZE + 1][BOARD_SIZE + 1];

    private JMenuBar menuBar;
    private JMenuItem saveItem;
    private JMenuItem loadItem;
    private JMenuItem saveAsItem;

    public gameFrame() {

        // 1) Load boards from JSON
        BoardLoader loader = new BoardLoader();
        List<BoardConfig> boards = loader.getAvailableBoards();

        // 2) Ask user which board layout to use
        BoardConfig selectedBoard = askUserForBoard(boards);

        // 3) Create model with that layout (null => standard)
        model = new gameModel(selectedBoard);

        // 4) Ask for human / AI counts
        model.setNumberOfPlayers(getNumberOfPlayers());
        model.setNumberOfAIPlayers(getNumberOfAIPlayers());

        // 5) Ask for human player names
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < model.getNumberOfPlayers(); i++) {
            String input = JOptionPane.showInputDialog(
                    "Enter name for player " + (i + 1) + " :"
            );
            names.add(input);
        }
        model.setPlayersList(names);
        model.setAIPlayersList();  // add AI players

        createMenuBar();

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
                } else {
                    // Actual playable board cell
                    Cell boardCell = modelBoard[row - 1][col - 1];

                    JLabel label = new JLabel("", SwingConstants.CENTER);
                    label.setFont(new Font("Arial", Font.BOLD, 12));

                    if (boardCell.getMultiplier() > 1) {
                        cell.setBackground(getCellColor(boardCell));
                        label.setText(getCellText(boardCell)); // TW/DW/TL/DL when empty
                    } else {
                        label.setText("");
                    }

                    cell.add(label, BorderLayout.CENTER);

                    // Hook up to controller, using 0-based model coordinates
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

        JButton undoButton = new JButton("Undo");
        undoButton.setBackground(Color.RED);
        undoButton.setForeground(Color.WHITE);
        undoButton.setFocusPainted(false);
        undoButton.addActionListener(gc);

        JButton redoButton = new JButton("Redo");
        redoButton.setBackground(Color.RED);
        redoButton.setForeground(Color.WHITE);
        redoButton.setFocusPainted(false);
        redoButton.addActionListener(gc);

        buttonPanel.add(playButton);
        buttonPanel.add(swapButton);
        buttonPanel.add(passButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);

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
            tileButtons[i].setText(tile.toString());

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

    private void createMenuBar() {
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        saveItem = new JMenuItem("Save Game", KeyEvent.VK_S);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        saveItem.addActionListener(e -> saveGame());

        saveAsItem = new JMenuItem("Save Game As...", KeyEvent.VK_A);
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | KeyEvent.SHIFT_DOWN_MASK));
        saveAsItem.addActionListener(e -> saveGameAs());

        loadItem = new JMenuItem("Load Game", KeyEvent.VK_L);
        loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        loadItem.addActionListener(e -> loadGame());

        JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void loadGame() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Loading a game will replace the current game. Continue?",
                "Confirm Load",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Scrabble Game");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".dat");
            }

            @Override
            public String getDescription() {
                return "Scrabble Save Files (*.dat)";
            }
        });

        // Start in the default save directory
        String defaultDir = model.getDefaultSaveDirectory();
        File defaultDirectory = new File(defaultDir);
        if (defaultDirectory.exists() && defaultDirectory.isDirectory()) {
            fileChooser.setCurrentDirectory(defaultDirectory);
        }

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            String filePath = fileToLoad.getAbsolutePath();

            boolean success = model.loadGame(filePath);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Game successfully loaded from:\n" + filePath,
                        "Load Complete",
                        JOptionPane.INFORMATION_MESSAGE);

                // Refresh the display
                handleAdvanceTurn();
            }
        }
    }

    private void saveGame() {
        String defaultPath = model.getDefaultSaveFilePath();

        int choice = JOptionPane.showConfirmDialog(this,
                "Save game to default location?\n" + defaultPath,
                "Save Game",
                JOptionPane.YES_NO_CANCEL_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            boolean success = model.saveGame(defaultPath);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Game successfully saved!",
                        "Save Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (choice == JOptionPane.NO_OPTION) {
            saveGameAs();
        }
    }

    private void saveGameAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        fileChooser.setSelectedFile(new File("scrabble_save.dat"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".dat");
            }

            @Override
            public String getDescription() {
                return "Scrabble Save Files (*.dat)";
            }
        });

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Ensure .dat extension
            if (!filePath.toLowerCase().endsWith(".dat")) {
                filePath += ".dat";
            }

            // Check if file exists
            File targetFile = new File(filePath);
            if (targetFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(this,
                        "File already exists. Overwrite File?",
                        "Confirm Overwrite",
                        JOptionPane.YES_NO_OPTION);

                if (overwrite != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            boolean success = model.saveGame(filePath);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Game saved successfully to:\n" + filePath,
                        "Save Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    // Board-selection dialog
    private BoardConfig askUserForBoard(List<BoardConfig> boards) {
        if (boards == null || boards.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No boards found in configuration. Using standard board.",
                    "Board Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        String[] options = new String[boards.size()];
        for (int i = 0; i < boards.size(); i++) {
            options[i] = boards.get(i).getName();
        }

        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Choose a board layout:",
                "Board Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == null) {
            return boards.get(0);
        }

        for (BoardConfig cfg : boards) {
            if (cfg.getName().equals(choice)) {
                return cfg;
            }
        }
        return boards.get(0);
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
                    JOptionPane.showInputDialog("Enter number of human Players:")
            );
            if (numberOfPlayers >= 1 && numberOfPlayers <= 4) {
                break;
            } else {
                System.out.println("The number of human players should be between 1 and 4");
            }
        }
        return numberOfPlayers;
    }

    public int getNumberOfAIPlayers() {
        int aiPlayers;
        while (true) {
            aiPlayers = Integer.parseInt(
                    JOptionPane.showInputDialog("Enter number of AI Players (0-3):")
            );
            int total = aiPlayers + model.getNumberOfPlayers();
            if (aiPlayers >= 0 && aiPlayers <= 3 && total >= 2 && total <= 4) {
                break;
            } else {
                System.out.println("Total players (human + AI) must be 2 to 4, AI between 0 and 3.");
            }
        }
        return aiPlayers;
    }

    private String buildScoreText() {
        StringBuilder scoreText = new StringBuilder();
        for (int i = 0; i < model.getPlayersList().size(); i++) {
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

    @Override
    public void handleAdvanceTurn() {
        Player cp = model.getCurrentPlayer();

        tilesLabelBottom.setText(cp.getName() + "'s Turn");
        tilesLabel.setText("Tiles In Bag: " + model.getTileBag().getNumberOfTilesLeft());


        for (int i = 0; i < 7 && i < cp.getRack().size(); i++) {
            tileButtons[i].setText(String.valueOf(cp.getRack().get(i)));
        }

        refreshBoard();
    }

    private Color getCellColor(Cell cell) {
        if (cell.getMultiplier() > 1) {
            if (cell.isWordMultiplier()) {
                return cell.getMultiplier() == 3
                        ? new Color(255, 150, 150)
                        : new Color(255, 200, 200);
            } else {
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

    private String getTileDisplayText(Cell cell) {
        Tile t = cell.getTile();
        if (t == null) {
            return "";
        }
        if (t.isBlank()) {
            char assigned = t.getAssignedLetter();
            if (assigned != '\0') {
                return String.valueOf(Character.toLowerCase(assigned));
            }
            return "*";
        }
        return String.valueOf(t.getLetter());
    }
}
