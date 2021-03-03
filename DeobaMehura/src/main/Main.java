package main;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;
import jdk.nashorn.internal.parser.JSONParser;
import menus.*;
import models.GameModel;
import models.User;
import objects.*;
import objects.weapons.Bullet;
import objects.weapons.BunkerBullet;
import org.json.*;

import playerStates.MovingLeftState;
import playerStates.MovingRightState;




import java.io.*;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static objects.Player.PLAYER_HEIGHT;
import static objects.Player.PLAYER_WIDTH;

public class Main extends Application {

	private static Group okolina;
	private Group  root;
	private Group time;
	
	private AnimationTimer timer;

	private long startTime, endTime;
	private static long cnt;
	private static Text prikazVremena;
	private static Rectangle vreme;
	private Text poruka;
	private Text poeniMsg;
	//private double ind = 0.3;
	private static Text name;
	private static Text level;

	private static ScaleTransition scaleTransition;
	private static TranslateTransition translateTransition;

	private static Life[] zivoti ;
	//private int br = 4;
	private static Group ziv;


	private static Player player;

	private int zasticenCnt = 0;

	private int svetlucka = 0;


	private static FadeTransition fadeTransition = null;

	public static long protokVremena;
	public static int imamBunker;
	public static Bunker bunker;
	public static long bunkerCounter;

	private Bunker bunkerIcon;
	private static ScaleTransition bunkerScaling;
	private int prviPutBunker;

	private Scene scene;
	private double ratio;
	private double initHeight;
	private double initWidth;
	public static Stage primaryStage;

	private Menu currentMenu;


	@Override
	public void start ( Stage primaryStage ){
		okolina = new Group();
		root = new Group ( );
		this.primaryStage = primaryStage;



		name = new Text(GameModel.getInstance().getSceneWidth() / 16.8 - GameModel.getInstance().getSceneWidth() / 26.88,
				GameModel.getInstance().getSceneHeight() / 13.745, GameModel.getInstance().getPlayerName());
		name.setFill(Color.RED);
//		name.setFont((Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(),
//				(GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/45.65)));
		name.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, (GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/60));
		name.setStrokeWidth(5);

		readPlayerName();

		if (GameModel.getInstance().getPlayerName().equals("")){
			GameModel.getInstance().getAllUsers().add(new User("Player1", 0, 0));
			GameModel.getInstance().setPlayerName("Player1");
			name.setText("Player1");
			((InputPlayerNameMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.playerNameMenu.ordinal()]).
					getInputName().setText(GameModel.getInstance().getPlayerName());
		}

		readConfig();

		((ViewConfigMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.viewConfigMenu.ordinal()]).setPars();

		primaryStage.setTitle ( "Deoba mehura" );
		Scene scene = new Scene ( okolina, GameModel.getInstance ( ).getSceneWidth ( ) + GameModel.getInstance().getSceneOffsetWidth(), GameModel.getInstance ( ).getSceneHeight ( ) + GameModel.getInstance().getSceneOffsetHeight() );
		primaryStage.setScene ( scene );
		
		// disable resizing and maximize button
		if (!primaryStage.isFullScreen()) {
			primaryStage.setResizable(true);
		}

		primaryStage.sizeToScene ( );

//		Ball ball = new Ball( new Point2D ( GameModel.getInstance().getSceneWidth() / 13.44, GameModel.getInstance().getSceneWidth() / 7.56 ) );
//		GameModel.getInstance ( ).getBalls ( ).add ( ball );
		
//		player = new Player ( new Point2D ( GameModel.getInstance().getSceneWidth() / 13.44, GameModel.getInstance ( ).getSceneHeight ( ) - PLAYER_HEIGHT ) );
		player = new Player ( new Point2D (
				GameModel.getInstance().getSceneWidth() / 13.44, GameModel.getInstance ( ).getSceneHeight ( ) - PLAYER_HEIGHT ) );
		GameModel.getInstance ( ).setPlayer ( player );

		protokVremena = 1000;
		imamBunker = 0;

		time = new Group();

		vreme = new Rectangle(GameModel.getInstance().getSceneOffsetWidth()/2,
				(int) (GameModel.getInstance().getSceneOffsetHeight()/2 + GameModel.getInstance().getSceneHeight() +
						GameModel.getInstance().getSceneOffsetHeight()/8 ),
				(int)(GameModel.getInstance().getSceneWidth()),GameModel.getInstance().getSceneOffsetHeight()/4);
		vreme.setFill(Color.RED);
		vreme.setStroke(Color.RED);

		scaleTransition = new ScaleTransition(Duration.seconds((long)GameModel.getInstance().getJsonObject().getDouble("Time")),vreme);
		scaleTransition.setFromX(1);
		scaleTransition.setToX(0);

		translateTransition = new TranslateTransition(Duration.seconds((long)GameModel.getInstance().getJsonObject().getDouble("Time")),vreme);
		translateTransition.setByX(-GameModel.getInstance().getSceneWidth()/2);


		cnt = (long)GameModel.getInstance().getJsonObject().getDouble("Time");
		prikazVremena = new Text(GameModel.getInstance().getSceneOffsetWidth()/2+(GameModel.getInstance().getSceneWidth()/2) - GameModel.getInstance().getSceneOffsetWidth()/8,GameModel.getInstance().getSceneOffsetHeight()/2 + GameModel.getInstance().getSceneHeight() + GameModel.getInstance().getSceneOffsetHeight()/3.03,"" + cnt);
		prikazVremena.setFill(Color.BLUE);
		prikazVremena.setFont(new Font(
				(GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/45.65)); //razmisli
		prikazVremena.setStrokeWidth(3);

		time.getChildren().addAll(vreme,prikazVremena);

		poruka = new Text(GameModel.getInstance().getSceneWidth()*0.5 , GameModel.getInstance().getSceneHeight()*0.5+GameModel.getInstance().getSceneHeight() / 75.6, "");
		poruka.setFill(Color.RED);
		poruka.setFont(Font.loadFont(Main.class.getResource("/resources/CracklingFire.ttf").toExternalForm(),
				(GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight()) / 21));
		poruka.setStrokeWidth(5);


		int poeni = 0 ;

		poeniMsg = new Text((GameModel.getInstance().getSceneWidth() + GameModel.getInstance().getSceneWidth() / 5.6)*0.8 +
				GameModel.getInstance().getSceneWidth()/89.6, GameModel.getInstance().getSceneHeight() / 13.745, "Score:  " + poeni);
		poeniMsg.setFill(Color.RED);
		poeniMsg.setFont((Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(),
				(GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/45.65))); // razmisli
		poeniMsg.setStrokeWidth(5);

		root.getChildren ( ).addAll ( new Background( ), player , poruka);


		GameModel.getInstance ( ).setRoot ( root );

		root.setTranslateX(GameModel.getInstance().getSceneOffsetWidth()/2);
		root.setTranslateY(GameModel.getInstance().getSceneOffsetHeight()/2);



		GameModel.getInstance().setNumOfLifes((int)GameModel.getInstance().getJsonObject().getDouble("Num Of Lifes"));



		ziv = new Group();

		zivoti = new Life[GameModel.getInstance().getNumOfLifes()];


		for (int i = 0; i<GameModel.getInstance().getNumOfLifes();i++){
			zivoti[i] = new Life(new Point2D((int)(GameModel.getInstance().getSceneWidth() / 5.65 +
					GameModel.getInstance().getSceneWidth() / 16.8 + GameModel.getInstance().getSceneWidth() / 26.88 * (i)),
					GameModel.getInstance().getSceneHeight() / 13.745));
			ziv.getChildren().add(zivoti[i]);
		}


		level = new Text(GameModel.getInstance().getSceneWidth() / 2 + GameModel.getInstance().getSceneWidth() / 44.8, GameModel.getInstance().getSceneHeight() / 13.745,
				"LEVEL : " + (GameModel.getInstance().getCurrentLevel() + 1));
		level.setFill(Color.RED);
		level.setFont((Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(),
				(GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/45.65)));
		level.setStrokeWidth(5);



		//ziv.getChildren().addAll(zivoti);


//		bunkerIcon = makeBunkerIcon();

		okolina.getChildren().addAll(new BackgroundEnviroment(),root,poeniMsg,time,ziv,name,level);

		((PauseMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.pauseMenu.ordinal()]).setMain(Main.this);

		scene.setOnKeyPressed ( event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case SPACE:
                	if (translateTransition.getStatus() == Animation.Status.RUNNING) {
						Bullet bullet = new Bullet(player.getPosition().add(0.1 * PLAYER_WIDTH, -2.3 * PLAYER_HEIGHT));
						root.getChildren().remove(GameModel.getInstance().getWeapon());
						GameModel.getInstance().setWeapon(bullet);
						root.getChildren().addAll(bullet);
						if (((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
							//((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getHarpunMusicPlayer().seek(Duration.ZERO);
							((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getHarpunMusicPlayer().play();
						}
					}
                    break;

				case ESCAPE:
                    if (scaleTransition.getStatus() == Animation.Status.RUNNING) { //  u igri sam
                        timer.stop ( );
                        scaleTransition.pause();
                        translateTransition.pause();
                        for (int i = 0; i < GameModel.getInstance().getDolari().size(); i++){
							GameModel.getInstance().getDolari().get(i).getFadeTransition().pause();
						}

						for (int i = 0; i < GameModel.getInstance().getRoot().getChildren().size(); i++){
							if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof Barrier) {
								Barrier barrier = (Barrier)GameModel.getInstance().getRoot().getChildren().get(i);
								if (barrier.getScaleTransition().getStatus() == Animation.Status.RUNNING) {
									barrier.getScaleTransition().pause();
									barrier.getTranslateTransition().pause();
								}
							}
						}
						if (fadeTransition!=null && fadeTransition.getStatus()== Animation.Status.RUNNING){
							fadeTransition.pause();
						}
						if (bunkerScaling != null && bunkerScaling.getStatus() == Animation.Status.RUNNING) {
							bunkerScaling.pause();
						}

                        GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.pauseMenu.ordinal()]);
                        if (!primaryStage.isFullScreen()) {
							primaryStage.setResizable(true);
						}
                    }
                    break;

                case UP:
                	for(int i = 0; i < GameModel.getInstance().getRoot().getChildren().size(); i++){
                		if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  MainMenu){
							((MainMenu)GameModel.getInstance().getRoot().getChildren().get(i)).changeOption(-1);
							break;
						}

						if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  PauseMenu){
							((PauseMenu)GameModel.getInstance().getRoot().getChildren().get(i)).changeOption(-1);
							break;
						}
					}
                    break;

                case DOWN:
				case TAB:
					for(int i = 0; i < GameModel.getInstance().getRoot().getChildren().size(); i++){
						if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  MainMenu){
							((MainMenu)GameModel.getInstance().getRoot().getChildren().get(i)).changeOption(+1);
							break;
						}

						if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  PauseMenu){
							((PauseMenu)GameModel.getInstance().getRoot().getChildren().get(i)).changeOption(+1);
							break;
						}

