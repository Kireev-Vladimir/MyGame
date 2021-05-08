import org.newdawn.slick.Image;

public class Gun {
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
    String name;
    int holderID;
    GunType type;

    public void shoot(float angle){}

    public void reload(){}

    public void swap(){
        if(!isSwaped){
            swapTimeLeft = swapTime;
        }
        isSwaped = !isSwaped;
    }
}
