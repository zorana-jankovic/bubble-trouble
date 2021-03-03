package menus;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Main;
import models.GameModel;

import java.net.URISyntaxException;

public class OptionsMenu extends Menu {

    private Text mainMenu;
    private double textSize;

    private Text backgroundMusic, soundsEffects;
    private Slider musicSlider, effectSlider;
    private CheckBox musicCheckBox, effectsCheckBox;


    private Text screenMode, fullScreen, windowed;

    private MediaPlayer backgroundMusicPlayer;

    private AudioClip harpunMusicPlayer;

    private AudioClip ballCrushMusicPlayer,  dolarMusicPlayer, bunkerMusicPlayer,
                            slowTimeMusicPlayer, watchMusicPlayer, winMusicPlayer, loseMusicPlayer, shieldMusicPlayer, comboMusicPlayer,
                                ballPlayerMusicPlayer, heartMusicPlayer;

    private Main main;


    public OptionsMenu() {
        super(new Point2D(GameModel.getInstance().getSceneWidth() / 2 - GameModel.getInstance().getSceneWidth() / 3,
                GameModel.getInstance().getSceneHeight() / 2 - GameModel.getInstance().getSceneHeight() / 3));

        Group playerName = new Group();

        textSize = ((GameModel.getInstance().getSceneWidth() + GameModel.getInstance().getSceneHeight()) / 60);


        Rectangle meniBackground = new Rectangle(GameModel.getInstance().getSceneWidth() / 1.5,
                GameModel.getInstance().getSceneHeight() / 1.5);
        meniBackground.setFill(Color.color(0, 0, 0, 0.5));
        meniBackground.setStroke(Color.rgb(244, 196, 48));
        meniBackground.setStrokeType(StrokeType.CENTERED);
        meniBackground.setStrokeWidth(10);


        backgroundMusic = new Text(GameModel.getInstance().getSceneWidth() / 20 - GameModel.getInstance().getSceneWidth()/44.8, GameModel.getInstance().getSceneHeight() / 8, "BACKGROUND MUSIC");
        backgroundMusic.setStroke(Color.YELLOW);
        backgroundMusic.setFill(Color.YELLOW);
        backgroundMusic.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        String uri = null;
        try {
            uri = Main.class.getResource("/resources/background2.mp3").toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Media hit = new Media(uri);
        backgroundMusicPlayer = new MediaPlayer(hit);
        backgroundMusicPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                backgroundMusicPlayer.seek(Duration.ZERO);
            }
        });

        //backgroundMusicPlayer.play();

        musicSlider = new Slider(0, 1, 0.25);
        musicSlider.setOrientation(Orientation.HORIZONTAL);
        musicSlider.setPrefWidth(GameModel.getInstance().getSceneWidth()/9.2);
        backgroundMusicPlayer.volumeProperty().bindBidirectional(musicSlider.valueProperty());

        musicSlider.setTranslateX(GameModel.getInstance().getSceneWidth()/1.92);
        musicSlider.setTranslateY(GameModel.getInstance().getSceneHeight()/10.08);

        musicSlider.setScaleX(1.3);
        musicSlider.setScaleY(1.1);


        musicCheckBox = new CheckBox("ON/OFF");
        musicCheckBox.setTextFill(Color.YELLOW);
        musicCheckBox.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        musicCheckBox.setTranslateX(GameModel.getInstance().getSceneWidth()/3.13);
        musicCheckBox.setTranslateY(GameModel.getInstance().getSceneHeight()/13.03);

        musicCheckBox.setScaleX(0.8);
        musicCheckBox.setScaleY(0.8);
        musicCheckBox.setSelected(true);

