import UI.BasicButton;
import UI.ButtonAction;
import org.newdawn.slick.*;

import java.util.HashMap;
import java.util.Map;

public class MainMenu {

    HashMap<String, BasicButton> buttons = new HashMap<>();
    //HashMap<String, UI.CascadingMenu> cascades = new HashMap<>();
    Input input;
    GameContainer container;
    int state = 0;


    public MainMenu(Input input) {
        this.input = input;
    }

    public void init(GameContainer gameContainer) throws SlickException {
        buttons.put("gameButton", new BasicButton(500, 150, 400, 120,
                    Color.blue, new Color(51, 51, 255), new Color(102, 102, 255), Color.black, Color.white, "Play", new ButtonGameAct()));
        buttons.put("mapCreatorButton", new BasicButton(500, 310, 400, 120,
                    Color.blue, new Color(51, 51, 255), new Color(102, 102, 255), Color.black, Color.white, "Create map", new ButtonMapAct()));
        buttons.put("assetCreatorButton", new BasicButton(500, 470, 400, 120,
                    Color.blue, new Color(51, 51, 255), new Color(102, 102, 255), Color.black, Color.white, "Create asset", new ButtonAssetAct()));
        buttons.put("exitButton", new BasicButton(500, 630, 190, 120,
                    Color.blue, new Color(51, 51, 255), new Color(102, 102, 255), Color.black, Color.white, "Leave", new ButtonLeaveAct()));
        buttons.put("settingsButton", new BasicButton(710, 630, 190, 120,
                    Color.blue, new Color(51, 51, 255), new Color(102, 102, 255), Color.black, Color.white, "Settings", new ButtonSettingsAct()));

        for (Map.Entry<String, BasicButton> buttonEntry: buttons.entrySet()) {
            buttonEntry.getValue().setInput(input);
            buttonEntry.getValue().inputStartedMy();
        }

        this.container = gameContainer;

        System.out.println();
    }

    public int update(GameContainer gameContainer, int i) throws SlickException {
        //if(state != 0)
            //input.removeAllMouseListeners();
        return state;
    }

    public void render(GameContainer gameContainer, Graphics graphics){
        graphics.setColor(Color.white);
        graphics.fillRect(0,0, 1600, 900);
        for (Map.Entry<String, BasicButton> buttonEntry: buttons.entrySet())
            buttonEntry.getValue().render(graphics);
    }

    class ButtonGameAct extends ButtonAction{

        @Override
        public void run() {
            state = 1;
        }
    }

    class ButtonMapAct extends ButtonAction{

        @Override
        public void run() {
            state = 2;
        }
    }

    class ButtonAssetAct extends ButtonAction{

        @Override
        public void run() {
            state = 3;
        }
    }

    class ButtonLeaveAct extends ButtonAction {

        @Override
        public void run() {
            state = 4;
        }
    }

    class ButtonSettingsAct extends ButtonAction{
        @Override
        public void run() {
            System.out.println(5);
        }
    }

}
