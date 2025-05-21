import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Player extends GameObject {
    private int lives;
    private int keysCollected;
    private int prevX, prevY;
    private ArrayList<Wall> walls; // Danh sách tường để kiểm tra va chạm

    public Player(int x, int y, ArrayList<Wall> walls) {
        super(x, y, Constants.PLAYER_IMAGE, Color.BLUE);
        this.lives = Constants.TOTAL_LIVES;
        this.keysCollected = 0;
        this.prevX = x;
        this.prevY = y;
        this.walls = walls;
    }

    public void keyPressed(KeyEvent e) {
        int dx = 0, dy = 0;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                dy = -Constants.TILE_SIZE;
                break;
            case KeyEvent.VK_DOWN:
                dy = Constants.TILE_SIZE;
                break;
            case KeyEvent.VK_LEFT:
                dx = -Constants.TILE_SIZE;
                break;
            case KeyEvent.VK_RIGHT:
                dx = Constants.TILE_SIZE;
                break;
        }
        move(dx, dy);
    }

    public void move(int dx, int dy) {
        prevX = x;
        prevY = y;
        int newX = x + dx;
        int newY = y + dy;

        // Kiểm tra giới hạn màn hình
        if (newX < 0 || newX >= Constants.SCREEN_WIDTH || newY < 0 || newY >= Constants.SCREEN_HEIGHT) {
            return;
        }

        // Kiểm tra va chạm với tường
        Rectangle newBounds = new Rectangle(newX, newY, Constants.TILE_SIZE, Constants.TILE_SIZE);
        for (Wall wall : walls) {
            if (newBounds.intersects(wall.getBounds())) {
                return; // Không di chuyển nếu va chạm
            }
        }

        // Di chuyển nếu không va chạm
        x = newX;
        y = newY;
    }

    public void revertToPreviousPosition() {
        x = prevX;
        y = prevY;
    }

    public boolean answerQuestion(Question question, JComponent parent) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Câu hỏi chìa khóa", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        final String[] selectedAnswer = {null};

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(5, 5));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel questionLabel = new JLabel(question.getQuestion(), SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contentPanel.add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1, 5, 5));
        String[] options = question.getOptions();
        for (String option : options) {
            JButton button = new JButton(option);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                selectedAnswer[0] = option;
                dialog.dispose();
            });
            optionsPanel.add(button);
        }
        contentPanel.add(optionsPanel, BorderLayout.CENTER);

        JButton skipButton = new JButton("Bỏ qua");
        skipButton.setFont(new Font("Arial", Font.PLAIN, 14));
        skipButton.setFocusPainted(false);
        skipButton.addActionListener(e -> {
            selectedAnswer[0] = null;
            dialog.dispose();
        });

        JPanel skipPanel = new JPanel();
        skipPanel.add(skipButton);
        contentPanel.add(skipPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        if (selectedAnswer[0] == null) {
            lives--;
            return false;
        }
        boolean isCorrect = selectedAnswer[0].equals(options[question.getCorrectAnswerIndex()]);
        if (isCorrect) {
            keysCollected++;
        } else {
            lives--;
        }
        return isCorrect;
    }

    public int getLives() {
        return lives;
    }

    public int getKeysCollected() {
        return keysCollected;
    }

    public boolean hasEnoughKeys() {
        return keysCollected >= Constants.REQUIRED_KEYS;
    }
}