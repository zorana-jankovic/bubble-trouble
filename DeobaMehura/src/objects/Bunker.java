package objects;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.*;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Main;
import menus.OptionsMenu;
import models.GameModel;

public class Bunker  extends MovingGameObject {

    //private GameModel model = GameModel.getInstance();
    //private float bunkerSpeedX = GameModel.getInstance().getSceneWidth() / 1680; // 0.8
    private float bunkerSpeedY = GameModel.getInstance().getSceneHeight() / 675; // 1.12


    //private static final int Bunker_Width = ( int ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.01 );
    public static final int Bunker_Width = (int) (GameModel.getInstance().getSceneWidth() / 38); // 19.2

    //private static final int Bunker_Height = ( int ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.026 );
    public static final int Bunker_Height = (int) (GameModel.getInstance().getSceneHeight() / 15.144); // 49.92

    //private Bunker me;



    {
        //System.out.println("bunker speed :" + bunkerSpeedX);
    }


    public Bunker(Point2D position) {
        super(position);

        Group bonus = new Group();

        LinearGradient linearGradient = new LinearGradient(0,0,1,1,true, CycleMethod.REPEAT,
                new Stop[]{new Stop(0, Color.DARKGREEN), new Stop(0.38, Color.GREEN), new Stop(1, Color.GREENYELLOW)});

        Arc postolje  = new Arc(0,0,Bunker_Width,Bunker_Height,0,180);
        postolje.setType(ArcType.CHORD);
        postolje.setFill(linearGradient);

        Rectangle ispaljivac  = new Rectangle(-Bunker_Width/3,-Bunker_Height - Bunker_Height * 0.3, 2 * Bunker_Width / 3, Bunker_Height/3);
//        ispaljivac.setFill(Color.rgb(0,128,0));
        ispaljivac.setFill(linearGradient);

        bonus.getChildren().addAll(postolje, ispaljivac);
        this.getChildren().addAll(bonus);

        super.speedX = 0;
        super.speedY = bunkerSpeedY;

        //me = this;


//        fadeTransition = new FadeTransition(Duration.seconds(10), this);
//        fadeTransition.setFromValue(1.0);
//        fadeTransition.setToValue(0.0);
//        fadeTransition.play();
//
//        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) { //check!!!
//                GameModel.getInstance().getRoot().getChildren().remove(me);
//                GameModel.getInstance().getDolari().remove(me);
//            }
//        });
    }


    @Override
    public void updatePosition() {
        handleCollisions();

        double faktor = (1000./ Main.protokVremena);
        position = new Point2D(position.getX() + speedX * faktor, position.getY() + speedY * faktor);
        setTranslateX(getTranslateX() + speedX * faktor);
        setTranslateY(getTranslateY() + speedY * faktor);

        if (speedY < 0) {
            speedY += 0.17 * (GameModel.getInstance().getSceneHeight() / 756);
        } else {
            speedY = bunkerSpeedY;
        }
    }


    @Override
    protected void handleCollisions() {
        handleBorderCollisions();
        handlePlayerCollisions();
    }

    private void handlePlayerCollisions() {
        if (this.getBoundsInParent().intersects(GameModel.getInstance().getPlayer().getBoundsInParent())) {
            //GameModel.getInstance().setPoeni(GameModel.getInstance().getPoeni() * 2);

            // AKCIJA KAD SE SUDARI SA USER-OM
            Main.imamBunker = 1;

            GameModel.getInstance().getRoot().getChildren().remove(this);
            GameModel.getInstance().getBunkeri().remove(this);

            if(((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
                //((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getBunkerMusicPlayer().seek(Duration.ZERO);
                ((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getBunkerMusicPlayer().play();
            }

            //fadeTransition.stop();
        }
    }


    private void handleBorderCollisions() {
        if (position.getY() > GameModel.getInstance().getSceneHeight()) {

            GameModel.getInstance().getRoot().getChildren().remove(this);
            GameModel.getInstance().getBunkeri().remove(this);

            //fadeTransition.stop();
        }
    }
}