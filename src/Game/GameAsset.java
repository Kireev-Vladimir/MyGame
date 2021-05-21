package Game;

import UI.Drawable;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import org.newdawn.slick.geom.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class GameAsset implements Drawable {
    Image image;
    Polygon polygon;
    Polygon visiblePolygon;
    boolean isVisible;
    float posX;
    float posY;
    float visibleX;
    float visibleY;

    int width;
    int height;

    public GameAsset(File poly, File texture){
        try {
            String imagePath = texture.getAbsolutePath();
            image = new Image(imagePath);
            width = image.getWidth();
            height = image.getHeight();
        } catch (SlickException e) {
            e.printStackTrace();
        }
        createPolygon(poly);
    }

    private void createPolygon(File poly){
        try {
            Scanner scanner = new Scanner(poly);
            scanner.useLocale(Locale.US);
            if(scanner.hasNextFloat()) {
                posX = scanner.nextFloat();
                posY = scanner.nextFloat();
            }
            float x, y;
            polygon = new org.newdawn.slick.geom.Polygon();
            while(scanner.hasNextFloat()) {
                x = scanner.nextFloat();
                y = scanner.nextFloat();
                polygon.addPoint(x, y);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void camMove(float camX, float camY){
        float[] dots = polygon.getPoints();
        visiblePolygon = new Polygon();

        visibleX = (posX - camX);
        visibleY = (posY - camY);

        boolean isVisibleX = (visibleX >= 0 && visibleX <= 1600) ||
                (visibleX + width >= 0 && visibleX + width <= 1600);
        boolean isVisibleY = (visibleY >= 0 && visibleY <= 900) ||
                (visibleY + height >= 0 && visibleY + height <= 900);
        isVisible = isVisibleX && isVisibleY;

        if(isVisible)
            for(int i = 0; i < polygon.getPointCount(); i++){
                float[] dot = polygon.getPoint(i);
                dot[0] = ((posX - camX) + dot[0]);
                dot[1] = ((posY - camY) + dot[1]);
                visiblePolygon.addPoint(dot[0], dot[1]);
            }
    }

    @Override
    public void render(Graphics graphics) {
        image.draw(visibleX, visibleY);
    }

    public void renderPolygon(Graphics graphics){
        if(isVisible)
            graphics.draw(visiblePolygon);
    }
}
