import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends JFrame implements KeyListener {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int DOT_SIZE = 20;
    private static final int DELAY = 150;

    private List<Point> snake;
    private Point fruit;
    private int direction;
    private boolean gameOver;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snake = new ArrayList<>();
        snake.add(new Point(0, 0)); // initial snake position
        generateFruit();

        direction = KeyEvent.VK_RIGHT;
        gameOver = false;

        Timer timer = new Timer(DELAY, e -> {
            if (!gameOver) {
                move();
                checkCollision();
                repaint();
            }
        });
        timer.start();
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.y -= DOT_SIZE;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y += DOT_SIZE;
                break;
            case KeyEvent.VK_LEFT:
                newHead.x -= DOT_SIZE;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x += DOT_SIZE;
                break;
        }

        snake.add(0, newHead);
        snake.remove(snake.size() - 1);
    }

    private void checkCollision() {
        Point head = snake.get(0);

        // Check if snake collides with itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                break;
            }
        }

        // Check if snake collides with the boundaries
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            gameOver = true;
        }

        // Check if snake eats the fruit
        if (head.equals(fruit)) {
            snake.add(new Point(-1, -1)); // add a new tail segment
            generateFruit();
        }
    }

    private void generateFruit() {
        Random random = new Random();
        int rows = HEIGHT / DOT_SIZE;
        int cols = WIDTH / DOT_SIZE;

        int x = random.nextInt(cols) * DOT_SIZE;
        int y = random.nextInt(rows) * DOT_SIZE;

        fruit = new Point(x, y);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Game Over!", WIDTH / 2 - 80, HEIGHT / 2);
        } else {
            g.setColor(Color.GREEN);

            for (Point point : snake) {
                g.fillRect(point.x, point.y, DOT_SIZE, DOT_SIZE);
            }

            g.setColor(Color.RED);
            g.fillRect(fruit.x, fruit.y, DOT_SIZE, DOT_SIZE);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

