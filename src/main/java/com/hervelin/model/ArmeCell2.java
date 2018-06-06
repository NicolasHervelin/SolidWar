
package com.hervelin.model;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ArmeCell2  extends ListCell<Arme> {
    private ImageView imageView = new ImageView();
    @Override
    public void updateItem(Arme item, boolean empty) {
        super.updateItem(item, empty);
        String name = null;
        if (item == null || empty) {
            imageView.setImage(new Image("images/nop.png"));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            this.setText(null);
            setGraphic(imageView);
        } else {

            imageView.setImage(item.getImage());
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            name = item.getName() ;
            this.setText(name);
            this.setGraphic(imageView);
        }

    }
}

