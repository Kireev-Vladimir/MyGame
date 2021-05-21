package Game;

import UI.Drawable;
import org.newdawn.slick.Graphics;

public abstract class Entity implements Drawable {
    double posX;
    double posY;
    Sprite sprite;
    double visibleX;
    double visibleY;
    boolean isVisible;

    public Entity() {
        this.posX = 100;
        this.posY = 100;
        this.sprite = new Sprite("C:\\Users\\Я\\Desktop\\player.png", 2);
        //camMove(0, 0);
    }

    /*public void camMove(int camX, int camY){
        visibleX = posX - camX;
        visibleY = posY - camY;

        boolean isVisibleX = (visibleX >= 0 && visibleX <= 1600) ||
                (visibleX + sprite.getWidth() >= 0 && visibleX + sprite.getWidth() <= 1600);
        boolean isVisibleY = (visibleY >= 0 && visibleY <= 900) ||
                (visibleY + sprite.getHeigth() >= 0 && visibleY + sprite.getHeigth() <= 900);
        isVisible = isVisibleX && isVisibleY;
    }*/

    @Override
    public void render(Graphics graphics) {
        //camMove(0, 0);
        sprite.render(graphics, 780, 420);
        graphics.drawString(Double.toString(posX), 1500, 20);
        graphics.drawString(Double.toString(posY), 1500, 40);
    }
}