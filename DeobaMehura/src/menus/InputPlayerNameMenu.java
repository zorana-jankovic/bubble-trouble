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
import models.User;
import objects.GameObject;

import java.util.ArrayList;

public class InputPlayerNameMenu extends Menu {

    private Text finish;
    private double textSize, xAsis, yStart;

    private TextField inputName;

    public InputPlayerNameMenu() {
        super(new Point2D(GameModel.getInstance().getSceneWidth() / 2 - GameModel.getInstance().getSceneWidth()/6.4,
                GameModel.getInstance().getSceneHeight()/2 - GameModel.getInstance().getSceneHeight() / 5.81));

        Group playerName = new Group();

        textSize =  ((GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/60);



        Rectangle meniBackground = new Rectangle(GameModel.getInstance().getSceneWidth() / 3,
                GameModel.getInstance().getSceneHeight() / 3);
        meniBackground.setFill(Color.color(0, 0, 0, 0.5));
        meniBackground.setStroke(Color.rgb(244,196,48));
        meniBackground.setStrokeType(StrokeType.CENTERED);
        meniBackground.setStrokeWidth(10);

        inputName = new TextField();
        inputName.setPrefColumnCount(10);
        inputName.setTranslateX(GameModel.getInstance().getSceneWidth() / 9.465);
        inputName.setTranslateY(GameModel.getInstance().getSceneHeight() / 9.45);
        inputName.setScaleX(1.5);
        inputName.setScaleY(1.5);

        finish = new Text(GameModel.getInstance().getSceneWidth() / 7.91, GameModel.getInstance().getSceneHeight() / 3.88, "FINISH");
        finish.setStroke(Color.YELLOW);
        finish.setFill(Color.YELLOW);
        finish.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        finish.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (inputName.getText().equals("")){
                    return;
                }

                Main.saveOptions();
                Main.readConfig();

                if (inputName.getText().toString().length() > 8){
                    inputName.setText(inputName.getText().toString().substring(0,8));
                }
                GameModel.getInstance().setPlayerName(inputName.getText());
                Main.setNamePlayer(inputName.getText().toString());

                ArrayList<User> users = GameModel.getInstance().getAllUsers();
                int i = 0;
                for(i = 0 ; i < users.size(); i++){
                    if(users.get(i).getName().equals(GameModel.getInstance().getPlayerName())) {
                        GameModel.getInstance().setLastLevel((int) users.get(i).getLevel());
                        break;
                    }
                }

                if(i>= users.size()){
                    users.add(new User(inputName.getText(), 0, 0));
                    GameModel.getInstance().setLastLevel(0);
                }

                ((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).refreshValues();
                Main.readOptions();
                Main.readConfig();
                ((ViewConfigMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.viewConfigMenu.ordinal()]).setPars();
                Main.initAfterLoadConfig();

                GameModel.getInstance().getRoot().getChildren().remove(InputPlayerNameMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()]);
            }
        });

        formatMenuOption(finish);

        playerName.getChildren().addAll(meniBackground,inputName,finish);
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
        Event.fireEvent(finish, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                0, 0, 0, 0, MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    public TextField getInputName() {
        return inputName;
    }
}

