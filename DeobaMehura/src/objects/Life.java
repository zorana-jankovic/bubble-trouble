package objects;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import models.GameModel;


public class Life extends GameObject {
   // private double headRadius = ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.005);
   private double headRadius = ( float ) ( GameModel.getInstance ( ).getSceneWidth() / 135.76); // 9.9

    {
        Group face = new Group();
        Circle head = new Circle(headRadius);
        head.setFill(Color.rgb(224, 172, 105));

        Path rightEye = new Path();
        MoveTo moveTo = new MoveTo(headRadius/2,0);
        LineTo lineTo1 = new LineTo( headRadius/2 + headRadius/2, -headRadius/2);
        LineTo lineTo2 = new LineTo( GameModel.getInstance().getSceneWidth() / 1344 , -headRadius/2);
        LineTo lineTo3 = new LineTo(headRadius/2, 0);
        rightEye.getElements().addAll(moveTo, lineTo1,lineTo2,lineTo3);
        rightEye.setFill(Color.BLACK);

        Path leftEye = new Path();
        MoveTo moveTol = new MoveTo(-headRadius/2, 0);
        LineTo lineTo1l = new LineTo( -headRadius / 2 - headRadius / 2, -headRadius/2);
        LineTo lineTo2l = new LineTo( - GameModel.getInstance().getSceneWidth() / 1344 , -headRadius/2);
        LineTo lineTo3l = new LineTo(-headRadius/2, 0);
        leftEye.getElements().addAll(moveTol, lineTo1l,lineTo2l,lineTo3l);
        leftEye.setFill(Color.BLACK);

        Arc mouth = new Arc(0,headRadius/3,headRadius/2,headRadius/3,180,180);
        mouth.setType(ArcType.CHORD);
        mouth.setFill(Color.rgb(137, 47, 49));



        Group hat = new Group();
        Rectangle horizontal = new Rectangle(-headRadius-headRadius/2,-headRadius - 0.1*headRadius, 2*headRadius/2 + 2 * headRadius,0.25*headRadius);
        horizontal.setFill(Color.BLACK);
        Rectangle vertical = new Rectangle(-0.7*headRadius,-2.3*headRadius,1.5*headRadius,1.38*headRadius);
        vertical.setFill(Color.BLACK);
        hat.getChildren().addAll(horizontal,vertical);

        face.getChildren().addAll(head,rightEye,leftEye, mouth);

        face.setTranslateY(-headRadius*0.5 - GameModel.getInstance().getSceneHeight() / 290.77);
        hat.setTranslateY(-headRadius*0.5 - GameModel.getInstance().getSceneHeight() / 290.77);
        this.getChildren ( ).addAll ( face, hat);
    }

    public Life ( Point2D position ) {
        super ( position );
    }

}