//        musicSlider.valueProperty().addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
//
//                if ((newValue.doubleValue() > 0)) {
//                    musicCheckBox.selectedProperty().setValue(true);
//                } else {
//                    musicCheckBox.selectedProperty().setValue(false);
//                }
//
//            }
//        });


        if (musicCheckBox.isSelected()){
            //musicSlider.setValue(0.5);
            backgroundMusicPlayer.play();
        }else{
            //musicSlider.setValue(0);
            backgroundMusicPlayer.pause();
        }

        EventHandler eh = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox chk = (CheckBox) event.getSource();
                    if ("ON/OFF".equals(chk.getText())) {
                        if(chk.isSelected()){
                            //musicSlider.setValue(0.5);
                            backgroundMusicPlayer.play();
                        }else{
                            //musicSlider.setValue(0);
                            backgroundMusicPlayer.pause();
                        }
                    }
                }
            }
        };

        musicCheckBox.setOnAction(eh);

        soundsEffects = new Text(GameModel.getInstance().getSceneWidth() / 20 - GameModel.getInstance().getSceneWidth()/44.8,
                GameModel.getInstance().getSceneHeight()/3.47, "SOUND EFFECTS");
        soundsEffects.setStroke(Color.YELLOW);
        soundsEffects.setFill(Color.YELLOW);
        soundsEffects.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        String uri2 = null;
        try {
            uri2 = Main.class.getResource("/resources/harpun1.wav").toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

//        Media hit2 = new Media(uri2);
//        harpunMusicPlayer = new MediaPlayer(hit2);

        harpunMusicPlayer = new AudioClip(uri2);


        effectSlider = new Slider(0, 1, 0.25);
        effectSlider.setOrientation(Orientation.HORIZONTAL);
        effectSlider.setPrefWidth(GameModel.getInstance().getSceneWidth()/9.2);

        harpunMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());

        effectSlider.setTranslateX(GameModel.getInstance().getSceneWidth()/1.92);
        effectSlider.setTranslateY(GameModel.getInstance().getSceneHeight()/3.78);

        effectSlider.setScaleX(1.3);
        effectSlider.setScaleY(1.1);

        effectsCheckBox = new CheckBox("ON/OFF");
        effectsCheckBox.setTextFill(Color.YELLOW);
        effectsCheckBox.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        effectsCheckBox.setTranslateX(GameModel.getInstance().getSceneWidth()/3.13);
        effectsCheckBox.setTranslateY(GameModel.getInstance().getSceneHeight()/4.13);

        effectsCheckBox.setScaleX(0.8);
        effectsCheckBox.setScaleY(0.8);

        effectsCheckBox.setSelected(true);

//        if (effectsCheckBox.isSelected()){
//            effectSlider.setValue(0.5);
//        }else{
//            effectSlider.setValue(0);
//        }
//
        EventHandler eh2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox chk = (CheckBox) event.getSource();
                    if ("ON/OFF".equals(chk.getText())) {
                        if(chk.isSelected()){
                            //harpunMusicPlayer.seek(Duration.ZERO);
                            harpunMusicPlayer.play();
                            //System.out.println("played");
                        }else{
                            //effectSlider.setValue(0);
                        }
                    }
                }
            }
        };

        effectsCheckBox.setOnAction(eh2);

