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
import javafx.scene.text.Text;
import main.Main;
import models.GameModel;

public class CommandsMenu extends Menu {

    private Text mainMenu;
    private double textSize, xAsis, yStart;

    private Text leftArrow, rightArrow, space,
            xTaster, escTaster;


    public CommandsMenu() {
        super(new Point2D(GameModel.getInstance().getSceneWidth() / 2 - GameModel.getInstance().getSceneWidth() / 3,
                GameModel.getInstance().getSceneHeight() / 2 - GameModel.getInstance().getSceneHeight() / 2.38));

        Group playerName = new Group();

        textSize = ((GameModel.getInstance().getSceneWidth() + GameModel.getInstance().getSceneHeight()) / 60);


        Rectangle meniBackground = new Rectangle(GameModel.getInstance().getSceneWidth() / 1.5,
                GameModel.getInstance().getSceneHeight() / 1.2);
        meniBackground.setFill(Color.color(0, 0, 0, 0.5));
        meniBackground.setStroke(Color.rgb(244, 196, 48));
        meniBackground.setStrokeType(StrokeType.CENTERED);
        meniBackground.setStrokeWidth(10);

        leftArrow = new Text(GameModel.getInstance().getSceneWidth()/16.8, GameModel.getInstance().getSceneHeight()/ 10.8, "MOVING   LEFT                           --->          LEFT ARROW ");
        leftArrow.setStroke(Color.YELLOW);
        leftArrow.setFill(Color.YELLOW);
        leftArrow.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        rightArrow = new Text(GameModel.getInstance().getSceneWidth()/16.8, GameModel.getInstance().getSceneHeight()/4.45, "MOVING   RIGHT                       --->          RIGHT ARROW ");
        rightArrow.setStroke(Color.YELLOW);
        rightArrow.setFill(Color.YELLOW);
        rightArrow.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        space = new Text(GameModel.getInstance().getSceneWidth()/16.8, GameModel.getInstance().getSceneHeight()/ 2.8, "SHOOTING                                  --->          SPACE ");
        space.setStroke(Color.YELLOW);
        space.setFill(Color.YELLOW);
        space.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        xTaster = new Text(GameModel.getInstance().getSceneWidth()/16.8, GameModel.getInstance().getSceneHeight()/ 2.04, "BUNKER   POSITIONING          --->          TASTER X ");
        xTaster.setStroke(Color.YELLOW);
        xTaster.setFill(Color.YELLOW);
        xTaster.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));


        escTaster = new Text(GameModel.getInstance().getSceneWidth()/16.8, GameModel.getInstance().getSceneHeight()/ 1.575, "PAUSING   THE   GAME            --->          TASTER ESC ");
        escTaster.setStroke(Color.YELLOW);
        escTaster.setFill(Color.YELLOW);
        escTaster.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));


        mainMenu = new Text(GameModel.getInstance().getSceneWidth()/16.8, GameModel.getInstance().getSceneHeight()/ 1.303, "START MENU");
        mainMenu.setStroke(Color.YELLOW);
        mainMenu.setFill(Color.YELLOW);
        mainMenu.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameModel.getInstance().getRoot().getChildren().remove(CommandsMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()]);
            }
        });

        formatMenuOption(mainMenu);

        playerName.getChildren().addAll(meniBackground, mainMenu, leftArrow, rightArrow,space,xTaster, escTaster);
        this.getChildren().addAll(playerName);
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

}