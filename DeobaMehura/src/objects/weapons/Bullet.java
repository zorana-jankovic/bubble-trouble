package objects.weapons;

import javafx.geometry.Point2D;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import models.GameModel;

public class Bullet extends Weapon {
	public static final float  BULLET_SPEED    = -(GameModel.getInstance().getSceneHeight()/58.15f);
	public static final double BULLET_SIZE = GameModel.getInstance ( ).getSceneWidth ( ) * 0.015;

	private Path harpun;
	private double startY;
	private int direction ;


	private int numOfCubCurves = 1;

	
	{
		//Circle bullet = new Circle ( BULLET_DIAMETER );
		Path bullet = new Path();
		MoveTo moveTo = new MoveTo(BULLET_SIZE/2,0);
		LineTo lineTo1 = new LineTo(0,Math.sqrt(Math.pow(BULLET_SIZE,2)- Math.pow(BULLET_SIZE/2, 2)));
		LineTo lineTo2 = new LineTo(-BULLET_SIZE/2,0);
		LineTo lineTo3 = new LineTo(BULLET_SIZE/2,0);
		bullet.getElements().addAll(moveTo,lineTo1,lineTo2,lineTo3);
		bullet.setFill ( Color.BLUE );
		bullet.setStroke(Color.BLUE);
		bullet.setRotate(180);

		harpun = new Path();

		MoveTo moveTo1 = new MoveTo(0,Math.sqrt(Math.pow(BULLET_SIZE,2)- Math.pow(BULLET_SIZE/2, 2)));
		LineTo line = new LineTo(0,Math.sqrt(Math.pow(BULLET_SIZE,2)- Math.pow(BULLET_SIZE/2, 2))+5);
		startY = Math.sqrt(Math.pow(BULLET_SIZE,2)- Math.pow(BULLET_SIZE/2, 2))+5;

		CubicCurveTo cubicCurveTo1 = new CubicCurveTo((-BULLET_SPEED)/2.6,startY + 1,
				(-BULLET_SPEED)/2.6,startY + (-BULLET_SPEED -1), 0 , startY + (-BULLET_SPEED));

		startY = startY + (-BULLET_SPEED);
		direction = (int) -((-BULLET_SPEED)/2.6);



		LineTo lineTo = new LineTo(0, startY);

		harpun.getElements().addAll(moveTo1,line,cubicCurveTo1,lineTo);

		harpun.setStroke(Color.rgb(188, 71, 54));



		this.getChildren ( ).addAll ( bullet , harpun);
	}
	
	public Bullet ( Point2D position ) {
		super ( position );
	}
	
	public Bullet ( ) { }
	
	// initialize speed
	{
		super.speedX = 0;
		super.speedY = BULLET_SPEED;
	}


	@Override
	public void updatePosition ( ) {
			position = new Point2D(position.getX() + speedX, position.getY() + speedY);
			//setTranslateX(getTranslateX() + speedX);
			setTranslateY(getTranslateY() + speedY);
			//linija.setEndY(linija.getEndY() - speedY);

			CubicCurveTo cubicCurveTo1 = new CubicCurveTo(direction, startY + 1, direction,
					startY + (-BULLET_SPEED -1), 0, startY + (-BULLET_SPEED));

			startY = startY + (-BULLET_SPEED);
			direction = -direction;

			LineTo lineTo = new LineTo(0, startY);

			harpun.getElements().remove(harpun.getElements().size()-1);

			harpun.getElements().addAll(cubicCurveTo1, lineTo);

			// Generatin all again

//			numOfCubCurves++;
//			MoveTo moveTo1 = new MoveTo(0,Math.sqrt(Math.pow(BULLET_SIZE,2)- Math.pow(BULLET_SIZE/2, 2)));
//			LineTo line = new LineTo(0,Math.sqrt(Math.pow(BULLET_SIZE,2)- Math.pow(BULLET_SIZE/2, 2))+5);
//			startY = Math.sqrt(Math.pow(BULLET_SIZE,2)- Math.pow(BULLET_SIZE/2, 2))+5;
//			CubicCurveTo cubicCurveTo1 = new CubicCurveTo(5,startY + 1,5,startY + 12, 0 , startY + 13);
//			startY = startY + 13;
//			direction = -5;
//			harpun.getElements().clear();
//			this.getChildren ( ).remove ( harpun);
//			harpun = new Path();
//			harpun.setStroke(Color.rgb(188, 71, 54));
//			this.getChildren ( ).add ( harpun);
//			harpun.getElements().addAll(moveTo1,line,cubicCurveTo1);
//			for (int i=1; i < numOfCubCurves; i++){
//				CubicCurveTo cubicCurveTo2 = new CubicCurveTo(direction, startY + 1, direction, startY + 12, 0, startY + 13);
//
//				startY = startY + 13;
//				direction = -direction;
//				LineTo lineTo = new LineTo(0, startY);
//
//				harpun.getElements().remove(harpun.getElements().size()-1);
//
//				harpun.getElements().addAll(cubicCurveTo2, lineTo);
//			}




		handleCollisions ( );
	}
}
