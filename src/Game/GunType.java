package Game;

public enum GunType {
    PISTOL, MELEE, SMG, AR, SR, SG, DMR;

    public int returnSlot(){
        if(this == SMG || this == AR || this == SR || this == SG || this == DMR)
            return 1;
        else if(this == PISTOL)
            return 2;
        else
            return 3;
    }
}
