import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font.*;


public class Button{
    enum State{Untouched, Touched, Pressed}

    private int x;
    private int y;
    private int CenterX;
    private int CenterY;
    private int width;
    private int height;
    private Color colorPressed;
    private Color colorTouched;
    private Color colorUntouched;
    private Color colorOutline;
    private Color colorText;
    protected String text;
    protected Rectangle rectangle;
    protected State state;
    protected boolean pressed;
    Runnable act;



    public Button(int x, int y, int width, int height, Color colorPressed) {
        this.x = x;
        this.y = y;
        this.CenterX = x + width / 2;
        this.CenterY = y + height / 2;
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
    }

    public Button(int x, int y, int width, int height, Color colorPressed, Color colorTouched, Color colorUntouched, Color colorOutline) {
        this.x = x;
        this.y = y;
        this.CenterX = x + width / 2;
        this.CenterY = y + height / 2;
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
    }

    public Button(int x, int y, int width, int height, Color colorPressed, Color colorTouched,
                  Color colorUntouched, Color colorOutline, Color colorText, String text) {
        this.x = x;
        this.y = y;
        this.CenterX = x + width / 2;
        this.CenterY = y + height / 2;
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
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColorPressed() {
        return colorPressed;
    }

    public Color getColorTouched() {
        return colorTouched;
    }

    public Color getColorUntouched() {
        return colorUntouched;
    }

    public Color getColorOutline() {
        return colorOutline;
    }

    public Color getColorText() {
        return colorText;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCenterX(int centerX) {
        CenterX = centerX;
    }

    public void setCenterY(int centerY) {
        CenterY = centerY;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setColorPressed(Color colorPressed) {
        this.colorPressed = colorPressed;
    }

    public void setColorTouched(Color colorTouched) {
        this.colorTouched = colorTouched;
    }

    public void setColorUntouched(Color colorUntouched) {
        this.colorUntouched = colorUntouched;
    }

    public void setColorOutline(Color colorOutline) {
        this.colorOutline = colorOutline;
    }

    public boolean isPressed() {
        if(pressed) {
            pressed = false;
            return true;
        }
        else
            return false;
    }

    public void render(Graphics graphics){
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
        graphics.drawString(text, this.x+20, this.CenterY-10);
    }

    public void mouseMoved(int x, int y){
        if(rectangle.contains(x, y))
            state = State.Touched;
        else
            state = State.Untouched;
    }

    /*public void mouseClicked(int x, int y){
        if(rectangle.contains(x, y)) {
            state = State.Pressed;
            onPressAction();
        }
        else
            state = State.Untouched;
    }*/

    public void mousePressed(int x, int y){
        if(rectangle.contains(x, y))
            state = State.Pressed;
        else
            state = State.Untouched;
    }

    public void mouseReleased(int x, int y){
        if(rectangle.contains(x, y) && state == State.Pressed) {
            state = State.Touched;
            pressed = true;
        }
        else
            state = State.Untouched;
    }

}
