package objects;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import main.Main;
import menus.OptionsMenu;
import models.GameModel;

public class Watch extends MovingGameObject {

    //private GameModel model = GameModel.getInstance();
   // private float watchSpeedX =  GameModel.getInstance().getSceneWidth()/1680;
    private float watchSpeedY = GameModel.getInstance().getSceneHeight() / 675; // 1.12

    //private static final double BALL_DIAMETER = (float) (GameModel.getInstance().getScreenSize().width * 0.02);
    private static final double BALL_DIAMETER = (float) (GameModel.getInstance().getSceneWidth() / 35); // 38.4


    public Watch(Point2D position) {
        super(position);


        Circle ball = new Circle(BALL_DIAMETER);
        ball.setFill(Color.WHITE);
        ball.setStrokeWidth(5);
        ball.setStroke(Color.RED);

        Line small = new Line(0 , 0, BALL_DIAMETER/2,0);
        small.setStroke(Color.BLACK);
        small.setStrokeWidth(3);

        Line big = new Line(0 , 0, 0,-BALL_DIAMETER * 0.8);
        big.setStroke(Color.BLACK);
        big.setStrokeWidth(3);

        this.getChildren().addAll(ball, small, big); //adding Circle in group for Ball


        super.speedX = 0;
        super.speedY = watchSpeedY;
    }


    @Override
    public void updatePosition() {
        handleCollisions ( );

        double faktor = (1000./ Main.protokVremena);

        position = new Point2D ( position.getX ( ) + speedX * faktor, position.getY ( ) + speedY * faktor);
        setTranslateX ( getTranslateX ( ) + speedX * faktor);
        setTranslateY ( getTranslateY ( ) + speedY * faktor);

        if ( speedY < 0 ) {
            speedY += 0.17 * GameModel.getInstance().getSceneHeight() / 756;
        } else {
            speedY = watchSpeedY;
        }
    }

    @Override
    protected void handleCollisions() {
        handleBorderCollisions ( );
        handlePlayerCollisions ( );
    }


    private void handlePlayerCollisions() {
        if ( this.getBoundsInParent ( ).intersects ( GameModel.getInstance ( ).getPlayer ( ).getBoundsInParent ( ) ) ) {
            GameModel.getInstance().setImamSat(1);

            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
            GameModel.getInstance().getSatovi().remove(this);

            if(((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
                //((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getWatchMusicPlayer().seek(Duration.ONE);
                ((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getWatchMusicPlayer().play();
            }


        }
    }


    private void handleBorderCollisions() {
        if (position.getY ( ) + BALL_DIAMETER > GameModel.getInstance ( ).getSceneHeight ( ) ) {
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
            GameModel.getInstance().getSatovi().remove(this);
        }
    }


}