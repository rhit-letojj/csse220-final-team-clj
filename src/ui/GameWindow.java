package ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.GameLogic;

public class GameWindow {

    private static final String START = "start";
    private static final String GAME = "game";
    private static final String OVER = "over";
    private static final String WIN = "win";

    private static final String[] LEVELS = {"level1.txt", "level2.txt", "level3.txt"};

    private static JFrame frame;
    private static JPanel root;
    private static CardLayout cards;

    private static GameLogic model;
    private static int levelIndex = 0;
    
    private static Sound bgm;

    public static void show() {
        frame = new JFrame("CSSE220 Final Project");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cards = new CardLayout();
        root = new JPanel(cards);

        try {
            var playerSprite = ImageIO.read(GameWindow.class.getResource("steveSprite.png"));
            var enemySprite = ImageIO.read(GameWindow.class.getResource("zombieSprite.png"));
            var gemSprite = ImageIO.read(GameWindow.class.getResource("gemSprite.png"));

            model = new GameLogic(playerSprite, enemySprite, gemSprite);
            model.loadLevel(LEVELS[0]);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        bgm = new Sound("bgm.wav");
        bgm.setVolume(-15f);
        bgm.loop();

        root.add(makeStartPanel(), START);
        root.add(makeEndPanel("GAME OVER", "Play Again", () -> {
            try {
                restartToLevel1();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }), OVER);

        root.add(makeEndPanel("YOU WIN!", "Play Again", () -> {
            try {
                restartToLevel1();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }), WIN);

        root.add(new JPanel(), GAME);

        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        cards.show(root, START);
    }

    private static JPanel makeStartPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(155, 188, 15));

        JLabel title = new JLabel("BLOCKY MAZE!", SwingConstants.CENTER);
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 60));
        title.setBounds(0, 120, 800, 80);

        JButton start = new JButton("START");
        start.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));
        start.setBounds(300, 260, 200, 70);
        start.addActionListener(e -> {
            try {
                restartToLevel1();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        p.setPreferredSize(new java.awt.Dimension(800, 800));
        p.add(title);
        p.add(start);
        return p;
    }

    private static JPanel makeEndPanel(String bigText, String buttonText, Runnable onClick) {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(88, 144, 252));

        JLabel title = new JLabel(bigText, SwingConstants.CENTER);
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 72));
        title.setBounds(0, 180, 800, 80);

        JButton btn = new JButton(buttonText);
        btn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));
        btn.setBounds(270, 320, 260, 70);
        btn.addActionListener(e -> onClick.run());

        p.setPreferredSize(new java.awt.Dimension(800, 800));
        p.add(title);
        p.add(btn);
        return p;
    }

    private static void showGameScreen() throws Exception {
        model.loadLevel(LEVELS[levelIndex]);

        GameComponent gc = new GameComponent(
                model,
                () -> {
                    try {
                        nextLevelOrWin();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> cards.show(root, OVER)
        );

        root.add(gc, GAME);
        cards.show(root, GAME);
        frame.pack();
        gc.requestFocusInWindow();
    }

    private static void nextLevelOrWin() throws Exception {
        levelIndex++;
        if (levelIndex >= LEVELS.length) {
            cards.show(root, WIN);
        } else {
            showGameScreen();
        }
    }

    private static void restartToLevel1() throws Exception {
        levelIndex = 0;
        model.restartGame();
        showGameScreen();
    }
}