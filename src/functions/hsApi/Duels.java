package functions.hsApi;

public class Duels {
    private boolean relevant;
    private boolean constructed;

    

    public Duels() {
    }

    public Duels(boolean relevant, boolean constructed) {
        this.relevant = relevant;
        this.constructed = constructed;
    }

    public boolean isRelevant() {
        return relevant;
    }

    public void setRelevant(boolean relevant) {
        this.relevant = relevant;
    }

    public boolean isConstructed() {
        return constructed;
    }

    public void setConstructed(boolean constructed) {
        this.constructed = constructed;
    }
}
