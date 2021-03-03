package menus;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Main;
import models.GameModel;
import models.User;
import org.json.JSONObject;

public class ViewConfigMenu  extends Menu {

    private Text mainMenu;

    private Text rankNum, name, value;

    private int num = 15;

    private Text[] rankNums = new Text[num];
    private Text[] names = new Text[num];
    private Text[] values = new Text[num];



    private double textSize;



    public ViewConfigMenu() {
        super(new Point2D(GameModel.getInstance().getSceneWidth() / 2 - GameModel.getInstance().getSceneWidth() / 4.86,
                GameModel.getInstance().getSceneHeight()/2 - GameModel.getInstance().getSceneHeight() / 2.16));

        Group rankListMenu = new Group();

        textSize =  ((GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/90);
        //xAsis =  (GameModel.getInstance().getSceneWidth() / 9.96);
        //yStart = GameModel.getInstance().getSceneHeight() / 10.8;

        //jsonObject = GameModel.getInstance().getJsonObject();


        Rectangle meniBackground = new Rectangle(GameModel.getInstance().getSceneWidth() / 2.3,
                GameModel.getInstance().getSceneHeight() / 1.07);
        meniBackground.setFill(Color.color(0, 0, 0, 0.5));
        meniBackground.setStroke(Color.rgb(244,196,48));
        meniBackground.setStrokeType(StrokeType.CENTERED);
        meniBackground.setStrokeWidth(10);


        rankNum = new Text(GameModel.getInstance().getSceneWidth()/67.2, GameModel.getInstance().getSceneHeight()/21, "ORDINAL");
        rankNum.setStroke(Color.YELLOW);
        rankNum.setFill(Color.YELLOW);
        rankNum.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        name = new Text(GameModel.getInstance().getSceneWidth()/9.74, GameModel.getInstance().getSceneHeight()/21, "NAME");
        name.setStroke(Color.YELLOW);
        name.setFill(Color.YELLOW);
        name.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));


        value = new Text(GameModel.getInstance().getSceneWidth()/2.99, GameModel.getInstance().getSceneHeight()/21, "VALUE");
        value.setStroke(Color.YELLOW);
        value.setFill(Color.YELLOW);
        value.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        int i = 0;
        double ind = 0.5;
        int rBr = 1;



//        for(i = 0; i < 10; i++){
//            users[i] = new User("zzdadsfasfasfasfa", 36476);
//        }

        for(i = 0; i < num; i++){
            rankNums[i] = new Text(GameModel.getInstance().getSceneWidth()/35.37, (ind + 1.5)* GameModel.getInstance().getSceneHeight()/21, rBr +".");
            rankNums[i].setStroke(Color.YELLOW);
            rankNums[i].setFill(Color.YELLOW);
            rankNum.setStrokeWidth(0.5);
            rankNums[i].setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
            ind += 1.1;
            rBr++;
        }

        ind  = 0.5;

        for(i = 0; i < num; i++){
            names[i] = new Text(GameModel.getInstance().getSceneWidth()/9.74, (ind + 1.5)* GameModel.getInstance().getSceneHeight()/21,"");
            names[i].setStroke(Color.YELLOW);
            names[i].setFill(Color.YELLOW);
            rankNum.setStrokeWidth(0.5);
            names[i].setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
            ind += 1.1;
        }

        ind  = 0.5;

        for(i = 0; i < num; i++){
            values[i] = new Text(GameModel.getInstance().getSceneWidth()/2.954, (ind + 1.5)*GameModel.getInstance().getSceneHeight()/21,  "");
            values[i].setStroke(Color.YELLOW);
            values[i].setFill(Color.YELLOW);
            values[i].setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
            ind += 1.1;
        }


        mainMenu = new Text(GameModel.getInstance().getSceneWidth()/6.25, GameModel.getInstance().getSceneHeight()/1.116, "START MENU");
        mainMenu.setStroke(Color.YELLOW);
        mainMenu.setFill(Color.YELLOW);
        //rankNum.setStrokeWidth(2);
        mainMenu.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameModel.getInstance().getRoot().getChildren().remove(ViewConfigMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()]);
            }
        });

        formatMenuOption(mainMenu);

        rankListMenu.getChildren().addAll(meniBackground, rankNum, name, value, mainMenu);

        for (i = 0; i < num; i++){
            rankListMenu.getChildren().add(rankNums[i]);
            if (names[i] != null){
                rankListMenu.getChildren().addAll(names[i], values[i]);
            }
        }
        this.getChildren().addAll(rankListMenu);
    }

    protected void formatMenuOption(Text option) {
        option.setFill(Color.GRAY);
        option.setOnMouseEntered(e -> {
            option.setFill(Color.GRAY);
        });

        option.setOnMouseExited(e -> {
            option.setScaleX(1);
            option.setScaleY(1);
            //option.setFill(Color.YELLOW);
        });

        option.setOnMousePressed(e -> {
            option.setScaleX(0.9);
            option.setScaleY(0.9);
            option.setFill(Color.GRAY);
        });

        option.setOnMouseReleased(e -> {
            option.setScaleX(1);
            option.setScaleY(1);
        });

    }

    public void clickOnOption(){
        Event.fireEvent(mainMenu, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                0, 0, 0, 0, MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    public void setPars(){


        int i = 0;


        for(i = 0;  i < num; i++){
            names[i].setText(GameModel.getInstance().getConfigParsNames()[i]);
        }



        for(i = 0;  i < num; i++){
            Double val = GameModel.getInstance().getJsonObject().getDouble(GameModel.getInstance().getConfigParsNames()[i]);
            String s = val.toString();
            if (s.length() > 5){
                values[i].setText(s.substring(0,5));
            }else{
                values[i].setText(s);
            }

        }
    }
}
