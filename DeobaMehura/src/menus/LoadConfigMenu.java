package menus;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import main.Main;
import models.GameModel;

import java.io.File;
import java.net.URISyntaxException;

public class LoadConfigMenu extends Menu {

    private Text mainMenu;

    private double textSize;

    // create a File chooser
    FileChooser fil_chooser = new FileChooser();



    private Text load, finish;
    private Text message;
    private Text uspesno;
    File file = null;

    private int indOption;

    private Text[] menuOptions = new Text[3];


    public LoadConfigMenu() {
        super(new Point2D(GameModel.getInstance().getSceneWidth() / 2 - GameModel.getInstance().getSceneWidth() / 4.86,
                GameModel.getInstance().getSceneHeight()/2 - GameModel.getInstance().getSceneHeight() / 3.8));

        Group rankListMenu = new Group();


        textSize =  ((GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/90);
        //xAsis =  (GameModel.getInstance().getSceneWidth() / 9.96);
        //yStart = GameModel.getInstance().getSceneHeight() / 10.8;

        //jsonObject = GameModel.getInstance().getJsonObject();


        Rectangle meniBackground = new Rectangle(GameModel.getInstance().getSceneWidth() / 2.3,
                GameModel.getInstance().getSceneHeight() / 3);
        meniBackground.setFill(Color.color(0, 0, 0, 0.5));
        meniBackground.setStroke(Color.rgb(244,196,48));
        meniBackground.setStrokeType(StrokeType.CENTERED);
        meniBackground.setStrokeWidth(10);

        message = new Text(GameModel.getInstance().getSceneWidth()/13.25, GameModel.getInstance().getSceneHeight()/15, "File Not Selected");
        message.setStroke(Color.RED);
        message.setFill(Color.RED);
        //rankNum.setStrokeWidth(2);
        message.setFont(new Font(textSize/1.5));
        message.setWrappingWidth(GameModel.getInstance().getSceneWidth()/3);

        load = new Text(GameModel.getInstance().getSceneWidth()/13.25, GameModel.getInstance().getSceneHeight()/6, "Load Config File");
        load.setStroke(Color.YELLOW);
        load.setFill(Color.YELLOW);
        //rankNum.setStrokeWidth(2);
        load.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        finish = new Text(GameModel.getInstance().getSceneWidth()/3.8, GameModel.getInstance().getSceneHeight()/6, "Done");
        finish.setStroke(Color.YELLOW);
        finish.setFill(Color.YELLOW);
        //rankNum.setStrokeWidth(2);
        finish.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        uspesno = new Text(GameModel.getInstance().getSceneWidth()/13.25, GameModel.getInstance().getSceneHeight()/4.5, "");
        uspesno.setStroke(Color.GREEN);
        uspesno.setFill(Color.GREEN);
        //rankNum.setStrokeWidth(2);
        uspesno.setFont(new Font(textSize/1.5));


        mainMenu = new Text(GameModel.getInstance().getSceneWidth()/6.25, GameModel.getInstance().getSceneHeight()/3.5, "START MENU");
        mainMenu.setStroke(Color.YELLOW);
        mainMenu.setFill(Color.YELLOW);
        //rankNum.setStrokeWidth(2);
        mainMenu.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameModel.getInstance().getRoot().getChildren().remove(LoadConfigMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()]);
            }
        });

        try {
            File dir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile();
            fil_chooser.setInitialDirectory(dir);
            fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // create an Event Handler
        EventHandler<MouseEvent> event =
                new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent e)
                    {

                        // get the file selected
                        file = fil_chooser.showOpenDialog(Main.primaryStage);


                        if (file != null) {

                            message.setText(file.getAbsolutePath());
                        }
                    }
                };

        load.setOnMouseClicked(event);

        finish.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(file != null) {
                    String rez = Main.loadConfig(file);
                    if(rez.equals("Successful")){
                        ((ViewConfigMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.viewConfigMenu.ordinal()]).setPars();
                        Main.initAfterLoadConfig();
                    }
                    uspesno.setText(rez);
                }
            }
        });



        menuOptions[indOption++] = load;
        menuOptions[indOption++] = finish;
        menuOptions[indOption] = mainMenu;

        for (int i = 0 ; i < menuOptions.length; i++){
            formatMenuOption(menuOptions[i]);
        }


        rankListMenu.getChildren().addAll(meniBackground, mainMenu, load, finish, message, uspesno);
        this.getChildren().addAll(rankListMenu);

        menuOptions[indOption].setFill(Color.GRAY);
    }

    protected void formatMenuOption(Text option) {
        option.setOnMouseEntered(e -> {
            menuOptions[indOption].setFill(Color.YELLOW);
            for (int i = 0; i < menuOptions.length; i++) {
                if (option == menuOptions[i]) {
                    indOption = i;
                    break;
                }
            }

            option.setFill(Color.GRAY);
        });

        option.setOnMouseExited(e -> {
            boolean inmenus = false;
            for (int i = 0; i < menuOptions.length; i++) {
                if (option == menuOptions[i]) {
                    inmenus = true;
                    break;
                }
            }

            if (!inmenus) {
                option.setFill(Color.YELLOW);
            }
            option.setScaleX(1);
            option.setScaleY(1);
        });

        option.setOnMousePressed(e -> {
            option.setScaleX(0.9);
            option.setScaleY(0.9);
            option.setFill(Color.GRAY);

            if (option != menuOptions[indOption]) {
                menuOptions[indOption].setFill(Color.YELLOW);
                for (int i = 0; i < menuOptions.length; i++) {
                    if (option == menuOptions[i]) {
                        indOption = i;
                        break;
                    }
                }
            }

        });

        option.setOnMouseReleased(e -> {
            option.setScaleX(1);
            option.setScaleY(1);
            //option.setFill(Color.BLACK);

            //menuOptions[indOption].setFill(Color.GRAY);

        });

    }

    public void changeOption(int pomeraj){
        menuOptions[indOption].setFill(Color.YELLOW);

        if (indOption == 0){
            if (pomeraj == -1){
                indOption = menuOptions.length - 1;
            }else{
                indOption++;
            }
        }else if (indOption == menuOptions.length - 1){
            if (pomeraj == 1){
                indOption = 0;
            }else{
                indOption--;
            }
        }else{
            indOption = indOption + pomeraj;
        }

        menuOptions[indOption].setFill(Color.GRAY);
    }


    public void clickOnOption(){
        Event.fireEvent(menuOptions[indOption], new MouseEvent(MouseEvent.MOUSE_CLICKED,
                0, 0, 0, 0, MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    public  void refreshValues(){
        message.setText("File Not Selected");
        uspesno.setText("");
        file = null;
    }
}
