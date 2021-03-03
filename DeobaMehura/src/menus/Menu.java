package menus;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import objects.GameObject;

public abstract class Menu extends GameObject {

    public Menu(Point2D position) {
        super(position);
    }


    protected abstract void formatMenuOption(Text option);

    public abstract void clickOnOption();

}
