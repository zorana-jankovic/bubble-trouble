package menus;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
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
import objects.Barrier;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PauseMenu extends Menu {
    private Text resume;
    private Text playAgain;
    private Text startMenu;

    private double textSize, xAsis, yStart, dy;

    private int indOption;

    private Text[] menuOptions = new Text[3];

    private Main main;

    public PauseMenu() {
        super(new Point2D(GameModel.getInstance().getSceneWidth() / 2 - GameModel.getInstance().getSceneWidth() / 7,
                GameModel.getInstance().getSceneHeight() / 2 - 150));

        Group mainMenu = new Group();

        textSize = ((GameModel.getInstance().getSceneWidth() + GameModel.getInstance().getSceneHeight()) / 60);
        xAsis = (GameModel.getInstance().getSceneWidth() / 12);
        yStart = GameModel.getInstance().getSceneHeight() / 10.0;
        dy = 0;

        int currOption = 0;
        indOption = 0;

        Rectangle meniBackground = new Rectangle(GameModel.getInstance().getSceneWidth() / 3.5,
                GameModel.getInstance().getSceneHeight() / 2.6);
        meniBackground.setFill(Color.color(0, 0, 0, 0.5));
        meniBackground.setStroke(Color.rgb(244, 196, 48));
        meniBackground.setStrokeType(StrokeType.CENTERED);
        meniBackground.setStrokeWidth(10);

        resume = new Text(xAsis + GameModel.getInstance().getSceneWidth()/53.76, yStart + dy, "Resume");
        resume.setStroke(Color.YELLOW);
        resume.setFill(Color.YELLOW);
        resume.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = resume;

        resume.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (GameModel.getInstance().getNumOfLifes() > 0) {
                    main.getTimer().start();
                    Main.getScaleTransition().play();
                    Main.getTranslateTransition().play();
                    GameModel.getInstance().getRoot().getChildren().remove(PauseMenu.this);
                    Main.primaryStage.setResizable(false);
                    for (int i = 0; i < GameModel.getInstance().getDolari().size(); i++){
                        GameModel.getInstance().getDolari().get(i).getFadeTransition().play();
                    }

                    for (int i = 0; i < GameModel.getInstance().getRoot().getChildren().size(); i++){
                        if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof Barrier) {
                            Barrier barrier = (Barrier)GameModel.getInstance().getRoot().getChildren().get(i);
                            if ((barrier.getScaleTransition().getStatus() == Animation.Status.PAUSED)) {
                                barrier.getScaleTransition().play();
                                barrier.getTranslateTransition().play();
                            }
                        }
                    }

                    FadeTransition fadeTransition = Main.getFadeTransition();
                    if (fadeTransition!=null && fadeTransition.getStatus()== Animation.Status.PAUSED){
                        fadeTransition.play();
                    }

                    ScaleTransition bunkerScaling = Main.getBunkerScaling();
                    if (bunkerScaling != null && bunkerScaling.getStatus() == Animation.Status.PAUSED) {
                        bunkerScaling.play();
                    }
                }
            }
        });

        dy += yStart;

        playAgain = new Text(xAsis, yStart + dy, "Play Again");
        playAgain.setStroke(Color.rgb(255, 239, 0));
        playAgain.setFill(Color.rgb(255, 239, 0));
        playAgain.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = playAgain;

        playAgain.setOnMouseClicked(new EventHandler<MouseEvent>() { // STA SA POENIMA???
            @Override
            public void handle(MouseEvent event) {
                GameModel.getInstance().setNumOfLifes((int)GameModel.getInstance().getJsonObject().getDouble("Num Of Lifes"));
                GameModel.getInstance().setPoeni(GameModel.getInstance().getCurrentPoints());
                main.createLevel();
                GameModel.getInstance().getRoot().getChildren().remove(PauseMenu.this);
                Main.primaryStage.setResizable(false);
            }
        });

        dy += yStart;

        startMenu = new Text(xAsis, yStart + dy, "Start Menu");
        startMenu.setStroke(Color.rgb(255, 239, 0));
        startMenu.setFill(Color.rgb(255, 239, 0));
        startMenu.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        menuOptions[currOption++] = startMenu;

        startMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameModel.getInstance().getRoot().getChildren().remove(PauseMenu.this);
                GameModel.getInstance().setNumOfLifes((int)GameModel.getInstance().getJsonObject().getDouble("Num Of Lifes"));
                GameModel.getInstance().setPoeni(0);
                GameModel.getInstance().setCurrentLevel(0);
                main.initialization();
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()]);
            }
        });



        for (int i = 0; i < menuOptions.length; i++) {
            formatMenuOption(menuOptions[i]);
        }

        mainMenu.getChildren().addAll(meniBackground, resume, playAgain, startMenu);
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

    public void setMain(Main main) {
        this.main = main;
    }
}