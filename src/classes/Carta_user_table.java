package classes;

import javafx.scene.image.ImageView;

public class Carta_user_table {
    
 
    private ImageView imageColumn1;
    private ImageView imageColumn2;
    private ImageView imageColumn3;

    public Carta_user_table() {
    }

    public Carta_user_table(ImageView imageColumn1, ImageView imageColumn2, ImageView imageColumn3) {
        this.imageColumn1 = imageColumn1;
        this.imageColumn2 = imageColumn2;
        this.imageColumn3 = imageColumn3;
    }
    
    
    public ImageView getImageColumn1() {
        return imageColumn1;
    }

    public void setImageColumn1(ImageView imageColumn1) {
        this.imageColumn1 = imageColumn1;
    }

    public ImageView getImageColumn2() {
        return imageColumn2;
    }

    public void setImageColumn2(ImageView imageColumn2) {
        this.imageColumn2 = imageColumn2;
    }

    public ImageView getImageColumn3() {
        return imageColumn3;
    }

    public void setImageColumn3(ImageView imageColumn2) {
        this.imageColumn3 = imageColumn2;
    }
    
  
    
}