						if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  LoadConfigMenu){
							((LoadConfigMenu)GameModel.getInstance().getRoot().getChildren().get(i)).changeOption(+1);
							break;
						}
					}
                    break;

                case ENTER:
					for(int i = 0; i < GameModel.getInstance().getRoot().getChildren().size(); i++){
//						if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  MainMenu){
//							((MainMenu)GameModel.getInstance().getRoot().getChildren().get(i)).clickOnOption();
//							break;
//						}
//
//						if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  PauseMenu){
//							((PauseMenu)GameModel.getInstance().getRoot().getChildren().get(i)).clickOnOption();
//							break;
//						}

						if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  Menu){
							((Menu) GameModel.getInstance().getRoot().getChildren().get(i)).clickOnOption();
							break;
						}
					}
                    break;

				case F1:
					if (event.isShiftDown() && event.isControlDown()){ // prikazi config
						for(int i = 0; i < GameModel.getInstance().getRoot().getChildren().size(); i++){
							if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  MainMenu){

								GameModel.getInstance().getRoot().getChildren().remove((MainMenu) GameModel.getInstance().getRoot().getChildren().get(i));
								GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.viewConfigMenu.ordinal()]);
								setCurrentMenu(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.viewConfigMenu.ordinal()]);
								break;

							}

//							if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  PauseMenu){
//								((PauseMenu)GameModel.getInstance().getRoot().getChildren().get(i)).changeOption(-1);
//								break;
//							}
						}
					}
					break;
				case F3:
					if (event.isAltDown() && event.isControlDown()){ // predji nivo
						if(scaleTransition.getStatus()== Animation.Status.RUNNING){
							GameModel.getInstance().setCurrentPoints(GameModel.getInstance().getPoeni());
							poruka.setText("YOU WON!");
							poruka.setFill(Color.GREEN);
							timer.stop ( );
							scaleTransition.stop();
							translateTransition.stop();
							GameModel.getInstance().setCurrentLevel(GameModel.getInstance().getCurrentLevel() + 1);
							if (GameModel.getInstance().getCurrentLevel() > GameModel.getInstance().getLastLevel()){
								GameModel.getInstance().setLastLevel(GameModel.getInstance().getCurrentLevel());
								presaoNivo();
							}
//							if (GameModel.getInstance().getCurrentLevel() == GameModel.getInstance().getBallsPerLevels().length){
//								// pobedio si
//								GameModel.getInstance().addUser(new User(GameModel.getInstance().getPlayerName(), GameModel.getInstance().getPoeni()));
//								((RankListMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.rankListMenu.ordinal()]).setUsers();
//								if (!primaryStage.isFullScreen()) {
//									primaryStage.setResizable(true);
//								}
//
//								GameModel.getInstance().getRoot().getChildren().remove(poruka);
//								ScaleTransition porukaTrans = new ScaleTransition(Duration.seconds(1.5), poruka);
//								porukaTrans.setFromX(1);
//								porukaTrans.setToX(1.5);
//								porukaTrans.setFromY(1);
//								porukaTrans.setToY(1.5);
//
//
//								ScaleTransition porukaTrans2 = new ScaleTransition(Duration.seconds(1.5), poruka);
//								porukaTrans2.setFromX(1.5);
//								porukaTrans2.setToX(1);
//								porukaTrans2.setFromY(1.5);
//								porukaTrans2.setToY(1);
//
//
//								GameModel.getInstance().getRoot().getChildren().add(poruka);
//
//								porukaTrans.play();
//
//								porukaTrans.setOnFinished(new EventHandler<ActionEvent>() {
//									@Override
//									public void handle(ActionEvent event) {
//										porukaTrans2.play();
//									}
//								});
//
//								porukaTrans2.setOnFinished(new EventHandler<ActionEvent>() {
//									@Override
//									public void handle(ActionEvent event) {
//										poruka.setText("");
//										GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()
//												[GameModel.mappingMenuNames.rankListMenu.ordinal()]);
//									}
//								});
//
//								((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getWinMusicPlayer().seek(Duration.ZERO);
//								((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getWinMusicPlayer().play();
//
//							}else
							{
								//if (GameModel.getInstance().getNumOfLifes()< (int)GameModel.getInstance().getJsonObject().getDouble("Num Of Lifes")) {
									GameModel.getInstance().setNumOfLifes((int) GameModel.getInstance().getJsonObject().getDouble("Num Of Lifes"));
								//}
								createLevel();
							}
						}
					}
					break;
				case F5:
					if (event.isShiftDown() && event.isControlDown()){ // ucitaj config
						for(int i = 0; i < GameModel.getInstance().getRoot().getChildren().size(); i++){
							if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  MainMenu){

								((LoadConfigMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.loadConfigMenu.ordinal()]).refreshValues();
								GameModel.getInstance().getRoot().getChildren().remove((MainMenu) GameModel.getInstance().getRoot().getChildren().get(i));
								GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.loadConfigMenu.ordinal()]);
								setCurrentMenu(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.loadConfigMenu.ordinal()]);
								break;

							}
						}
					}
					break;
            }

		} );
		primaryStage.show ( );


		startTime = System.currentTimeMillis();

