package Game;

import Game.Guns.Ak47;
import Game.Guns.Usp;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

public class Player extends BasicPlayer implements KeyListener, MouseListener {

    Input input;
    boolean acceptingInput;

    public Player(Input input){
        this.input = input;
        input.addMouseListener(this);
        input.addKeyListener(this);
        inputStarted();
        guns[0] = new Ak47();
        gunNum = 0;
        gunThreads[0] = new Thread(guns[0]);
        gunThreads[0].setDaemon(true);
        gunThreads[0].start();
        guns[0].swap();
        guns[1] = new Usp();
        gunNum = 1;
        gunThreads[1] = new Thread(guns[1]);
        gunThreads[1].setDaemon(true);
        gunThreads[1].start();
    }

    @Override
    public void keyPressed(int i, char c) {
        if(c == 'd') {
            state = PlayerState.runRight;
        }
        else if(c == 'a')
            state = PlayerState.runLeft;
        else if(c == 'w' || c == ' ')
            jump();
        if(c >= '1' && c <= '4'){
            if(guns[c - '1'] != null && c - '1' != gunNum){
                synchronized (this){
                    guns[gunNum].swap();
                    gunNum = c - '1';
                    guns[gunNum].swap();
                }
            }
        }
        else if(c == 'r'){
            guns[gunNum].reload();
        }
        else if(c == 'e') {
            standing = !standing;
        }
    }

    @Override
    public void keyReleased(int i, char c) {
            if(c == 'd' && state == PlayerState.runRight)
            state = PlayerState.stop;
        else if(c == 'a' && state == PlayerState.runLeft)
            state = PlayerState.stop;
    }

    @Override
    public void setInput(Input input) {
        this.input = input;
        acceptingInput = true;
    }

    @Override
    public boolean isAcceptingInput() {
        return acceptingInput;
    }

    @Override
    public void inputEnded() {
        acceptingInput = false;
    }

    @Override
    public void inputStarted() {
        acceptingInput = true;
    }

    @Override
    public void mouseWheelMoved(int i) {

    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mousePressed(int i, int i1, int i2) {
        if(i == 0)
            guns[gunNum].start_shooting();
    }

    @Override
    public void mouseReleased(int i, int i1, int i2) {
        if(i == 0)
            guns[gunNum].stop_shooting();
    }

    @Override
    public void mouseMoved(int i, int i1, int x, int y) {
        mousePos.x = x;
        mousePos.y = y;
    }

    @Override
    public void mouseDragged(int i, int i1, int x, int y) {
        mousePos.x = x;
        mousePos.y = y;
    }

    public void shoot(float dmg){
        System.out.println("Boom!");
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        if(guns[gunNum] != null)
            guns[gunNum].drawInfo(graphics);
    }
}
