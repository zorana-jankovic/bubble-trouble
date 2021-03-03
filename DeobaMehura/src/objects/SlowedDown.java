package objects;

import javafx.animation.FadeTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import main.Main;
import menus.OptionsMenu;
import models.GameModel;

public class SlowedDown extends MovingGameObject {

    //private GameModel model = GameModel.getInstance();
    //private float slowedDownSpeedX = GameModel.getInstance().getSceneWidth() / 1680; // 0.8
    private float slowedDownSpeedY = GameModel.getInstance().getSceneHeight() / 675; // 1.12


    //private static final int Bunker_Width = ( int ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.01 );
    private static final int SlowedDown_Width = (int) (GameModel.getInstance().getSceneWidth() / 50); // 19.2

    //private static final int Bunker_Height = ( int ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.026 );
    private static final int SlowedDown_Height = (int) (GameModel.getInstance().getSceneHeight() / 30); // 49.92

    //private SlowedDown me;

    //private FadeTransition fadeTransition;

    {
        //System.out.println("bunker speed :" + slowedDownSpeedX);
    }


    public SlowedDown(Point2D position) {
        super(position);

        Group bonus = new Group();

        LinearGradient linearGradient1 = new LinearGradient(1, 0, 1, 1, true, CycleMethod.REPEAT,
                new Stop[]{new Stop(0, Color.rgb(255,255,153)), new Stop(1, Color.rgb(100,149,237))});

        LinearGradient linearGradient2 = new LinearGradient(1, 0, 1, 1, true, CycleMethod.REPEAT,
                new Stop[]{ new Stop(0, Color.rgb(230,230,250)), new Stop(1, Color.rgb(100,149,237))});

        Polyline prviTrougao = new Polyline();
        prviTrougao.getPoints().addAll(new Double[]{0.0,0.0,(double)SlowedDown_Width, (double) SlowedDown_Height,
                                             (double) -SlowedDown_Width, (double) SlowedDown_Height , 0.0, 0.0});
        prviTrougao.setFill(linearGradient2);
        prviTrougao.setStroke(null);
        //prviTrougao.setRotate(180);
        prviTrougao.getTransforms().addAll(new Translate(0, + SlowedDown_Height / 10),new Rotate(180,0,0));

        Polyline drugiTrougao = new Polyline();
        drugiTrougao.getPoints().addAll(new Double[]{0.0,0.0,(double)SlowedDown_Width, (double) SlowedDown_Height,
                                            (double) -SlowedDown_Width, (double) SlowedDown_Height , 0.0, 0.0});
        drugiTrougao.setFill(linearGradient2);
        drugiTrougao.setStroke(null);
        drugiTrougao.getTransforms().addAll(new Translate(0, -SlowedDown_Height / 10));



        bonus.getChildren().addAll(prviTrougao,drugiTrougao);
        this.getChildren().addAll(bonus);

        super.speedX = 0;
        super.speedY = slowedDownSpeedY;

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
            speedY = slowedDownSpeedY;
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

            // AKCIJA KADA SE SUDARI SA IGRACEM
            Main.usporiVreme(2000);

            GameModel.getInstance().getRoot().getChildren().remove(this);
            GameModel.getInstance().getUsporeniSnimci().remove(this);

            if(((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
                //((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getSlowTimeMusicPlayer().seek(Duration.ZERO);
                ((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getSlowTimeMusicPlayer().play();
            }


            // fadeTransition.stop();
        }
    }


    private void handleBorderCollisions() {
        if (position.getY() + SlowedDown_Height> GameModel.getInstance().getSceneHeight()) {

            GameModel.getInstance().getRoot().getChildren().remove(this);
            GameModel.getInstance().getUsporeniSnimci().remove(this);

            //fadeTransition.stop();
        }
    }
}