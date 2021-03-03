package objects;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import main.Main;
import menus.OptionsMenu;
import models.GameModel;
import objects.weapons.Weapon;

import javax.swing.text.Position;
import java.util.ArrayList;

public class Ball extends MovingGameObject {

	//private GameModel model = GameModel.getInstance();
	private float ballSpeedX = GameModel.getInstance().getSceneWidth() / 224 * (float) GameModel.getInstance().getJsonObject().getDouble("Bubbles SpeedX"); // 6
	private float ballSpeedY = GameModel.getInstance().getSceneHeight() / 90; // 8.4

	//private static final double BALL_DIAMETER = (float) (GameModel.getInstance().getScreenSize().width * 0.025);
	static double BALL_DIAMETER = (float) (GameModel.getInstance().getSceneWidth() / 28) *
			GameModel.getInstance().getJsonObject().getDouble("Bubble Size"); // 48


	private int level;
	private double radius;

	private Ball parent;
	private int rockBottom;
	private int fade;

	private float koef = GameModel.getInstance().getSceneHeight()/756;

	private int brojPodela=3;

	private Circle ball;


//	{
//		Circle ball = new Circle ( BALL_DIAMETER );
//		ball.setFill ( Color.RED );
//		this.getChildren ( ).addAll ( ball );
//	}
//
//	// initialize speed
//	{
//		super.speedX = ballSpeedX;
//		super.speedY = ballSpeedY;
//	}


	public Ball(Point2D position) {
		super(position);

		level = 0;
		radius = BALL_DIAMETER;
		ball = new Circle(BALL_DIAMETER);
//		ball.setFill(Color.RED);
        ball.setFill(GameModel.getInstance().getColors()[GameModel.getInstance().getIndexColor()]);
        GameModel.getInstance().setIndexColor((GameModel.getInstance().getIndexColor() + 1) % GameModel.getInstance().getColors().length);
		this.getChildren().addAll(ball); //adding Circle in group for Ball


		parent = null;
		rockBottom = 0;


		fade = 0;
		super.speedX = ballSpeedX;
		super.speedY = ballSpeedY;
	}

	public Ball(Point2D position, int fade) {
		super(position);

		level = 0;
		radius = BALL_DIAMETER;
		ball = new Circle(BALL_DIAMETER);

		int red = (int) (Math.random() * (256));
		int blue = (int) (Math.random() * (256));
		int green = (int) (Math.random() * (256));
		Color color;

		if (fade == 1) {
			color = Color.rgb(red, green, blue, 0.2);
		} else {
			color = Color.rgb(red, green, blue);
		}

        Color baseColor = GameModel.getInstance().getColors()[GameModel.getInstance().getIndexColor()];

		if (fade == 1){
			color = Color.color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 0.2);
		} else{
			color = baseColor;
		}

        GameModel.getInstance().setIndexColor((GameModel.getInstance().getIndexColor() + 1) % GameModel.getInstance().getColors().length);


		ball.setFill(color);

		this.getChildren().addAll(ball); //adding Circle in group for Ball


		parent = null;
		rockBottom = 0;


