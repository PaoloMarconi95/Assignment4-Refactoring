package monsters;

import model.Randomizer;

public class Glouton extends Monster {

	public Glouton() {
		super(Randomizer.random.nextInt(5) + 10, 5, "Glouton");
		//random life (btw 10-15)
	}

}
