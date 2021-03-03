package objects;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import main.Main;
import menus.OptionsMenu;
import models.GameModel;

public class Heart extends MovingGameObject {

    //private GameModel model = GameModel.getInstance();
    // private float dollarSpeedX = GameModel.getInstance().getSceneWidth() / 1680; // 0.8
    private float heartSpeedY = GameModel.getInstance().getSceneHeight() / 675; // 1.12


    //private static final int Dollar_Width = ( int ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.01 );
    private static final int Heart_Width = ( int ) ( GameModel.getInstance ( ).getSceneWidth() / 70 ); // 19.2

    //private static final int Dollar_Height = ( int ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.026 );
    private static final int Heart_Height = ( int ) ( GameModel.getInstance ( ).getSceneHeight() / 50); // 49.92


    public Heart(Point2D position){
        super(position);

        Group bonus = new Group();


        Arc leviArc  = new Arc(0,0,Heart_Width,Heart_Height,0,180);
        leviArc.setType(ArcType.CHORD);
        leviArc.setFill(Color.RED);
        leviArc.getTransforms().add(new Translate(-2*Heart_Width/3,0,0));
        leviArc.setStroke(null);

        Arc desniArc  = new Arc(0,0,Heart_Width,Heart_Height,0,180);
        desniArc.setType(ArcType.CHORD);
        desniArc.setFill(Color.RED);
        desniArc.getTransforms().add(new Translate(2*Heart_Width/3,0,0));
        desniArc.setStroke(null);

        Path donjiDeo = new Path();
        MoveTo moveTo = new MoveTo(-Heart_Width - 2*Heart_Width/3,0);
        LineTo lineTo1 = new LineTo( 0, Heart_Height*2);
        LineTo lineTo2 = new LineTo( Heart_Width + 2*Heart_Width/3 , 0);
        donjiDeo.getElements().addAll(moveTo, lineTo1,lineTo2);
        donjiDeo.setFill(Color.RED);
        donjiDeo.setStroke(null);




        bonus.getChildren().addAll(leviArc, desniArc, donjiDeo);
        this.getChildren ( ).addAll ( bonus );

        super.speedX = 0;
        super.speedY = heartSpeedY;
    }


    @Override
    public void updatePosition() {
        handleCollisions ( );

        double faktor = (1000./ Main.protokVremena);

        position = new Point2D ( position.getX ( ) + speedX * faktor, position.getY ( ) + speedY * faktor);
        setTranslateX ( getTranslateX ( ) + speedX * faktor);
        setTranslateY ( getTranslateY ( ) + speedY * faktor);

        if ( speedY < 0 ) {
            speedY += 0.17 * (GameModel.getInstance().getSceneHeight() / 756);
        } else {
            speedY = heartSpeedY;
        }
    }


    @Override
    protected void handleCollisions() {
        handleBorderCollisions ( );
        handlePlayerCollisions ( );
    }

    private void handlePlayerCollisions() {
        if ( this.getBoundsInParent ( ).intersects ( GameModel.getInstance ( ).getPlayer ( ).getBoundsInParent ( ) ) ) {
 //           GameModel.getInstance().setPoeni((int)(GameModel.getInstance().getPoeni()*GameModel.getInstance().getJsonObject().getDouble("Dolar Bonus")));
            GameModel.getInstance().setNumOfLifes(GameModel.getInstance().getNumOfLifes()+2);
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );

            GameModel.getInstance().getSrca().remove(this);
            Main.resetLifes();

//            ((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getDolarMusicPlayer().seek(Duration.ZERO);
//            ((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getDolarMusicPlayer().play();

            if(((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
                //((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getHeartMusicPlayer().seek(Duration.ZERO);
                ((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getHeartMusicPlayer().play();
            }

        }
    }


    private void handleBorderCollisions() {
        if (position.getY ( ) + Heart_Height*2 > GameModel.getInstance ( ).getSceneHeight ( ) ) {
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );

            GameModel.getInstance().getDolari().remove(this);

        }
    }
}