		this.fade = fade;
		super.speedX = ballSpeedX;
		super.speedY = ballSpeedY;
	}

	public Ball(Point2D position, int level, double radius, int direction, Color color, Ball parent, int fade) {
		super(position);

        Color baseColor = GameModel.getInstance().getColors()[GameModel.getInstance().getIndexColor()];
        GameModel.getInstance().setIndexColor((GameModel.getInstance().getIndexColor() + 1) % GameModel.getInstance().getColors().length);

		if (fade == 1){
			color = Color.color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 0.2);
		} else {
			color = baseColor;
		}


		this.level = level;
		this.radius = radius;
		ball = new Circle(radius);
		ball.setFill(color);
		this.getChildren().addAll(ball); //adding Circle in group for Ball

		if (direction == 0) {
			super.speedX = -ballSpeedX;
		} else {
			super.speedX = ballSpeedX;
		}
		super.speedY = -ballSpeedY*4/5;

		this.parent = parent;
		rockBottom = 0;

		this.fade = fade;
	}

	public Ball(Point2D position, int level, double radius, int direction) {
		super(position);

		Color color = GameModel.getInstance().getColors()[GameModel.getInstance().getIndexColor()];
		GameModel.getInstance().setIndexColor((GameModel.getInstance().getIndexColor() + 1) % GameModel.getInstance().getColors().length);



		this.level = level;
		this.radius = radius;
		ball = new Circle(radius);
		ball.setFill(color);
		this.getChildren().addAll(ball); //adding Circle in group for Ball

		if (direction == 0) {
			super.speedX = ballSpeedX;
		} else {
			if (direction == -1){
				super.speedX = 0;
			}else {
				super.speedX = -ballSpeedX;
			}
		}
		super.speedY = ballSpeedY;

		this.parent = null;
		rockBottom = 0;

		this.fade = 0;
	}

	@Override
	public void updatePosition() {

		double faktor = (1000./ Main.protokVremena);
		position = new Point2D(position.getX() + speedX * faktor, position.getY() + speedY * faktor);
		setTranslateX(getTranslateX() + speedX * faktor);
		setTranslateY(getTranslateY() + speedY * faktor);

		int pomeraj = level;
		if(pomeraj>3){
			pomeraj = 3;
		}
		if (speedY < 0) {
			//speedY += (0.23 - level * 0.03); // max height of jump...?
			speedY += (0.17 - pomeraj * 0.03) * koef * faktor;
		} else {
			speedY = ballSpeedY;
		}

		handleCollisions();
	}

	@Override
	protected void handleCollisions() {
		handleBorderCollisions();
		handleBarrierCollisions();
		handlePlayerCollisions();
		handleBulletCollisions();
	}

	private void handleWeaponColl(Weapon weapon, int location){
		if (weapon == null){
			return;
		}

		if (this.getBoundsInParent().intersects(weapon.getBoundsInParent())) {
			//remove ball
			GameModel.getInstance().getRoot().getChildren().remove(this);
			GameModel.getInstance().getBalls().remove(this);


			//remove weapon
			if (location == 1) {
				GameModel.getInstance().getRoot().getChildren().remove(GameModel.getInstance().getWeapon());
				GameModel.getInstance().setWeapon(null);
			}
			else{
				GameModel.getInstance().getRoot().getChildren().remove(GameModel.getInstance().getBunkerWeapon());
				GameModel.getInstance().setBunkerWeapon(null);
			}

			//divide
			if (level < brojPodela) {
				int red = (int) (Math.random() * (256));
				int blue = (int) (Math.random() * (256));
				int green = (int) (Math.random() * (256));
				Color color;

				if (fade == 1) {
					color = Color.rgb(red, green, blue, 0.3);
				} else {
					color = Color.rgb(red, green, blue);
				}


				Ball b1 = new Ball(new Point2D(position.getX() , position.getY()), level + 1, radius * 0.75, 1, color, this, fade);
				Ball b2 = new Ball(new Point2D(position.getX() , position.getY()), level + 1, radius * 0.75, 0, color, this, fade);
				if (lopticeUIgracu.contains(this)) {
					lopticeUIgracu.remove(this);
					lopticeUIgracu.add(b1);
					lopticeUIgracu.add(b2);
				}
				b1.setSpeedX(this.speedX);
				b1.setBrojPodela(this.brojPodela);
				b2.setSpeedX(-this.speedX);
				b2.setBrojPodela(this.brojPodela);
				b1.handleCollisions();
				b2.handleCollisions();

				GameModel.getInstance().getBalls().add(b1);
				GameModel.getInstance().getBalls().add(b2);
				GameModel.getInstance().getRoot().getChildren().addAll(b1, b2);

				if (this.fade == 0) {
					GameModel.getInstance().setPoeni(GameModel.getInstance().getPoeni() +
							(int)GameModel.getInstance().getJsonObject().getDouble("Bubbles Crashing Points"));
				}



				if(fade == 0){
					pogodak();
				}



				//GameModel.getInstance().setComboHit(1);
			}else{
				if (this.fade == 0) {
					GameModel.getInstance().setPoeni(GameModel.getInstance().getPoeni() +
							(int) GameModel.getInstance().getJsonObject().getDouble("Crashing Smallest Bubble"));
				}
				if(fade == 0){
					pogodak();
				}

				int podigni = 1;
				while (GameModel.getInstance().getBarriers().size() != 0 && podigni == 1) {

					podigni = 1;
					for (int i = 0; i < GameModel.getInstance().getBalls().size(); i++) {
						if (GameModel.getInstance().getBalls().get(i).fade == 0) {
							if (GameModel.getInstance().getBalls().get(i).position.getX() <= GameModel.getInstance().getBarriers().get(0).position.getX()) {
								podigni = 0;
								break;
							}
						}
					}

					if (podigni == 1) {
						GameModel.getInstance().getBarriers().get(0).getTranslateTransition().play();
						GameModel.getInstance().getBarriers().get(0).getScaleTransition().play();
						GameModel.getInstance().getBarriers().remove(GameModel.getInstance().getBarriers().get(0));
					}
				}
			}

			int won = 1;
			for (int i = 0; i < GameModel.getInstance().getBalls().size(); i++) {
				if (GameModel.getInstance().getBalls().get(i).fade == 0) {
					won = 0;
					break;
				}
			}

			if (won == 1) {
				GameModel.getInstance().setGameWon(true);
			}

			new Thread(new Runnable() {
				@Override
				public void run() {
					if(((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
						//((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getBallCrushMusicPlayer().seek(Duration.ZERO);
						((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getBallCrushMusicPlayer().play();
					}

				}
			}).start();
			}
	}

	private void handleBulletCollisions() {
		handleWeaponColl(GameModel.getInstance().getWeapon(), 1);
		handleWeaponColl(GameModel.getInstance().getBunkerWeapon(), 0);
	}

	public static ArrayList<Ball> lopticeUIgracu = new ArrayList<>();

	private void handlePlayerCollisions() {
		if (this.getBoundsInParent().intersects(GameModel.getInstance().getPlayer().getBoundsInParent())) {
			if (fade == 0) {
				if(lopticeUIgracu.contains(this)){

				}else{
					lopticeUIgracu.add(this);
					GameModel.getInstance().setGameLost(true);
					new Thread(new Runnable() {
						@Override
						public void run() {
							if(((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getEffectsCheckBox().isSelected()) {
								//((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getBallPlayerMusicPlayer().seek(Duration.ZERO);
								((OptionsMenu) GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.optionsMenu.ordinal()]).getBallPlayerMusicPlayer().play();
							}
						}
					}).start();
				}
			} else {
				GameModel.getInstance().getRoot().getChildren().remove(this);
				GameModel.getInstance().getBalls().remove(this);
			}
		}else{
			if(lopticeUIgracu.contains(this)){
				lopticeUIgracu.remove(this);
			}
		}
	}
	
	private void handleBorderCollisions ( ) {
		if ( position.getX ( ) - radius < 0 || position.getX ( ) > GameModel.getInstance ( ).getSceneWidth ( ) - radius ) {
			speedX = -speedX;
		}
		
		if ( position.getY ( ) - radius < 0 || position.getY ( ) > GameModel.getInstance ( ).getSceneHeight ( ) - radius ) {
			speedY = -speedY;
			//speedY = Math.signum(speedY) * GameModel.getInstance().ballSpeedY;
		}

		if (position.getX ( ) - radius < 0){
			setTranslateX ( radius );
			position = new Point2D(radius, position.getY());
		}

		if (position.getX ( ) > GameModel.getInstance ( ).getSceneWidth ( ) - radius ){
			setTranslateX ( GameModel.getInstance ( ).getSceneWidth ( ) - radius );
			position = new Point2D(GameModel.getInstance ( ).getSceneWidth ( ) - radius, position.getY());
		}

		if (position.getY ( ) - radius < 0 ){



			// COMBO!!!

			if ( parent!= null && parent.getRockBottom() == 0 && rockBottom == 0){
				//COMBO
				//GameModel.getInstance().setPoeni(GameModel.getInstance().getPoeni() + 100);
				GameModel.getInstance().setComboHit(1);
				int won = 1;
				for (int i = 0; i < GameModel.getInstance().getBalls().size(); i++) {
					if (GameModel.getInstance().getBalls().get(i).fade == 0) {
						won = 0;
						break;
					}
				}

				if (won == 1) {
					GameModel.getInstance().setGameWon(true);
				}
			}
			else{
				GameModel.getInstance().setPoeni(GameModel.getInstance().getPoeni() + 10);
			}

			GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
			GameModel.getInstance().getBalls().remove(this);
			pogodak();


			if(GameModel.getInstance().getBalls().size() == 0) {
				GameModel.getInstance ( ).setGameWon ( true );
			}


		}

		if (position.getY() > GameModel.getInstance ( ).getSceneHeight ( ) - radius){
			setTranslateY ( GameModel.getInstance ( ).getSceneHeight ( ) - radius );
			position = new Point2D(position.getX(),GameModel.getInstance ( ).getSceneHeight ( ) - radius);
			rockBottom = 1;
		}
	}

	public void handleBarrierCollisions(){
		for(int i = 0; i < GameModel.getInstance().getRoot().getChildren().size(); i++){
			if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof Barrier) {
				Barrier barrier = (Barrier) GameModel.getInstance().getRoot().getChildren().get(i);

				if (this.getBoundsInParent().intersects(barrier.getBoundsInParent())) {
					if (speedX > 0) { // idem udesno
						setTranslateX(barrier.getPosition().getX() - Barrier.Barrier_Width / 2 - radius);
						position = new Point2D(barrier.getPosition().getX() - Barrier.Barrier_Width / 2 - radius, position.getY());
					} else { //ulevo
						setTranslateX(barrier.getPosition().getX() + Barrier.Barrier_Width / 2 + radius);
						position = new Point2D(barrier.getPosition().getX() + Barrier.Barrier_Width / 2 + radius, position.getY());
					}

					speedX = -speedX;
					break;
				}
			}
		}
	}

	private void pogodak(){
		int random = (int) (Math.random()*(100000)); // random elements between 0-99999...
		double ok = 100000 * GameModel.getInstance().getJsonObject().getDouble("Bonus Posibillity");

		if (random < ok){
			GameModel.getInstance().setHit(1);
			GameModel.getInstance().setHitX(position.getX());
			GameModel.getInstance().setHity(position.getY());
		}

	}


	public int getRockBottom() {
		return rockBottom;
	}

	public void setRockBottom(int rockBottom) {
		this.rockBottom = rockBottom;
	}

	public static double getBallDiameter() {
		return BALL_DIAMETER;
	}

	public static void setBallDiameter(double ballDiameter) {
		BALL_DIAMETER = ballDiameter;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
		ball.setRadius(radius);
	}

	public int getBrojPodela() {
		return brojPodela;
	}

	public void setBrojPodela(int brojPodela) {
		this.brojPodela = brojPodela;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedX() {
		return speedX;
	}
}