//		scaleTransition.play();
//		translateTransition.play();


		prviPutBunker = 0;

		root.addEventFilter ( KeyEvent.KEY_PRESSED, event -> {
			switch ( event.getCode ( ) ) {
				case X:
					if (imamBunker == 1){
						imamBunker = 0;
						bunker = new Bunker(new Point2D(player.getPosition().getX(), GameModel.getInstance().getSceneHeight()));
						bunkerCounter = (int) GameModel.getInstance().getJsonObject().getDouble("Bunker Duration");
						root.getChildren ( ).addAll (bunker);
						deleteBunkerIcon();
						prviPutBunker = 0;
					}
					break;
			}
		});

		//okolina.scaleXProperty().bind( scene.widthProperty().divide( GameModel.getInstance().getSceneWidth() + GameModel.getInstance().getSceneOffsetWidth() ) );
		//okolina.scaleYProperty().bind( scene.heightProperty().divide( GameModel.getInstance().getSceneHeight() + GameModel.getInstance().getSceneOffsetHeight() ) );

		initWidth  = scene.getWidth();
		initHeight = scene.getHeight();
		ratio      = initWidth / initHeight;
		this.primaryStage = primaryStage;
		this.scene = scene;

//		primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(ratio));
//		primaryStage.minHeightProperty().bind(scene.widthProperty().divide(ratio));

		//primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(ratio));
		//primaryStage.maxWidthProperty().bind(scene.heightProperty().multiply(ratio));;

