import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AssetHandler {
    protected List<Asset> assets;
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

    public void saveAll(Path pathToSave){
        String polygonsPath = pathToSave + "\\polygons";
        String texturesPath = pathToSave + "\\textures";

        new File(polygonsPath).mkdir();
        new File(texturesPath).mkdir();
        for(int i = 0; i <assets.size(); i++){
            assets.get(i).saveAs(polygonsPath, texturesPath, i);
        }
    }

    public boolean readAssets(File polygons, File textures){
        File[] polygonFiles = polygons.listFiles();
        File[] textureFiles = textures.listFiles();
        if(polygonFiles.length != textureFiles.length)
            return false;
        for(int i = 0; i < polygonFiles.length; i++){
            Asset asset = new Asset(polygonFiles[i], textureFiles[i]);
            input.addMouseListener(asset);
            assets.add(asset);
        }
        return true;
    }
}
