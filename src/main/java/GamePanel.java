import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {
    private Player player;
    private ArrayList<Key> keys;
    private Door door;
    private ArrayList<Wall> walls;
    private boolean running;
    private Thread gameThread;
    private long startTime;
    private int secondsPlayed;
    private boolean showingDoorMessage;

    public GamePanel() {
        setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        initGame();
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                player.keyPressed(e);
            }
        });
        startGameThread();
    }

    private void initGame() {
        walls = new ArrayList<>();
        player = new Player(0, 0, walls); // Truyền walls vào Player
        door = new Door(768, 608);
        keys = new ArrayList<>();
        Random rand = new Random();
        showingDoorMessage = false;

        for (int i = 0; i < Constants.TOTAL_WALLS; i++) {
            int x, y;
            do {
                x = rand.nextInt(Constants.SCREEN_WIDTH / Constants.TILE_SIZE) * Constants.TILE_SIZE;
                y = rand.nextInt(Constants.SCREEN_HEIGHT / Constants.TILE_SIZE) * Constants.TILE_SIZE;
            } while (isOccupied(x, y));
            walls.add(new Wall(x, y));
        }

        for (int i = 0; i < Constants.TOTAL_KEYS; i++) {
            int x, y;
            do {
                x = rand.nextInt(Constants.SCREEN_WIDTH / Constants.TILE_SIZE) * Constants.TILE_SIZE;
                y = rand.nextInt(Constants.SCREEN_HEIGHT / Constants.TILE_SIZE) * Constants.TILE_SIZE;
            } while (isOccupied(x, y));
            keys.add(new Key(x, y));
        }

        startTime = System.nanoTime();
    }

    private boolean isOccupied(int x, int y) {
        if (x == player.getX() && y == player.getY()) return true;
        if (x == door.getX() && y == door.getY()) return true;
        for (Wall wall : walls) {
            if (x == wall.getX() && y == wall.getY()) return true;
        }
        for (Key key : keys) {
            if (x == key.getX() && y == key.getY()) return true;
        }
        return false;
    }

    private void startGameThread() {
        gameThread = new Thread(this);
        running = true;
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / 60.0;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (running) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        secondsPlayed = (int) ((System.nanoTime() - startTime) / 1000000000);

        for (int i = 0; i < keys.size(); i++) {
            Key key = keys.get(i);
            if (!key.isCollected() && player.getBounds().intersects(key.getBounds())) {
                boolean correct = player.answerQuestion(key.getQuestion(), this);
                if (correct) {
                    key.collect();
                    JOptionPane.showMessageDialog(this, "Correct! Key collected.");
                } else {
                    key.collect();
                    JOptionPane.showMessageDialog(this, "Wrong or skipped! Lost one life.");
                }
                if (player.getLives() <= 0) {
                    JOptionPane.showMessageDialog(this, "Game Over! No lives left.");
                    running = false;
                    return;
                }
            }
        }

        if (player.getBounds().intersects(door.getBounds()) && !showingDoorMessage) {
            showingDoorMessage = true;
            if (player.hasEnoughKeys()) {
                String timeFormatted = String.format("%d:%02d", secondsPlayed / 60, secondsPlayed % 60);
                JOptionPane.showMessageDialog(this, "You won! Time played: " + timeFormatted);
                running = false;
            } else {
                JOptionPane.showMessageDialog(this, "You need " + Constants.REQUIRED_KEYS + " keys to open the door!");
                player.revertToPreviousPosition();
            }
            showingDoorMessage = false;
        }
        else if (!player.getBounds().intersects(door.getBounds())) {
            showingDoorMessage = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (Wall wall : walls) {
            wall.draw(g2);
        }
        for (Key key : keys) {
            if (!key.isCollected()) {
                key.draw(g2);
            }
        }
        door.draw(g2);
        player.draw(g2);

        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Lives: " + player.getLives(), Constants.SCREEN_WIDTH - 110, 20);
        g2.drawString("Keys: " + player.getKeysCollected() + "/" + Constants.REQUIRED_KEYS, Constants.SCREEN_WIDTH - 110, 40);
        String timeFormatted = String.format("Time: %d:%02d", secondsPlayed / 60, secondsPlayed % 60);
        g2.drawString(timeFormatted, Constants.SCREEN_WIDTH - 110, 60);

        g2.dispose();
    }
}