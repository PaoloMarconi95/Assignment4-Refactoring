package items.weapons;

public abstract class Weapon {
	private final int power;
	private final String name;
	
	public Weapon(int power, String name) {
		this.power = power;
		this.name = name;
	}

	public int getPower() {
		return power;
	}

	public String getName() {
		return name;
	}

}
