package Game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.io.File;

public class Sprite{
    int framesAmount;
    int frame;
    double frametime;
    double lastframe;
    Image image;
    float width;
    float heigth;

    protected Sprite(String path, int amt){
        lastframe = System.currentTimeMillis();
        framesAmount = amt;
        frame = 0;
        frametime = 1;
        try {
            image = new Image(path);
            width = (float)image.getWidth() / framesAmount;
            heigth = image.getHeight();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics graphics, double x, double y){
        image.getSubImage((int)(frame*width), 0, (int)width, (int)heigth).draw((float)x, (float)y);
        if((double)(System.currentTimeMillis() - lastframe) / 1000 > frametime) {
            frame++;
            if (frame == framesAmount)
                frame = 0;
            lastframe = System.currentTimeMillis();
        }
    }

    public float getHeigth() {
        return heigth;
    }

    public float getWidth() {
        return width;
    }
}
