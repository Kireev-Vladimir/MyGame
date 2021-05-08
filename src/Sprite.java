import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.io.File;

public class Sprite{
    int framesAmount;
    int frame;
    Image image;
    float width;
    float heigth;

    protected Sprite(String path, int amt){
        framesAmount = amt;
        frame = 0;
        try {
            image = new Image(path);
            width = (float)image.getWidth() / framesAmount;
            heigth = image.getHeight();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics graphics, float x, float y){
        image.getSubImage((int)(frame*width), 0, (int)width, (int)heigth).draw(x, y);
        frame++;
        if(frame == framesAmount)
            frame = 0;
    }

    public float getHeigth() {
        return heigth;
    }

    public float getWidth() {
        return width;
    }
}
