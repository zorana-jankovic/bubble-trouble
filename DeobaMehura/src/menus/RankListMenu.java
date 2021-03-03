package menus;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.Main;
import models.GameModel;
import models.User;

import java.util.Random;

public class RankListMenu extends Menu {

    private Text mainMenu;

    private Text rankNum, name, score;

    private Text[] rankNums = new Text[10];
    private Text[] names = new Text[10];
    private Text[] scores = new Text[10];

    private Main main;


    private double textSize;

    public RankListMenu() {
        super(new Point2D(GameModel.getInstance().getSceneWidth() / 2 - GameModel.getInstance().getSceneWidth() / 5.376,
                GameModel.getInstance().getSceneHeight()/2 - GameModel.getInstance().getSceneHeight() / 2.16));

        Group rankListMenu = new Group();

        textSize =  ((GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/90);
        // xAsis =  (GameModel.getInstance().getSceneWidth() / 9.96);
        //yStart = GameModel.getInstance().getSceneHeight() / 10.8;


        Rectangle meniBackground = new Rectangle(GameModel.getInstance().getSceneWidth() / 2.688,
                GameModel.getInstance().getSceneHeight() / 1.112);
        meniBackground.setFill(Color.color(0, 0, 0, 0.5));
        meniBackground.setStroke(Color.rgb(244,196,48));
        meniBackground.setStrokeType(StrokeType.CENTERED);
        meniBackground.setStrokeWidth(10);


        rankNum = new Text(GameModel.getInstance().getSceneWidth()/67.2, GameModel.getInstance().getSceneHeight()/21, "RANK");
        rankNum.setStroke(Color.YELLOW);
        rankNum.setFill(Color.YELLOW);
        //rankNum.setStrokeWidth(2);
        rankNum.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        name = new Text(GameModel.getInstance().getSceneWidth()/10.34, GameModel.getInstance().getSceneHeight()/21, "NAME");
        name.setStroke(Color.YELLOW);
        name.setFill(Color.YELLOW);
        //rankNum.setStrokeWidth(2);
        name.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));


        score = new Text(GameModel.getInstance().getSceneWidth()/3.84, GameModel.getInstance().getSceneHeight()/21, "SCORE");
        score.setStroke(Color.YELLOW);
        score.setFill(Color.YELLOW);
        //rankNum.setStrokeWidth(2);
        score.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        int i = 0;
        double ind = 1;
        int rBr = 1;



//        for(i = 0; i < 10; i++){
//            users[i] = new User("zzdadsfasfasfasfa", 36476);
//        }

        for(i = 0; i < 10; i++){
            rankNums[i] = new Text(GameModel.getInstance().getSceneWidth()/35.37, (ind + 1.5)* GameModel.getInstance().getSceneHeight()/21, rBr +".");
            rankNums[i].setStroke(Color.YELLOW);
            rankNums[i].setFill(Color.YELLOW);
            rankNum.setStrokeWidth(0.5);
            rankNums[i].setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
            ind += 1.5;
            rBr++;
        }

        ind  = 1;

        for(i = 0; i < 10; i++){
            names[i] = new Text(GameModel.getInstance().getSceneWidth()/10.34, (ind + 1.5)* GameModel.getInstance().getSceneHeight()/21,"");
            names[i].setStroke(Color.YELLOW);
            names[i].setFill(Color.YELLOW);
            rankNum.setStrokeWidth(0.5);
            //names[i].setFont(Font.font((GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/80));
            names[i].setFont(Font.font("Arial", FontWeight.BOLD, (GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/80));
            //names[i].setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
            ind += 1.5;
        }

        ind  = 1;

        for(i = 0; i < 10; i++){
            scores[i] = new Text(GameModel.getInstance().getSceneWidth()/3.84, (ind + 1.5)* GameModel.getInstance().getSceneHeight()/21, "");
            scores[i].setStroke(Color.YELLOW);
            scores[i].setFill(Color.YELLOW);
            rankNum.setStrokeWidth(0.5);
            scores[i].setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
            ind += 1.5;
        }


        mainMenu = new Text(GameModel.getInstance().getSceneWidth()/7.47, GameModel.getInstance().getSceneHeight()/1.185, "START MENU");
        mainMenu.setStroke(Color.YELLOW);
        mainMenu.setFill(Color.YELLOW);
        //rankNum.setStrokeWidth(2);
        mainMenu.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameModel.getInstance().getRoot().getChildren().remove(RankListMenu.this);
                GameModel.getInstance().setPoeni(0);
                GameModel.getInstance().setCurrentLevel(0);
                GameModel.getInstance().setNumOfLifes((int)GameModel.getInstance().getJsonObject().getDouble("Num Of Lifes"));
                main.initialization();

                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()]);

            }
        });

        formatMenuOption(mainMenu);

        rankListMenu.getChildren().addAll(meniBackground, rankNum, name, score, mainMenu);

        for (i = 0; i < 10; i++){
            rankListMenu.getChildren().add(rankNums[i]);
            if (names[i] != null){
                rankListMenu.getChildren().addAll(names[i], scores[i]);
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

    public void setUsers(){

        User[] users = GameModel.getInstance().getUsers();
        String ime = GameModel.getInstance().getPlayerName();
        int i = 0;


        for(i = 0; i < 10 && users[i]!= null; i++){
            names[i].setFill(Color.YELLOW);
            if(users[i].getName().equals(ime)){
                names[i].setFill(Color.RED);
            }
           names[i].setText(users[i].getName());
        }



        for(i = 0; i < 10 && users[i]!= null; i++){
            scores[i].setFill(Color.YELLOW);
            if(users[i].getName().equals(ime)){
                scores[i].setFill(Color.RED);
            }
            scores[i].setText(users[i].getScores()+"");
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
