import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import static java.lang.Math.*;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 500;
    static final int DELAY = 6;

    final int gap = 100;
    final int BLOCK_WIDTH = 15;
    final int BLOCK_HEIGHT = 100;
    final int OVAL_RADIUS = 50;
    final int BALL_RADIUS = 20;
    final int PLAYER_SPEED = 2;
    final int DRIFT = 1;
    final int leftX = gap, rightX = SCREEN_WIDTH - gap - BLOCK_WIDTH;
    int leftY = SCREEN_HEIGHT / 2 - BLOCK_HEIGHT / 2, rightY = SCREEN_HEIGHT / 2 - BLOCK_HEIGHT / 2;
    double ballX = SCREEN_WIDTH / 2 - BALL_RADIUS / 2, ballY = SCREEN_HEIGHT / 2 - BALL_RADIUS / 2;
    int direction = 0;
    double ballSpeed = 3;
    boolean running = false;
    int leftMovement = 0, rightMovement = 0;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        running = true;
        direction = random.nextInt(45);
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running) {
            //blocks
            g.setColor(Color.blue);
            g.fillRect(leftX, leftY, BLOCK_WIDTH, BLOCK_HEIGHT);
            g.setColor(Color.red);
            g.fillRect(rightX, rightY, BLOCK_WIDTH, BLOCK_HEIGHT);
            //field
            g.setColor(Color.white);
            g.drawLine(SCREEN_WIDTH / 2, 0, SCREEN_WIDTH / 2, SCREEN_HEIGHT);
            g.drawOval(SCREEN_WIDTH / 2 - OVAL_RADIUS / 2, SCREEN_HEIGHT / 2 - OVAL_RADIUS / 2, OVAL_RADIUS, OVAL_RADIUS);
            //ball
            g.setColor(new Color(45, 180, 0));
            g.fillOval((int)ballX, (int)ballY, BALL_RADIUS, BALL_RADIUS);
        }
        else{
            gameOver(g);
        }
    }
    private void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }
    public void move(){
        ballX += cos(toRadians(direction)) * ballSpeed;
        ballY += sin(toRadians(direction)) * ballSpeed;

        if(ballX >= leftX && ballX <= leftX + BLOCK_WIDTH && ballY + BALL_RADIUS >= leftY && ballY <= leftY + BLOCK_HEIGHT) {
            ballX -= cos(toRadians(direction)) * ballSpeed;
            ballY -= sin(toRadians(direction)) * ballSpeed;
            if(direction < 180){
                direction = 180 - direction;
            }
            else{
                direction = 180 + 360 - direction;
            }
            ballSpeed += 0.2;
            int mid = leftY + BLOCK_HEIGHT / 2;
            direction += DRIFT * (ballY - mid);
            if(direction < 240 && direction > 180){
                direction = 240;
            }
            if(direction > 60 && direction <= 180){
                direction = 60;
            }
        }
        if(ballX + BALL_RADIUS >= rightX && ballX + BALL_RADIUS <= rightX + BLOCK_WIDTH && ballY + BALL_RADIUS >= rightY && ballY <= rightY + BLOCK_HEIGHT){
            ballX -= cos(toRadians(direction)) * ballSpeed;
            ballY -= sin(toRadians(direction)) * ballSpeed;
            if(direction < 90){
                direction = 180 - direction;
            }
            else{
                direction = 180 + 360 - direction;
            }
            ballSpeed += 0.2;
            int mid = rightY + BLOCK_HEIGHT / 2;
            direction += DRIFT * (ballY - mid);
            if(direction > 240){
                direction = 240;
            }
            if(direction < 100){
                direction = 100;
            }
        }
        //top and bottom
        if(ballY < 0){
            ballX -= cos(toRadians(direction)) * ballSpeed;
            ballY -= sin(toRadians(direction)) * ballSpeed;
            direction = 360 - direction;
        }
        if(ballY + BALL_RADIUS > SCREEN_HEIGHT){
            ballX -= cos(toRadians(direction)) * ballSpeed;
            ballY -= sin(toRadians(direction)) * ballSpeed;
            direction = 360 - direction;
        }

        //player movement
        leftY += leftMovement * PLAYER_SPEED;
        if(leftY < 0) leftY = 0;
        else if(leftY + BLOCK_HEIGHT > SCREEN_HEIGHT) leftY = SCREEN_HEIGHT - BLOCK_HEIGHT;
        rightY += rightMovement * PLAYER_SPEED;
        if(rightY < 0) rightY = 0;
        else if(rightY + BLOCK_HEIGHT > SCREEN_HEIGHT) rightY = SCREEN_HEIGHT - BLOCK_HEIGHT;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkGameOver();
        }
        repaint();
    }
    private void checkGameOver() {
        if(ballX + BALL_RADIUS < 0 || ballX > SCREEN_WIDTH){
            running = false;
        }
    }
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_O:
                    if(rightMovement == 1){
                        rightMovement = 0;
                    } else {
                        rightMovement = -1;
                    }
                    break;
                case KeyEvent.VK_L:
                    if(rightMovement == -1){
                        rightMovement = 0;
                    } else {
                        rightMovement = 1;
                    }
                    break;
                case KeyEvent.VK_W:
                    if(leftMovement == 1){
                        leftMovement = 0;
                    } else {
                        leftMovement = -1;
                    }
                    break;
                case KeyEvent.VK_S:
                    if(leftMovement == -1){
                        leftMovement = 0;
                    } else {
                        leftMovement = 1;
                    }
                    break;
            }
        }
        public void keyReleased(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_O:
                    rightMovement += 1;
                    break;
                case KeyEvent.VK_L:
                    rightMovement -= 1;
                    break;
                case KeyEvent.VK_W:
                    leftMovement += 1;
                    break;
                case KeyEvent.VK_S:
                    leftMovement -= 1;
                    break;
            }
        }
    }
}
