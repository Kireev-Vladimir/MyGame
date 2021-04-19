import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

public class CascadingMenu extends BasicButton{
    private ArrayList<BasicButton> buttons = new ArrayList<>();
    private int size;
    private boolean opened;
    private Rectangle openedRectangle = new Rectangle(0, 0, 0 , 0);
    private int openedWidth;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addButton(String str){
        size++;
        buttons.add(new BasicButton(super.getX(), (int) (super.getY()+super.getHeight()+openedRectangle.getHeight()), openedWidth, super.getHeight(), super.getColorPressed(),
                    super.getColorTouched(), super.getColorUntouched(), super.getColorOutline(), super.getColorText(), str));
        openedRectangle.setHeight(openedRectangle.getHeight()+super.getHeight());
        buttons.get(buttons.size()-1).setInput(input);
        buttons.get(buttons.size()-1).inputEndedMy();
        //buttons.get(buttons.size()-1).inputStarted();
    }

    public void addAction(int index, Runnable act){
        buttons.get(index).act = act;
    }

    /*public void stopInput(){
        input.removeMouseListener(this);
        for (BasicButton button: buttons)
            button.inputEnded();
    }*/

    public int isButtonPressed(){
        for(int i = 0; i < buttons.size(); i++)
            if(buttons.get(i).isPressed()) {
                opened = false;
                return i;
            }
        return -1;
    }

    public CascadingMenu(int x, int y, int width, int height, Color colorPressed) {
        super(x, y, width, height);
        openedWidth = width;
        openedRectangle.setX(x);
        openedRectangle.setY(y+height);
        openedRectangle.setWidth(openedWidth);
        openedRectangle.setHeight(0);
        opened = false;
    }

    public CascadingMenu(int x, int y, int width, int height, Color colorPressed, Color colorTouched, Color colorUntouched, Color colorOutline) {
        super(x, y, width, height, colorPressed, colorTouched, colorUntouched, colorOutline);
        openedWidth = width;
        openedRectangle.setX(x);
        openedRectangle.setY(y+height);
        openedRectangle.setWidth(openedWidth);
        openedRectangle.setHeight(0);
        opened = false;
    }

    public CascadingMenu(int x, int y, int width, int height, Color colorPressed, Color colorTouched, Color colorUntouched, Color colorOutline, Color colorText, String text) {
        super(x, y, width, height, colorPressed, colorTouched, colorUntouched, colorOutline, colorText, text);
        openedWidth = width;
        openedRectangle.setX(x);
        openedRectangle.setY(y+height);
        openedRectangle.setWidth(openedWidth);
        openedRectangle.setHeight(0);
        opened = false;
    }

    public void setOpenedWidth(int openedWidth) {
        this.openedWidth = openedWidth;
        openedRectangle.setWidth(openedWidth);
    }

    public boolean isOpened(){
        return opened;
    }


    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if(rectangle.contains(newx, newy)) {
            if (state == State.Untouched)
                state = State.Touched;
        }
        else if(state != State.Pressed)
            state = State.Untouched;
    }

    @Override
    public void mousePressed(int mouseButton, int x, int y) {
        if(rectangle.contains(x, y))
            state = State.Pressed;
        else if(opened && !openedRectangle.contains(x, y)) {
            state = State.Untouched;
            opened = false;
            for(BasicButton button: buttons)
                button.inputEndedMy();
        }
    }

    @Override
    public void mouseReleased(int mouseButton, int x, int y) {
        if(state == State.Pressed){
            if(opened){
                if(rectangle.contains(x, y) || openedRectangle.contains(x, y)){
                    state = State.Touched;
                    opened = false;
                    for(BasicButton button: buttons) {
                        button.mouseReleased(mouseButton, x, y);
                        button.inputEndedMy();
                    }
                }
            }
            else if(rectangle.contains(x, y)){
                opened = true;
                for (BasicButton button: buttons)
                    button.inputStartedMy();
            }
            else {
                opened = false;
                for (BasicButton button: buttons)
                    button.inputEndedMy();
                state = State.Untouched;
            }
        }
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        if(opened)
            for(BasicButton button: buttons)
                button.render(graphics);
    }

    @Override
    public void inputStartedMy() {
        isAcceptingInput = true;
    }

    @Override
    public void inputEndedMy() {
        isAcceptingInput = false;
        for (BasicButton button: buttons)
            button.inputEnded();
    }
}
