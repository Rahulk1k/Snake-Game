import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    int B_Height = 400;
    int B_width = 400;

    int MAX_DOTS = 1600;
    int Dot_size = 10;
    int DOTS;
    int x[] = new int[MAX_DOTS];
    int y[] = new int[MAX_DOTS];

    int apple_x;
    int apple_y;


    //Images
    Image body, head, apple;

    Timer timer;
    int DELAY = 150;

    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    boolean ingame = true;



    Board() {
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(B_width, B_Height));
        setBackground(Color.black);
        intGame();
        loadImages();

    }

    //Initialize the snake
    public void intGame() {
        DOTS = 3;

        x[0] = 250;
        y[0] = 250;
        for (int i = 0; i < DOTS; i++) {
            x[i] = x[0] + Dot_size * i;
            y[i] = y[0];
        }

        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    //Load Images from recources
    public void loadImages() {
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }

    // draw images at snakes and apples positon
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    //draw image
    public void doDrawing(Graphics g) {
        if(ingame){
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < DOTS; i++) {
                if (i == 0) {
                    g.drawImage(head, x[0], y[0], this);
                } else
                    g.drawImage(body, x[i], y[i], this);
            }
        }
        else{
            gameOver(g);
            timer.stop();
        }

    }

    //Randomize apple's position
    public void locateApple() {
        apple_x = ((int) (Math.random() * 39)) * Dot_size;
        apple_y = ((int) (Math.random() * 39)) * Dot_size;
    }

    //collision with border and body
    public void checkCollision(){
        //Collision with body
        for(int i=1;i<DOTS;i++){
            if(i>4&&x[0]==x[i]&&y[0]==y[1]){
                ingame = false;
            }
        }
        //collision with Border
        if(x[0]<0){
            ingame = false;
        }
        if(x[0]>B_Height){
            ingame = false;
        }
        if(y[0]<0){
            ingame = false;
        }
        if(y[0]>=B_Height){
            ingame = false;
        }
    }
    public void gameOver(Graphics g){
        String msg = "Game Over";
        int score = (DOTS-3)*100;
        String scoremsg = "Score:"+Integer.toString(score);
        Font Small = new Font("Helvetica",Font.BOLD,14);
        FontMetrics fontMetrics = getFontMetrics(Small);

        g.setColor(Color.WHITE);
        g.setFont(Small);
        g.drawString(msg , (B_width-fontMetrics.stringWidth(msg))/2 , B_Height/4);
        g.drawString(scoremsg , (B_width-fontMetrics.stringWidth(scoremsg))/2 , 3*B_Height/4);

    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(ingame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    //Make Snake Move
    public void move() {
        for (int i = DOTS - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (leftDirection) {
            x[0] -= Dot_size;
        }
        if (rightDirection) {
            x[0] += Dot_size;
        }
        if (upDirection) {
            y[0] -= Dot_size;
        }
        if (downDirection) {
            y[0] += Dot_size;
        }
    }

    //Make snake Eat food
    public void checkApple(){
        if(apple_x==x[0]&&apple_y==y[0]){
            DOTS++;
            locateApple();
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int key = keyEvent.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            } else if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            } else if (key == KeyEvent.VK_UP && !downDirection) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            } else if (key == KeyEvent.VK_DOWN && !upDirection) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}