package Game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Gamer {

    GameContainer gameContainer;
    static GameAssetHandler assetHandler;
    static float gravity = 0.001f;
    static BasicPlayer players;
    static public Player player;
    static float camX = 0;
    static float camY = 0;
    Image backgroundImage;
    float mapWidth;
    float mapHeight;
    Thread playerThread;

    public Gamer() {

    }

    public void init(GameContainer gameContainer) throws SlickException {
        this.gameContainer = gameContainer;
        assetHandler = new GameAssetHandler();
        openMap();
        player = new Player(gameContainer.getInput());
        playerThread = new Thread(player);
        playerThread.setDaemon(true);
        playerThread.start();
    }

    public int update(GameContainer gameContainer, int i) throws SlickException {
        return 0;
    }

    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        if(backgroundImage != null)
            backgroundImage.draw(-camX, -camY);
        assetHandler.render(graphics);
        player.render(graphics);
    }

    public static void camMove(int x, int y){
        assetHandler.camMove(x, y);
        camX = x;
        camY = y;
    }

    private void openMap(){
        JPanel panel = new JPanel();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int canceled = fileChooser.showDialog(panel, "Open map");
        if(canceled == 0){
            File file = fileChooser.getSelectedFile();
            File[] files = file.listFiles();
            if(files != null && files.length == 3 && files[0].getName().equals("Background.png") && files[1].getName().equals("polygons") && files[2].getName().equals("textures")){
                File backgroundFile = files[0];
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
}
