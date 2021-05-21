package MapCreator;

import UI.Drawable;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class Asset implements MouseListener, Drawable {
    String imagePath;
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

    public Asset(File poly, File texture){
        try {
            imagePath = texture.getAbsolutePath();
            image = new Image(imagePath);
            width = image.getWidth();
            height = image.getHeight();
        } catch (SlickException e) {
            e.printStackTrace();
        }
        createPolygon(poly);
        dragged = false;
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
            polygon = new Polygon();
            while(scanner.hasNextFloat()) {
                x = scanner.nextFloat();
                y = scanner.nextFloat();
                polygon.addPoint(x, y);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
                    imagePath = png[0].getAbsolutePath();
                    image = new Image(imagePath);
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
        if(isVisible)
            graphics.draw(visiblePolygon);
    }

    private void writePolygon(File polygonFile) throws IOException {
        FileWriter fileWriter = new FileWriter(polygonFile);
        fileWriter.write(posX + " " + posY + "\n");
        float[] point;
        for(int i = 0; i < polygon.getPointCount(); i++){
            point = polygon.getPoint(i);
            fileWriter.write(point[0] + " ");
            fileWriter.write(point[1] + "\n");
        }
        fileWriter.flush();
    }

    public void saveAs(String polygonsPath, String texturesPath, Integer index){
        File poly = new File(polygonsPath + "\\" + index.toString() + ".polygon");
        try {
            Files.copy(Path.of(imagePath), Path.of(texturesPath + "\\" + index.toString() + ".png"));
            writePolygon(poly);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
