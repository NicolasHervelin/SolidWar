
package com.hervelin.model.FX;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;


public class ArmeCell  extends ListCell<Arme> {
    private ImageView imageView = new ImageView();
@Override
public void updateItem(Arme item, boolean empty) {
        super.updateItem(item, empty);
        String name = null;
        if (item == null || empty) {
            this.setText(null);
            setGraphic(null);
        } else {
            imageView.setImage(item.getImage());
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            name = item.getName() ;
        }
        this.setText(name);
        this.setGraphic(imageView);
    }
}

