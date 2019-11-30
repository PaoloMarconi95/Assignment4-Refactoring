package items;

import java.util.Map.Entry;

import model.Direction;
import rooms.Room;


public class Torch {
	private static final int	MAX_FIRE	= 20;
	private int					fire;

	public Torch() {
		fire = MAX_FIRE;
	}

	public void use(){
		fire--;
		System.out.println("Possible directions : (Torch = " + fire + ") ");
	}

	public boolean empty(){
		if(fire > 0)
			return true;
		else
			return false;
	}

	public void extinguish() {
		fire = 0;
	}

	public void reload() {
		fire = MAX_FIRE;
	}

	public int getFire() {
		return fire;
	}

}
