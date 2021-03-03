package objects.weapons;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import models.GameModel;

public class BunkerBullet extends Weapon {
    public static final float  BULLET_SPEED    = -(GameModel.getInstance().getSceneHeight()/58.15f);
    public static final double BULLET_SIZE = GameModel.getInstance ( ).getSceneWidth ( ) * 0.015;


    private double startY;
    private int direction ;


    private int numOfCubCurves = 1;


    {
        Circle bullet = new Circle ( BULLET_SIZE / 3 );
        bullet.setFill(Color.BLUE);

        this.getChildren ( ).addAll ( bullet );
    }

    public BunkerBullet ( Point2D position ) {
        super ( position );
    }

    public BunkerBullet ( ) { }

    // initialize speed
    {
        super.speedX = 0;
        super.speedY = BULLET_SPEED;
    }


    @Override
    public void updatePosition ( ) {
        position = new Point2D(position.getX() + speedX, position.getY() + speedY);
        //setTranslateX(getTranslateX() + speedX);
        setTranslateY(getTranslateY() + speedY);

        handleCollisions ( );
    }
}
