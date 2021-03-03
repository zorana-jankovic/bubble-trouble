package objects;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import main.Main;
import menus.OptionsMenu;
import models.GameModel;
import playerStates.MovingLeftState;
import playerStates.MovingRightState;
import playerStates.StandingState;
import playerStates.State;

public class Shield extends MovingGameObject {

   // private float shieldSpeedX = GameModel.getInstance().getSceneWidth()/1680;
    private float shieldSpeedY = GameModel.getInstance().getSceneHeight() / 675; // 1.12

   // private double headRadius = ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.008 );

    private double headRadius = ( float ) ( GameModel.getInstance ( ).getSceneWidth() / 87.5 ); // 15.36


    {
        speedX = 0;
        speedY = shieldSpeedY;
        //Rectangle player = new Rectangle ( PLAYER_WIDTH, PLAYER_HEIGHT );
        //player.setFill ( Color.LIGHTBLUE );
        Group face = new Group();
        Circle head = new Circle(headRadius);
        head.setFill(Color.rgb(224, 172, 105,0.5));

        Path rightEye = new Path();
        MoveTo moveTo = new MoveTo(headRadius/2,0);
        LineTo lineTo1 = new LineTo( headRadius/2 + headRadius/2, -headRadius/2);
        LineTo lineTo2 = new LineTo( GameModel.getInstance().getSceneWidth() / 1344 , -headRadius/2);
        LineTo lineTo3 = new LineTo(headRadius/2, 0);
        rightEye.getElements().addAll(moveTo, lineTo1,lineTo2,lineTo3);
        rightEye.setFill(Color.rgb(0,0,0,0.5));

        Path leftEye = new Path();
        MoveTo moveTol = new MoveTo(-headRadius/2, 0);
        LineTo lineTo1l = new LineTo( -headRadius / 2 - headRadius / 2, -headRadius/2);
        LineTo lineTo2l = new LineTo( -GameModel.getInstance().getSceneWidth() / 1344 , -headRadius/2);
        LineTo lineTo3l = new LineTo(-headRadius/2, 0);
        leftEye.getElements().addAll(moveTol, lineTo1l,lineTo2l,lineTo3l);
        leftEye.setFill(Color.rgb(0,0,0,0.5));

        Arc mouth = new Arc(0,headRadius/3,headRadius/2,headRadius/3,180,180);
        mouth.setType(ArcType.CHORD);
        mouth.setFill(Color.rgb(137, 47, 49,0.5));



        Group hat = new Group();
        Rectangle horizontal = new Rectangle(-headRadius-headRadius/2,-headRadius - 0.1*headRadius, 2*headRadius/2 + 2 * headRadius,0.25*headRadius);
        horizontal.setFill(Color.BLACK);
        Rectangle vertical = new Rectangle(-0.7*headRadius,-2.3*headRadius,1.5*headRadius,1.38*headRadius);
        vertical.setFill(Color.rgb(0,0,0,0.5));
        hat.getChildren().addAll(horizontal,vertical);


        Path body = new Path();
        MoveTo move = new MoveTo(0,0.66*headRadius);
        LineTo line1 = new LineTo(-1.8*headRadius,2.8*headRadius);

        CubicCurveTo cubicCurveTo1 = new CubicCurveTo
                (headRadius/3,headRadius*0.7,headRadius*0.8,2.6*headRadius,2*headRadius,2.2*headRadius);
        LineTo line2 = new LineTo(0,0.66*headRadius);

        body.getElements().addAll(move,line1,cubicCurveTo1,line2);
        body.setFill(Color.color(1,0,0,0.5));
        body.setStroke(Color.color(1,0,0,0.5));

        face.getChildren().addAll(body,head,rightEye,leftEye, mouth);


        body.setTranslateY(-GameModel.getInstance().getSceneHeight() / 290.77);

        face.setTranslateY(-headRadius*0.5 - GameModel.getInstance().getSceneHeight() / 290.77);
        hat.setTranslateY(-headRadius*0.5 - GameModel.getInstance().getSceneHeight() / 290.77);
        this.getChildren ( ).addAll ( face, hat);

    }

    public Shield ( Point2D position ) {
        super ( position );
    }

    @Override
    public void updatePosition ( ) {

        handleCollisions ( );

        double faktor = (1000./ Main.protokVremena);

        position = new Point2D ( position.getX ( ) + speedX * faktor, position.getY ( ) + speedY * faktor );
        setTranslateX ( getTranslateX ( ) + speedX * faktor );
        setTranslateY ( getTranslateY ( ) + speedY * faktor);

        if ( speedY < 0 ) {
            speedY += 0.17 * (GameModel.getInstance().getSceneHeight()/756);
        } else {
            speedY = shieldSpeedY;
        }

    }

    @Override
    protected void handleCollisions ( ) {
        handleBorderCollisions ( );
        handlePlayerCollisions ( );
    }

    private void handleBorderCollisions() {

        if (position.getY ( ) + 1.5 * headRadius> GameModel.getInstance ( ).getSceneHeight ( ) ) {
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
            GameModel.getInstance().getStitovi().remove(this);
        }

    }

    private void handlePlayerCollisions() {

        if ( this.getBoundsInParent ( ).intersects ( GameModel.getInstance ( ).getPlayer ( ).getBoundsInParent ( ) ) ) {
            //GameModel.getInstance().setPoeni(GameModel.getInstance().getPoeni()*2);
            GameModel.getInstance().setImamStit(1);
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
            GameModel.getInstance().getStitovi().remove(this);

            if(((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
                //((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getShieldMusicPlayer().seek(Duration.ZERO);
                ((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getShieldMusicPlayer().play();
            }

        }

    }

}