//		primaryStage.minHeightProperty().bind(scene.widthProperty().divide(ratio));
//		primaryStage.maxHeightProperty().bind(scene.widthProperty().divide(ratio));

		scene.widthProperty().addListener(w -> resize());
		scene.heightProperty().addListener(h -> resize());



		//primaryStage.setFullScreen(true);

		//System.out.println(primaryStage.getX() + " " + primaryStage.getY());

		primaryStage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				primaryStage.setMaximized(false);
				primaryStage.setFullScreen(true);
				((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).colorOption();
			}
		});



		//MainMenu mainMenu = new MainMenu();
		//GameModel.getInstance().setMainMenu(mainMenu);
        currentMenu = GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()];
		((MainMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()]).setMain(this);
		GameModel.getInstance().getRoot().getChildren().addAll(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()]);

		((RankListMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.rankListMenu.ordinal()]).setMain(this);
		dohvatiRangListu();

		//InputPlayerNameMenu inputPlayerNameMenu = new InputPlayerNameMenu();
		//GameModel.getInstance().setInputPlayerNameMenu(inputPlayerNameMenu);

		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);


		timer = new AnimationTimer ( ) {
			int i = 0;
			@Override
			public void handle ( long l ) {
				if (i<0) {
					i++;
					Heart heart = new Heart(new Point2D(GameModel.getInstance().getSceneWidth() / 2, GameModel.getInstance().getSceneHeight() / 2));
					GameModel.getInstance().getRoot().getChildren().addAll(heart);
					GameModel.getInstance().getSrca().add(heart);
				}
//
//				SlowedDown slowedDown = new SlowedDown(new Point2D(GameModel.getInstance().getSceneWidth()/2, GameModel.getInstance().getSceneHeight()/2));
//				GameModel.getInstance().getRoot().getChildren().addAll(slowedDown);


//				if (i == 0 ) {
//					i = 1;
//					Bunker bunker1 = new Bunker(new Point2D(GameModel.getInstance().getSceneWidth()/2, GameModel.getInstance().getSceneHeight()/2));
//					GameModel.getInstance().getRoot().getChildren().addAll(bunker1);
//					GameModel.getInstance().getBunkeri().add(bunker1);
//
//					SlowedDown slowedDown1 = new SlowedDown(new Point2D(GameModel.getInstance().getSceneWidth()/3, GameModel.getInstance().getSceneHeight()/2));
//					GameModel.getInstance().getRoot().getChildren().addAll(slowedDown1);
//					GameModel.getInstance().getUsporeniSnimci().add(slowedDown1);
//
//					Barrier barrier = new Barrier(new Point2D( GameModel.getInstance().getSceneWidth()/10, 0));
//					GameModel.getInstance().getRoot().getChildren().addAll(barrier);
//
//					Barrier barrier2 = new Barrier(new Point2D( 2.4 * GameModel.getInstance().getSceneWidth()/10, 0));
//					GameModel.getInstance().getRoot().getChildren().addAll(barrier2);
//
//					Barrier barrier3 = new Barrier(new Point2D( 4 * GameModel.getInstance().getSceneWidth()/10, 0));
//					GameModel.getInstance().getRoot().getChildren().addAll(barrier3);
//
////					MainMenu mainMenu = new MainMenu();
////
////					GameModel.getInstance().getRoot().getChildren().addAll(mainMenu);
//				}



				//player.setOpacity(0.3);
				endTime = System.currentTimeMillis();
				if ((endTime - startTime) >= protokVremena){
					cnt--;
					startTime = endTime;
					prikazVremena.setText(""+cnt);
					if (cnt == 0) {
						GameModel.getInstance().setGameLost(true);
					}

					double pravi = (Math.random()*1);
					double mestoX = (Math.random()* (GameModel.getInstance().getSceneWidth() - Ball.getBallDiameter())) + Ball.getBallDiameter();
					double mestoY = (Math.random()* (0.6 * GameModel.getInstance().getSceneHeight() - 0.3* GameModel.getInstance().getSceneHeight())) + 0.3 *GameModel.getInstance().getSceneHeight();

					if (pravi < GameModel.getInstance().getJsonObject().getDouble("Fade Bubbles Posibillity")){

						Ball lazni = new Ball(new Point2D(mestoX,
								mestoY), 1);
						GameModel.getInstance().getBalls().add(lazni);
						GameModel.getInstance().getRoot().getChildren().add(lazni);

					}

					if (player.getFade() == 1) {
						zasticenCnt++;
					}

					if(zasticenCnt == (int) GameModel.getInstance().getJsonObject().getDouble("Shield Time") && svetlucka == 0){
						//player.setFade(0);
						//kreni da svetluckas
						svetlucka = (int) GameModel.getInstance().getJsonObject().getDouble("Blinking Time");
						zasticenCnt = 0;
					}

					if (svetlucka == (int) GameModel.getInstance().getJsonObject().getDouble("Blinking Time") ){

						fadeTransition = new FadeTransition(Duration.seconds(0.5),player);
						fadeTransition.setFromValue(1.0);
						fadeTransition.setToValue(0.0);
						fadeTransition.setCycleCount((int) GameModel.getInstance().getJsonObject().getDouble("Blinking Time")*2);
						fadeTransition.setAutoReverse(true);


						svetlucka = 0;

						fadeTransition.play();

						fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								player.setFade(0);
							}
						});
					}

					if (bunker != null){
						BunkerBullet bullet = new BunkerBullet ( bunker.getPosition ( ) .add ( 0,- Bunker.Bunker_Height - Bullet.BULLET_SIZE * 2.2) );
						root.getChildren ( ).remove ( GameModel.getInstance ( ).getBunkerWeapon ( ) );
						GameModel.getInstance ( ).setBunkerWeapon ( bullet );
						root.getChildren ( ).addAll ( bullet );
						if(((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
							//((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getHarpunMusicPlayer().seek(Duration.ZERO);
							((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getHarpunMusicPlayer().play();
						}
					}

					if (bunkerCounter <= 0 && bunker != null){
						root.getChildren ( ).remove (  bunker );
						if (GameModel.getInstance().getBunkerWeapon() != null){
							root.getChildren ( ).remove ( GameModel.getInstance ( ).getBunkerWeapon ( ) );
							GameModel.getInstance ( ).setBunkerWeapon ( null );
						}
						bunker = null;
					}

					if (bunkerCounter > 0) {
						bunkerCounter--;
					}

				}

				if (imamBunker == 1 && bunker == null && prviPutBunker == 0){
					prviPutBunker = 1;
					bunkerIcon = makeBunkerIcon();
					okolina.getChildren().addAll(bunkerIcon);
				}

				if (GameModel.getInstance().getImamStit() == 1){
					//System.out.println("usaoooooooooooooooooo");
					GameModel.getInstance().setImamStit(0);
					zasticenCnt = 0;
					player.setFade(1);
					player.setOpacity(0.5);
					if (fadeTransition!=null && fadeTransition.getStatus()== Animation.Status.RUNNING){
						fadeTransition.stop();
					}
					svetlucka = 0;
				}


				if (GameModel.getInstance().isGameLost() && cnt > 0) {
					if (player.getFade() == 1){
						//kreni da svetluckas
						if (fadeTransition == null || fadeTransition.getStatus()!= Animation.Status.RUNNING){
							svetlucka = (int) GameModel.getInstance().getJsonObject().getDouble("Blinking Time");
						}
						GameModel.getInstance().setGameLost(false);
					}

					else {
						if (GameModel.getInstance().getNumOfLifes() > 1 ) {
							if (fadeTransition!=null && fadeTransition.getStatus()== Animation.Status.RUNNING){
								fadeTransition.stop();
							}
							GameModel.getInstance().setGameLost(false);
							//initialization();
							resetLifes();
						} else {
							poruka.setText("YOU LOST!");
							resetLifes();
						}
					}

				}

				if (cnt == 0){
					poruka.setText("YOU LOST!");
				}

				if (GameModel.getInstance().isGameWon()) {
					poruka.setText("YOU WON!");
					poruka.setFill(Color.GREEN);
				}



				if ( GameModel.getInstance ( ).isGameLost ( ) || GameModel.getInstance ( ).isGameWon ( ) ) {
					timer.stop ( );
					scaleTransition.stop();
					translateTransition.stop();

					for (int i = 0; i < GameModel.getInstance().getDolari().size(); i++){
						GameModel.getInstance().getDolari().get(i).getFadeTransition().pause();
					}

					for (int i = 0; i < GameModel.getInstance().getRoot().getChildren().size(); i++){
						if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof Barrier) {
							Barrier barrier = (Barrier)GameModel.getInstance().getRoot().getChildren().get(i);
							if (barrier.getScaleTransition().getStatus() == Animation.Status.RUNNING) {
								barrier.getScaleTransition().pause();
								barrier.getTranslateTransition().pause();
							}
						}
					}
					if (fadeTransition!=null && fadeTransition.getStatus()== Animation.Status.RUNNING){
						fadeTransition.pause();
					}
					if (bunkerScaling != null && bunkerScaling.getStatus() == Animation.Status.RUNNING) {
						bunkerScaling.pause();
					}


					if (GameModel.getInstance().isGameWon()){
						GameModel.getInstance().setCurrentPoints(GameModel.getInstance().getPoeni());
						GameModel.getInstance().setCurrentLevel(GameModel.getInstance().getCurrentLevel() + 1);
						if (GameModel.getInstance().getCurrentLevel() > GameModel.getInstance().getLastLevel()){
							GameModel.getInstance().setLastLevel(GameModel.getInstance().getCurrentLevel());
							presaoNivo();
						}

//						if (GameModel.getInstance().getCurrentLevel() == GameModel.getInstance().getBallsPerLevels().length){
//							// pobedio si
//							GameModel.getInstance().addUser(new User(GameModel.getInstance().getPlayerName(), GameModel.getInstance().getPoeni()));
//							((RankListMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.rankListMenu.ordinal()]).setUsers();
//							if (!primaryStage.isFullScreen()) {
//								primaryStage.setResizable(true);
//							}
//
//							GameModel.getInstance().getRoot().getChildren().remove(poruka);
//							ScaleTransition porukaTrans = new ScaleTransition(Duration.seconds(1.5), poruka);
//							porukaTrans.setFromX(1);
//							porukaTrans.setToX(1.5);
//							porukaTrans.setFromY(1);
//							porukaTrans.setToY(1.5);
//
//
//							ScaleTransition porukaTrans2 = new ScaleTransition(Duration.seconds(1.5), poruka);
//							porukaTrans2.setFromX(1.5);
//							porukaTrans2.setToX(1);
//							porukaTrans2.setFromY(1.5);
//							porukaTrans2.setToY(1);
//
//
//							GameModel.getInstance().getRoot().getChildren().add(poruka);
//
//							porukaTrans.play();
//
//							porukaTrans.setOnFinished(new EventHandler<ActionEvent>() {
//								@Override
//								public void handle(ActionEvent event) {
//									porukaTrans2.play();
//								}
//							});
//
//							porukaTrans2.setOnFinished(new EventHandler<ActionEvent>() {
//								@Override
//								public void handle(ActionEvent event) {
//									poruka.setText("");
//									GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()
//											[GameModel.mappingMenuNames.rankListMenu.ordinal()]);
//								}
//							});
//
//							((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getWinMusicPlayer().seek(Duration.ZERO);
//							((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getWinMusicPlayer().play();
//
//						}else
						{
							//if (GameModel.getInstance().getNumOfLifes()< (int)GameModel.getInstance().getJsonObject().getDouble("Num Of Lifes")) {
								GameModel.getInstance().setNumOfLifes((int) GameModel.getInstance().getJsonObject().getDouble("Num Of Lifes"));
							//}
							createLevel();
						}
					}else{
						// izgubio si
						GameModel.getInstance().addUser(new User(GameModel.getInstance().getPlayerName(), GameModel.getInstance().getPoeni()));
						if (GameModel.getInstance().getCurrentLevel() > GameModel.getInstance().getLastLevel()){
							GameModel.getInstance().setLastLevel(GameModel.getInstance().getCurrentLevel());
						}
						((RankListMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.rankListMenu.ordinal()]).setUsers();
						if (!primaryStage.isFullScreen()) {
							primaryStage.setResizable(true);
						}

						GameModel.getInstance().getRoot().getChildren().remove(poruka);
						ScaleTransition porukaTrans = new ScaleTransition(Duration.seconds(1.5), poruka);
						porukaTrans.setFromX(1);
						porukaTrans.setToX(1.5);
						porukaTrans.setFromY(1);
						porukaTrans.setToY(1.5);


						ScaleTransition porukaTrans2 = new ScaleTransition(Duration.seconds(1.5), poruka);
						porukaTrans2.setFromX(1.5);
						porukaTrans2.setToX(1);
						porukaTrans2.setFromY(1.5);
						porukaTrans2.setToY(1);


						GameModel.getInstance().getRoot().getChildren().add(poruka);

						porukaTrans.play();

						porukaTrans.setOnFinished(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								porukaTrans2.play();
							}
						});

						porukaTrans2.setOnFinished(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								poruka.setText("");
								GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()
										[GameModel.mappingMenuNames.rankListMenu.ordinal()]);
							}
						});

						if(((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
							//((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getWinMusicPlayer().seek(Duration.ZERO);
							((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getWinMusicPlayer().play();
						}
					}
				}



				for ( Ball ball : GameModel.getInstance ( ).getBalls ( ) ) {
					ball.updatePosition ( );
				}

				if (GameModel.getInstance().getComboHit() == 1){
					Text prikazCombo = new Text(GameModel.getInstance().getSceneWidth()/2 - GameModel.getInstance().getSceneWidth() / 12.22 , GameModel.getInstance().getSceneHeight()/15.12 , "Combo!");
					prikazCombo.setFont(new Font(
							(GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/ 42)); //razmislis
					prikazCombo.setStrokeWidth(5);
					prikazCombo.setFill(Color.RED);

					FadeTransition fadeTransition = new FadeTransition(Duration.seconds(5),prikazCombo);
					fadeTransition.setFromValue(1.0);
					fadeTransition.setToValue(0.0);
					fadeTransition.play();

					GameModel.getInstance().setComboHit(0);

					GameModel.getInstance().getRoot().getChildren().add(prikazCombo);

					GameModel.getInstance().setPoeni(GameModel.getInstance().getPoeni() + 100);

					fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {

							GameModel.getInstance().getRoot().getChildren().remove(prikazCombo);
						}
					});

					if(((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
						//((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getComboMusicPlayer().seek(Duration.ZERO);
						((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getComboMusicPlayer().play();
					}

				}
				
				GameModel.getInstance ( ).getPlayer ( ).updatePosition ( );
				if ( GameModel.getInstance ( ).getWeapon ( ) != null ) {
					GameModel.getInstance ( ).getWeapon ( ).updatePosition ( );
				}

				if (GameModel.getInstance().getBunkerWeapon() != null){
					GameModel.getInstance().getBunkerWeapon().updatePosition();
				}


				poeniMsg.setText("Score:  " + GameModel.getInstance().getPoeni());

				if (GameModel.getInstance().getHit() == 1){



					int rand1 = (int) (Math.random()*(100000)); // random elements between 0-99999...
					int ok1 = 100000 * 1/6; // dolar
					int ok2 = 100000 * 2/6; // stit
					int ok3 = 100000 * 3/6; // sat
					int ok4 = 100000 * 4/6; // bunker
					int ok5 = 100000 * 5/6; // usporeni snimak

					if (rand1 < ok1) {
						Dollar dollar = new Dollar(new Point2D
								(GameModel.getInstance().getHitX(), GameModel.getInstance().getHity()));
						GameModel.getInstance().getRoot().getChildren().addAll(dollar);
						GameModel.getInstance().getDolari().add(dollar);
					} else if (rand1 < ok2) {
							Shield shield = new Shield(new Point2D(GameModel.getInstance().getHitX(), GameModel.getInstance().getHity()));
							GameModel.getInstance().getRoot().getChildren().addAll(shield);
							GameModel.getInstance().getStitovi().add(shield);
						}
						else if (rand1 < ok3){
							Watch watch = new Watch(new Point2D(GameModel.getInstance().getHitX(), GameModel.getInstance().getHity()));
							GameModel.getInstance().getRoot().getChildren().addAll(watch);
							GameModel.getInstance().getSatovi().add(watch);
						} else if (rand1 < ok4 && bunker == null && imamBunker == 0){ // ne pravi ako vec ima bunker...
							//bunker
							int i;
							int ok = 1;
							for ( i = 0; i < GameModel.getInstance().getRoot().getChildren().size(); i++){
								if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof  Bunker){
									ok = 0;
									break;
								}
							}
							if (ok == 1) {
								Bunker bunker = new Bunker(new Point2D(GameModel.getInstance().getHitX(), GameModel.getInstance().getHity()));
								GameModel.getInstance().getRoot().getChildren().addAll(bunker);
								GameModel.getInstance().getBunkeri().add(bunker);
							}
						} else if (rand1 < ok5 ){
							// usporeni snimak
							SlowedDown slowedDown = new SlowedDown(new Point2D(GameModel.getInstance().getHitX(), GameModel.getInstance().getHity()));
							GameModel.getInstance().getRoot().getChildren().addAll(slowedDown);
							GameModel.getInstance().getUsporeniSnimci().add(slowedDown);
						}else{
							if(GameModel.getInstance().getNumOfLifes() < 8) {
								Heart heart1 = new Heart(new Point2D(GameModel.getInstance().getHitX(), GameModel.getInstance().getHity()));
								GameModel.getInstance().getRoot().getChildren().addAll(heart1);
								GameModel.getInstance().getSrca().add(heart1);
							}
						}
					}

				GameModel.getInstance().setHit(0);


				for(Iterator<Dollar> iterator = GameModel.getInstance().getDolari().iterator(); iterator.hasNext();){
					iterator.next().updatePosition();
				}

				for(Iterator<Shield> iterator = GameModel.getInstance().getStitovi().iterator(); iterator.hasNext();){
					iterator.next().updatePosition();
				}

				for(Iterator<Watch> iterator = GameModel.getInstance().getSatovi().iterator(); iterator.hasNext();){
					iterator.next().updatePosition();
				}

				for(Iterator<Bunker> iterator = GameModel.getInstance().getBunkeri().iterator(); iterator.hasNext();){
					iterator.next().updatePosition();
				}

				for(Iterator<SlowedDown> iterator = GameModel.getInstance().getUsporeniSnimci().iterator(); iterator.hasNext();){
					iterator.next().updatePosition();
				}

				for(Iterator<Heart> iterator = GameModel.getInstance().getSrca().iterator(); iterator.hasNext();){
					iterator.next().updatePosition();
				}


				if (GameModel.getInstance().getImamSat() == 1){
					GameModel.getInstance().setImamSat(0);

					if (cnt + (long)GameModel.getInstance().getJsonObject().getDouble("Time Bonus") >
							(long)GameModel.getInstance().getJsonObject().getDouble("Time") ){
						cnt  = (long)GameModel.getInstance().getJsonObject().getDouble("Time");
					}
					else{
						cnt  =  cnt + (long)GameModel.getInstance().getJsonObject().getDouble("Time Bonus");
					}


					scaleTransition.jumpTo(Duration.seconds((long)GameModel.getInstance().getJsonObject().getDouble("Time") - cnt));
					translateTransition.jumpTo(Duration.seconds((long)GameModel.getInstance().getJsonObject().getDouble("Time") - cnt));

				}

				if (protokVremena != 1000){
					long tek = System.currentTimeMillis();
					if (tek - startUsporenoVreme >= (int) GameModel.getInstance().getJsonObject().getDouble("SlowedDown Duration") * 1000){
						Main.usporiVreme(1000);
					}
				}

			}
		};


		GameModel.getInstance().setSceneX((float)primaryStage.getX());
		GameModel.getInstance().setSceneY((float)primaryStage.getY());
		GameModel.getInstance().setStageWidth((float)primaryStage.getWidth());
		GameModel.getInstance().setStageHeight((float)primaryStage.getHeight());


		readOptions();
		dohvatiUserScoresLevels();
		dohvatiRangListu();

		//timer.start ( );
	}



	public void initialization(){
		okolina.getChildren().clear();
		root.getChildren().clear();
		time.getChildren().clear();
		GameModel.getInstance().getBalls().clear();
		GameModel.getInstance().getSatovi().clear();
		GameModel.getInstance().getStitovi().clear();
		GameModel.getInstance().getDolari().clear();
		GameModel.getInstance().getBunkeri().clear();
		GameModel.getInstance().getUsporeniSnimci().clear();
		GameModel.getInstance().getBarriers().clear();
		GameModel.getInstance().getSrca().clear();
		GameModel.getInstance().setWeapon(null);
		GameModel.getInstance().setBunkerWeapon(null);
		GameModel.getInstance().setImamSat(0);
		GameModel.getInstance().setImamStit(0);
		GameModel.getInstance().setGameLost(false);
		GameModel.getInstance().setComboHit(0);
		GameModel.getInstance().setGameWon(false);
		GameModel.getInstance().setHit(0);
		scaleTransition.stop();
		translateTransition.stop();




		if (fadeTransition!=null && fadeTransition.getStatus()== Animation.Status.RUNNING){
			fadeTransition.stop();
			fadeTransition.setRate(1);
		}


		zasticenCnt = 0;
		svetlucka = 0;
		player.setOpacity(1.0);

//		Ball ball = new Ball( new Point2D ( GameModel.getInstance().getSceneWidth() / 13.44, GameModel.getInstance().getSceneHeight() / 7.56 ) );
//		GameModel.getInstance ( ).getBalls ( ).add ( ball );

		player.setFade(0);

		player.setTranslateX(GameModel.getInstance().getSceneWidth() / 13.44);
		player.setTranslateY(GameModel.getInstance ( ).getSceneHeight ( ) - PLAYER_HEIGHT);
		player.setPosition(new Point2D(GameModel.getInstance().getSceneWidth() / 13.44,GameModel.getInstance ( ).getSceneHeight ( ) - PLAYER_HEIGHT));

		vreme = new Rectangle(GameModel.getInstance().getSceneOffsetWidth()/2,
				(int) (GameModel.getInstance().getSceneOffsetHeight()/2 + GameModel.getInstance().getSceneHeight() +
						GameModel.getInstance().getSceneOffsetHeight()/8 ),
				(int)(GameModel.getInstance().getSceneWidth()),GameModel.getInstance().getSceneOffsetHeight()/4);
		vreme.setFill(Color.RED);
		vreme.setStroke(Color.RED);

		scaleTransition = new ScaleTransition(Duration.seconds((long)GameModel.getInstance().getJsonObject().getDouble("Time")),vreme);
		scaleTransition.setFromX(1);
		scaleTransition.setToX(0);

		translateTransition = new TranslateTransition(Duration.seconds((long)GameModel.getInstance().getJsonObject().getDouble("Time")),vreme);
		translateTransition.setByX(-GameModel.getInstance().getSceneWidth()/2);


		cnt = (long)GameModel.getInstance().getJsonObject().getDouble("Time");
		prikazVremena = new Text(GameModel.getInstance().getSceneOffsetWidth()/2+(GameModel.getInstance().getSceneWidth()/2) -
				GameModel.getInstance().getSceneOffsetWidth()/8,GameModel.getInstance().getSceneOffsetHeight()/2 +
				GameModel.getInstance().getSceneHeight() + GameModel.getInstance().getSceneOffsetHeight()/3.03,"" + cnt);
		prikazVremena.setFill(Color.BLUE);
		prikazVremena.setFont(new Font(
				(GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/45.65));
		prikazVremena.setStrokeWidth(3);

		time.getChildren().addAll(vreme,prikazVremena);

		poruka = new Text(GameModel.getInstance().getSceneWidth()*0.35 , GameModel.getInstance().getSceneHeight()*0.5+GameModel.getInstance().getSceneHeight() / 75.6, "");
		poruka.setFill(Color.RED);
		poruka.setFont((Font.loadFont(Main.class.getResource("/resources/CracklingFire.ttf").toExternalForm(),
				(GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/21)));
		poruka.setStrokeWidth(5);



		poeniMsg = new Text((GameModel.getInstance().getSceneWidth() + GameModel.getInstance().getSceneWidth() / 5.6)*0.8 +
				GameModel.getInstance().getSceneWidth() / 89.6, GameModel.getInstance().getSceneHeight() / 13.745,
				"Score:  " + GameModel.getInstance().getPoeni());
		poeniMsg.setFill(Color.RED);
		poeniMsg.setFont((Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(),
				(GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/45.65)));
		poeniMsg.setStrokeWidth(5);


		root.getChildren ( ).addAll ( new Background( ), player , poruka);


		name = new Text(GameModel.getInstance().getSceneWidth() / 16.8 - GameModel.getInstance().getSceneWidth() / 26.88,
				GameModel.getInstance().getSceneHeight() / 13.745, GameModel.getInstance().getPlayerName());
		name.setFill(Color.RED);
//		name.setFont((Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(),
//				(GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/45.65)));
		name.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, (GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/60));
		name.setStrokeWidth(5);



		zivoti = new Life[GameModel.getInstance().getNumOfLifes()];

		ziv.getChildren().clear();

		for (int i = 0; i<GameModel.getInstance().getNumOfLifes();i++){
			zivoti[i] = new Life(new Point2D((int)(GameModel.getInstance().getSceneWidth() / 5.65 +
					GameModel.getInstance().getSceneWidth() / 16.8 + GameModel.getInstance().getSceneWidth() / 26.88 * (i)),
					GameModel.getInstance().getSceneHeight() / 13.745));
			ziv.getChildren().add(zivoti[i]);
		}


		level = new Text(GameModel.getInstance().getSceneWidth() / 2 + GameModel.getInstance().getSceneWidth() / 44.8, GameModel.getInstance().getSceneHeight() / 13.745,
				"LEVEL : " + (GameModel.getInstance().getCurrentLevel()+1));
		level.setFill(Color.RED);
		level.setFont((Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(),
				(GameModel.getInstance().getSceneWidth() + GameModel .getInstance().getSceneHeight())/45.65)));
		level.setStrokeWidth(5);

		okolina.getChildren().addAll(new BackgroundEnviroment(),root,poeniMsg,time,ziv,name, level);

		startTime = System.currentTimeMillis();

//		scaleTransition.play();
//		translateTransition.play();

		scaleTransition.setRate(1);
		translateTransition.setRate(1);

		protokVremena = 1000;

		if (bunker != null){
			GameModel.getInstance().getRoot().getChildren().remove(bunker);
		}

		bunker = null;
		prviPutBunker = 0;
		imamBunker = 0;
		if (bunkerScaling != null && bunkerScaling.getStatus() == Animation.Status.RUNNING) {
			bunkerScaling.stop();
		}
	}

	public static void initAfterLoadConfig(){
		scaleTransition = new ScaleTransition(Duration.seconds((long)GameModel.getInstance().getJsonObject().getDouble("Time")), vreme);
		scaleTransition.setFromX(1);
		scaleTransition.setToX(0);

		translateTransition = new TranslateTransition(Duration.seconds((long)GameModel.getInstance().getJsonObject().getDouble("Time")), vreme);
		translateTransition.setByX(-GameModel.getInstance().getSceneWidth()/2);


		cnt = (long)GameModel.getInstance().getJsonObject().getDouble("Time");
		prikazVremena.setText("" + cnt);
		GameModel.getInstance().setNumOfLifes((int)GameModel.getInstance().getJsonObject().getDouble("Num Of Lifes") + 1);
		resetLifes();

		Player.PLAYER_SPEED = GameModel.getInstance().getSceneWidth()/134.4f * (float) GameModel.getInstance().getJsonObject().getDouble("Player SpeedX");
		player.setSpeedX(Player.PLAYER_SPEED);

		Ball.setBallDiameter((GameModel.getInstance().getSceneWidth() / 28) * GameModel.getInstance().getJsonObject().getDouble("Bubble Size"));

	}

	public static void resetLifes(){

		okolina.getChildren().remove(ziv);

		GameModel.getInstance().setNumOfLifes(GameModel.getInstance().getNumOfLifes()-1);

		zivoti = new Life[GameModel.getInstance().getNumOfLifes()];

		ziv.getChildren().clear();

		for (int i = 0; i<GameModel.getInstance().getNumOfLifes();i++){
			zivoti[i] = new Life(new Point2D((int)(GameModel.getInstance().getSceneWidth() / 5.65 +
					GameModel.getInstance().getSceneWidth() / 16.8 + GameModel.getInstance().getSceneWidth() / 26.88 * (i)),
					GameModel.getInstance().getSceneHeight() / 13.745));
			ziv.getChildren().add(zivoti[i]);
		}

		okolina.getChildren().add(ziv);

	}

	public static long startUsporenoVreme;

	public static void usporiVreme(long novoVreme){
		protokVremena = novoVreme;
		scaleTransition.setRate(1000./protokVremena);
		translateTransition.setRate(1000./protokVremena);
		startUsporenoVreme = System.currentTimeMillis();

		for(int i = 0 ; i < GameModel.getInstance().getDolari().size(); i++){
			GameModel.getInstance().getDolari().get(i).getFadeTransition().setRate(1000./protokVremena);
		}

//		for(int i = 0 ; i < GameModel.getInstance().getBarriers().size(); i++){
//			GameModel.getInstance().getBarriers().get(i).getScaleTransition().setRate(1000./protokVremena);
//			GameModel.getInstance().getBarriers().get(i).getTranslateTransition().setRate(1000./protokVremena);
//		}

		if (fadeTransition != null){
			fadeTransition.setRate(1000./protokVremena);
		}
	}

	private Bunker makeBunkerIcon(){

		bunkerIcon = new Bunker(new Point2D((int)((GameModel.getInstance().getSceneWidth()) / 22 ),
				GameModel.getInstance().getSceneHeight()/3));
		bunkerIcon.getTransforms().addAll(new Scale(0.7,0.7,0,0));

		bunkerScaling = new ScaleTransition(Duration.seconds(1.0), bunkerIcon);
		bunkerScaling.setFromX(1.0);
		bunkerScaling.setToX(0.85);
		bunkerScaling.setFromY(1.0);
		bunkerScaling.setToY(0.85);
		bunkerScaling.setAutoReverse(true);
		bunkerScaling.setCycleCount(Animation.INDEFINITE);
		bunkerScaling.play();

		//okolina.getChildren().add(bunkerIcon);
		return bunkerIcon;
	}

	private void deleteBunkerIcon(){
		bunkerScaling.stop();
		okolina.getChildren().remove(bunkerIcon);
		bunkerIcon = null;
	}



	public void resize() {
		final double newWidth  = scene.getWidth();
		final double newHeight = scene.getHeight();

		double scaleFactorX = newWidth / initWidth;

		double scaleFactorY = newHeight / initHeight;

		Scale scale = new Scale(scaleFactorX, scaleFactorY);
		scale.setPivotX(0);
		scale.setPivotY(0);
		scene.getRoot().getTransforms().setAll(scale);
	}

	public static void dohvatiRangListu(){
        try {

            File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile();

            File resf = new File(f.getAbsolutePath() + "/" + "ResultsFile.txt");

            if (!resf.exists()) {
                resf.createNewFile();
            }

			FileInputStream fileInputStream = new FileInputStream(resf);
			BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));

            //BufferedReader br = new BufferedReader(new FileReader(resf));
            String name;
            User names[] = new User[10];
            int i = 0;
            while ((name = br.readLine()) != null) {
                String split[] = name.split(":");
                names[i++] = new User(split[0], Integer.parseInt(split[1]));
            }

            GameModel.getInstance().setUsers(names);

            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void sacuvajRangListu(){
        FileWriter fw = null;
        BufferedWriter out = null;
        PrintWriter writer = null;
        try {

            File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile();

            File resf = new File(f.getAbsolutePath() + "/" + "ResultsFile.txt");

            if (!resf.exists()) {
                resf.createNewFile();
            }

            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resf), StandardCharsets.UTF_8));

			writer = new PrintWriter(out);

            User[] users = GameModel.getInstance().getUsers();

            for (int i = 0; i < 10; i++) {
                if (users[i] == null) {
                    break;
                }
                writer.write(users[i].getName() + ":" + users[i].getScores() + "\r\n");
            }
            writer.flush();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }

    public static void saveOptions(){
		PrintWriter writer = null;
		try {

			File dir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI()).getParentFile();

			File resDir = new File(dir.getAbsolutePath() + "/options");

			if(!resDir.exists()){
				resDir.mkdirs();
			}

			File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI()).getParentFile();

			File resf = new File(f.getAbsolutePath() + "/options/" + "optionsFile_" + GameModel.getInstance().getPlayerName()+".txt");

			if (!resf.exists()) {
				resf.createNewFile();
			}

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resf), StandardCharsets.UTF_8));

			writer = new PrintWriter(out);

			//writer = new PrintWriter(resf);

			writer.write(((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getMusicSlider().getValue() + "\r\n");
			writer.write(((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectSlider().getValue() + "\r\n");
			writer.write(((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getMusicCheckBox().isSelected() + "\r\n");
			writer.write(((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected() + "\r\n");
			writer.write(primaryStage.isFullScreen() + "\r\n");
			writer.write(primaryStage.getX() + "\r\n");
			writer.write(primaryStage.getY() + "\r\n");
			writer.write(primaryStage.getWidth() + "\r\n");
			writer.write(primaryStage.getHeight() + "\r\n");
			//writer.write(GameModel.getInstance().getLastLevel() + "\r\n");

			writer.flush();

		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			writer.close();
		}
    }

    public  static void readOptions(){
		try {

			File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI()).getParentFile();

			File dir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI()).getParentFile();

			File resDir = new File(dir.getAbsolutePath() + "/options");

			if(!resDir.exists()){
				resDir.mkdirs();
			}

			File resf = new File(f.getAbsolutePath() + "/options/" + "optionsFile_" + GameModel.getInstance().getPlayerName()+".txt");

			if (!resf.exists()) {
				resf.createNewFile();
			} else {
				//System.out.println("USAOOOO");
				FileInputStream fileInputStream = new FileInputStream(resf);
				BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
				//BufferedReader br = new BufferedReader(new FileReader(resf));
				((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getMusicSlider().setValue(Double.parseDouble(br.readLine()));
				((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectSlider().setValue(Double.parseDouble(br.readLine()));
				((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getMusicCheckBox().setSelected(Boolean.parseBoolean(br.readLine()));
				((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().setSelected(Boolean.parseBoolean(br.readLine()));

				Boolean isFullScreen = Boolean.parseBoolean(br.readLine());

				double x = Double.parseDouble(br.readLine());
				double y = Double.parseDouble(br.readLine());
				double wid = Double.parseDouble(br.readLine());
				double heigh = Double.parseDouble(br.readLine());

				if (isFullScreen){
					primaryStage.setFullScreen(true);
				}else{
					primaryStage.setFullScreen(false);
					primaryStage.setX(x);
					primaryStage.setY(y);
					primaryStage.setWidth(wid);
					primaryStage.setHeight(heigh);
				}

				//GameModel.getInstance().setLastLevel(Integer.parseInt(br.readLine()));

				((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).colorOption();

				if(((OptionsMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getMusicCheckBox().isSelected()){
					((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getBackgroundMusicPlayer().seek(Duration.ZERO);
					((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getBackgroundMusicPlayer().play();
				}else{
					((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getBackgroundMusicPlayer().pause();
				}


				br.close();
			}


		} catch (FileNotFoundException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}



	public static void savePlayerName(){
		PrintWriter writer = null;
		try {

			File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI()).getParentFile();

			File resf = new File(f.getAbsolutePath() + "/" + "PlayerNameFile.txt");

			if (!resf.exists()) {
				resf.createNewFile();
			}

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resf), StandardCharsets.UTF_8));

			writer = new PrintWriter(out);

			//writer = new PrintWriter(resf);

			writer.write(GameModel.getInstance().getPlayerName() + "\r\n");

			writer.flush();

		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			writer.close();
		}
	}

	public static void readPlayerName(){
		try {

			File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI()).getParentFile();

			File resf = new File(f.getAbsolutePath() + "/" + "PlayerNameFile.txt");

			if (!resf.exists()) {
				resf.createNewFile();
			} else {
				//System.out.println("USAOOOO");
				FileInputStream fileInputStream = new FileInputStream(resf);
				BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
				//BufferedReader br = new BufferedReader(new FileReader(resf));
				String ime = br.readLine();
				GameModel.getInstance().setPlayerName(ime);
				name.setText(ime);
				((InputPlayerNameMenu)GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.playerNameMenu.ordinal()]).
						getInputName().setText(GameModel.getInstance().getPlayerName());

				br.close();
			}


		} catch (FileNotFoundException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

    public  void createLevel(){
		int level = GameModel.getInstance().getCurrentLevel() % GameModel.getInstance().getBallsPerLevels().length;
		int ind = 0;

		initialization();

		for (ind = 0; ind < GameModel.getInstance().getBallsPerLevels()[level].length; ind++){
			Ball ball = new Ball(GameModel.getInstance().getBallsPerLevels()[level][ind], GameModel.getInstance().getBallNumOfDivisionsPerLevel()[level][ind],
								Ball.getBallDiameter() * (Math.pow(0.75,GameModel.getInstance().getBallNumOfDivisionsPerLevel()[level][ind])),
										GameModel.getInstance().getBallsDirectionsPerLevel()[level][ind] );
			GameModel.getInstance().getBalls().add(ball);
			GameModel.getInstance().getRoot().getChildren().add(ball);
		}

		int iteration = GameModel.getInstance().getCurrentLevel() / GameModel.getInstance().getBallsPerLevels().length;

		iteration--;

		if (iteration>=0){

			// dodaj lopticu
			int brojacZasebnihIteracija = iteration/4 + 1;

			for (int i = 0; i < brojacZasebnihIteracija; i++) {
				int indBall = i % GameModel.getInstance().getBallsPerLevels()[level].length;

				double maxPomerajX = Ball.getBallDiameter() * 0.9;
				double maxPomerajY = Ball.getBallDiameter() * 1.8;
				double pomerajX = Math.random() * (2 * maxPomerajX) - maxPomerajX;
				double pomerajY = Math.random() * (2 * maxPomerajY) - maxPomerajY;

				Point2D newPosition = new Point2D(GameModel.getInstance().getBallsPerLevels()[level][indBall].getX() + pomerajX,
						GameModel.getInstance().getBallsPerLevels()[level][indBall].getY() + pomerajY);

				Ball ball = new Ball(newPosition,
						GameModel.getInstance().getBallNumOfDivisionsPerLevel()[level][indBall],
						Ball.getBallDiameter() * (Math.pow(0.75, GameModel.getInstance().getBallNumOfDivisionsPerLevel()[level][indBall])),
						GameModel.getInstance().getBallsDirectionsPerLevel()[level][indBall]);
				GameModel.getInstance().getBalls().add(ball);
				GameModel.getInstance().getRoot().getChildren().add(ball);
			}


			 // povecaj brzinu
			int brojacZabrzinu = brojacZasebnihIteracija;
			if (iteration % 4 < 1){
				brojacZabrzinu--;
			}
			float procenatUbrzanja = (float)brojacZabrzinu/10f + 1;

			for(int i = 0; i < GameModel.getInstance().getBalls().size(); i++){
				GameModel.getInstance().getBalls().get(i).setSpeedX(GameModel.getInstance().getBalls().get(i).getSpeedX()*procenatUbrzanja);
			}


			// povecaj velicinu
			int brojacVelicinu = brojacZasebnihIteracija;
			if (iteration % 4 < 2){
				brojacVelicinu--;
			}
			float procenatVel = (float)brojacVelicinu/10f +1;

			for(int i = 0; i < GameModel.getInstance().getBalls().size(); i++){
				GameModel.getInstance().getBalls().get(i).setRadius(GameModel.getInstance().getBalls().get(i).getRadius()*procenatVel);
			}


			// povecaj broj podela
			int brojacPodelu = brojacZasebnihIteracija;
			if (iteration % 4 < 3){
				brojacPodelu--;
			}
			for(int i = 0; i < GameModel.getInstance().getBalls().size(); i++){
				GameModel.getInstance().getBalls().get(i).setBrojPodela(3+brojacPodelu);
			}

		}


		for (ind = 0; ind < GameModel.getInstance().getBarriersPerLevels()[level].length; ind++){
			Barrier barrier = new Barrier(GameModel.getInstance().getBarriersPerLevels()[level][ind]);
			GameModel.getInstance().getBarriers().add(barrier);
			GameModel.getInstance().getRoot().getChildren().add(barrier);
		}

		timer.start();
		scaleTransition.play();
		translateTransition.play();

	}

    public AnimationTimer getTimer() {
        return timer;
    }

    public static ScaleTransition getScaleTransition() {
        return scaleTransition;
    }

    public static TranslateTransition getTranslateTransition() {
        return translateTransition;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

	@Override
	public void stop() throws Exception {
		super.stop();
		sacuvajRangListu();
		savePlayerName();
		saveOptions();
		sacuvajUserScoresLevels();
	}

	public static void readConfig(){

		File f = null;
		try {
			f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI()).getParentFile();

			//System.out.println("USAO U READ CONFIG");

			File dir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI()).getParentFile();

			File resDir = new File(dir.getAbsolutePath() + "/config");

			if(!resDir.exists()){
				resDir.mkdirs();
			}

			File resf = new File(f.getAbsolutePath() + "/config/" + "configFile_" + GameModel.getInstance().getPlayerName()+".json");


			if (!resf.exists()) {
				resf.createNewFile();
				// create json object and save to file
				JSONObject jsonObject = new JSONObject();



				for(int i = 0 ; i < GameModel.getInstance().getConfigParsNames().length; i++){
				    jsonObject.put(GameModel.getInstance().getConfigParsNames()[i],
                            GameModel.getInstance().getDefaultValuesParameters()[i]);
                }

				System.out.println("posle FORA ISPIS");

				GameModel.getInstance().setJsonObject(jsonObject);

				String s = jsonObject.toString();

				System.out.println("JSON STRING :        "  + s);


				PrintWriter writer = null;



				writer = new PrintWriter(resf);

				writer.write(s);

				writer.flush();

				writer.close();
			}

			else {

				//System.out.println("USAO U ELSEEEEEEEEEEEEEEEEE");
				BufferedReader br = new BufferedReader(new FileReader(resf));
				String line;
				String jsonString = "";
				while ((line = br.readLine()) != null) {
					jsonString += line;
				}
				br.close();


				JSONObject obj = null;

				try{
					obj = new JSONObject(jsonString);
				}catch (Exception e){
					JSONObject jsonObject = new JSONObject();
					for(int i = 0 ; i < GameModel.getInstance().getConfigParsNames().length; i++){
						jsonObject.put(GameModel.getInstance().getConfigParsNames()[i],
								GameModel.getInstance().getDefaultValuesParameters()[i]);
					}
					obj = jsonObject;
				}

				for(int i = 0 ; i < GameModel.getInstance().getConfigParsNames().length; i++) {
					if(!obj.has(GameModel.getInstance().getConfigParsNames()[i])){
						obj.put(GameModel.getInstance().getConfigParsNames()[i], GameModel.getInstance().getDefaultValuesParameters()[i]);
					}
				}

				int i;
				for( i = 0 ; i < GameModel.getInstance().getConfigParsNames().length; i++) {
					double val = obj.getDouble(GameModel.getInstance().getConfigParsNames()[i]);
					if (val < GameModel.getInstance().getMinValuesParameters()[i] ||
							val > GameModel.getInstance().getMaxValuesParameters()[i]) {
						System.out.println(GameModel.getInstance().getConfigParsNames()[i] + " " + val);

						obj.remove(GameModel.getInstance().getConfigParsNames()[i]);
						obj.put(GameModel.getInstance().getConfigParsNames()[i],
								GameModel.getInstance().getDefaultValuesParameters()[i]);
					}
				}

				GameModel.getInstance().setJsonObject(obj);

//			jsonObject.put("test", 2f);
//			jsonObject.put("lepo", 3.4f);
//			System.out.println(jsonObject.toString());

			}

		} catch(Exception e) {
			e.printStackTrace();
			//System.out.println("USAO U EXCEPTION");
		}
	}

//	public static Text getName() {
//		return name;
//	}

	public static String loadConfig(File resf){
		File f = null;
		try {

			//System.out.println("USAO U ELSEEEEEEEEEEEEEEEEE");
			BufferedReader br = new BufferedReader(new FileReader(resf));
			String line;
			String jsonString = "";
			while ((line = br.readLine()) != null) {
				jsonString += line;
			}
			br.close();


			JSONObject obj = null;

			try{
				obj = new JSONObject(jsonString);
			}catch (Exception e){
				return "Bad JSON File Format";
			}

			int i;

			for( i = 0 ; i < GameModel.getInstance().getConfigParsNames().length; i++) {
				if(!obj.has(GameModel.getInstance().getConfigParsNames()[i])){
					return "Not Defined Value for: " +  GameModel.getInstance().getConfigParsNames()[i];
				}
			}

			for( i = 0 ; i < GameModel.getInstance().getConfigParsNames().length; i++) {
				double val = obj.getDouble(GameModel.getInstance().getConfigParsNames()[i]);
				if (val < GameModel.getInstance().getMinValuesParameters()[i] ||
						val > GameModel.getInstance().getMaxValuesParameters()[i]) {
					System.out.println(GameModel.getInstance().getConfigParsNames()[i] + " " + val);
					return "Invalid Range for: " + GameModel.getInstance().getConfigParsNames()[i];
				}
			}

			GameModel.getInstance().setJsonObject(obj);


		} catch(Exception e) {
			e.printStackTrace();
			//System.out.println("USAO U EXCEPTION");
		}

		return "Successful";
	}

	public static void setNamePlayer(String ime) {
		Main.name.setText(ime);
	}

	public static FadeTransition getFadeTransition() {
		return fadeTransition;
	}

	public static ScaleTransition getBunkerScaling() {
		return bunkerScaling;
	}

	public static void main (String[] args ) {
		launch ( args );
	}


	public static void dohvatiUserScoresLevels(){
		try {

			File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI()).getParentFile();

			File resf = new File(f.getAbsolutePath() + "/" + "Users.txt");

			if (!resf.exists()) {
				resf.createNewFile();
			}
			FileInputStream fileInputStream = new FileInputStream(resf);
			BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
			String name;
			ArrayList<User> users = new ArrayList<>();
			int i = 0;
			while ((name = br.readLine()) != null) {
				String split[] = name.split(":");
				users.add (new User(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2])));
				if(split[0].equals(GameModel.getInstance().getPlayerName())){
					GameModel.getInstance().setLastLevel(Integer.parseInt(split[2]));
				}
			}

			GameModel.getInstance().setAllUsers(users);

			br.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void sacuvajUserScoresLevels(){
		FileWriter fw = null;
		BufferedWriter out = null;
		PrintWriter writer = null;
		try {

			File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI()).getParentFile();

			File resf = new File(f.getAbsolutePath() + "/" + "Users.txt");

			if (!resf.exists()) {
				resf.createNewFile();
			}

			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resf), StandardCharsets.UTF_8));

			writer = new PrintWriter(out);

			ArrayList<User> users = GameModel.getInstance().getAllUsers();

			for (int i = 0; i < users.size(); i++) {
				writer.write(users.get(i).getName() + ":" + users.get(i).getScores() +":" + users.get(i).getLevel() + "\r\n");
			}
			writer.flush();

		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			writer.close();
		}
	}

	public void presaoNivo(){

		ArrayList<User> users = GameModel.getInstance().getAllUsers();
		int i = 0;
		for( i = 0; i < GameModel.getInstance().getAllUsers().size(); i++){
			if (GameModel.getInstance().getPlayerName().equals(users.get(i).getName())){
				if(users.get(i).getLevel()<GameModel.getInstance().getCurrentLevel()){
					users.get(i).setLevel(GameModel.getInstance().getCurrentLevel());
					users.get(i).setScores(GameModel.getInstance().getPoeni());

				}else if(users.get(i).getLevel()==GameModel.getInstance().getCurrentLevel() && users.get(i).getScores()<GameModel.getInstance().getPoeni()){
					users.get(i).setLevel(GameModel.getInstance().getCurrentLevel());
					users.get(i).setScores(GameModel.getInstance().getPoeni());
				}
				break;
			}
		}

		if(i==users.size()){
			users.add(new User(GameModel.getInstance().getPlayerName(), 0, 0));
		}
	}
}
