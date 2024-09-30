package functions.hsApi;

public class RuneCost {
    private int blood;
    private int frost;
    private int unholy;

    

    public RuneCost() {
    }

    public RuneCost(int blood, int frost, int unholy) {
        this.blood = blood;
        this.frost = frost;
        this.unholy = unholy;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int getFrost() {
        return frost;
    }

    public void setFrost(int frost) {
        this.frost = frost;
    }

    public int getUnholy() {
        return unholy;
    }

    public void setUnholy(int unholy) {
        this.unholy = unholy;
    }
}
