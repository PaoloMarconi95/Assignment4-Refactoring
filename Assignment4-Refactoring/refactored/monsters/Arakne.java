package monsters;

import model.Randomizer;

public class Arakne extends Monster{

	public Arakne() {
		super(Randomizer.random.nextInt(6) + 20, 10, "Arakne");
		//random life (btw 20-25)
	}
}
