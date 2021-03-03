package objects;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import models.GameModel;
import playerStates.MovingLeftState;
import playerStates.MovingRightState;
import playerStates.StandingState;
import playerStates.State;

public class Player extends MovingGameObject {
	//private GameModel model = GameModel.getInstance();
	private             State state         = new StandingState ( this );
	//public static final float PLAYER_WIDTH  = ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.01 );
	public static final float PLAYER_WIDTH  = ( float ) ( GameModel.getInstance ( ).getSceneWidth() /67.88 ); // 19.8
	public static final float PLAYER_HEIGHT = 2 * PLAYER_WIDTH;
	public static float PLAYER_SPEED  = GameModel.getInstance().getSceneWidth()/134.4f * (float) GameModel.getInstance().getJsonObject().getDouble("Player SpeedX");
	//private double headRadius = ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.010 );

	private double headRadius = ( float ) ( GameModel.getInstance ( ).getSceneWidth() / 70 ); // 19.2

	private int fade = 0;
	{
		// this is necessary in order for this class to respond to key events
		this.setFocusTraversable ( true );
	}
	
	{
		speedX = PLAYER_SPEED;
		speedY = 0;
		//Rectangle player = new Rectangle ( PLAYER_WIDTH, PLAYER_HEIGHT );
		//player.setFill ( Color.LIGHTBLUE );
		Group face = new Group();
		Circle head = new Circle(headRadius);
		head.setFill(Color.rgb(224, 172, 105));

		Path rightEye = new Path();
		MoveTo moveTo = new MoveTo(headRadius/2,0);
		LineTo lineTo1 = new LineTo( headRadius/2 + headRadius/2, -headRadius/2);
		LineTo lineTo2 = new LineTo( GameModel.getInstance().getSceneWidth()/1344 , -headRadius/2);
		LineTo lineTo3 = new LineTo(headRadius/2, 0);
		rightEye.getElements().addAll(moveTo, lineTo1,lineTo2,lineTo3);
		rightEye.setFill(Color.BLACK);

		Path leftEye = new Path();
		MoveTo moveTol = new MoveTo(-headRadius/2, 0);
		LineTo lineTo1l = new LineTo( -headRadius / 2 - headRadius / 2, -headRadius/2);
		LineTo lineTo2l = new LineTo( -GameModel.getInstance().getSceneWidth() / 1344 , -headRadius/2);
		LineTo lineTo3l = new LineTo(-headRadius/2, 0);
		leftEye.getElements().addAll(moveTol, lineTo1l,lineTo2l,lineTo3l);
		leftEye.setFill(Color.BLACK);

		Arc mouth = new Arc(0,headRadius/3,headRadius/2,headRadius/3,180,180);
		mouth.setType(ArcType.CHORD);
		mouth.setFill(Color.rgb(137, 47, 49));



		Group hat = new Group();
		Rectangle horizontal = new Rectangle(-headRadius-headRadius/2,-headRadius - 0.1*headRadius, 2*headRadius/2 + 2 * headRadius,0.25*headRadius);
		horizontal.setFill(Color.BLACK);
		Rectangle vertical = new Rectangle(-0.7*headRadius,-2.3*headRadius,1.5*headRadius,1.38*headRadius);
		vertical.setFill(Color.BLACK);
		hat.getChildren().addAll(horizontal,vertical);


		Path body = new Path();
		MoveTo move = new MoveTo(0,0.66*headRadius);
		LineTo line1 = new LineTo(-1.8*headRadius,2.8*headRadius);

		CubicCurveTo cubicCurveTo1 = new CubicCurveTo
				(headRadius/3,headRadius*0.7,headRadius*0.8,2.6*headRadius,2*headRadius,2.2*headRadius);
		LineTo line2 = new LineTo(0,0.66*headRadius);

		body.getElements().addAll(move,line1,cubicCurveTo1,line2);
		body.setFill(Color.RED);
		body.setStroke(Color.RED);

		face.getChildren().addAll(body,head,rightEye,leftEye, mouth);


		body.setTranslateY(-GameModel.getInstance().getSceneHeight() / 290.77);

		face.setTranslateY(-headRadius*0.5 - GameModel.getInstance().getSceneHeight() / 290.77);
		hat.setTranslateY(-headRadius*0.5 - GameModel.getInstance().getSceneHeight() / 290.77);
		this.getChildren ( ).addAll ( face, hat);


		
		this.addEventFilter ( KeyEvent.KEY_PRESSED, event -> {
			switch ( event.getCode ( ) ) {
				case RIGHT:
					state = new MovingRightState ( GameModel.getInstance ( ).getPlayer ( ) );
					break;
				case LEFT:
					state = new MovingLeftState ( GameModel.getInstance ( ).getPlayer ( ) );
					break;
			}
		} );
		
		this.addEventFilter (
				KeyEvent.KEY_RELEASED, event -> {

					if ( (event.getCode ( ) == KeyCode.LEFT && state instanceof MovingRightState)
							|| (event.getCode ( ) == KeyCode.RIGHT && state instanceof MovingLeftState)){
						return;
					}

					if ( event.getCode ( ) == KeyCode.LEFT || event.getCode ( ) == KeyCode.RIGHT ) {
						state = new StandingState ( GameModel.getInstance ( ).getPlayer ( ) );
					}
				}
		);
	}
	
