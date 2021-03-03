package objects.weapons;

import javafx.geometry.Point2D;
import models.GameModel;
import objects.MovingGameObject;

public abstract class Weapon extends MovingGameObject {
	@Override
	protected void handleCollisions ( ) {
		// if the weapon get's out of the window, remove it from the scene
		if ( position.getY ( ) <= 0 ) {
			if (this == GameModel.getInstance ( ).getWeapon ()) {
				GameModel.getInstance().setWeapon(null);
			}else {
				GameModel.getInstance().setBunkerWeapon(null);
			}
			GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
		}
	}
	
	public Weapon ( Point2D position ) {
		super ( position );
	}
	
	public Weapon ( ) { }
	
	@Override
	public void updatePosition ( ) {
		position = new Point2D ( position.getX ( ) + speedX, position.getY ( ) + speedY );
		//setTranslateX(getTranslateX() + speedX);
		setTranslateY ( getTranslateY ( ) + speedY );
		
		handleCollisions ( );
	}
}