//        effectSlider.valueProperty().addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
//
//                if ((newValue.doubleValue() > 0)) {
//                    effectsCheckBox.selectedProperty().setValue(true);
//                } else {
//                    effectsCheckBox.selectedProperty().setValue(false);
//                }
//            }
//        });


        effectSlider.setOnMouseReleased(e -> {
            //harpunMusicPlayer.seek(Duration.ZERO);
            if (effectsCheckBox.isSelected()) {
                harpunMusicPlayer.play();
            }
        });

        screenMode = new Text(GameModel.getInstance().getSceneWidth() / 20 - GameModel.getInstance().getSceneWidth() / 44.8,
                GameModel.getInstance().getSceneHeight()/2.23, "SCREEN MODE             ");
        screenMode.setStroke(Color.YELLOW);
        screenMode.setFill(Color.YELLOW);
        screenMode.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        fullScreen = new Text(GameModel.getInstance().getSceneWidth() / 20 + GameModel.getInstance().getSceneWidth()/3.73,
                GameModel.getInstance().getSceneHeight()/2.23, "FULL SCREEN");
        fullScreen.setStroke(Color.YELLOW);
        fullScreen.setFill(Color.YELLOW);
        fullScreen.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        fullScreen.setOnMouseClicked(e -> {
            Main.primaryStage.setFullScreen(true);
            main.resize();
            colorOption();
        });

        windowed = new Text(GameModel.getInstance().getSceneWidth() / 20 + GameModel.getInstance().getSceneWidth()/2.17,
                GameModel.getInstance().getSceneHeight()/2.23, "WINDOWED");
        windowed.setStroke(Color.YELLOW);
        windowed.setFill(Color.YELLOW);
        windowed.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        windowed.setOnMouseClicked(e -> {
            Main.primaryStage.setFullScreen(false);
            main.resize();
            colorOption();
        });


        mainMenu = new Text(GameModel.getInstance().getSceneWidth() / 7, GameModel.getInstance().getSceneHeight() / 1.68, "START MENU");
        mainMenu.setStroke(Color.YELLOW);
        mainMenu.setFill(Color.YELLOW);
        mainMenu.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Main.saveOptions();
                GameModel.getInstance().getRoot().getChildren().remove(OptionsMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()]);
            }
        });

        formatMenuOption(mainMenu);
        formatMenuOption(fullScreen);
        formatMenuOption(windowed);

        playerName.getChildren().addAll(meniBackground, mainMenu, backgroundMusic, musicSlider, musicCheckBox, soundsEffects, effectSlider, effectsCheckBox,
                screenMode, fullScreen, windowed);
        this.getChildren().addAll(playerName);

        colorOption();
        makePlayers();
    }

    public void colorOption() {
        if(Main.primaryStage.isFullScreen()){
            fullScreen.setFill(Color.RED);
            windowed.setFill(Color.YELLOW);
        }else{
            windowed.setFill(Color.RED);
            fullScreen.setFill(Color.YELLOW);

        }
    }

    protected void formatMenuOption(Text option) {
        if(option == mainMenu) {
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
        }else{

            option.setOnMouseExited(e -> {
                option.setScaleX(1);
                option.setScaleY(1);
            });

            option.setOnMousePressed(e -> {
                option.setScaleX(0.9);
                option.setScaleY(0.9);
            });

            option.setOnMouseReleased(e -> {
                option.setScaleX(1);
                option.setScaleY(1);
            });
        }

    }

    public Slider getMusicSlider() {
        return musicSlider;
    }

    public void setMusicSlider(Slider musicSlider) {
        this.musicSlider = musicSlider;
    }

    public Slider getEffectSlider() {
        return effectSlider;
    }

    public void setEffectSlider(Slider effectSlider) {
        this.effectSlider = effectSlider;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public AudioClip createNewPlayer(String s){
        try {
            String uri2 = Main.class.getResource("/resources/" + s).toURI().toString();
            //Media hit2 = new Media(uri2);
            AudioClip mediaPlayer = new AudioClip(uri2);
            return mediaPlayer;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

      return null;
    }

    public void makePlayers(){
        ballCrushMusicPlayer = createNewPlayer("ball_crash2.wav");
        ballCrushMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());

        dolarMusicPlayer = createNewPlayer("dollar.wav");
        dolarMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());

        bunkerMusicPlayer = createNewPlayer("bunker.wav");
        bunkerMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());

        slowTimeMusicPlayer = createNewPlayer("slowdown.wav");
        slowTimeMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());

        watchMusicPlayer = createNewPlayer("time1.wav");
        watchMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());

        winMusicPlayer = createNewPlayer("win.wav");
        winMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());

        loseMusicPlayer = createNewPlayer("lose.wav");
        loseMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());


        shieldMusicPlayer = createNewPlayer("shield.wav");
        shieldMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());

        comboMusicPlayer = createNewPlayer("combo.wav");
        comboMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());

        ballPlayerMusicPlayer = createNewPlayer("ballplayer.wav");
        ballPlayerMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());

        heartMusicPlayer= createNewPlayer("srce.wav");
        heartMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());
    }

    public AudioClip getHarpunMusicPlayer() {
        return harpunMusicPlayer;
    }

    public AudioClip getBallCrushMusicPlayer() {
        return ballCrushMusicPlayer;
    }

    public AudioClip getDolarMusicPlayer() {
        return dolarMusicPlayer;
    }

    public AudioClip getBunkerMusicPlayer() {
        return bunkerMusicPlayer;
    }

    public AudioClip getSlowTimeMusicPlayer() {
        return slowTimeMusicPlayer;
    }

    public AudioClip getWatchMusicPlayer() {
        return watchMusicPlayer;
    }

    public AudioClip getWinMusicPlayer() {
        return winMusicPlayer;
    }

    public AudioClip getLoseMusicPlayer() {
        return loseMusicPlayer;
    }

    public AudioClip getShieldMusicPlayer() {
        return shieldMusicPlayer;
    }

    public AudioClip getComboMusicPlayer() {
        return comboMusicPlayer;
    }

    public AudioClip getBallPlayerMusicPlayer() {
        return ballPlayerMusicPlayer;
    }

    public CheckBox getMusicCheckBox() {
        return musicCheckBox;
    }

    public CheckBox getEffectsCheckBox() {
        return effectsCheckBox;
    }


    public AudioClip getHeartMusicPlayer() {
        return heartMusicPlayer;
    }

    public MediaPlayer getBackgroundMusicPlayer() {
        return backgroundMusicPlayer;
    }

    public void clickOnOption(){
        Event.fireEvent(mainMenu, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                0, 0, 0, 0, MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    public void refreshValues(){
        this.getChildren().clear();

        Group playerName = new Group();

        textSize = ((GameModel.getInstance().getSceneWidth() + GameModel.getInstance().getSceneHeight()) / 60);


        Rectangle meniBackground = new Rectangle(GameModel.getInstance().getSceneWidth() / 1.5,
                GameModel.getInstance().getSceneHeight() / 1.5);
        meniBackground.setFill(Color.color(0, 0, 0, 0.5));
        meniBackground.setStroke(Color.rgb(244, 196, 48));
        meniBackground.setStrokeType(StrokeType.CENTERED);
        meniBackground.setStrokeWidth(10);


        backgroundMusic = new Text(GameModel.getInstance().getSceneWidth() / 20 - GameModel.getInstance().getSceneWidth()/44.8, GameModel.getInstance().getSceneHeight() / 8, "BACKGROUND MUSIC");
        backgroundMusic.setStroke(Color.YELLOW);
        backgroundMusic.setFill(Color.YELLOW);
        backgroundMusic.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        String uri = null;
        try {
            uri = Main.class.getResource("/resources/background2.mp3").toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Media hit = new Media(uri);
        backgroundMusicPlayer.stop();
        backgroundMusicPlayer = new MediaPlayer(hit);
        backgroundMusicPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                backgroundMusicPlayer.seek(Duration.ZERO);
            }
        });

        //backgroundMusicPlayer.play();

        musicSlider = new Slider(0, 1, 0.25);
        musicSlider.setOrientation(Orientation.HORIZONTAL);
        musicSlider.setPrefWidth(GameModel.getInstance().getSceneWidth()/9.2);
        backgroundMusicPlayer.volumeProperty().bindBidirectional(musicSlider.valueProperty());

        musicSlider.setTranslateX(GameModel.getInstance().getSceneWidth()/1.92);
        musicSlider.setTranslateY(GameModel.getInstance().getSceneHeight()/10.08);

        musicSlider.setScaleX(1.3);
        musicSlider.setScaleY(1.1);


        musicCheckBox = new CheckBox("ON/OFF");
        musicCheckBox.setTextFill(Color.YELLOW);
        musicCheckBox.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        musicCheckBox.setTranslateX(GameModel.getInstance().getSceneWidth()/3.13);
        musicCheckBox.setTranslateY(GameModel.getInstance().getSceneHeight()/13.03);

        musicCheckBox.setScaleX(0.8);
        musicCheckBox.setScaleY(0.8);
        musicCheckBox.setSelected(true);

//        musicSlider.valueProperty().addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
//
//                if ((newValue.doubleValue() > 0)) {
//                    musicCheckBox.selectedProperty().setValue(true);
//                } else {
//                    musicCheckBox.selectedProperty().setValue(false);
//                }
//
//            }
//        });


        if (musicCheckBox.isSelected()){
            //musicSlider.setValue(0.5);
            backgroundMusicPlayer.play();
        }else{
            //musicSlider.setValue(0);
            backgroundMusicPlayer.pause();
        }

        EventHandler eh = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox chk = (CheckBox) event.getSource();
                    if ("ON/OFF".equals(chk.getText())) {
                        if(chk.isSelected()){
                            //musicSlider.setValue(0.5);
                            backgroundMusicPlayer.play();
                        }else{
                            //musicSlider.setValue(0);
                            backgroundMusicPlayer.pause();
                        }
                    }
                }
            }
        };

        musicCheckBox.setOnAction(eh);

        soundsEffects = new Text(GameModel.getInstance().getSceneWidth() / 20 - GameModel.getInstance().getSceneWidth()/44.8,
                GameModel.getInstance().getSceneHeight()/3.47, "SOUND EFFECTS");
        soundsEffects.setStroke(Color.YELLOW);
        soundsEffects.setFill(Color.YELLOW);
        soundsEffects.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        String uri2 = null;
        try {
            uri2 = Main.class.getResource("/resources/harpun1.wav").toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

//        Media hit2 = new Media(uri2);
//        harpunMusicPlayer = new MediaPlayer(hit2);

        harpunMusicPlayer = new AudioClip(uri2);


        effectSlider = new Slider(0, 1, 0.25);
        effectSlider.setOrientation(Orientation.HORIZONTAL);
        effectSlider.setPrefWidth(GameModel.getInstance().getSceneWidth()/9.2);

        harpunMusicPlayer.volumeProperty().bindBidirectional(effectSlider.valueProperty());

        effectSlider.setTranslateX(GameModel.getInstance().getSceneWidth()/1.92);
        effectSlider.setTranslateY(GameModel.getInstance().getSceneHeight()/3.78);

        effectSlider.setScaleX(1.3);
        effectSlider.setScaleY(1.1);

        effectsCheckBox = new CheckBox("ON/OFF");
        effectsCheckBox.setTextFill(Color.YELLOW);
        effectsCheckBox.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));
        effectsCheckBox.setTranslateX(GameModel.getInstance().getSceneWidth()/3.13);
        effectsCheckBox.setTranslateY(GameModel.getInstance().getSceneHeight()/4.13);

        effectsCheckBox.setScaleX(0.8);
        effectsCheckBox.setScaleY(0.8);

        effectsCheckBox.setSelected(true);

