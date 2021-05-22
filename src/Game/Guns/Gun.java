package Game.Guns;

import Game.Gamer;
import Game.GunType;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class Gun implements Runnable {
    Image image;

    protected int standartBulletsLoaded;
    protected int standartBulletsUnloaded;
    protected int bulletsLoaded;
    protected int bulletsLeft;
    protected int price;
    protected float damage;
    protected float reloadTime;
    protected float reloadTimeLeft;
    protected boolean isReloading;
    protected float readyTime;
    protected float readyTimeLeft;
    protected boolean isReady;
    protected float swapTime;
    protected float swapTimeLeft;
    protected boolean isSwaping;
    protected boolean isSwaped;
    protected boolean isHolding;
    protected boolean isShooting;
    protected String name;
    protected int holderID;
    protected GunType type;
    protected long lastTime;
    protected boolean alive;

    private void mainLoop(){
        lastTime = System.currentTimeMillis();
        while(alive) {
            synchronized (this){
                if (isHolding) {
                    long cur_time = System.currentTimeMillis();
                    double timeDiff = (double) (cur_time - lastTime) / 1000;
                    lastTime = cur_time;
                    if (isSwaping) {
                        swapTimeLeft -= timeDiff;
                        if (swapTimeLeft <= 0) {
                            isSwaped = true;
                            isSwaping = false;
                            isReady = true;
                        }
                    } else if (isReloading && !isSwaping) {
                        reloadTimeLeft -= timeDiff;
                        System.out.println(reloadTimeLeft);
                        if (reloadTimeLeft <= 0) {
                            int bulletsToLoad = Integer.min(bulletsLeft, standartBulletsLoaded - bulletsLoaded);
                            bulletsLeft -= bulletsToLoad;
                            bulletsLoaded += bulletsToLoad;
                            isReloading = false;
                            isReady = true;
                        }
                    }
                    else if (!isReady) {
                        if (readyTimeLeft > 0) {
                            readyTimeLeft -= timeDiff;
                            if (readyTimeLeft < 0)
                                isReady = true;
                        }
                    }
                    if (isShooting) {
                        if (isReady && bulletsLoaded != 0) {
                            Gamer.player.shoot(damage);
                            isReady = false;
                            bulletsLoaded -= 1;
                            readyTimeLeft = readyTime;
                        }
                    }
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void reload(){
        if(bulletsLeft > 0 && standartBulletsLoaded != bulletsLoaded && isSwaped) {
            isReloading = true;
            isReady = false;
            reloadTimeLeft = reloadTime;
        }
    }

    public void swap(){
        synchronized (this){
            if (!isHolding) {
                lastTime = System.currentTimeMillis();
                swapTimeLeft = swapTime;
                isHolding = true;
                isSwaping = true;
                isShooting = false;
            } else {
                isHolding = false;
            }
        }
        isReloading = false;
        isReady = false;
    }

    public void start_shooting(){
        isShooting = true;
    }

    public void stop_shooting(){
        isShooting = false;
    }

    @Override
    public void run() {
        mainLoop();
    }

    public void drawInfo(Graphics graphics){
        graphics.drawString("bullets " + bulletsLoaded + " / " + bulletsLeft, 1300, 700);
        graphics.drawString("reload time left: " + reloadTimeLeft, 1300, 720);
        graphics.drawString("ready time left: " + readyTimeLeft, 1300, 740);
        graphics.drawString("Is ready: " + isReady, 1300, 760);
        graphics.drawString("Is shooting: " + isShooting, 1300, 780);
        graphics.drawString("Name: " + name, 1300, 800);
        graphics.drawString("is swaping: " + isSwaping, 1300, 820);
        graphics.drawString("swap time left" + swapTimeLeft, 1300, 840);
    }

}
