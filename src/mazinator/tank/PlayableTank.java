package mazinator.tank;

import mazinator.main.Mazinator;

public abstract class PlayableTank extends Tank {
	public PlayableTank (Mazinator game) {
		super(game);
	}
	
	public final void move () {
		
	}
	
	public abstract int getForwardEvent ();
	
	public abstract int getBackwardEvent ();
	
	public abstract int getLeftEvent ();
	
	public abstract int getRightEvent ();
	
	public abstract int getShootEvent ();
}