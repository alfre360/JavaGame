package com.zetcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    //Constructor
    public Board() {
        
        initBoard();
    }

    //Inicializa el tablero
    private void initBoard() {

        //Agrega un KeyListener al tablero
        addKeyListener(new TAdapter());
        //Establece el color de fondo
        setBackground(Color.black);
        setFocusable(true);

        //Establece el tamaño del tablero
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        //Carga las imagenes
        loadImages();
        //Inicializa el juego
        initGame();
    }

    //Carga las imagenes
    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();
    }

    private void initGame() {

        //Inicializa la cantidad de puntos
        dots = 3;

        //Inicializa la posicion de los puntos
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        
        //Ubicar la manzana
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        //Si el juego esta en curso
        if (inGame) {

            //Se dibuja la manzana
            g.drawImage(apple, apple_x, apple_y, this);

            //Se dibuja la serpiente
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    //Se dibuja la cabeza
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    //Se dibuja el cuerpo
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            //Se sincroniza el dibujo
            Toolkit.getDefaultToolkit().sync();

        } else {

            //Si el juego no esta en curso, se dibuja el mensaje de Game Over
            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        //Se dibuja el mensaje de Game Over
        g.setColor(Color.white);
        g.setFont(small);
        //Se centra el mensaje
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {

        //Si la cabeza de la serpiente esta en la misma posicion que la manzana
        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            //Se incrementa la cantidad de puntos
            dots++;
            //Se ubica una nueva manzana
            locateApple();
        }
    }

    private void move() {

        //Se mueven los puntos
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        //Se mueve la cabeza
        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            //Si la cabeza de la serpiente choca con su cuerpo
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        //Si la cabeza de la serpiente choca con el borde del tablero

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        //Genera un numero aleatorio entre 0 y 29
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        //Genera un numero aleatorio entre 0 y 29
        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //Si el juego esta en curso
        if (inGame) {

            //Se verifica si la serpiente comio una manzana
            checkApple();
            //Se verifica si la serpiente choco
            checkCollision();
            //Se mueve la serpiente
            move();
        }

        //Se vuelve a dibujar el tablero
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            //Si se presiona la tecla izquierda y la serpiente no se esta moviendo a la derecha
            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            //Si se presiona la tecla derecha y la serpiente no se esta moviendo a la izquierda
            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            //Si se presiona la tecla arriba y la serpiente no se esta moviendo hacia abajo
            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            //Si se presiona la tecla abajo y la serpiente no se esta moviendo hacia arriba
            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
