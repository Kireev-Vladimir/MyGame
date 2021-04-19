import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Objects;
import java.util.Scanner;

public class Asset implements MouseListener, Drawable{
    File file;
    Image image;
    Polygon polygon;
    Rectangle visibleRect;
    Polygon visiblePolygon;
    float visibleX;
    float visibleY;
    float visibleWidth;
    float visibleHeight;
    String path;
    boolean isVisible;
    float posX;
    float posY;
    float zoom;

    int width;
    int height;

    boolean dragged;

    public Asset(float camX, float camY, float zoom){
        openAsset();
        posX = 0;
        posY = 0;
        camMove(camX, camY, zoom);
    }

    @Override
    public void mouseWheelMoved(int i) {

    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if(button == 0) {
            visibleRect = new Rectangle(visibleX, visibleY, visibleWidth, visibleHeight);
            if (visibleRect.contains(x, y))
                dragged = true;
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if(button == 0)
            dragged = false;
    }

    @Override
    public void mouseMoved(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        if(dragged){
            posX += (newx - oldx) / zoom;
            posY += (newy - oldy) / zoom;
            visibleX += (newx - oldx);
            visibleY += (newy - oldy);

            visiblePolygon.setX(visiblePolygon.getX() + (newx - oldx));
            visiblePolygon.setY(visiblePolygon.getY() + (newy - oldy));
        }
    }

    @Override
    public void setInput(Input input) {

    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {

    }

    @Override
    public void inputStarted() {

    }

    public void camMove(float camX, float camY, float zoom){
        this.zoom = zoom;
        float[] dots = polygon.getPoints();
        visiblePolygon = new Polygon();

        visibleX = (posX - camX) * zoom;
        visibleWidth = width * zoom;
        visibleY = (posY - camY) * zoom;
        visibleHeight = height * zoom;

        boolean isVisibleX = (visibleX >= 0 && visibleX <= 1600) ||
                (visibleX + visibleWidth >= 0 && visibleX + visibleWidth <= 1600);
        boolean isVisibleY = (visibleY >= 0 && visibleY <= 900) ||
                (visibleY + visibleHeight >= 0 && visibleY + visibleHeight <= 900);
        isVisible = isVisibleX && isVisibleY;

        if(isVisible)
            for(int i = 0; i < polygon.getPointCount(); i++){
                float[] dot = polygon.getPoint(i);
                dot[0] = ((posX - camX) + dot[0]) * zoom;
                dot[1] = ((posY - camY) + dot[1]) * zoom;
                visiblePolygon.addPoint(dot[0], dot[1]);
            }
    }

    private void readPolygon(File poly) throws FileNotFoundException {
        Scanner scanner = new Scanner(poly);
        polygon = new org.newdawn.slick.geom.Polygon();
        int x, y;
        while(scanner.hasNextInt()) {
            x = scanner.nextInt();
            y = scanner.nextInt();
            polygon.addPoint(x, y);
        }
    }

    private void openAsset(){
        JPanel panel = new JPanel();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open asset");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(true);

        fileChooser.setFileFilter(new FileFilter() {

            private int getPngAmount(File f){
                return Objects.requireNonNull(f.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".png");
                    }
                })).length;
            }

            private int getPolygonAmount(File f){
                return Objects.requireNonNull(f.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".polygon");
                    }
                })).length;
            }

            @Override
            public boolean accept(File f) {
                if(f.isDirectory()){
                    return getPngAmount(f) == 1 && getPolygonAmount(f) == 1;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Assets";
            }
        });
        int canceled = fileChooser.showDialog(panel, "Open Texture");
        if(canceled == 0) {
            path = fileChooser.getSelectedFile().getAbsolutePath();
            File[] png = fileChooser.getSelectedFile().listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".png");
                }
            });
            File[] poly = fileChooser.getSelectedFile().listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".polygon");
                }
            });
            if(png.length == 1 && poly.length == 1) {
                try {
                    image = new Image(png[0].getAbsolutePath());
                } catch (SlickException e) {
                    e.printStackTrace();
                }
                try {
                    readPolygon(poly[0]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        width = image.getWidth();
        height = image.getHeight();
    }

    @Override
    public void render(Graphics graphics) {
        image.draw(visibleX, visibleY, zoom);
    }

    public void renderPolygon(Graphics graphics){
        graphics.draw(visiblePolygon);
    }
}
