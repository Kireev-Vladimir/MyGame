import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BasicPlayer extends Entity{
    float hp;
    float armor;

    boolean isCrouched;

    int money;
    String name;

    List<Gun> guns = new ArrayList<>();

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
    }

    Rectangle head;
    Rectangle body;
    Rectangle legs;

    public void buyGun(){

    }
}
