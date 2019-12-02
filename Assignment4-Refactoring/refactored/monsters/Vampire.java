package monsters;

import model.Randomizer;

public class Vampire extends Monster {

	public Vampire() {
		super(Randomizer.random.nextInt(5) + 10, 12, "Vampire");
		// random life (btw 10-15)
	}

}