//        if (effectsCheckBox.isSelected()){
//            effectSlider.setValue(0.5);
//        }else{
//            effectSlider.setValue(0);
//        }
//
        EventHandler eh2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox chk = (CheckBox) event.getSource();
                    if ("ON/OFF".equals(chk.getText())) {
                        if(chk.isSelected()){
                            //harpunMusicPlayer.seek(Duration.ZERO);
                            harpunMusicPlayer.play();
                            //System.out.println("played");
                        }else{
                            //effectSlider.setValue(0);
                        }
                    }
                }
            }
        };

        effectsCheckBox.setOnAction(eh2);

//        effectSlider.valueProperty().addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
//
//                if ((newValue.doubleValue() > 0)) {
//                    effectsCheckBox.selectedProperty().setValue(true);
//                } else {
//                    effectsCheckBox.selectedProperty().setValue(false);
//                }
//            }
//        });


        effectSlider.setOnMouseReleased(e -> {
            //harpunMusicPlayer.seek(Duration.ZERO);
            if (effectsCheckBox.isSelected()) {
                harpunMusicPlayer.play();
            }
        });

        screenMode = new Text(GameModel.getInstance().getSceneWidth() / 20 - GameModel.getInstance().getSceneWidth() / 44.8,
                GameModel.getInstance().getSceneHeight()/2.23, "SCREEN MODE             ");
        screenMode.setStroke(Color.YELLOW);
        screenMode.setFill(Color.YELLOW);
        screenMode.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        fullScreen = new Text(GameModel.getInstance().getSceneWidth() / 20 + GameModel.getInstance().getSceneWidth()/3.73,
                GameModel.getInstance().getSceneHeight()/2.23, "FULL SCREEN");
        fullScreen.setStroke(Color.YELLOW);
        fullScreen.setFill(Color.YELLOW);
        fullScreen.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        fullScreen.setOnMouseClicked(e -> {
            Main.primaryStage.setFullScreen(true);
            main.resize();
            colorOption();
        });

        windowed = new Text(GameModel.getInstance().getSceneWidth() / 20 + GameModel.getInstance().getSceneWidth()/2.17,
                GameModel.getInstance().getSceneHeight()/2.23, "WINDOWED");
        windowed.setStroke(Color.YELLOW);
        windowed.setFill(Color.YELLOW);
        windowed.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        windowed.setOnMouseClicked(e -> {
            Main.primaryStage.setFullScreen(false);
            main.resize();
            colorOption();
        });


        mainMenu = new Text(GameModel.getInstance().getSceneWidth() / 7, GameModel.getInstance().getSceneHeight() / 1.68, "START MENU");
        mainMenu.setStroke(Color.YELLOW);
        mainMenu.setFill(Color.YELLOW);
        mainMenu.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Main.saveOptions();
                GameModel.getInstance().getRoot().getChildren().remove(OptionsMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()]);
            }
        });

        formatMenuOption(mainMenu);
        formatMenuOption(fullScreen);
        formatMenuOption(windowed);

        playerName.getChildren().addAll(meniBackground, mainMenu, backgroundMusic, musicSlider, musicCheckBox, soundsEffects, effectSlider, effectsCheckBox,
                screenMode, fullScreen, windowed);
        this.getChildren().addAll(playerName);

        Main.primaryStage.setFullScreen(false);
        Main.primaryStage.setX(GameModel.getInstance().getSceneX());
        Main.primaryStage.setY(GameModel.getInstance().getSceneY());
        Main.primaryStage.setWidth(GameModel.getInstance().getStageWidth());
        Main.primaryStage.setHeight(GameModel.getInstance().getStageHeight());
        colorOption();
        makePlayers();
    }
}
