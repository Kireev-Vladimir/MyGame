import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AssetCreator implements MouseListener, KeyListener {

    enum Instruments{Drag, AddPoint, RemovePoints, DragPoint}

    float zoom = 1F;
    Input input;
    CascadingMenu file = null;
    CascadingMenu edit = null;
    CascadingMenu view = null;
    CascadingMenu instruments = null;
    BasicButton exit = null;
    File pngFile = null;
    File polygonFile = null;
    List<Integer> polygonDots = new ArrayList();
    Polygon polygon = null;
    Image image;
    String path = null;
    GameContainer gameContainer;
    List<Integer> keysPressed = new ArrayList<>();
    Rectangle workspaceRectangle = new Rectangle(800, 450, 0, 0);
    List<Drawable> toRender = new ArrayList<>();
    Instruments currentInstrument = Instruments.Drag;
    int centerX = 800;
    int centerY = 450;
    boolean pngOpened;
    boolean saved = false;
    boolean pointAdded = false;
    int exitButtonPressed = 0;


    public AssetCreator(){
    }

    public void init(GameContainer gameContainer) {
        this.gameContainer = gameContainer;

        file = new CascadingMenu(0, 0, 80, 29, Color.decode("0X0079db"), Color.decode("0X42aaff"),  Color.decode("0X75c1ff"),
                                Color.decode("0X42aaff"), Color.darkGray, "File");
        file.setInput(input);
        file.setOpenedWidth(230);
        toRender.add(file);
        file.addButton("Open texture (ctrl+n)");
        file.addAction(0, this::openTexture);
        file.addButton("Open asset (ctrl+o)");
        file.addAction(1, this::openAsset);
        file.addButton("Save (ctrl+s)");
        file.addAction(2, this::save);
        file.addButton("Save as");
        file.addAction(3, this::saveAs);

        edit = new CascadingMenu(80, 0, 80, 29, Color.decode("0X0079db"), Color.decode("0X42aaff"),  Color.decode("0X75c1ff"),
                Color.decode("0X42aaff"), Color.darkGray, "Edit");
        edit.setInput(input);
        edit.setOpenedWidth(160);
        toRender.add(edit);
        edit.addButton("Undo (ctrl+z)");
        edit.addAction(0, this::undo);
        edit.addButton("Redo (ctrl+y)");
        edit.addAction(1, this::redo);

        view = new CascadingMenu(160, 0, 80, 29, Color.decode("0X0079db"), Color.decode("0X42aaff"),  Color.decode("0X75c1ff"),
                Color.decode("0X42aaff"), Color.darkGray, "View");
        view.setInput(input);
        view.setOpenedWidth(170);
        toRender.add(view);
        view.addButton("Zoom in (PgUp)");
        view.addAction(0, this::zoomIn);
        view.addButton("Zoom out (PgDn)");
        view.addAction(1, this::zoomOut);
        view.addButton("showPolygons");

        instruments = new CascadingMenu(240, 0, 140, 29, Color.decode("0X0079db"), Color.decode("0X42aaff"),  Color.decode("0X75c1ff"),
                Color.decode("0X42aaff"), Color.darkGray, "Instruments");
        instruments.setInput(input);
        instruments.setOpenedWidth(155);
        toRender.add(instruments);
        instruments.addButton("Drag");
        instruments.addAction(0, ()->currentInstrument = Instruments.Drag);
        instruments.addButton("Add point");
        instruments.addAction(1, ()->currentInstrument = Instruments.AddPoint);
        instruments.addButton("Remove points");
        instruments.addAction(2, ()->currentInstrument = Instruments.RemovePoints);
        instruments.addButton("Drag Point");
        instruments.addAction(3, ()->currentInstrument = Instruments.DragPoint);

        exit = new BasicButton(1571, 0, 30, 29, Color.decode("0Xff496c"), Color.decode("0Xff8099"), Color.decode("0X75c1ff"),
                Color.decode("0X75c1ff"), Color.darkGray, "X", ()->exitButtonPressed = 1);
        exit.setIndent(8);
        exit.setInput(input);
        toRender.add(exit);
    }

    public int update(GameContainer gameContainer, int i) {
        if(exitButtonPressed == 1)
            input.removeAllMouseListeners();
        return exitButtonPressed;
    }

    public void render(GameContainer gameContainer, Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, 1600, 900);
        graphics.setColor(Color.white);
        resetWorkspaceRectangle();
        graphics.fill(workspaceRectangle);

        if(pngOpened)
            renderAsset(graphics);
        if(/*polygon != null && polygon.getPointCount() != 0*/ polygonDots.size() > 0)
            renderPolygon(graphics);

        graphics.setColor(Color.decode("0X75c1ff"));
        graphics.fillRect(0, 0, 1600, 30);
        for (Drawable draw: toRender)
            draw.render(graphics);
        graphics.drawString("Zoom: " + zoom, 1500, 50);
        graphics.drawString("X: " + getMouseX(), 1500, 65);
        graphics.drawString("Y: " + getMouseY(), 1500, 80);
    }

    private void renderAsset(Graphics graphics){
        image.draw(workspaceRectangle.getX(), workspaceRectangle.getY(), zoom);
    }

    private void renderPolygon(Graphics graphics){
        Polygon tmp = new Polygon();
        float[] dot = new float[2];
        for(int i = 0; i < polygonDots.size()/2; i++){
            dot[0] = polygonDots.get(i*2);
            dot[1] = polygonDots.get(i*2+1);
            tmp.addPoint(dot[0] * zoom, dot[1] * zoom);
        }
        tmp.setX(workspaceRectangle.getX());
        tmp.setY(workspaceRectangle.getY());
        graphics.setColor(Color.green);
        graphics.draw(tmp);
    }

    private void resetWorkspaceRectangle(){
        if(image != null) {
            workspaceRectangle.setWidth(image.getHeight() * zoom);
            workspaceRectangle.setHeight(image.getHeight() * zoom);
            workspaceRectangle.setCenterX(centerX);
            workspaceRectangle.setCenterY(centerY);
        }
    }

    private int getMouseX(){
        return (int) ((int) ((Integer.min(Integer.max(input.getMouseX(), (int) ((int) workspaceRectangle.getX()-(4*zoom))), (int) (workspaceRectangle.getX()+workspaceRectangle.getWidth()+(4*zoom))) - workspaceRectangle.getX())) / zoom);
    }

    private int getMouseY(){
        return (int) ((int) ((Integer.min(Integer.max(input.getMouseY(), (int) ((int) workspaceRectangle.getY()-(4*zoom))), (int) (workspaceRectangle.getY()+workspaceRectangle.getHeight()+(4*zoom))) - workspaceRectangle.getY())) / zoom);
    }

    @Override
    public void keyPressed(int i, char c) {
        System.out.println(c);
        if(i == 29) {
            keysPressed.add(29);
            System.out.println(keysPressed.contains(29));
        }
        if(i == 201) // 201 = PgUp
            zoomIn();
        if(i == 209) // 209 = PgDn
            zoomOut();
        if(c == 'n' && keysPressed.contains(29))
            openTexture();
        if(c == 'o' && keysPressed.contains(29)) {
            openAsset();
        }
    }

    @Override
    public void keyReleased(int i, char c) {
        System.out.println(i);
        if(i == 29 && keysPressed.contains(29))
            keysPressed.remove((Integer) 29);
    }

    @Override
    public void setInput(Input input) {
        this.input = input;
        input.addMouseListener(this);
        input.addKeyListener(this);
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

    @Override
    public void mouseWheelMoved(int i) {
        if(i > 0)
            zoomIn();
        if(i < 0)
            zoomOut();
    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {
    }

    private void printPolygon(){
        for(int i = 0; i < polygonDots.size(); i++){
            System.out.print(polygonDots.get(i) + " ");
        }
        System.out.print("\n");
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if(currentInstrument == Instruments.AddPoint && workspaceRectangle.contains(x, y)){
            polygonDots.add(getMouseX());
            polygonDots.add(getMouseY());
            pointAdded = true;
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if(currentInstrument == Instruments.AddPoint) {
            pointAdded = false;
            printPolygon();
        }
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {

    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        if(currentInstrument == Instruments.Drag){
            int xchange = newx - oldx;
            int ychange = newy - oldy;
            if(centerX + xchange < 200)
                centerX = 200;
            else if(centerX + xchange > 1400)
                centerX = 1400;
            else
                centerX = centerX + xchange;
            if(centerY + ychange < 200)
                centerY = 200;
            else if(centerY + ychange > 700)
                centerY = 700;
            else
                centerY = centerY + ychange;
            System.out.println("x: " + (centerX + xchange));
            System.out.println("y: " + (centerY + ychange));
        }
        if(currentInstrument == Instruments.AddPoint && pointAdded){
            polygonDots.set(polygonDots.size()-2, getMouseX());
            polygonDots.set(polygonDots.size()-1, getMouseY());
        }
    }

    private void readPolygon(File poly) throws FileNotFoundException {
        Scanner scanner = new Scanner(poly);
        polygon = new Polygon();
        int x, y;
        while(scanner.hasNextInt()) {
            x = scanner.nextInt();
            y = scanner.nextInt();
            polygon.addPoint(x, y);
        }
    }

    private void zoomIn(){
        if(zoom <= 4.75)
            zoom += 0.25;
    }

    private void zoomOut(){
        if(zoom >= 0.5)
            zoom -= 0.25;
    }

    private void openTexture(){
        JPanel panel = new JPanel();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open texture");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".png") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Images(.png)";
            }
        });
        int canceled = fileChooser.showDialog(panel, "Open Texture");
        if(canceled == 0) {
            pngFile = fileChooser.getSelectedFile();
            pngOpened = true;
            try {
                image = new Image(pngFile.getAbsolutePath());
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
        polygon = new Polygon();
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
                pngOpened = true;
                polygonFile = poly[0];
                try {
                    readPolygon(polygonFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writePolygon(File polygonFile) throws IOException {
        FileWriter fileWriter = new FileWriter(polygonFile);
        float[] point;
        for(int i = 0; i < polygonDots.size(); i++){
            fileWriter.write(polygonDots.get(i) + " ");
            fileWriter.write(polygonDots.get(++i) + "\n");
        }
        fileWriter.flush();
    }

    private void save(){
        if(pngFile == null){
            System.out.println("No png");
        }
        else if(path == null)
            saveAs();
        else{
            polygonFile.delete();
            try {
                polygonFile.createNewFile();
                writePolygon(polygonFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAs(){
        if(pngFile == null){
            System.out.println("No png");
        }
        else {
            JPanel panel = new JPanel();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save as");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int canceled = fileChooser.showDialog(panel, "Save as");
            if (canceled == 0) {
                try {
                    File file = fileChooser.getSelectedFile();
                    String pathToSave = file.getAbsolutePath();
                    if (file.isDirectory() && Objects.requireNonNull(file.listFiles()).length == 0) {
                        Files.copy(Path.of(pngFile.getAbsolutePath()), Path.of(pathToSave + "\\texture.png"));
                    }
                    File poly = new File(pathToSave + "\\polygon.txt");
                    writePolygon(poly);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void undo(){

    }

    private void redo(){

    }
}