package com.hervelin.model.FX;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ArmeCellFactory2 implements Callback<ListView<Arme>, ListCell<Arme>> {

    @Override
    public ListCell<Arme> call(ListView<Arme> listview)
    {
        return new ArmeCell2();
    }
}
