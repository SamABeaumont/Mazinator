package mazinator.tank;

import java.awt.event.KeyEvent;

import mazinator.main.Mazinator;

public final class Blue extends PlayableTank {
	public Blue (Mazinator game) {
		super(game);
		setCode(Tank.BLUE);
	}
	
	@Override
	public int getForwardEvent () {
		return KeyEvent.VK_UP;
	}
	
	@Override
	public int getBackwardEvent () {
		return KeyEvent.VK_DOWN;
	}
	
	@Override
	public int getLeftEvent () {
		return KeyEvent.VK_LEFT;
	}
	
	@Override
	public int getRightEvent () {
		return KeyEvent.VK_RIGHT;
	}
	
	@Override
	public int getShootEvent () {
		return KeyEvent.VK_M;
	}
}
