package objects;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Main;
import menus.OptionsMenu;
import models.GameModel;




public class Dollar extends MovingGameObject {

    //private GameModel model = GameModel.getInstance();
   // private float dollarSpeedX = GameModel.getInstance().getSceneWidth() / 1680; // 0.8
    private float dollarSpeedY = GameModel.getInstance().getSceneHeight() / 675; // 1.12


    //private static final int Dollar_Width = ( int ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.01 );
    private static final int Dollar_Width = ( int ) ( GameModel.getInstance ( ).getSceneWidth() / 70 ); // 19.2

    //private static final int Dollar_Height = ( int ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.026 );
    private static final int Dollar_Height = ( int ) ( GameModel.getInstance ( ).getSceneHeight() / 15.144); // 49.92

    private Dollar me;

    private  FadeTransition fadeTransition;

    public Dollar(Point2D position){
        super(position);

        Group bonus = new Group();

        Rectangle rectangle = new Rectangle(-Dollar_Width/2,-Dollar_Height/2,Dollar_Width,Dollar_Height);
        rectangle.setFill(Color.GREEN);

        Text tekst = new Text(-GameModel.getInstance().getSceneWidth() / 268.8,GameModel.getInstance().getSceneHeight() / 252,"$");
        tekst.setStrokeWidth(10);
        tekst.setFont(new Font((GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/105));



        bonus.getChildren().addAll(rectangle, tekst);
        this.getChildren ( ).addAll ( bonus );

        super.speedX = 0;
        super.speedY = dollarSpeedY;

        me = this;


        fadeTransition = new FadeTransition(Duration.seconds(10),this);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();

        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { //check!!!
                GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( me );
                GameModel.getInstance().getDolari().remove(me);
            }
        });
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
            speedY = dollarSpeedY;
        }
    }


    @Override
    protected void handleCollisions() {
        handleBorderCollisions ( );
        handlePlayerCollisions ( );
    }

    private void handlePlayerCollisions() {
        if ( this.getBoundsInParent ( ).intersects ( GameModel.getInstance ( ).getPlayer ( ).getBoundsInParent ( ) ) ) {
            GameModel.getInstance().setPoeni((int)(GameModel.getInstance().getPoeni()*GameModel.getInstance().getJsonObject().getDouble("Dolar Bonus")));
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );

            GameModel.getInstance().getDolari().remove(this);

            fadeTransition.stop();

            if(((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
                //((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getDolarMusicPlayer().seek(Duration.ZERO);
                ((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getDolarMusicPlayer().play();
            }

        }
    }


    private void handleBorderCollisions() {
        if (position.getY ( ) + Dollar_Height/2 > GameModel.getInstance ( ).getSceneHeight ( ) ) {
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );

            GameModel.getInstance().getDolari().remove(this);

            fadeTransition.stop();
        }
    }

    public FadeTransition getFadeTransition() {
        return fadeTransition;
    }
}
