package menus;

import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Main;
import models.GameModel;
import models.User;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


public class MainMenu extends Menu {
    private Text startNewGame;
    private Text continueGame;
    private Text playerName;
    private Text rankList;
    private Text options;
    private Text commands;
    private Text manual;
    private Text about;
    private Text exit;

    private double textSize, xAsis, yStart, dy;

    private int indOption;

    private Text[] menuOptions = new Text[9];

    private Main main;

    public MainMenu() {
        super(new Point2D(GameModel.getInstance().getSceneWidth() / 2 - GameModel.getInstance().getSceneWidth() / 5.376,
                GameModel.getInstance().getSceneHeight()/2 - GameModel.getInstance().getSceneHeight() / 2.16));

        Group mainMenu = new Group();

        textSize =  ((GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/60);
        xAsis =  (GameModel.getInstance().getSceneWidth() / 10.2);
        yStart = GameModel.getInstance().getSceneHeight() / 10.8;
        dy = 0;

        int currOption = 0;
        indOption = 0;

        Rectangle meniBackground = new Rectangle(GameModel.getInstance().getSceneWidth() / 2.688,
                GameModel.getInstance().getSceneHeight() / 1.112);
        meniBackground.setFill(Color.color(0, 0, 0, 0.5));
        meniBackground.setStroke(Color.rgb(244,196,48));
        meniBackground.setStrokeType(StrokeType.CENTERED);
        meniBackground.setStrokeWidth(10);

        startNewGame = new Text(xAsis, yStart + dy, "Start New Game");
        startNewGame.setStroke(Color.YELLOW);
        startNewGame.setFill(Color.YELLOW);
        startNewGame.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = startNewGame;

        startNewGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameModel.getInstance().setCurrentLevel(0);
                GameModel.getInstance().setNumOfLifes((int)GameModel.getInstance().getJsonObject().getDouble("Num Of Lifes"));
                GameModel.getInstance().setPoeni(0);
                main.createLevel();
                GameModel.getInstance().getRoot().getChildren().remove(MainMenu.this);
                Main.primaryStage.setResizable(false);
                main.setCurrentMenu(null);
                GameModel.getInstance().setCurrentPoints(0);
            }
        });

        dy+=yStart;

        continueGame = new Text(xAsis, yStart + dy, "Continue Game  (" + (GameModel.getInstance().getLastLevel() + 1)+")");
        continueGame.setStroke(Color.rgb(255,239,0));
        continueGame.setFill(Color.rgb(255,239,0));
        continueGame.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = continueGame;

        continueGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //GameModel.getInstance().getRoot().getChildren().remove(MainMenu.this);
                //GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.pauseMenu.ordinal()]);
                GameModel.getInstance().setCurrentLevel(GameModel.getInstance().getLastLevel());
                GameModel.getInstance().setNumOfLifes((int)GameModel.getInstance().getJsonObject().getDouble("Num Of Lifes"));
                ArrayList<User> users = GameModel.getInstance().getAllUsers();
                int i = 0;

                main.createLevel();
                GameModel.getInstance().getRoot().getChildren().remove(MainMenu.this);
                Main.primaryStage.setResizable(false);
                main.setCurrentMenu(null);

                for(i = 0 ; i < users.size(); i++){
                    if(users.get(i).getName().equals(GameModel.getInstance().getPlayerName())) {
                        GameModel.getInstance().setPoeni((int) users.get(i).getScores());
                        break;
                    }
                }
                GameModel.getInstance().setCurrentPoints(GameModel.getInstance().getPoeni());

            }
        });

        dy+=yStart;

        playerName =  new Text(xAsis, yStart + dy, "Enter Player Name");
        playerName.setStroke(Color.rgb(255,239,0));
        playerName.setFill(Color.rgb(255,239,0));
        playerName.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = playerName;

        playerName.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Main.saveOptions();
                GameModel.getInstance().getRoot().getChildren().remove(MainMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.playerNameMenu.ordinal()]);
                main.setCurrentMenu(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.playerNameMenu.ordinal()]);
            }
        });

        dy+=yStart;

        rankList = new Text(xAsis, yStart + dy, "Rank list");
        rankList.setStroke(Color.YELLOW);
        rankList.setFill(Color.YELLOW);
        rankList.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = rankList;

        rankList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((RankListMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.rankListMenu.ordinal()]).setUsers();
                GameModel.getInstance().getRoot().getChildren().remove(MainMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.rankListMenu.ordinal()]);
                main.setCurrentMenu(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.rankListMenu.ordinal()]);
            }
        });

        dy+=yStart;

        options = new Text(xAsis, yStart + dy, "Options");
        options.setStroke(Color.YELLOW);
        options.setFill(Color.YELLOW);
        options.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = options;

        options.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).setMain(main);
                GameModel.getInstance().getRoot().getChildren().remove(MainMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]);
                main.setCurrentMenu(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]);
            }
        });

        dy+=yStart;

        commands = new Text(xAsis, yStart + dy, "Commands");
        commands.setStroke(Color.YELLOW);
        commands.setFill(Color.YELLOW);
        commands.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = commands;

        commands.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameModel.getInstance().getRoot().getChildren().remove(MainMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.commandsMenu.ordinal()]);
                main.setCurrentMenu(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.commandsMenu.ordinal()]);
            }
        });

        dy+=yStart;

        manual = new Text(xAsis, yStart + dy, "Manual");
        manual.setStroke(Color.YELLOW);
        manual.setFill(Color.YELLOW);
        manual.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = manual;

        manual.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    InputStream manualAsStream = getClass().getClassLoader().getResourceAsStream("resources/manual.pdf");
                    Path tempOutput = Files.createTempFile("manual", ".pdf");
                    tempOutput.toFile().deleteOnExit();

                    Files.copy(manualAsStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);

                    File file = new File(tempOutput.toFile().getPath());
                    if (file.exists()) {
                        Desktop.getDesktop().open(file);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        dy+=yStart;

        about = new Text(xAsis, yStart + dy, "About");
        about.setStroke(Color.YELLOW);
        about.setFill(Color.YELLOW);
        about.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = about;

        about.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameModel.getInstance().getRoot().getChildren().remove(MainMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.aboutMenu.ordinal()]);
                main.setCurrentMenu(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.aboutMenu.ordinal()]);
            }
        });

        dy+=yStart;

        exit = new Text(xAsis, yStart + dy, "Exit");
        exit.setStroke(Color.YELLOW);
        exit.setFill(Color.YELLOW);
        exit.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = exit;

        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Main.saveOptions();
                Main.savePlayerName();
                Main.sacuvajUserScoresLevels();
                Main.sacuvajRangListu();
                System.exit(0);
                main.setCurrentMenu(null);
            }
        });


        for (int i = 0 ; i < menuOptions.length; i++){
            formatMenuOption(menuOptions[i]);
        }

        mainMenu.getChildren().addAll(meniBackground,startNewGame,continueGame,playerName, rankList, options, commands, manual, about, exit);
        this.getChildren().addAll(mainMenu);

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

    public void changeContinueOptionLevel(){
        continueGame.setText( "Continue Game  (" + (GameModel.getInstance().getLastLevel() + 1) +")");
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
