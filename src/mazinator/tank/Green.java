package mazinator.tank;

import java.awt.event.KeyEvent;

import mazinator.main.Mazinator;

public final class Green extends PlayableTank {
	public Green (Mazinator game) {
		super(game);
		setCode(Tank.GREEN);
	}
	
	@Override
	public int getForwardEvent() {
		return KeyEvent.VK_E;
	}
	
	@Override
	public int getBackwardEvent() {
		return KeyEvent.VK_D;
	}
	
	@Override
	public int getLeftEvent() {
		return KeyEvent.VK_S;
	}
	
	@Override
	public int getRightEvent() {
		return KeyEvent.VK_F;
	}
	
	@Override
	public int getShootEvent() {
		return KeyEvent.VK_Q;
	}
}