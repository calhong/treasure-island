package com.adinkwok.TreasureIsland;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;

class MainMenu extends JPanel implements ActionListener, KeyListener {
    private int mMenuSelection, mMenuItemX;
    private int[] mMenuItemY = {400, 500, 600};
    private int[] mImageDimen = new int[2];
    private JFrame mJFrame;
    private Game mGame;

    private int x, y;

    private BufferedImage background, ktoGr2, gr3to4, gr5plus, select;

    MainMenu(JFrame jFrame) {
        mJFrame = jFrame;
        mGame = null;
        this.setDoubleBuffered(true);
        Timer mTimer = new Timer(5, this);
        mTimer.start();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        try {
            background = ImageIO.read(getClass().getResource("background.png"));
            ktoGr2 = ImageIO.read(getClass().getResource("ktogr2.png"));
            gr3to4 = ImageIO.read(getClass().getResource("gr3to4.png"));
            gr5plus = ImageIO.read(getClass().getResource("gr5plus.png"));
            select = ImageIO.read(getClass().getResource("select.png"));
            mImageDimen[0] = getWidth();
            mImageDimen[1] = getHeight();
            InputStream input = getClass().getResourceAsStream("music.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(input);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void resizeImages(int x, int y) {
        this.x = x;
        this.y = y;
        mImageDimen[0] = x * 350 / 1920;
        mImageDimen[1] = y * 100 / 1080;
        mMenuItemX = (x / 2) - (mImageDimen[0] / 2);
        mMenuItemY[0] = (int) (y / 2.0655);
        mMenuItemY[1] = (int) (y / 1.6524);
        mMenuItemY[2] = (int) (y / 1.377);
        if (mGame != null) mGame.resizeImages(x, y);
    }

    void endGame() {
        mGame = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        g2d.drawImage(ktoGr2, mMenuItemX, mMenuItemY[0], mImageDimen[0], mImageDimen[1], this);
        g2d.drawImage(gr3to4, mMenuItemX, mMenuItemY[1], mImageDimen[0], mImageDimen[1], this);
        g2d.drawImage(gr5plus, mMenuItemX, mMenuItemY[2], mImageDimen[0], mImageDimen[1], this);
        g2d.drawImage(select, mMenuItemX, mMenuItemY[mMenuSelection], mImageDimen[0], mImageDimen[1], this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        if (mGame != null) {
            mGame.repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (mMenuSelection <= 0) {
                    mMenuSelection = mMenuItemY.length - 1;
                } else {
                    mMenuSelection--;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (mMenuSelection >= mMenuItemY.length - 1) {
                    mMenuSelection = 0;
                } else {
                    mMenuSelection++;
                }
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
                mGame = null;
                mGame = new Game(mMenuSelection, mJFrame, this);
                mJFrame.setContentPane(mGame);
                mJFrame.validate();
                resizeImages(x, y);
                mGame.requestFocus();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
