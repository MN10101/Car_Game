import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Random;

public class CarGame extends JPanel implements ActionListener, KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int ROAD_W = WIDTH / 2;
    private static final int ROADMARK_W = WIDTH / 80;
    private static final int CAR_WIDTH = 100;
    private static final int CAR_HEIGHT = 100;
    private static final int INIT_SPEED = 10;

    private Timer timer;
    private int speed = INIT_SPEED;
    private int counter = 0;
    private boolean gameOver = false;

    private Image car;
    private Image car2;
    private Rectangle carRect;
    private Rectangle car2Rect;
    private int rightLane;
    private int leftLane;

    private Clip roadClip;
    private Clip hornClip;
    private Clip brakeClip;
    private Clip collisionClip;

    public CarGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.GRAY);

        car = new ImageIcon(getClass().getResource("/car.png")).getImage().getScaledInstance(CAR_WIDTH, CAR_HEIGHT, Image.SCALE_SMOOTH);
        car2 = new ImageIcon(getClass().getResource("/car2.png")).getImage().getScaledInstance(CAR_WIDTH, CAR_HEIGHT, Image.SCALE_SMOOTH);

        rightLane = WIDTH / 2 + ROAD_W / 4 - CAR_WIDTH / 2;
        leftLane = WIDTH / 2 - ROAD_W / 4 - CAR_WIDTH / 2;

        carRect = new Rectangle(rightLane, HEIGHT * 4 / 5, CAR_WIDTH, CAR_HEIGHT);
        car2Rect = new Rectangle(leftLane, -200, CAR_WIDTH, CAR_HEIGHT);

        timer = new Timer(10, this);
        timer.start();

        this.setFocusable(true);
        this.addKeyListener(this);

        // Load sound clips
        roadClip = loadSound("/road.wav");
        hornClip = loadSound("/horn.wav");
        brakeClip = loadSound("/brake.wav");
        collisionClip = loadSound("/collision.wav");

        // Loop road sound continuously
        if (roadClip != null) {
            roadClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    private Clip loadSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (Exception e) {
            System.err.println("Error loading sound: " + filePath);
            e.printStackTrace();
            return null;
        }
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);  // Rewind to the beginning
            clip.start();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        drawBackground(g2d);
        drawRoad(g2d);
        drawCars(g2d);
        drawInfo(g2d);
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRect(0, 0, WIDTH / 4, HEIGHT);
        g2d.fillRect(3 * WIDTH / 4, 0, WIDTH / 4, HEIGHT);
    }

    private void drawRoad(Graphics2D g2d) {
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRect(WIDTH / 2 - ROAD_W / 2, 0, ROAD_W, HEIGHT);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(WIDTH / 2 - ROADMARK_W / 2, 0, ROADMARK_W, HEIGHT);
        g2d.fillRect(WIDTH / 2 - ROAD_W / 2 + 2 * ROADMARK_W, 0, ROADMARK_W, HEIGHT);
        g2d.fillRect(WIDTH / 2 + ROAD_W / 2 - 3 * ROADMARK_W, 0, ROADMARK_W, HEIGHT);
    }

    private void drawCars(Graphics2D g2d) {
        g2d.drawImage(car, carRect.x, carRect.y, null);
        g2d.drawImage(car2, car2Rect.x, car2Rect.y, null);
    }

    private void drawInfo(Graphics2D g2d) {
        if (gameOver) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            g2d.drawString("GAME OVER!", WIDTH / 2 - 100, HEIGHT / 2);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            g2d.drawString("Speed: " + speed, 10, 20);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            counter++;
            if (counter == 500) {
                speed += 0.15;
                counter = 0;
                System.out.println("Level up: Speed = " + speed);
            }

            car2Rect.y += speed;
            if (car2Rect.y > HEIGHT) {
                Random rand = new Random();
                if (rand.nextInt(2) == 0) {
                    car2Rect.setLocation(rightLane, -200);
                } else {
                    car2Rect.setLocation(leftLane, -200);
                }
            }

            if (carRect.intersects(car2Rect)) {
                System.out.println("GAME OVER!");
                gameOver = true;
                timer.stop();
                playSound(brakeClip);
                playSound(collisionClip);
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            carRect.setLocation(leftLane, carRect.y);
            playSound(hornClip);
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            carRect.setLocation(rightLane, carRect.y);
            playSound(hornClip);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Do nothing
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Car Game");
        CarGame game = new CarGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
