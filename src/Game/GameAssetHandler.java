package Game;

import MapCreator.Asset;
import org.newdawn.slick.Graphics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameAssetHandler {
    private List<GameAsset> assets;

    boolean polygonsVisible;

    public void AssetHandler(){
        this.polygonsVisible = false;
    }

    public void changePolygonsVisible(){
        polygonsVisible = !polygonsVisible;
    }

    public void render(Graphics graphics){
        if(!polygonsVisible)
            for(GameAsset asset: assets)
                asset.render(graphics);
        else
            for(GameAsset asset: assets){
                asset.render(graphics);
                asset.renderPolygon(graphics);
            }
    }

    public boolean readAssets(File polygons, File textures){
        assets = new ArrayList<>();
        File[] polygonFiles = polygons.listFiles();
        File[] textureFiles = textures.listFiles();
        if(polygonFiles.length != textureFiles.length)
            return false;
        for(int i = 0; i < polygonFiles.length; i++){
            GameAsset asset = new GameAsset(polygonFiles[i], textureFiles[i]);
            assets.add(asset);
        }
        camMove(0, 0);
        return true;
    }

    public void camMove(float camX, float camY){
        for(GameAsset asset: assets)
            asset.camMove(camX, camY);
    }
}
