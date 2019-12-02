package monsters;

import model.Randomizer;

public class Gnome extends Monster {
	public Gnome() {
		super(Randomizer.random.nextInt(5) + 10, 2, "Gnome");
		//random life (btw 10-15)
	}
}
