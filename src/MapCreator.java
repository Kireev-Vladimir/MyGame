import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.gui.AbstractComponent;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapCreator implements MouseListener{

    Input input;
    CascadingMenu file;
    CascadingMenu edit;
    CascadingMenu view;
    CascadingMenu instruments;
    BasicButton exit;
    File backgroundFile;
    Image backgroundImage;
    boolean backgroundOpened = false;
    List<Drawable> toRender = new ArrayList<>();
    private int exitButtonPressed = 0;
    float zoom = 1f;
    float camX = 0;
    float camY = 0;
    int mapWidth = 0;
    int mapHeight = 0;
    boolean rmbPressed = false;
    AssetHandler assetHandler;


    GameContainer gameContainer;

    public MapCreator() {

    }

    public void init(GameContainer gameContainer) throws SlickException {
        this.gameContainer = gameContainer;
        input = gameContainer.getInput();

        file = new CascadingMenu(0, 0, 80, 29, Color.decode("0X0079db"), Color.decode("0X42aaff"),  Color.decode("0X75c1ff"),
                Color.decode("0X42aaff"), Color.darkGray, "File");
        file.setInput(input);
        file.setOpenedWidth(200);
        toRender.add(file);
        file.addButton("Create map");
        file.addAction(0, this::createMap);
        file.addButton("Open map");
        file.addAction(1, this::openMap);
        file.addButton("Add asset");
        file.addAction(2, this::addAsset);
        file.addButton("Save as");
        file.addAction(3, ()-> {
            try {
                saveAs();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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
        view.addButton("Show polygons");
        view.addAction(2, ()->assetHandler.changePolygonsVisible());

        instruments = new CascadingMenu(240, 0, 140, 29, Color.decode("0X0079db"), Color.decode("0X42aaff"),  Color.decode("0X75c1ff"),
                Color.decode("0X42aaff"), Color.darkGray, "Instruments");
        instruments.setInput(input);
        instruments.setOpenedWidth(155);
        toRender.add(instruments);
        instruments.addButton("Add asset");
        instruments.addAction(0, this::addAsset);
        instruments.addButton("Delete");
        instruments.addAction(1, this::delete);

        exit = new BasicButton(1571, 0, 30, 29, Color.decode("0Xff496c"), Color.decode("0Xff8099"), Color.decode("0X75c1ff"),
                Color.decode("0X75c1ff"), Color.darkGray, "X", ()->exitButtonPressed = 1);
        exit.setIndent(8);
        exit.setInput(input);
        toRender.add(exit);

        input.addMouseListener(this);

        assetHandler = new AssetHandler(input);
    }

    public int update(GameContainer gameContainer, int i) throws SlickException {
        if(exitButtonPressed == 1)
            input.removeAllMouseListeners();
        return exitButtonPressed;
    }

    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.setBackground(Color.white);

        if(backgroundOpened)
            backgroundImage.draw(-camX * zoom, (-camY + 30) * zoom, zoom);

        assetHandler.render(graphics);

        graphics.setColor(Color.decode("0X75c1ff"));
        graphics.fillRect(0, 0, 1600, 30);
        for(var button: toRender)
            button.render(graphics);

        graphics.drawString("Zoom: " + zoom, 1500, 50);
        graphics.drawString("X: " + (int)camX, 1500, 65);
        graphics.drawString("Y: " + (int)camY, 1500, 80);

    }

    private void createMap(){
        JPanel panel = new JPanel();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open background");
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
            backgroundFile = fileChooser.getSelectedFile();
            backgroundOpened = true;
            try {
                backgroundImage = new Image(backgroundFile.getAbsolutePath());
            } catch (SlickException e) {
                e.printStackTrace();
            }
            mapWidth = backgroundImage.getWidth();
            mapHeight = backgroundImage.getHeight();
        }
    }

    private void openMap(){
        JPanel panel = new JPanel();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int canceled = fileChooser.showDialog(panel, "Save as");
        if(canceled == 0){
            File file = fileChooser.getSelectedFile();
            File[] files = file.listFiles();
            if(files != null && files.length == 3 && files[0].getName().equals("Background.png") && files[1].getName().equals("polygons") && files[2].getName().equals("textures")){
                backgroundFile = files[0];
                backgroundOpened = true;
                try {
                    backgroundImage = new Image(backgroundFile.getAbsolutePath());
                } catch (SlickException e) {
                    e.printStackTrace();
                }
                mapWidth = backgroundImage.getWidth();
                mapHeight = backgroundImage.getHeight();
                assetHandler.readAssets(files[1], files[2]);
            }
        }
    }

    private void addAsset(){
        assetHandler.addAsset(camX, camY, zoom);
    }

    private void save(){

    }

    private void saveAs() throws IOException {
        JPanel panel = new JPanel();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int canceled = fileChooser.showDialog(panel, "Save as");
        if (canceled == 0) {
            File file = fileChooser.getSelectedFile();
            String pathToSave = file.getAbsolutePath();
            Files.copy(Path.of(backgroundFile.getAbsolutePath()), Path.of(pathToSave  + "\\Background.png"));
            assetHandler.saveAll(Path.of(pathToSave));
        }
    }

    private void zoomIn(){
        if(zoom <= 5.75)
            zoom += 0.25;
        assetHandler.camMove(camX, camY, zoom);
    }

    private void zoomOut(){
        if(zoom >= 0.5)
            zoom -= 0.25;
        assetHandler.camMove(camX, camY, zoom);
    }

    private void undo(){

    }

    private void redo(){

    }

    private void setBackground(){

    }

    private void delete(){

    }

    @Override
    public void mouseWheelMoved(int i) {
        if(i > 0)
            zoomIn();
        else
            zoomOut();
    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mousePressed(int i, int i1, int i2) {
        if(i == 1)
            rmbPressed = true;
    }

    @Override
    public void mouseReleased(int i, int i1, int i2) {
        if(i == 1)
            rmbPressed = false;
    }

    @Override
    public void mouseMoved(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        if(rmbPressed){
            camX -= (newx - oldx) / zoom;
            camY -= (newy - oldy) / zoom;
            assetHandler.camMove(camX, camY, zoom);
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
}
