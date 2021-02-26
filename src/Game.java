import com.sun.tools.javac.Main;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;

public class Game extends BasicGame {
    enum currentWindow {mainMenu, assetCreator, mapCreator, game}

    currentWindow curWindow;
    MainMenu mainMenu;
    AssetCreator assetCreator;
    MapCreator mapCreator = new MapCreator();
    Gamer gamer = new Gamer();
    Input input;


    public Game(String title) {
        super(title);
        curWindow = currentWindow.mainMenu;
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        gameContainer.setShowFPS(false);
        this.input = gameContainer.getInput();
        mainMenu = new MainMenu(input);
        mainMenu.init(gameContainer);
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        int state = 0;
        if(curWindow == currentWindow.mainMenu){
            state = mainMenu.update(gameContainer, i);
            if(state == 1)
                curWindow = currentWindow.game;
            else if(state == 2)
                curWindow = currentWindow.mapCreator;
            else if(state == 3) {
                curWindow = currentWindow.assetCreator;
                assetCreator = new AssetCreator();
                assetCreator.setInput(input);
                assetCreator.init(gameContainer);
            }
            else if(state == 4)
                gameContainer.exit();
        }
        if(curWindow == currentWindow.assetCreator){
            if(assetCreator.update(gameContainer, i) == 1) {
                curWindow = currentWindow.mainMenu;
                mainMenu = new MainMenu(input);
                mainMenu.init(gameContainer);
            }
        }
        if(curWindow == currentWindow.mapCreator){
            if(mapCreator.update(gameContainer, i) == 1)
                curWindow = currentWindow.mainMenu;
        }
        if(curWindow == currentWindow.game){
            if(gamer.update(gameContainer, i) == 1)
                curWindow = currentWindow.mainMenu;
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        if(curWindow == currentWindow.mainMenu){
            mainMenu.render(gameContainer, graphics);
        }
        if(curWindow == currentWindow.assetCreator){
            assetCreator.render(gameContainer, graphics);
        }
        if(curWindow == currentWindow.mapCreator){
            mapCreator.render(gameContainer, graphics);
        }
        if(curWindow == currentWindow.game){
            gamer.render(gameContainer, graphics);
        }
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer game = new AppGameContainer(new Game("Game"));
        game.setDisplayMode(1600, 900, false);
        game.setForceExit(false);
        game.start();
    }
}