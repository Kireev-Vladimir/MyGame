package UI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Rectangle;

public class BasicButton implements MouseListener, Drawable {

    enum State{Untouched, Touched, Pressed}

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int indent = 20;
    protected Color colorUntouched;
    protected Color colorTouched;
    protected Color colorPressed;
    protected Color colorOutline;
    protected Color colorText;
    protected String text;
    protected Rectangle rectangle;
    protected State state;
    protected Input input;
    protected boolean pressed;
    protected boolean isAcceptingInput = false;
    Runnable act;

    public BasicButton(int x, int y, int width, int height, Runnable act) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorUntouched = Color.white;
        this.colorTouched = Color.lightGray;
        this.colorPressed = Color.darkGray;
        this.colorOutline = Color.black;
        this.colorText = Color.black;
        this.text = "";
        this.rectangle = new Rectangle(x, y, width, height);
        this.state = State.Untouched;
        this.pressed = false;
        this.act = act;
    }

    public BasicButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorUntouched = Color.white;
        this.colorTouched = Color.lightGray;
        this.colorPressed = Color.darkGray;
        this.colorOutline = Color.black;
        this.colorText = Color.black;
        this.text = "";
        this.rectangle = new Rectangle(x, y, width, height);
        this.state = State.Untouched;
        this.pressed = false;
    }

    public BasicButton(int x, int y, int width, int height, Color colorPressed, Runnable act) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorPressed = colorPressed;
        this.colorTouched = colorPressed;
        this.colorTouched.a = colorPressed.a * 0.75F;
        this.colorUntouched = colorPressed;
        this.colorUntouched.a = colorPressed.a * 0.5F;
        this.colorOutline = colorPressed;
        this.colorOutline.a = 1.0F;
        this.colorText = Color.black;
        this.text="";
        this.rectangle = new Rectangle(x, y, width, height);
        this.state = State.Untouched;
        this.pressed = false;
        this.act = act;
    }

    public BasicButton(int x, int y, int width, int height, Color colorPressed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorPressed = colorPressed;
        this.colorTouched = colorPressed;
        this.colorTouched.a = colorPressed.a * 0.75F;
        this.colorUntouched = colorPressed;
        this.colorUntouched.a = colorPressed.a * 0.5F;
        this.colorOutline = colorPressed;
        this.colorOutline.a = 1.0F;
        this.colorText = Color.black;
        this.text="";
        this.rectangle = new Rectangle(x, y, width, height);
        this.state = State.Untouched;
        this.pressed = false;
    }

    public BasicButton(int x, int y, int width, int height, Color colorPressed, Color colorTouched, Color colorUntouched, Color colorOutline, Runnable act) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorPressed = colorPressed;
        this.colorTouched = colorTouched;
        this.colorUntouched = colorUntouched;
        this.colorOutline = colorOutline;
        this.colorText = Color.black;
        this.text="";
        this.rectangle = new Rectangle(x, y, width, height);
        this.state = State.Untouched;
        this.pressed = false;
        this.act = act;
    }

    public BasicButton(int x, int y, int width, int height, Color colorPressed, Color colorTouched, Color colorUntouched, Color colorOutline) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorPressed = colorPressed;
        this.colorTouched = colorTouched;
        this.colorUntouched = colorUntouched;
        this.colorOutline = colorOutline;
        this.colorText = Color.black;
        this.text="";
        this.rectangle = new Rectangle(x, y, width, height);
        this.state = State.Untouched;
        this.pressed = false;
    }

    public BasicButton(int x, int y, int width, int height, Color colorPressed, Color colorTouched,
                  Color colorUntouched, Color colorOutline, Color colorText, String text, Runnable act) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorPressed = colorPressed;
        this.colorTouched = colorTouched;
        this.colorUntouched = colorUntouched;
        this.colorOutline = colorOutline;
        this.colorText = colorText;
        this.text = text;
        this.rectangle = new Rectangle(x, y, width, height);
        this.state = State.Untouched;
        this.pressed = false;
        this.act = act;
    }

    public BasicButton(int x, int y, int width, int height, Color colorPressed, Color colorTouched,
                       Color colorUntouched, Color colorOutline, Color colorText, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorPressed = colorPressed;
        this.colorTouched = colorTouched;
        this.colorUntouched = colorUntouched;
        this.colorOutline = colorOutline;
        this.colorText = colorText;
        this.text = text;
        this.rectangle = new Rectangle(x, y, width, height);
        this.state = State.Untouched;
        this.pressed = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setColorUntouched(Color colorUntouched) {
        this.colorUntouched = colorUntouched;
    }

    public void setColorTouched(Color colorTouched) {
        this.colorTouched = colorTouched;
    }

    public void setColorPressed(Color colorPressed) {
        this.colorPressed = colorPressed;
    }

    public void setColorOutline(Color colorOutline) {
        this.colorOutline = colorOutline;
    }

    public void setColorText(Color colorText) {
        this.colorText = colorText;
    }

    public Color getColorUntouched() {
        return colorUntouched;
    }

    public Color getColorTouched() {
        return colorTouched;
    }

    public Color getColorPressed() {
        return colorPressed;
    }

    public Color getColorOutline() {
        return colorOutline;
    }

    public Color getColorText() {
        return colorText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    @Override
    public void mouseWheelMoved(int i) {

    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if(rectangle.contains(x, y))
            state = State.Pressed;
        else
            state = State.Untouched;
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if(rectangle.contains(x, y) && state == State.Pressed) {
            state = State.Touched;
            act.run();
        }
        else
            state = State.Untouched;
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if(rectangle.contains(newx, newy))
            state = State.Touched;
        else
            state = State.Untouched;
    }

    public boolean isPressed(){
        if(pressed){
            pressed = false;
            return true;
        }
        else return false;
    }

    @Override
    public void mouseDragged(int i, int i1, int i2, int i3) {

    }

    @Override
    public void setInput(Input input) {
        this.input = input;
        input.addMouseListener(this);
        isAcceptingInput = true;
    }



    @Override
    public boolean isAcceptingInput() {
        return isAcceptingInput;
    }

    @Override
    public void inputEnded() {

    }

    @Override
    public void inputStarted() {

    }

    public void inputEndedMy() {
        isAcceptingInput = false;
    }

    public void inputStartedMy() {
        isAcceptingInput = true;
    }

    @Override
    public void render(Graphics graphics) {
        if(this.state == State.Pressed){
            graphics.setColor(colorPressed);
            graphics.fill(rectangle);
        }
        else if(this.state == State.Touched){
            graphics.setColor(colorTouched);
            graphics.fill(rectangle);
        }
        else if(this.state == State.Untouched){
            graphics.setColor(colorUntouched);
            graphics.fill(rectangle);
        }
        graphics.setColor(colorOutline);
        graphics.draw(rectangle);
        graphics.setColor(colorText);
        //graphics.drawString("123", 600, 250);
        graphics.drawString(text, this.x+indent, (this.y + this.height/2)-10);
    }

}
