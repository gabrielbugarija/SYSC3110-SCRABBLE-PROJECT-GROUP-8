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

public class gameFrame extends JFrame implements gameView{

    private Cell[][] board;
    private boolean empty;
    private static final int BOARD_SIZE = 15;
    private static final int CELL_SIZE = 40;

    gameModel model;
    JPanel mainPanel;
    JPanel scorePanel;
    JLabel scoreLabel;
    JPanel boardPanel;
    JPanel rightPanel;
    JLabel tilesLabel;
    JPanel bottomPanel;
    JPanel buttonPanel;
    JPanel tilesPanel;
    JLabel tilesLabelBottom;
    private JButton[] tileButtons;
    private JButton[][] boardButtons = new JButton[15][15];

    public gameFrame() {
        model = new gameModel();
        model.setNumberOfPlayers(getNumberOfPlayers());
        ArrayList<String> names = new ArrayList<>();

        for (int i = 0; i < model.getNumberOfPlayers(); i++) {
            String input = JOptionPane.showInputDialog("Enter name for player " + (i + 1) + " :");
            names.add(input);
        }
        model.setPlayersList(names);

        this.board = new Cell[15][15];
        initBoard();
        this.empty = true;

        setTitle("Scrabble Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        model.addGameView(this);
        gameController gc = new gameController(model);

        // Main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Top score panel
        scorePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        scorePanel.setBackground(new Color(64, 64, 64));

        StringBuilder scoreText = new StringBuilder();
        for (int i = 0; i < model.getNumberOfPlayers(); i++) {
            Player player = model.getPlayersList().get(i);
            scoreText.append(player.getName()).append("'s Score: ").append(player.getScore());
            if (i < model.getPlayersList().size() - 1) {
                scoreText.append("   ");
            }
        }

        scoreLabel = new JLabel(scoreText.toString());
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scorePanel.add(scoreLabel);

        // Board panel with grid
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE + 1, BOARD_SIZE + 1));
        boardPanel.setBackground(Color.BLACK);


        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                if (row == 0 && col == 0) {
                    cell.setBackground(new Color(64, 64, 64));
                } else if (row == 0) {
                    cell.setBackground(new Color(96, 96, 96));
                    JLabel label = new JLabel(String.valueOf(col), SwingConstants.CENTER);
                    label.setForeground(Color.WHITE);
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                    cell.setLayout(new BorderLayout());
                    cell.add(label, BorderLayout.CENTER);
                } else if (col == 0) {
                    cell.setBackground(new Color(96, 96, 96));
                    JLabel label = new JLabel(String.valueOf(row), SwingConstants.CENTER);
                    label.setForeground(Color.WHITE);
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                    cell.setLayout(new BorderLayout());
                    cell.add(label, BorderLayout.CENTER);
                } else {
                    cell.setBackground(Color.WHITE);
                    cell.setLayout(new BorderLayout());
                    cell.add(new JLabel(String.valueOf(board[row][col].getLetter()), SwingConstants.CENTER),
                            BorderLayout.CENTER);
                    cell.addActionListener(gc);
                    cell.setActionCommand(row + " " + col);
                }

                boardButtons[row][col] = cell;
                boardPanel.add(cell);
            }
        }


        // Right info panel
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(150, 0));

        tilesLabel = new JLabel("Tiles In Bag: "+model.getTileBag().getNumberOfTilesLeft(), SwingConstants.CENTER);
        tilesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        tilesLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        rightPanel.add(tilesLabel, BorderLayout.NORTH);

        // Bottom panel with buttons and tiles
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);

        // Button panel
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

        // Tiles panel
        tilesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tilesPanel.setBackground(Color.WHITE);
        tilesLabelBottom = new JLabel(model.getCurrentPlayer().getName() + " 's Tiles:");
        tilesLabelBottom.setFont(new Font("Arial", Font.PLAIN, 12));
        tilesPanel.add(tilesLabelBottom);

        // Add tile buttons
        tileButtons = new JButton[7];
        for (int i = 0; i < 7; i++) {
            tileButtons[i] = new JButton();
            tileButtons[i].setPreferredSize(new Dimension(50, 40));
            tileButtons[i].setBackground(new Color(220, 220, 200));
            tileButtons[i].setFont(new Font("Arial", Font.BOLD, 10));
            tileButtons[i].setText(String.valueOf(model.getCurrentPlayer().getRack().get(i).getLetter()));
            tileButtons[i].setFocusPainted(false);
            tileButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            tilesPanel.add(tileButtons[i]);
            tileButtons[i].setActionCommand("Tile: " + i);
            tileButtons[i].addActionListener(gc);
        }

        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(tilesPanel, BorderLayout.CENTER);

        // Add all panels to main panel
        mainPanel.add(scorePanel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    public void refreshBoard() {
        Cell[][] b = model.getBoard();

        for (int r = 1; r < 15; r++) {
            for (int c = 1; c < 15; c++) {
                JButton btn = boardButtons[r][c];
                if (btn.getComponentCount() > 0 && btn.getComponent(0) instanceof JLabel) {
                    JLabel lbl = (JLabel) btn.getComponent(0);
                    char ch = b[r][c].isEmpty() ? '-' : b[r][c].getLetter();
                    lbl.setText(String.valueOf(ch));
                }
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // Initial entry dialogue.
    public int getNumberOfPlayers() {
        int numberOfPlayers = 0;
        while (true) {
            numberOfPlayers = Integer.parseInt(JOptionPane.showInputDialog("Enter number of Players:"));
            if (numberOfPlayers <= 4 && numberOfPlayers >= 2) {
                break;
            } else {
                System.out.println("The number of player should be between 2 and 4");
            }
        }
        return numberOfPlayers;
    }

    public void updateScoreDisplay() {
        StringBuilder score = new StringBuilder();
        for (int i = 0; i < model.getNumberOfPlayers(); i++) {
            Player player = model.getCurrentPlayer();
            score.append(player.getName()).append("'s Score: ").append(player.getScore());

            if (i < model.getNumberOfPlayers() - 1) {
                score.append(", ");
            }
        }
        scoreLabel.setText(score.toString());

        scorePanel.revalidate();
        scorePanel.repaint();
    }
    // Handles advancing turn to the next player. Updates labels and buttons.
    @Override
    public void handleAdvanceTurn() {

        Player cp = model.getCurrentPlayer();


        String text = (cp.getName() + "'s Turn");
        tilesLabelBottom.setText(text);

        //Update current tile
        tilesLabel.setText("Tiles In Bag: "+model.getTileBag().getNumberOfTilesLeft());

        // Update tile rack on GUI
        for (int i = 0; i < 7; i++) {
            tileButtons[i].setText(String.valueOf(cp.getRack().get(i)));
        }

        updateScoreDisplay();
        refreshBoard();
    }
}