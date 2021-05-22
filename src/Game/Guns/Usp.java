package Game.Guns;

import Game.GunType;

public class Usp extends Gun{
    public Usp(){
        standartBulletsLoaded = 12;
        standartBulletsUnloaded = 24;
        bulletsLoaded = 12;
        bulletsLeft = 24;
        damage = 20;
        reloadTime = 1.2f;
        readyTimeLeft = 0;
        isReloading = false;
        readyTime = 0.2f;
        readyTimeLeft = 0;
        isReady = true;
        swapTime = 0.8f;
        swapTimeLeft = swapTime;
        isSwaping = true;
        isSwaped = false;
        isHolding = true;
        isShooting = false;
        name = "Usp";
        type = GunType.PISTOL;
        alive = true;
    }
}
