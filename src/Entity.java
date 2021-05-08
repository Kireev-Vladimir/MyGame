import org.newdawn.slick.Graphics;

import java.util.Timer;

class Thr extends Thread{
    @Override
    public void run() {
        super.run();
    }
}

public abstract class Entity implements Drawable {
    Timer timer;
    float posX;
    float posY;
    Sprite sprite;
    float visibleX;
    float visibleY;
    boolean isVisible;

    public void camMove(int camX, int camY){
        visibleX = posX - camX;
        visibleY = posY - camY;

        boolean isVisibleX = (visibleX >= 0 && visibleX <= 1600) ||
                (visibleX + sprite.getWidth() >= 0 && visibleX + sprite.getWidth() <= 1600);
        boolean isVisibleY = (visibleY >= 0 && visibleY <= 900) ||
                (visibleY + sprite.getHeigth() >= 0 && visibleY + sprite.getHeigth() <= 900);
        isVisible = isVisibleX && isVisibleY;
    }

    @Override
    public void render(Graphics graphics) {
        sprite.render(graphics, visibleX, visibleY);
    }
}