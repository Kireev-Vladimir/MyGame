package Game.Guns;

import Game.GunType;

public class Ak47 extends Gun {
    public Ak47(){
        standartBulletsLoaded = 30;
        standartBulletsUnloaded = 90;
        bulletsLoaded = 30;
        bulletsLeft = 90;
        damage = 30;
        reloadTime = 1;
        readyTimeLeft = 0;
        isReloading = false;
        readyTime = 0.08f;
        readyTimeLeft = 0;
        isReady = true;
        swapTime = 1.2f;
        swapTimeLeft = swapTime;
        isSwaping = true;
        isSwaped = false;
        isHolding = true;
        isShooting = false;
        name = "AK47";
        type = GunType.AR;
        alive = true;
    }
}
