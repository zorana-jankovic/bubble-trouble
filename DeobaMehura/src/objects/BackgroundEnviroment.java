package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import models.GameModel;

import javax.swing.border.StrokeBorder;

public class BackgroundEnviroment extends GameObject {


    public BackgroundEnviroment ( ) {
//        Rectangle background = new Rectangle (
//                GameModel.getInstance ( ).getSceneWidth ( ) + 240,
//                GameModel.getInstance ( ).getSceneHeight ( ) + 200
//        );

        float width = GameModel.getInstance ( ).getSceneWidth ( ) + GameModel.getInstance().getSceneOffsetWidth();
        float height = GameModel.getInstance ( ).getSceneHeight ( ) + GameModel.getInstance().getSceneOffsetHeight();


        height = height / 8;

        double predHeight = 0;

        Rectangle zid[] = new Rectangle[10];
        for (int i = 0 ; i < 9; i++){
            if (i == 3 || i == 6){
                zid[i] = new Rectangle(0, predHeight, width , height*0.7);
                predHeight = predHeight + height*0.7;
            }
            else {
                zid[i] = new Rectangle(0, predHeight, width, height);
                predHeight = predHeight + height*0.9;
            }
            zid[i].setStrokeWidth(5);
//            if (i < 2) {
//                zid[i].setStroke(Color.rgb(159, 159, 159));
//            }
//            else{
//                //zid[i].setStroke(Color.rgb(130, 130, 130));
//            }

            zid[i].setStroke(Color.rgb(140, 140, 140));
            zid[i].setStrokeType(StrokeType.CENTERED);
        }

        zid[9] = new Rectangle(0,predHeight,width,GameModel.getInstance().getSceneHeight() + GameModel.getInstance().getSceneOffsetHeight()- predHeight);
        zid[9].setStrokeWidth(5);
        zid[9].setStroke(Color.rgb(140, 140, 140));

        //background.setFill ( Color.LIGHTGRAY);

        zid[0].setFill(new LinearGradient(0,0,1,0,true,CycleMethod.NO_CYCLE,
                new Stop[]{new Stop(0,Color.GRAY), new Stop(0.2,Color.rgb(196, 196, 196)), new Stop(1, Color.GRAY)}));

        zid[1].setFill(new LinearGradient(0,0,1,0,true,CycleMethod.NO_CYCLE,
                new Stop[]{new Stop(0,Color.rgb(153, 153, 153)), new Stop(0.9,Color.rgb(203, 205, 205)), new Stop(1, Color.GRAY)}));
        zid[2].setFill(new LinearGradient(0,0,1,0,true,CycleMethod.NO_CYCLE,
                new Stop[]{new Stop(0,Color.rgb(153, 153, 153)), new Stop(1, Color.GRAY)}));
        zid[3].setFill(Color.DIMGREY);
        zid[4].setFill(Color.rgb(124, 124, 124));
        zid[5].setFill(Color.rgb(115, 115, 115));

        zid[6].setFill(new LinearGradient(0,0,1,0,true,CycleMethod.NO_CYCLE,
                new Stop[]{new Stop(0,Color.rgb(90, 90, 90)), new Stop(0.85,Color.rgb(160, 160, 160)), new Stop(1, Color.rgb(90, 90, 90))}));

        zid[7].setFill(Color.rgb(100, 100, 100));

        zid[8].setFill(new LinearGradient(0,0,1,0,true,CycleMethod.NO_CYCLE,
                new Stop[]{new Stop(0,Color.rgb(115, 115, 115)), new Stop(0.85,Color.rgb(120, 120, 120))}));

        zid[9].setFill(Color.rgb(100, 100, 100));

        this.getChildren ( ).addAll ( zid );
    }
}
