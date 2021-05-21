package Game;

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
        //inputStarted();
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
            if(guns[c - '1'] != null){
                guns[gunNum].swap();
                gunNum = c - '1';
                guns[gunNum].swap();
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
        return true;
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
        //guns[gunNum].start_shooting();
    }

    @Override
    public void mouseReleased(int i, int i1, int i2) {
        //guns[gunNum].stop_shooting();
    }

    @Override
    public void mouseMoved(int i, int i1, int x, int y) {
        mousePos.x = x;
        mousePos.y = y;
        //crosshair.x = (int) (x + posX - 780);
        //crosshair.y = (int) (y + posY - 420);
    }

    @Override
    public void mouseDragged(int i, int i1, int x, int y) {
        mousePos.x = x;
        mousePos.y = y;
    }
}