	public Player ( Point2D position ) {
		super ( position );
	}
	
	@Override
	protected void handleCollisions ( ) {
		if ( position.getX ( ) - 2 * PLAYER_WIDTH < 0 || position.getX ( )  > GameModel.getInstance ( ).getSceneWidth ( ) - 2 * PLAYER_WIDTH ) {
			state = new StandingState ( this );
			if ( position.getX ( )  - 2 * PLAYER_WIDTH < 0 ) {
				setPosition ( new Point2D ( 2*PLAYER_WIDTH, getPosition ( ).getY ( ) ) );
				setTranslateX ( 2*PLAYER_WIDTH );
			}
			
			if ( position.getX ( ) > GameModel.getInstance ( ).getSceneWidth ( ) -  2 * PLAYER_WIDTH ) {
				setPosition ( new Point2D ( GameModel.getInstance ( ).getSceneWidth ( ) - 2*PLAYER_WIDTH, getPosition ( ).getY ( ) ) );
				setTranslateX ( GameModel.getInstance ( ).getSceneWidth ( ) - 2*PLAYER_WIDTH );
			}
		}
	}

	public void handleBarrierCollisions(){
		for(int i = 0; i < GameModel.getInstance().getRoot().getChildren().size(); i++){
			if (GameModel.getInstance().getRoot().getChildren().get(i) instanceof Barrier) {
				Barrier barrier = (Barrier) GameModel.getInstance().getRoot().getChildren().get(i);

				if (this.getBoundsInParent().intersects(barrier.getBoundsInParent())) {
					if (speedX > 0) { // idem udesno
						setTranslateX(barrier.getPosition().getX() - Barrier.Barrier_Width / 2 - 2 * PLAYER_WIDTH);
						position = new Point2D(barrier.getPosition().getX() - Barrier.Barrier_Width / 2 - 2 * PLAYER_WIDTH, position.getY());
					} else { //ulevo
						setTranslateX(barrier.getPosition().getX() + Barrier.Barrier_Width / 2 + 2 * PLAYER_WIDTH);
						position = new Point2D(barrier.getPosition().getX() + Barrier.Barrier_Width / 2 + 2 * PLAYER_WIDTH, position.getY());
					}
				}
			}
		}
	}
	
	@Override
	public void updatePosition ( ) {
		state.update ( );
		handleCollisions ( );
		handleBarrierCollisions();
	}

	public int getFade() {
		return fade;
	}

	public void setFade(int fade) {
		this.fade = fade;
	}


}
