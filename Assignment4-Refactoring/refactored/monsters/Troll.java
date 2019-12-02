package monsters;

import model.Randomizer;

public class Troll extends Monster {

	public Troll() {
		super(Randomizer.random.nextInt(21) + 10, 20, "Troll");
		//random life (btw 10-15)
	}
}
