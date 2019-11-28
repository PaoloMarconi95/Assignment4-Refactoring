package monsters;

import model.Player;

public abstract class Monster {
	private int health;
	private final int power;
	private final String name;

	public Monster(int health, int power, String name){
		this.health = health;
		this.power = power;
		this.name = name;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getName() {
		return name;
	}

	public boolean isAlive(){
		return health > 0;
	}

	public int hit(Player p) {
		int dmg = power;
		int alea = (int)(Math.random()*21) * power / 100;
		dmg += Math.random()*101 > 50 ?	alea : -alea;
		p.setHealth(p.getHealth() - dmg);
		return dmg;
	}


}
