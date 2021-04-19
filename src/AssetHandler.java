import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.List;

public class AssetHandler {
    List<Asset> assets;
    Input input;

    boolean polygonsVisible;

    public AssetHandler(Input input){
        assets = new ArrayList<>();
        this.input = input;
        this.polygonsVisible = false;
    }

    public void changePolygonsVisible(){
        polygonsVisible = !polygonsVisible;
    }

    public void addAsset(float camX, float camY, float zoom){
        Asset asset = new Asset(camX, camY, zoom);
        input.addMouseListener(asset);
        assets.add(asset);
    }

    public void camMove(float camX, float camY, float zoom){
        for(Asset asset: assets)
            asset.camMove(camX, camY, zoom);
    }

    public void render(Graphics graphics){
        if(!polygonsVisible)
            for(Asset asset: assets)
                asset.render(graphics);
        else
            for(Asset asset: assets){
               asset.render(graphics);
               asset.renderPolygon(graphics);
            }
    }
}
