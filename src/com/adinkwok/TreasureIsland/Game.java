package com.adinkwok.TreasureIsland;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Game extends JPanel implements KeyListener {
    private int mGameMode;
    private JFrame mJFrame;
    private MainMenu mMainMenu;

    private int y;

    private boolean mDragon0, mDragon1, mDragon2, mDragon3, mDragon4;
    private int mDragon0Y, mDragon1Y, mDragon2Y, mDragon3Y, mDragon4Y;

    private int mBoatStart, mBoatX, mBoatY, mDragonStart;
    private int[] mBoatDimens = new int[2];

    private int[] mDragonDimens = new int[2];

    private int[] mLaneX = new int[5];

    private int mRepeatsRemaining;

    private LinkedList<Integer> launchableDragons = new LinkedList<>();

    private BufferedImage background, boat, dragon;

    private ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();

    private static final int SPEED = 5;

    private Runnable launchDragon = new Runnable() {
        @Override
        public void run() {
            int randomNum = launchableDragons.getFirst();
            switch (randomNum) {
                case 0:
                    mDragon0 = true;
                    break;
                case 1:
                    mDragon1 = true;
                    break;
                case 2:
                    mDragon2 = true;
                    break;
                case 3:
                    mDragon3 = true;
                    break;
                case 4:
                    mDragon4 = true;
                    break;
            }
            launchableDragons.removeFirst();
            if (launchableDragons.isEmpty()) {
                --mRepeatsRemaining;
                if (mRepeatsRemaining <= 0) {
                    endGame();
                    return;
                } else {
                    for (int i = 0; i < mLaneX.length; i++) {
                        launchableDragons.add(i);
                    }
                    Collections.shuffle(launchableDragons);
                }
            }
            mExecutor.schedule(launchDragon, 1500 / (mGameMode + 1), TimeUnit.MILLISECONDS);
        }
    };


    Game(int gameMode, JFrame jFrame, MainMenu mainMenu) {
        mGameMode = gameMode;
        mJFrame = jFrame;
        mMainMenu = mainMenu;
        mBoatX = 2;
        this.setDoubleBuffered(true);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        try {
            background = ImageIO.read(getClass().getResource("water.png"));
            boat = ImageIO.read(getClass().getResource("boat.png"));
            dragon = ImageIO.read(getClass().getResource("dragon.png"));
            mBoatDimens[0] = boat.getWidth();
            mBoatDimens[1] = boat.getHeight();
            mDragonDimens[0] = dragon.getWidth();
            mDragonDimens[1] = dragon.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mLaneX.length; i++) {
            launchableDragons.add(i);
        }
        Collections.shuffle(launchableDragons);

        mRepeatsRemaining = (mGameMode + 1) * 5;

        launchDragon.run();
    }

    void resizeImages(int x, int y) {
        this.y = y;
        mBoatDimens[0] = x * 100 / 1920;
        mBoatDimens[1] = y * 250 / 1080;
        mDragonDimens[0] = x * 400 / 1920;
        mDragonDimens[1] = y * 216 / 1080;
        mBoatStart = mBoatDimens[0] / 2;
        mDragonStart = mDragonDimens[0] / 2;
        mBoatY = (int) (y / 1.35);
        final int laneSeparation = x / 6;
        for (int i = 0; i < mLaneX.length; i++) {
            mLaneX[i] = (i + 1) * laneSeparation;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        g2d.drawImage(boat, mLaneX[mBoatX] - mBoatStart, mBoatY,
                mBoatDimens[0], mBoatDimens[1], this);
        lowerDragon();
        if (mDragon0) {
            g2d.drawImage(dragon, mLaneX[0] - mDragonStart, mDragon0Y - mDragonDimens[1],
                    mDragonDimens[0], mDragonDimens[1], this);
        }
        if (mDragon1) {
            g2d.drawImage(dragon, mLaneX[1] - mDragonStart, mDragon1Y - mDragonDimens[1],
                    mDragonDimens[0], mDragonDimens[1], this);
        }
        if (mDragon2) {
            g2d.drawImage(dragon, mLaneX[2] - mDragonStart, mDragon2Y - mDragonDimens[1],
                    mDragonDimens[0], mDragonDimens[1], this);
        }
        if (mDragon3) {
            g2d.drawImage(dragon, mLaneX[3] - mDragonStart, mDragon3Y - mDragonDimens[1],
                    mDragonDimens[0], mDragonDimens[1], this);
        }
        if (mDragon4) {
            g2d.drawImage(dragon, mLaneX[4] - mDragonStart, mDragon4Y - mDragonDimens[1],
                    mDragonDimens[0], mDragonDimens[1], this);
        }
    }

    private void lowerDragon() {
        if (mDragon0) {
            mDragon0Y += (mGameMode + SPEED);
            if (mBoatX == 0 && mDragon0Y >= mBoatY)
                endGame();
            else if (mDragon0Y >= this.y) {
                mDragon0Y = 0;
                mDragon0 = false;
            }
        }
        if (mDragon1) {
            mDragon1Y += (mGameMode + SPEED);
            if (mBoatX == 1 && mDragon1Y >= mBoatY)
                endGame();
            else if (mDragon1Y >= this.y) {
                mDragon1Y = 0;
                mDragon1 = false;
            }
        }
        if (mDragon2) {
            mDragon2Y += (mGameMode + SPEED);
            if (mBoatX == 2 && mDragon2Y >= mBoatY)
                endGame();
            else if (mDragon2Y >= this.y) {
                mDragon2Y = 0;
                mDragon2 = false;
            }
        }
        if (mDragon3) {
            mDragon3Y += (mGameMode + SPEED);
            if (mBoatX == 3 && mDragon3Y >= mBoatY)
                endGame();
            else if (mDragon3Y >= this.y) {
                mDragon3Y = 0;
                mDragon3 = false;
            }
        }
        if (mDragon4) {
            mDragon4Y += (mGameMode + SPEED);
            if (mBoatX == 4 && mDragon4Y >= mBoatY)
                endGame();
            else if (mDragon4Y >= this.y) {
                mDragon4Y = 0;
                mDragon4 = false;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (mBoatX > 0) {
                    --mBoatX;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (mBoatX < mLaneX.length - 1) {
                    ++mBoatX;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                endGame();
                break;
        }
    }

    private void endGame() {
        mExecutor.shutdown();
        mJFrame.setContentPane(mMainMenu);
        mJFrame.validate();
        mMainMenu.requestFocus();
        mMainMenu.endGame();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
