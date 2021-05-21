package Game;

import org.newdawn.slick.Image;

public abstract class Gun implements Runnable{
    Image image;

    int standartBulletsLoaded;
    int standartBulletsUnloaded;
    int bulletsLoaded;
    int bulletsUnloaded;
    int price;
    float damage;
    float damageReduce;
    float reloadTime;
    float reloadTimeLeft;
    boolean isReloading;
    float readyTime;
    float readyTimeLeft;
    boolean isReady;
    float swapTime;
    float swapTimeLeft;
    boolean isSwaped;
    boolean isHolding;
    boolean isShooting;
    String name;
    int holderID;
    GunType type;

    private void mainLoop(){

    }

    public void reload(){}

    public void swap(){
        if(!isSwaped){
            swapTimeLeft = swapTime;
        }
        isSwaped = !isSwaped;
    }

    public void start_shooting(){
        isShooting = true;
    }

    public void stop_shooting(){
        isShooting = false;
    }

    @Override
    public void run() {

    }
}
