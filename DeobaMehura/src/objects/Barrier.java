package objects;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import main.Main;
import models.GameModel;

import java.awt.event.ActionEvent;

public class Barrier extends GameObject {

    //private static final int Bunker_Width = ( int ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.01 );
    public static final int Barrier_Width = (int) (GameModel.getInstance().getSceneWidth() / 30); // 19.2

    //private static final int Bunker_Height = ( int ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.026 );
    public static final int Barrier_Height = (int) (GameModel.getInstance().getSceneHeight() * 0.85); // 49.92

    private Barrier me;

    private TranslateTransition translateTransition;
    private ScaleTransition scaleTransition;

    public Barrier(Point2D position) {
        super(position);

        Group zid = new Group();

        Rectangle prepreka = new Rectangle(-Barrier_Width/2, 0,Barrier_Width, Barrier_Height);

        LinearGradient linearGradient = new LinearGradient(0,0,1,1,true,CycleMethod.REPEAT,
                new Stop[]{new Stop(0,Color.GRAY), new Stop(0.125,Color.rgb(196, 196, 196)), new Stop(0.25, Color.GRAY),
                        new Stop(0.375,Color.rgb(153, 153, 153)), new Stop(0.5,Color.GRAY),
                        new Stop(0.625, Color.rgb(124, 124, 124)), new Stop(0.75, Color.GRAY), new Stop(0.875,Color.rgb(115, 115, 115)),
                        new Stop(1,Color.DARKGRAY)});

        prepreka.setFill(linearGradient);

        zid.getChildren().addAll(prepreka);
        this.getChildren().addAll(zid);

        me = this;

        int lev = GameModel.getInstance().getCurrentLevel();

        if (lev == -1){
            scaleTransition = new ScaleTransition(Duration.seconds(5),this);
            scaleTransition.setFromY(1);
            scaleTransition.setToY(0.85);

            translateTransition = new TranslateTransition(Duration.seconds(5), this);
            translateTransition.setByY(-Barrier_Height/2*(0.15));

        }else{
            scaleTransition = new ScaleTransition(Duration.seconds(10),this);
            scaleTransition.setFromY(1);
            scaleTransition.setToY(0);

            translateTransition = new TranslateTransition(Duration.seconds(10), this);
            translateTransition.setByY(-Barrier_Height/2);
        }

        translateTransition.setOnFinished(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {

                GameModel.getInstance().getRoot().getChildren().remove(me);

                GameModel.getInstance().getBarriers().remove(me);
            }
        });
    }

    public TranslateTransition getTranslateTransition() {
        return translateTransition;
    }

    public ScaleTransition getScaleTransition() {
        return scaleTransition;
    }


}